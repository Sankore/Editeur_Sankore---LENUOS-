package com.paraschool.editor.server.config;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.paraschool.commons.share.AudioResource;
import com.paraschool.commons.share.ImageResource;
import com.paraschool.commons.share.LinkResource;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.SWFResource;
import com.paraschool.commons.share.Template;
import com.paraschool.commons.share.VideoResource;
import com.paraschool.editor.server.ApplicationManager;
import com.paraschool.editor.server.EmailPublicationNotifier;
import com.paraschool.editor.server.FileSystemProjectManager;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.ProjectManagerProvider;
import com.paraschool.editor.server.PublicationNotifier;
import com.paraschool.editor.server.URLPublicationNotifier;
import com.paraschool.editor.server.assets.FileRepository;
import com.paraschool.editor.server.assets.FileSystemFileRepositoryImpl;
import com.paraschool.editor.server.assets.HDFSFileRepositoryImpl;
import com.paraschool.editor.server.content.ContentProvider;
import com.paraschool.editor.server.content.models.FileProjectModelProvider;
import com.paraschool.editor.server.content.models.ProjectModelProviderFactory;
import com.paraschool.editor.server.content.models.ProjectModels;
import com.paraschool.editor.server.content.models.UserProjectModelProviderFactoryImpl;
import com.paraschool.editor.server.content.models.WebAppProjectModelProvider;
import com.paraschool.editor.server.content.resources.AudioCreationHandler;
import com.paraschool.editor.server.content.resources.ImageCreationHandler;
import com.paraschool.editor.server.content.resources.LinkCreationHandler;
import com.paraschool.editor.server.content.resources.ResourceCreationHandler;
import com.paraschool.editor.server.content.resources.SWFCreationHandler;
import com.paraschool.editor.server.content.resources.VideoCreationHandler;
import com.paraschool.editor.server.content.samples.Samples;
import com.paraschool.editor.server.content.samples.WebAppContentSampleProvider;
import com.paraschool.editor.server.content.templates.FileTemplateProvider;
import com.paraschool.editor.server.content.templates.Templates;
import com.paraschool.editor.server.content.templates.WebAppContentTemplateProvider;
import com.paraschool.editor.server.exporter.HTMLViewerPackage;
import com.paraschool.editor.server.exporter.ViewerPackage;
import com.paraschool.editor.server.interceptor.Interceptable;
import com.paraschool.editor.server.interceptor.ProjectServiceInterceptor;
import com.paraschool.editor.server.interceptor.PublishInterceptor;
import com.paraschool.editor.server.rpc.ProjectServiceImpl;
import com.paraschool.editor.shared.Parameters;
import com.paraschool.editor.shared.ProjectModel;
import com.paraschool.editor.shared.Sample;
/*
 * Created at 30 juil. 2010
 * By bathily
 */
public class EditorGuiceModule extends AbstractModule {

	protected final Log logger = LogFactory.getLog(getClass());
	
	protected Configuration configuration;
	
