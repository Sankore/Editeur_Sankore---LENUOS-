package com.paraschool.editor.server;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.ResourceDetails;
import com.paraschool.commons.share.ResourceUtils;
import com.paraschool.editor.client.rpc.ProjectService.Sort;
import com.paraschool.editor.server.assets.FileRepository;
import com.paraschool.editor.server.assets.FileStream;
import com.paraschool.editor.server.content.resources.ResourceCreationHandler;
import com.paraschool.editor.server.content.resources.ResourceCreationHandlerProvider;
import com.paraschool.editor.server.exporter.CompositePackageProjectExporterFactory;
import com.paraschool.editor.server.exporter.ProjectExporter;
import com.paraschool.editor.server.utils.Zip;
import com.paraschool.editor.shared.Page;
import com.paraschool.editor.shared.ProjectModel;
import com.paraschool.editor.shared.ProjectModelRequest;
import com.paraschool.editor.shared.exception.MissingFeatureException;
import com.paraschool.editor.shared.exception.MissingModelException;
import com.paraschool.editor.shared.exception.MissingUserException;
import com.paraschool.editor.shared.exception.ProjectCreationException;
import com.paraschool.editor.shared.exception.ProjectException;
import com.paraschool.editor.shared.exception.ProjectFileException;
import com.paraschool.editor.shared.exception.ProjectFolderExistException;
import com.paraschool.editor.shared.exception.ProjectNotFoundException;

/*
 * Created at 21 juil. 2010
 * By Didier Bathily
 */
public class FileSystemProjectManager implements ProjectManager {

	private Log logger = LogFactory.getLog(getClass());
	
	private final ProjectDetails details;
	private final ProjectModelManager modelManager;
	private final ApplicationManager applicationManager;
	private final Marshaller marshaller;
	
	@Inject @Named("plubish.exporters") private List<String> publishExporters;
	@Inject private CompositePackageProjectExporterFactory exporterFactory;
	@Inject private ResourceCreationHandlerProvider resourceCreationHandlerProvider;
	@Inject private FileRepository fileRepository;
	
