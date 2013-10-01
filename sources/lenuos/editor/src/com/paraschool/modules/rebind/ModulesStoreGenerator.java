package com.paraschool.modules.rebind;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.google.classpath.ClassPath;
import com.google.classpath.ClassPathFactory;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.api.client.ModulesStore;

public class ModulesStoreGenerator extends Generator {

	static final String MODULES_DIRECTORY = "modules";
	private boolean introspectionIsAlreadyDone = false;

	ArrayList<Class<? extends EditorModule>> pluginsAvailables = new ArrayList<Class<? extends EditorModule>>();

	private boolean isCandidate(Class<?> aclass, boolean isRootClass) {
		int modifier = aclass.getModifiers();
		if(isRootClass && (!Modifier.isPublic(modifier) || Modifier.isAbstract(modifier)
				|| Modifier.isInterface(modifier)))
			return false;

		Class<?>[] interfaces = aclass.getInterfaces();
		for(Class<?> ainterface : interfaces){
			if(ainterface.equals(EditorModule.class)) {
				return true;
			}
		}

		Class<?> superClass = aclass.getSuperclass();
		if(superClass == null)
			return false;
		return isCandidate(superClass, false);
	}

	private void introspect(TreeLogger logger) {
		// Retrieve all root package in classloader
		ClassPathFactory factory = new ClassPathFactory();
		ClassPath classPath = factory.createFromJVM();

		String[] packages = classPath.listPackages("");
		logger.log(TreeLogger.DEBUG, "Introspection : find [" + packages.length + "] packages");
		// For each root package find recursively all plugin classes
		for (String rootPackageName : packages) {
			logger.log(TreeLogger.DEBUG, "Introspection : root package : " + rootPackageName);
			try {
				getClasses(logger, rootPackageName);
			} catch (Exception ex) {
				// Go to next root package when rootPackageName is not a java package
				continue;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void proceed(TreeLogger logger, Class<?> aclass) {
		if (isCandidate(aclass, true)){
			try{
				aclass.newInstance();
				//Validate descriptors
				logger.log(TreeLogger.INFO, "Introspection : find a module : " + aclass);
				pluginsAvailables.add((Class<? extends EditorModule>) aclass);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void getFromModulesDirectory(TreeLogger logger) {

		File modulesDirectory = new File(MODULES_DIRECTORY);
		String[] modules = modulesDirectory.list();
		
		if(modules == null)
			return;
		
		for (String module : modules) {			

			File file = new File(modulesDirectory, module);

			logger.log(TreeLogger.INFO, "Introspection : find a jar in modules directory [" + file + "]");

			JarInputStream jarFile;
			try {
				jarFile = new JarInputStream(new FileInputStream(file));
				JarEntry jarEntry;
				while((jarEntry=jarFile.getNextJarEntry ()) != null) {
					try{
						if(jarEntry.getName().endsWith(".class")){
							proceed(logger, Class.forName(jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6)));
						}
					}catch (Throwable e) {
						logger.log(TreeLogger.DEBUG, "Introspection : Omit [" + jarEntry.getName() + "] cause : "+e);
					}
				}
			} catch (Throwable e) {
			}

		}

	}

	/**
	 * Generate a store for editor's module dynamicly from
	 * classes in classloader.
	 * 
	 * @param logger
	 * @param context
	 * @param typeName class name to substitute
	 * @return created class name
	 * @throws UnableToCompleteException
	 */
	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {

		ClassSourceFileComposerFactory composer = null;
		SourceWriter out = null;
		try {

			if(!introspectionIsAlreadyDone){
				// Retrieve plugin list
				//First in project
				introspect(logger);
				//then from modules directory
				getFromModulesDirectory(logger);
			}
			introspectionIsAlreadyDone = true;

			// Generate the source file
			TypeOracle oracle = context.getTypeOracle();
			JClassType type = oracle.getType(typeName);
			PropertyOracle propertyOracle = context.getPropertyOracle();
			String packageName = type.getPackage().getName();
			String simpleName = type.getSimpleSourceName() + "_Generated_" + propertyOracle.getSelectionProperty(logger, "user.agent").getCurrentValue();
			composer = new ClassSourceFileComposerFactory(packageName, simpleName);
			// Define imports
			composer.addImport("java.util.*");
			composer.addImport(EditorModule.class.getCanonicalName());
			composer.addImport(ModulesStore.class.getCanonicalName());
			// Define implements
			composer.addImplementedInterface(ModulesStore.class.getCanonicalName());

			// Write "getModules" method
			PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
			if(printWriter != null) {
				out = composer.createSourceWriter(context, printWriter);
				out.indent();
				out.println("ArrayList<" + EditorModule.class.getSimpleName() + "> modules = null;");
				out.println("public List<" + EditorModule.class.getSimpleName() + "> getModules() {");
				out.indent();
				out.println("if(modules == null){");
				out.indent();
				out.println("modules = new ArrayList<" + EditorModule.class.getSimpleName() + ">();");
				for (Class<? extends EditorModule> pluginName : pluginsAvailables) {

					out.println("if(ONLY.size() == 0 || ONLY.contains("+pluginName.getCanonicalName()+".class)){");
					out.indent();
					out.println("EditorModule m = new " +pluginName.getCanonicalName() + "();");
					out.println("m.init();");
					out.println("modules.add(m);");
					out.outdent();
					out.println("}");

				}
				out.println("}");
				out.outdent();
				out.println("return modules;");
				out.outdent();
				out.println("}");
				out.outdent();
				out.commit(logger);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new UnableToCompleteException();			
		}
		return composer.getCreatedClassName();
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void getClasses(TreeLogger logger, String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);

		while (resources.hasMoreElements()) {			
			URL resource = resources.nextElement();

			if(!("jar".equals(resource.getProtocol())))
				findClasses(logger, new File(resource.getFile()), packageName);
		}

	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 * 
	 * @param directory The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private void findClasses(TreeLogger logger, File directory, String packageName) throws ClassNotFoundException {

		if (!directory.exists()) {
			return;
		}

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				findClasses(logger, file, packageName + "." + file.getName());
			} else if (file.getName().endsWith(".class")) {
				String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
				logger.log(TreeLogger.DEBUG,"Introspection : find class ["+className+"]");
				try{
					proceed(logger, Class.forName(className));
				}catch (Throwable e) {
					logger.log(TreeLogger.DEBUG,"Introspection : omit class ["+className+"], cause : "+ e);
					continue;
				}
			}
		}
	}
}
