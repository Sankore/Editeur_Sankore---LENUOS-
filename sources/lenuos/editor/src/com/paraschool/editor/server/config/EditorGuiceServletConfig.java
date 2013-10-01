package com.paraschool.editor.server.config;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.h2.server.web.WebServlet;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.jndi.JndiIntegration;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.paraschool.editor.server.MonoUserProvider;
import com.paraschool.editor.server.UserProvider;
import com.paraschool.editor.server.Viewers;
import com.paraschool.editor.server.content.models.ProjectModels;
import com.paraschool.editor.server.content.samples.Samples;
import com.paraschool.editor.server.content.templates.Templates;
import com.paraschool.editor.server.rpc.EditorServiceImpl;
import com.paraschool.editor.server.rpc.ExportServlet;
import com.paraschool.editor.server.rpc.InProjectServlet;
import com.paraschool.editor.server.rpc.MimetypesServlet;
import com.paraschool.editor.server.rpc.ProjectServiceImpl;
import com.paraschool.editor.server.rpc.PublishServlet;
import com.paraschool.editor.server.rpc.StatusServlet;
import com.paraschool.editor.server.upload.IconUploadServlet;
import com.paraschool.editor.server.upload.ModelUploadServlet;
import com.paraschool.editor.server.upload.ResourceUploadServlet;
import com.paraschool.editor.server.upload.URLPublicationTestServlet;

/*
 * Created at 30 juil. 2010
 * By bathily
 */
public class EditorGuiceServletConfig extends GuiceServletContextListener {
	
	protected class EditorServletModule extends ServletModule {

		private final Log logger = LogFactory.getLog(EditorGuiceModule.class);
		
		Configuration configuration;
		H2DataSourceProvider dbStarter;
		
		public EditorServletModule() {
			setConfiguration();
		}
		
		private void setConfiguration() {
			/*
			 * User can redefine all configuration
			 * by setting this system property.
			 */
			if(System.getProperty("editor.config") == null)
				System.setProperty("editor.config", "");
			
			ConfigurationFactory factory = new ConfigurationFactory();
			factory.setConfigurationURL(getClass().getResource("/config.xml"));
			try {
				configuration = factory.getConfiguration();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
		
		@Provides
		@Singleton
		protected Configuration provideConfiguration() {
			return configuration;
		}
		
		@Provides @Named("plubish.exporters")
		@Singleton
		@SuppressWarnings("unchecked")
		protected List<String> providePublishExporters() {
			return configuration.getList("publish.exporters");
		}
		
		private void initDbStarter() {
			dbStarter = new H2DataSourceProvider(configuration.getString("db.url"),
					configuration.getString("db.user"),
					configuration.getString("db.password"),
					configuration.getString("db.tcpServer"));
		}
		
		private void bindDataDourceProvider() {
			if(!configuration.getBoolean("use.database")) return;
			
			String source = configuration.getString("jndi.name");
			if(source != null && source.trim().length() != 0)
				bind(DataSource.class).toProvider(JndiIntegration.fromJndi(DataSource.class, source));
			else if((source = configuration.getString("datasource")) != null && source.trim().length() != 0) {
				bind(DataSource.class).toProvider(DataSourceProvider.class);
			} else {
				initDbStarter();
				bind(DataSource.class).toProvider(dbStarter);
			}
		}
		
		@Override
		protected void configureServlets() {
			
			super.configureServlets();
			
			Names.bindProperties(binder(), ConfigurationConverter.getProperties(configuration)) ;
			
			serve("/editor/project","/gip/project").with(ProjectServiceImpl.class);
			serve("/editor/app","/gip/app").with(EditorServiceImpl.class);
			
			serve("/inproject/*").with(InProjectServlet.class);
			serve("/exports/*").with(ExportServlet.class);
			serve("/publications/*").with(PublishServlet.class);
			serve("/rupload").with(ResourceUploadServlet.class);
			serve("/iupload").with(IconUploadServlet.class);
			
			bind(WebServlet.class).in(Singleton.class);
			serve("/databases/*").with(WebServlet.class);
			
			serve("/notify").with(URLPublicationTestServlet.class);
			serve("/status").with(StatusServlet.class);
			serve("/mimetypes").with(MimetypesServlet.class);
			
			bindDataDourceProvider();
			
			if(configuration.getBoolean("security.required")) {
				logger.info("Security activated");
				install(new SecurityGuiceModule(configuration));
			}else{
				logger.info("Security deactivated");
				bind(UserProvider.class).to(MonoUserProvider.class);
			}
			
			if(configuration.getBoolean("use.remote.logging")) {
				serve("/editor/remote_logging","/gip/remote_logging").with(RemoteLoggingServiceImpl.class);
				bind(RemoteLoggingServiceImpl.class).in(Singleton.class);
			}
			if(configuration.getBoolean("can.upload.model")) {
				serve("/mupload").with(ModelUploadServlet.class);
			}
		}
	}
	
	
	private EditorServletModule servletModule;
	
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		
		if(servletModule.dbStarter != null)
			servletModule.dbStarter.stop();
		
		super.contextDestroyed(servletContextEvent);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		try {
			
			if(servletModule.dbStarter != null)
				servletModule.dbStarter.start(servletContextEvent.getServletContext());
			
			Velocity.setApplicationAttribute("javax.servlet.ServletContext", servletContextEvent.getServletContext());
			Velocity.addProperty("file.resource.loader.path", servletModule.configuration.getString("vm.directory"));
			Velocity.init(getClass().getResource("/velocity.properties").getPath());
			
			Samples.initAll();
			ProjectModels.initAll();
			Templates.initAll();
			Viewers.initAll();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Injector getInjector() {
		servletModule  = new EditorServletModule();

		Injector injector = Guice.createInjector(servletModule, 
									new EditorGuiceModule(servletModule.configuration), 
									new ProjectExporterModule());
		
		return injector;
	}
	
}