	@Inject
	private FileSystemProjectManager(@Assisted @Nullable ProjectDetails details,
			ProjectModelManager modelManager, ApplicationManager applicationManager,
			Marshaller marshaller) {
		
		this.details = details;
		this.applicationManager = applicationManager;
		this.modelManager = modelManager;
		this.marshaller = marshaller;
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#getProjectDetails()
	 */
	@Override
	public ProjectDetails getProjectDetails() {
		return details;
	}
	
	private String getProjectDirectory() {
		String path = details.getPath() == null || details.getPath().trim().length() == 0 ? applicationManager.getUserAbsoluteDirectory() : details.getPath();
		if(!fileRepository.exist(path, details.getId()))  throw new ProjectNotFoundException(details);
		return path+fileRepository.separator()+details.getId();
	}
	
	private String getProjectResourceDirectory() {
		String projectDirectory = getProjectDirectory();
		return projectDirectory+fileRepository.separator()+PROJECT_RESOURCES_DIR;
	}
	
	private String getResourceThumbnailDirectory() {
		return getProjectResourceDirectory()+fileRepository.separator()+PROJECT_RESOURCES_THUMBNAILS_DIR;
	}

	private String getRelativePathInProject(String absolutePath) {
		return absolutePath.substring(getProjectDirectory().length()+1);
	}
	
	private String getExportDirectory(Project p) {
		return applicationManager.getExportDirectory()+fileRepository.separator()+p.getDetails().getId()+fileRepository.separator();
	}

	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#getApplicationManager()
	 */
	@Override
	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#createProject(com.paraschool.editor.shared.ProjectModelRequest)
	 */
	@Override
	public Project createProject(ProjectModelRequest modelRequest) {
		logger.debug("Create project "+details.getName());
		/*
		 *  Create project directory based on an unique id
		 */
		details.setId(UUID.randomUUID().toString());
		if(fileRepository.exist(getApplicationManager().getUserAbsoluteDirectory(), details.getId())) throw new ProjectFolderExistException(details);
		String directory = getApplicationManager().getUserAbsoluteDirectory()+fileRepository.separator()+details.getId();
		
		Project project = new Project(details);
		
		/*
		 * If project must be create based on a model, install this model
		 * in project directory
		 */
		
		if(modelRequest != null) {
			logger.debug("From model "+modelRequest.getId());
			ProjectModel model = modelManager.getModel(modelRequest);
			if(model == null) {
				logger.debug("Model "+modelRequest.getId()+" not found!");
				fileRepository.deleteDirectory(directory);
				throw new MissingModelException(modelRequest.getId(), details);
			}
			
			try {
				modelManager.installModel(model, modelRequest.getOwner(),  directory);
				InputStream input = fileRepository.getAssetByName(directory, PROJECT_FILE).getInputStream();
				project = (Project) marshaller.fromXML(input);
				project.setDetails(details);
				IOUtils.closeQuietly(input);
			} catch (IOException e) {
				logger.error(e);
			} catch (Exception e) {
				fileRepository.deleteDirectory(directory);
				logger.warn(e);
				throw new ProjectCreationException(details);
			}
			
		}

		/*
		 * Write project file. This will finish creation process
		 */
		persist(project, directory, PROJECT_FILE);
		
		return project;
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#createLocaleVersion(java.lang.String)
	 */
	@Override
	public Project createLocaleVersion(String locale) {
		Project project = getVersionForLocale(locale);
		if(project != null)
			return project;
		
		project = get();
		ProjectDetails details = project.getDetails();
		logger.debug("Create "+locale+" version for project ["+details.getId()+"-"+details.getName()+"] from "+details.getLocale());
		details.setLocale(locale);
		details.setLocaleName(getLocaleName(locale));
		persist(project, getProjectDirectory(), PROJECT_FILE_PREFIX+"_"+locale+PROJECT_FILE_EXT);
		return project;
	}
	
	private void persist(Project project, String directory, String xml) {
		logger.debug("Persit project file ["+xml+"] in ["+directory+"]");
		try {
			OutputStream output = fileRepository.getAssetOutputStream(directory, xml);
			marshaller.toXML(project, output);
			output.close();
		} catch (FileNotFoundException e) {
			throw new ProjectFileException(details);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#get()
	 */
	@Override
	public Project get() {
		return getVersionForLocale(details.getLocale());
	}
	
	@Override
	public Project copy(String newName) {
		Project newProject = get();
		newProject.getDetails().setId(UUID.randomUUID().toString());
		if(newName != null)
			newProject.getDetails().setName(newName);
		String directory = getApplicationManager().getUserAbsoluteDirectory()+fileRepository.separator()+newProject.getDetails().getId();
		try {
			copyProjectFolder(getProjectDirectory(), directory);
		} catch (IOException e) {
			e.printStackTrace();
			fileRepository.deleteDirectory(directory);
			return null;
		}
		persist(newProject, directory, PROJECT_FILE);
		return newProject;
	}

	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#getVersionForLocale(java.lang.String)
	 */
	@Override
	public Project getVersionForLocale(String locale) {
		return get(getProjectFileName(locale));
	}
	
	private String getProjectFileName(String locale) {
		String xml = PROJECT_FILE;
		if(locale != null)
			xml = PROJECT_FILE_PREFIX+"_"+locale+PROJECT_FILE_EXT;
		return xml;
	}
	
	private String getLocaleName(String locale) {
		Locale l = null;
		if(locale != null && (l = LocaleUtils.toLocale(locale)) != null)
			return l.getDisplayName();
		return null;
	}
	
	private Project get(String projectFile) {
		if(details == null || details.getId() == null) return null;
		
		logger.debug("Get project ["+details.getId()+"-"+details.getName()+","+projectFile+"] with path ["+details.getPath()+"]");
		String directory = getProjectDirectory();

		Project project = null;
		try {
			InputStream input = fileRepository.getAssetByName(directory, projectFile).getInputStream();
			project = (Project) marshaller.fromXML(input);
			
			// Si le projet ne se trouve pas à l'emplacement par défaut, on persite cette info
			if(details.getPath() != null)
				project.getDetails().setPath(details.getPath());
			
			//Trick pour donner le nom de la locale aux clients sans envoyer une autre requète rpc
			project.getDetails().setLocaleName(getLocaleName(project.getDetails().getLocale()));
			
			updateProjectFile(project, directory, projectFile);
			input.close();
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return project;
	}
	
	private ArrayList<Project> getAllVersions() {
		List<String> locales = getLocales();
		ArrayList<Project> projects = new ArrayList<Project>(locales.size()+1);
		projects.add(getVersionForLocale(null));
		for(String s : locales)
			projects.add(getVersionForLocale(s));
		return projects;
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#getProjects(int)
	 */
	@Override
	public Page<ProjectDetails> getProjects(int page, int limit, Sort sort) {
		logger.debug("Get project list");
		
		List<FileStream> files = getProjectFiles();
		
		if(sort == Sort.DATE) {
			Collections.sort(files, Ordering.natural().reverse().onResultOf(new Function<FileStream, Long>() {
				@Override
				public Long apply(FileStream input) {
					try {
						FileStream stream = fileRepository.getAssetByName(input.getPath(), PROJECT_FILE);
						IOUtils.closeQuietly(stream.getInputStream());
						return stream.getLastModified();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				}
			}));
		}
	
		List<ProjectDetails> projects = new ArrayList<ProjectDetails>();
		
		if(files != null){
			for(int i = 0 ; i < files.size() ; i++){
				String path = files.get(i).getPath();
				try {
					InputStream input = fileRepository.getAssetByName(path, PROJECT_FILE).getInputStream();
					Project project = (Project) marshaller.fromXML(input);
					updateProjectFile(project, path, PROJECT_FILE);
					projects.add(project.getDetails());
					input.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					logger.error("Failed to get project with xml at path "+path);
					logger.error(e);
				}
			}
		}
		
		if(sort == Sort.NAME)
			Collections.sort(projects, Ordering.natural().onResultOf(new Function<ProjectDetails, String>() {
				@Override
				public String apply(ProjectDetails input) {
					return input.getName();
				}
			}));
		
		int begin = page * limit;
		int end = begin + limit;
		
		List<ProjectDetails> result = new ArrayList<ProjectDetails>(limit);
		for(int i = begin ; i < Math.min(end, projects.size()) ; i++) {
			result.add(projects.get(i));
		}
		
		double size = (double)projects.size() / limit;
		if(size > Math.floor(size))
			size = size+1;
		return new Page<ProjectDetails>(page, (int)size, result.size(), result);
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#delete()
	 */
	@Override
	public boolean delete() throws ProjectException, MissingUserException {
		if(details == null || details.getId() == null) return true;
		logger.debug("Delete project ["+details.getId()+"-"+details.getName()+"]");
		return fileRepository.deleteDirectory(getProjectDirectory());
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#deleteLocale(java.lang.String)
	 */
	@Override
	public boolean deleteLocale(String locale) throws ProjectException, MissingUserException {
		if(details == null || details.getId() == null || locale == null || details.getLocale() == locale) return false;
		
		logger.debug("Delete locale ["+locale+"] for project ["+details.getId()+"-"+details.getName()+"]");
		
		Project p = getVersionForLocale(locale);
		for(Resource r : p.getResources().values()) {
			logger.debug(r.getId()+"-"+r.getLocale());
			if(locale.equals(r.getLocale())) {
				boolean succeed = removeResourceFile(r.details());
				if(logger.isDebugEnabled() && !succeed)
					logger.debug("Failed to remove resource ["+r.getId()+"] for project ["+details.getId()+","+locale+"]");
			}
		}
		
		return fileRepository.delete(getProjectDirectory(), getProjectFileName(locale));
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#save(com.paraschool.commons.share.Project)
	 */
	@Override
	public boolean save(Project project) {
		logger.debug("Save project ["+project.getDetails().getId()+"-"+project.getDetails().getName());
		return save(project, getProjectFileName(project.getDetails().getLocale()));
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#preview(com.paraschool.commons.share.Project)
	 */
	@Override
	public boolean preview(Project project) {
		logger.debug("Save project preview ["+project.getDetails().getId()+"-"+project.getDetails().getName());
		return save(project, "preview.xml");
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#createModel()
	 */
	@Override
	public ProjectModel createModel() throws ProjectException {

		String destination = getApplicationManager().getUserProjectModelsDirectory();

		try {
			String name = "Model-"+UUID.randomUUID().toString()+".zip";
			File tmpDir = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
			tmpDir.mkdir();
			fileRepository.copy(getProjectDirectory(), tmpDir, filesToIgnore(getProjectDirectory()+fileRepository.separator()));
			OutputStream output = fileRepository.getAssetOutputStream(destination, name);
			Zip.zip(tmpDir, output);
			IOUtils.closeQuietly(output);
			
			details.setId(name);

			ProjectModel model = new ProjectModel(details, destination+fileRepository.separator()+name);
			try {
				OutputStream poutput = fileRepository.getAssetOutputStream(destination, details.getId()+PROJECT_FILE_EXT);
				marshaller.toXML(model, poutput);
				IOUtils.closeQuietly(poutput);
				return model;
			} catch (Exception e) {
				logger.warn("Failed to save model ["+model.getId()+"] cause:"+e);
			}
		} catch (Exception e) {
			logger.warn("Failed to create model for project ["+details.getId()+"-"+details.getName()+"], cause :"+e);
		} 
		return null;
	}
	
	private boolean removeResourceFile(ResourceDetails resourceDetails) {
		String resources = getProjectDirectory();
		if(resourceDetails.getThumbnail() != null) {
			fileRepository.delete(resources, resourceDetails.getThumbnail());
		}
		return !fileRepository.exist(resources, resourceDetails.getUrl()) || fileRepository.delete(resources, resourceDetails.getUrl());
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#removeResource(com.paraschool.commons.share.ResourceDetails)
	 */
	@Override
	public boolean removeResource(ResourceDetails resourceDetails) {
		
		if(resourceDetails == null || !removeResourceFile(resourceDetails))
			return false;
		
		Project project = get(); 
		project.getResources().remove(resourceDetails.getId());
		return save(project);
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#removeIcon()
	 */
	@Override
	public boolean removeIcon() {
		String icon = null;
		if((icon = get().getDetails().getIcon()) != null)
			return fileRepository.delete(getProjectDirectory(), icon);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#addResource(com.paraschool.commons.share.Resource)
	 */
	@Override
	public Resource addResource(Resource resource) {
		logger.debug("Add resource ["+resource.getId()+" to project "+details.getName()+"]");
		
		try{
			ResourceCreationHandler<Resource> handler = resourceCreationHandlerProvider.get(resource);
			if(handler != null)
				handler.onCreate(resource, this);
		}catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
		
		Project project = get();
		project.addResource(resource);
		save(project);
		return resource;
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#export(com.paraschool.commons.share.Project, java.util.Map, java.util.List, java.lang.String)
	 */
	@Override
	public String export(Project project, Map<String, String> options, List<String> exportersId) {
		save(project);
		final File sourceDirectory = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
		sourceDirectory.mkdir();
		logger.debug("Tmp dir "+sourceDirectory.getAbsolutePath()+" create for export");
		String url = null;
		try{
			copyProjectFolder(getProjectDirectory(), sourceDirectory.getAbsolutePath());
			url = exportIn(project, options, exportersId, sourceDirectory, getExportDirectory(project));
		}catch (IOException e) {
			logger.error(e);
		}
		try{
			FileUtils.deleteDirectory(sourceDirectory);	
		}catch (Exception e) {}

		return url;
	}
	
	@Override
	public List<String> exportPages(final Project project, Integer[] index, Map<String, String> options, List<String> exportersId) {
		save(project);
		String newId = UUID.randomUUID().toString();
		final File sourceDirectory = new File(System.getProperty("java.io.tmpdir"), newId);
		sourceDirectory.mkdir();
		logger.debug("Tmp dir "+sourceDirectory.getAbsolutePath()+" create for export");
		
		ProjectDetails projectDetails = project.getDetails();
		List<String> urls = new ArrayList<String>();
		try{
			copyProjectFolder(getProjectDirectory(), sourceDirectory.getAbsolutePath());
			
			for(int i=0 ; i < index.length ; i++) {
				int pageIndex = index[i];
				logger.debug("Export page ["+pageIndex+"]");
				if(pageIndex < 0 || pageIndex >= project.getPages().size()) {
					logger.debug("Page ["+pageIndex+"] is not for this project.");
					continue;
				}
				
				ProjectDetails tempDetails = new ProjectDetails(newId+pageIndex, null, projectDetails.getLocale(),
						projectDetails.getName() +" - "+ (pageIndex+1), projectDetails.getDescription(), projectDetails.getObjectif(),
						projectDetails.getVersion(), projectDetails.getViewer(), projectDetails.getAuthor(), projectDetails.getDate(),
						projectDetails.getWidth(), projectDetails.getHeight());
				
				Project temp = new Project(tempDetails);
				temp.setResources(project.getResources());
				ArrayList<com.paraschool.commons.share.Page> tempPage = new ArrayList<com.paraschool.commons.share.Page>(1);
				tempPage.add(project.getPages().get(pageIndex));
				temp.setPages(tempPage);
				
				persist(temp, sourceDirectory.getAbsolutePath(), PROJECT_FILE);
				
				urls.add(exportIn(temp, options, exportersId, sourceDirectory, getExportDirectory(project)));
			}
		}catch (IOException e) {
			logger.error(e);
		}
		try{
			FileUtils.deleteDirectory(sourceDirectory);	
		}catch (Exception e) {}

		return urls;
	}
	
	@Override
	public String export(Project project, File source, Map<String, String> options, List<String> exportersId) {
		save(project);
		return exportIn(project, options, exportersId, source, getExportDirectory(project));
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#publish(com.paraschool.commons.share.Project, java.util.Map, javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public String publish(Project project, Map<String, String> options, HttpServletRequest request) {
		
		save(project);
		final File sourceDirectory = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
		sourceDirectory.mkdir();
		logger.debug("Tmp dir "+sourceDirectory.getAbsolutePath()+" create for export");
		String result = null;
		try{
			copyProjectFolder(getProjectDirectory(), sourceDirectory.getAbsolutePath());
			result = exportIn(project, options, publishExporters, sourceDirectory, getApplicationManager().getPublishDirectory());
		}catch (IOException e) {
			logger.error(e);
		}
		try{
			FileUtils.deleteDirectory(sourceDirectory);	
		}catch (Exception e) {}
		
		if(result != null) {
			String url = request.getRequestURL().toString();
			url = url.substring(0, url.indexOf(request.getServletPath())+1) + "publications/";
			result = url + result;
		}
		return  result;
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#getLocales()
	 */
	@Override
	public List<String> getLocales() {
		return fileRepository.getLocales(getProjectDirectory(),PROJECT_FILE_PREFIX+"(_.*)?"+PROJECT_FILE_EXT);
	}
	
	private void updateProjectFile(Project project, String directory, String xml) {
		ProjectDetails details =  project.getDetails();
		details.setId(FilenameUtils.getName(directory));
		try {
			details.setDate(new Date(fileRepository.getLastModified(directory, xml)));
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage());
		}
	}
	
	private List<FileStream> getProjectFiles() {
		String path = getApplicationManager().getUserAbsoluteDirectory();
		logger.debug("Get projects from "+path);
		return fileRepository.getProjects(path, PROJECT_FILE);
	}
	
	private boolean save(Project project, String file) {
		//Check if project == project.detail
		
		logger.debug("Save project ["+details.getId()+"-"+details.getName()+"] to "+file);
		project.getDetails().setDate(new Date());
		
		String directory = getProjectDirectory();
		String save = "save-"+file;
		
		try{
			FileStream stream = fileRepository.getAssetByName(directory, file);
			fileRepository.putAsset(directory, save, stream.getInputStream(), stream.getSize());
			IOUtils.closeQuietly(stream.getInputStream());
			fileRepository.delete(directory, file);
		}catch (Exception e) {}
		
		try {
			//Ne jamais persister le chemin du projet
			project.getDetails().setPath(null);
			
			OutputStream output = fileRepository.getAssetOutputStream(directory, file);
			marshaller.toXML(project, output);
			IOUtils.closeQuietly(output);
			
		} catch (Exception e) {
			logger.warn("Failed to save project ["+project.getDetails().getId()+"] cause:"+e.getCause());
			if(!fileRepository.exist(directory, file))
				try{
					FileStream stream = fileRepository.getAssetByName(directory, save);
					fileRepository.putAsset(directory, file, stream.getInputStream(), stream.getSize());
					IOUtils.closeQuietly(stream.getInputStream());
				}catch (Exception e2) {}
			return false;
		}
		return true;
	}
	
	private FileFilter filesToIgnore(final String projectFolder) {
		return new FileFilter() {
			final File preview = new File(projectFolder, "preview.xml");
			
			@Override
			public boolean accept(File pathname) {
				String path = pathname.getAbsolutePath();
				if(path.equals(preview.getAbsolutePath())){
					return false;
				}
				return true;
			}
		};
	}
	
	private void copyProjectFolder(final String source, final String destination) throws IOException {
		logger.debug("Copy project from ["+source+"] to ["+destination+"]");
		fileRepository.copy(source, destination, filesToIgnore(source+fileRepository.separator()));
	}
	
	private String exportIn(Project project, Map<String, String> options, List<String> exportersId, final File source, final String destination) {
		ProjectExporter exporter = exporterFactory.create(exportersId);
		
		if(destination == null) throw new MissingFeatureException();
		
		if(exporter == null || project == null)
			return null;
		
		try {
			
			String destinationName = exporter.getDestinationName(project.getDetails());
			if(fileRepository.exist(destination, destinationName)) fileRepository.delete(destination, destinationName);
			
			OutputStream destinationOutputStream = fileRepository.getAssetOutputStream(destination, destinationName);
			exporter.exportTo(/*getAllVersions()*/ Arrays.asList(project), options, source, destinationOutputStream);
			destinationOutputStream.close();
			return destinationName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public void addResourceFile(File file) {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			fileRepository.putAsset(getProjectResourceDirectory(), file.getName(), input, file.length());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}finally {
			IOUtils.closeQuietly(input);
		}
	}

	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#processUploadedResourceFileForProject(java.util.List)
	 */
	@Override
	public String processUploadedResourceFileForProject(List<FileItem> items) {
		String directory = getProjectResourceDirectory();
		String response = "";
		if(directory != null) {
			String locale = details.getLocale();
			logger.debug("Project found. Process file");
			for (FileItem item : items) {
				String contentType = item.getContentType();
				logger.debug(contentType);
				//ie envoie "image/pjpeg" au lieu de "image/jpeg"
				if("image/pjpeg".equals(contentType))
					contentType = "image/jpeg";
				//Chrome su windows envoie "audio/mp3" au lieu de "audio/mpeg"
				if("audio/mp3".equals(contentType))
					contentType = "audio/mpeg";

				if (false == item.isFormField() && ResourceUtils.getValidExtensions().contains(contentType)) {
					try {

						/// Create a new file.
						String itemName = item.getName();
						
						String suffix = "";
						
						/// Get the file extension
						int extIndex = itemName.lastIndexOf(".");
						if(extIndex > 1)
							suffix = itemName.substring(extIndex, itemName.length());
						
						String id = "PSEditor-"+UUID.randomUUID().toString()+suffix;
						fileRepository.putAsset(directory, id, item.getInputStream(), item.getSize());
						
						logger.debug("Resource ["+item.getName()+"] saved at ["+id+"]");

						response += marshaller.toXML(getResourceForItem(id, item, contentType, PROJECT_RESOURCES_DIR, locale));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return response;
	}
	
	/* (non-Javadoc)
	 * @see com.paraschool.editor.server.ProjectManager#processUploadedIconFileForProject(java.util.List)
	 */
	@Override
	public String processUploadedIconFileForProject(List<FileItem> items) {
		String response = "";
		removeIcon();
		String directory = getProjectDirectory();
		if(directory != null) {
			logger.debug("Project found. Process project icon");
			for (FileItem item : items) {
				if (false == item.isFormField() && ResourceUtils.isImage(item.getContentType())) {

					String itemName = item.getName();					
					String suffix = ".png";
					/// Get the file extension
					int extIndex = itemName.lastIndexOf(".");
					if(extIndex > 1)
						suffix = itemName.substring(extIndex, itemName.length());
					
					try {
						String id = "icon"+suffix;
						fileRepository.putAsset(directory, id, item.getInputStream(), item.getSize());
						response += "<url>"+id+"</url>";
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return response;
	}
	
	private Resource getResourceForItem(String id, FileItem item, String type, String directory, String locale) {
		logger.debug(locale);
		String url = directory+fileRepository.separator()+id;
		String name = item.getName();
		long size = item.getSize();
		
		return ResourceUtils.build(id, url, name, size, type, null, null, locale);
	}

	@Override
	public InputStream getResource(Resource resource) {
		try {
			return fileRepository.getAsset(getProjectDirectory()+fileRepository.separator()+resource.getUrl()).getInputStream();
		} catch (FileNotFoundException e) {
			logger.warn("Resource ["+resource.getId()+"] not found at ["+getProjectDirectory()+fileRepository.separator()+resource.getUrl()+"].");
		}
		return null;
	}

	@Override
	public void addThumbnailForResource(Resource resource, InputStream thumbnailStream, long lenght) {
		if(resource != null &&  thumbnailStream != null) {
			try {
				fileRepository.putAsset(getResourceThumbnailDirectory(), resource.getId(), thumbnailStream, lenght);
				resource.setThumbnail(getRelativePathInProject(getResourceThumbnailDirectory()+fileRepository.separator()+resource.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public InputStream getStreamForPathInProject(String path) {
		try {
			return fileRepository.getAsset(getProjectDirectory()+fileRepository.separator()+path).getInputStream();
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	private FileStream getFileResource(String directory, String path, String name) {
		try {
			FileStream stream = fileRepository.getAsset(directory+fileRepository.separator()+path);
			stream.setName(name);
			return stream;
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage());
			return null;
		}
	}

	@Override
	public FileStream getInProjectResource(String path) {
		return getFileResource(getProjectDirectory(), path, FilenameUtils.getName(path));
	}
	
	@Override
	public FileStream getExportResource(String path) {
		String ext = FilenameUtils.getExtension(path);
		Project p = get();
		return getFileResource(getExportDirectory(p), path, (p != null ? (p.getDetails().getName() + (ext != null ? "."+ext : "")): null));
	}

	@Override
	public FileStream getPublicationResource(String path) {
		return getFileResource(applicationManager.getPublishDirectory(), path, path);
	}

	@Override
	public String importExport(FileStream resource) {
		String exportDirectory = getExportDirectory(get());
		String id = UUID.randomUUID().toString()+"."+FilenameUtils.getExtension(resource.getName());
		
		if(fileRepository.exist(exportDirectory, id))
			fileRepository.delete(exportDirectory, id);
		
		try {
			fileRepository.putAsset(exportDirectory, id, resource.getInputStream(), resource.getSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileRepository.separator()+id;
	}

	@Override
	public boolean clean() {
		fileRepository.deleteDirectory(getExportDirectory(get()));
		return true;
	}

}