	public EditorGuiceModule(Configuration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	protected void configure() {
		
		Names.bindProperties(binder(), loadProperties());
		
		bind(Parameters.class).in(Singleton.class);
		bind(ApplicationManager.class).in(Singleton.class);
		
		install(new FactoryModuleBuilder().implement(ProjectManager.class, FileSystemProjectManager.class).build(ProjectManagerProvider.class));
		
		switch (configuration.getInt("fileRepository")) {
		case 0:
			bind(FileRepository.class).to(FileSystemFileRepositoryImpl.class);
			break;
		case 1:
			bind(FileRepository.class).to(HDFSFileRepositoryImpl.class);
			break;
		}
		
		bind(new TypeLiteral<ResourceCreationHandler<AudioResource>>(){}).to(AudioCreationHandler.class);
		bind(ResourceCreationHandler.class).annotatedWith(Names.named(AudioResource.class.toString())).to(AudioCreationHandler.class);
		
		bind(new TypeLiteral<ResourceCreationHandler<SWFResource>>(){}).to(SWFCreationHandler.class);
		bind(ResourceCreationHandler.class).annotatedWith(Names.named(SWFResource.class.toString())).to(SWFCreationHandler.class);
		
		bind(new TypeLiteral<ResourceCreationHandler<ImageResource>>(){}).to(ImageCreationHandler.class);
		bind(ResourceCreationHandler.class).annotatedWith(Names.named(ImageResource.class.toString())).to(ImageCreationHandler.class);
		
		bind(new TypeLiteral<ResourceCreationHandler<VideoResource>>(){}).to(VideoCreationHandler.class);
		bind(ResourceCreationHandler.class).annotatedWith(Names.named(VideoResource.class.toString())).to(VideoCreationHandler.class);
		
		bind(new TypeLiteral<ResourceCreationHandler<LinkResource>>(){}).to(LinkCreationHandler.class);
		bind(ResourceCreationHandler.class).annotatedWith(Names.named(LinkResource.class.toString())).to(LinkCreationHandler.class);
		
		Multibinder<ContentProvider<Sample>> sampleBinder = Multibinder.newSetBinder(binder(), new TypeLiteral<ContentProvider<Sample>>(){});
		sampleBinder.addBinding().to(WebAppContentSampleProvider.class).in(Singleton.class);
		
		Multibinder<ContentProvider<Template>> templateBinder = Multibinder.newSetBinder(binder(), new TypeLiteral<ContentProvider<Template>>(){});
		templateBinder.addBinding().to(FileTemplateProvider.class).in(Singleton.class);
		templateBinder.addBinding().to(WebAppContentTemplateProvider.class).in(Singleton.class);
		
		Multibinder<ContentProvider<ProjectModel>> modelBinder = Multibinder.newSetBinder(binder(), new TypeLiteral<ContentProvider<ProjectModel>>(){});
		modelBinder.addBinding().to(FileProjectModelProvider.class).in(Singleton.class);
		modelBinder.addBinding().to(WebAppProjectModelProvider.class).in(Singleton.class);
		
		bind(ProjectModelProviderFactory.class).annotatedWith(Names.named("user.models")).to(UserProjectModelProviderFactoryImpl.class);
		
		Multibinder<ViewerPackage> viewerPackage = Multibinder.newSetBinder(binder(), ViewerPackage.class);
		viewerPackage.addBinding().to(HTMLViewerPackage.class).in(Singleton.class);
		
		Multibinder<PublicationNotifier> publicationNotifiers = Multibinder.newSetBinder(binder(), PublicationNotifier.class);
		if(configuration.getBoolean("publication.notifiers.email"))
			publicationNotifiers.addBinding().to(EmailPublicationNotifier.class);
		if(configuration.getBoolean("publication.notifiers.http"))
			publicationNotifiers.addBinding().to(URLPublicationNotifier.class);
		
		try{
			PublishInterceptor publishInterceptor = new PublishInterceptor();
			requestInjection(publishInterceptor);
			bindInterceptor(Matchers.only(ProjectServiceImpl.class), Matchers.only(ProjectServiceImpl.class.getDeclaredMethod("publish", Project.class)),
					publishInterceptor);
		}catch (Exception ignore) {}
		
		bindInterceptor(Matchers.only(ProjectServiceImpl.class), Matchers.annotatedWith(Interceptable.class),
				new ProjectServiceInterceptor());
		
		requestStaticInjection(Samples.class);
		requestStaticInjection(Templates.class);
		requestStaticInjection(ProjectModels.class);
	}
	
	@Provides
	@Singleton
	protected org.apache.hadoop.conf.Configuration getFileSystemConfiguration() {
		return new org.apache.hadoop.conf.Configuration();
	}
	
	private Properties loadProperties() {
		 Properties properties = new Properties();
		 ClassLoader loader = getClass().getClassLoader();
		 URL url = loader.getResource("application.properties");
		 try {
			properties.load(url.openStream());
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
