package com.paraschool.editor.client;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.logging.client.SimpleRemoteLogHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.presenter.Presenter;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.client.config.ParametersProvider;
import com.paraschool.editor.client.event.app.AuthenticatedEvent;
import com.paraschool.editor.client.event.app.DeauthenticatedEvent;
import com.paraschool.editor.client.event.app.DownloadRequestEvent;
import com.paraschool.editor.client.event.app.DownloadRequestEventHandler;
import com.paraschool.editor.client.event.app.ExportRequestEvent;
import com.paraschool.editor.client.event.app.ParametersLoadedEvent;
import com.paraschool.editor.client.event.app.PreviewRequestEvent;
import com.paraschool.editor.client.event.app.PublishRequestEvent;
import com.paraschool.editor.client.event.app.QuitRequestEvent;
import com.paraschool.editor.client.event.app.SaveRequestEvent;
import com.paraschool.editor.client.event.appmenu.AppMenuEvent;
import com.paraschool.editor.client.event.appmenu.AppMenuEventHandler;
import com.paraschool.editor.client.event.appmenu.DisconnectRequestEvent;
import com.paraschool.editor.client.event.appmenu.QuitAppMenuEvent;
import com.paraschool.editor.client.event.project.CloseProjectEvent;
import com.paraschool.editor.client.event.project.ClosedProjectEvent;
import com.paraschool.editor.client.event.project.CreateProjectEvent;
import com.paraschool.editor.client.event.project.ListProjectEvent;
import com.paraschool.editor.client.event.project.OpenProjectEvent;
import com.paraschool.editor.client.event.project.OpenedProjectEvent;
import com.paraschool.editor.client.event.project.ProjectEvent;
import com.paraschool.editor.client.event.project.ProjectEventHandler;
import com.paraschool.editor.client.i18n.AppConstants;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.CreateProjectPresenter;
import com.paraschool.editor.client.presenter.HomePresenter;
import com.paraschool.editor.client.presenter.ListProjectPresenter;
import com.paraschool.editor.client.presenter.ProjectPresenter;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.AuthenticationServiceAsync;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;
import com.paraschool.editor.client.view.AppLayout;
import com.paraschool.editor.client.view.AppResources;
import com.paraschool.editor.shared.Parameters;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class AppController implements ValueChangeHandler<String> {

	private static final String COOKIE_OPENED_PROJECT_LOCALE = "OPENED_PROJECT_LOCALE";

	private static final String COOKIE_OPENED_PROJECT_PATH = "OPENED_PROJECT_PATH";

	private static final String COOKIE_OPENED_PROJECT_ID = "OPENED_PROJECT_ID";

	{
		AppResources.INSTANCE.appCss().ensureInjected();
	}
	
	private static final Logger logger = Logger.getLogger(AppController.class.getName());
	
	public static final String APP_PLACE = "app";
	public static final String LIST_PROJECT_PLACE = "list";
	public static final String NEW_PROJECT_PLACE = "newProject";
	public static final String HOME_PLACE = "home";
	
	private final AppLayout container;
	private final EditorServiceAsync editorRpcService;
	private final ProjectServiceAsync projectRpcService;
	private final AuthenticationServiceAsync authenticationService;
	private final EventBus eventBus;
	private final AppConstants constants;
	private final AppMessages messages;
	private final ParametersProvider parametersProvider;
	
	
	private Project project;
	private Presenter presenter;
	private boolean canPersistProject = true;
	
	@Inject Provider<ProjectPresenter> projectPresenterProvider;
	@Inject Provider<HomePresenter> homePresenterProvider;
	@Inject Provider<CreateProjectPresenter> createProjectPresenterProvider;
	@Inject Provider<ListProjectPresenter> listProjectPresenterProvider;
	
	@Inject private RpcActionFactory rpcActionFactory;
	
	@Inject
	private AppController(AppLayout container, EditorServiceAsync editorRpcService, ProjectServiceAsync projectRpcService, AuthenticationServiceAsync authenticationService, 
			EventBus eventBus, AppConstants constants, AppMessages messages, ParametersProvider parametersProvider) {
		
		this.container = container;
		this.editorRpcService = editorRpcService;
		this.projectRpcService = projectRpcService;
		this.authenticationService = authenticationService;
		this.eventBus = eventBus;
		this.constants = constants;
		this.messages = messages;
		this.parametersProvider = parametersProvider;
		
		bind();
	}
	
	private void bind() {
		History.addValueChangeHandler(this);
		
		eventBus.addHandler(ProjectEvent.TYPE, new ProjectEventHandler() {
			@Override
			public void onCreateProject(CreateProjectEvent event) {
				doCreateProject();
			}
			
			@Override
			public void onOpenProject(OpenProjectEvent event) {
				doOpenProject(event.getProject());
			}
			
			@Override
			public void onListProject(ListProjectEvent event) {
				doListProject();
			}
			
			@Override
			public void onCloseProject(final CloseProjectEvent event) {
				doCloseProject();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						Project toOpen = null;
						if((toOpen = event.getProjectToOpen()) != null)
							eventBus.fireEvent(new OpenProjectEvent(toOpen));
					}
				});
			}
		});
		
		eventBus.addHandler(AppMenuEvent.TYPE, new AppMenuEventHandler() {
			
			@Override
			public void onSave(AppMenuEvent event) {
				eventBus.fireEvent(new SaveRequestEvent());
			}
			
			@Override
			public void onPublish(AppMenuEvent event) {
				eventBus.fireEvent(new PublishRequestEvent());
			}
			
			@Override
			public void onPreview(AppMenuEvent event) {
				eventBus.fireEvent(new PreviewRequestEvent());
			}
			
			@Override
			public void onExport(AppMenuEvent event) {
				eventBus.fireEvent(new ExportRequestEvent());
			}
			
			@Override
			public void onQuit(QuitAppMenuEvent event) {
				eventBus.fireEvent(new QuitRequestEvent(event.isConfirm()));
			}

			@Override
			public void onDisconnectRequest(AppMenuEvent event) {
				disconnect();
			}
		});
		
		eventBus.addHandler(DownloadRequestEvent.TYPE, new DownloadRequestEventHandler() {
			@Override
			public void download(DownloadRequestEvent event) {
				Frame dFrame = new Frame(event.getUrl()+"&download=true");
				dFrame.setWidth("0px");
				dFrame.setHeight("0px");
				dFrame.setVisible(false);
				RootPanel.get().add(dFrame);
			}
		});
		
		Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

					private void consume(NativePreviewEvent preview) {
						preview.consume();
						preview.getNativeEvent().preventDefault();
					}

					@Override
					public void onPreviewNativeEvent(NativePreviewEvent preview) {
						NativeEvent event = preview.getNativeEvent();


						if(AppUtil.isMeta(event) && event.getAltKey()) {
							int keycode = event.getKeyCode();
							if(keycode == constants.quitProjectKeyStroke()) {
								consume(preview);
								eventBus.fireEvent(new DisconnectRequestEvent());
							}
						}
					}
				});
		
	}
	
	private void disconnect() {
		boolean sso = parametersProvider.getParameters().getIsOpenSankoreSSOEnabled();
		
		if(Window.confirm(sso ? messages.willDisconnectToSSO() : messages.willDisconnect())) {
			if(!sso)
				authenticationService.logout(new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {}
					@Override
					public void onSuccess(Void result) {
						eventBus.fireEvent(new DeauthenticatedEvent());
						if(project != null)
							eventBus.fireEvent(new QuitRequestEvent(false));
						else
							History.newItem("");
					}
				});
			else {
				Window.Location.replace(parametersProvider.getParameters().getOpenSankoreSSOExitUrl());
			}
		}
			
			
	}
	
	private void doCreateProject() {
		History.newItem(NEW_PROJECT_PLACE);
	}
	
	private void doOpenProject(final Project project) {
		this.project = project;
		
		presenter = projectPresenterProvider.get();
		presenter.go(container.getContent());
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				if(canPersistProject) {
					History.newItem(APP_PLACE, false);
					persist();
				}
				eventBus.fireEvent(new OpenedProjectEvent(project));
			}
		});

	}
	
	private void doListProject() {
		History.newItem(LIST_PROJECT_PLACE);
	}
	
	private void doCloseProject() {
		logger.fine("Close project");
		if(canPersistProject) {
			unpersisted();
			History.newItem(HOME_PLACE, true);
		}
		eventBus.fireEvent(new ClosedProjectEvent(project));
		this.project = null;
	}
	
	private void persist() {
		if(project != null) {
			logger.fine("Create project cookies");
			ProjectDetails details = project.getDetails();
			Cookies.setCookie(COOKIE_OPENED_PROJECT_ID, details.getId());
			String path = null;
			if((path = details.getPath()) != null)
				Cookies.setCookie(COOKIE_OPENED_PROJECT_PATH, path);
			String locale = null;
			if((locale = details.getLocale()) != null)
				Cookies.setCookie(COOKIE_OPENED_PROJECT_LOCALE, locale);
		}
	}
	
	private void unpersisted() {
		logger.fine("Remove project cookies");
		Cookies.removeCookie(COOKIE_OPENED_PROJECT_ID);
		Cookies.removeCookie(COOKIE_OPENED_PROJECT_PATH);
		Cookies.removeCookie(COOKIE_OPENED_PROJECT_LOCALE);
	}
	
	private ProjectDetails getPersisted() {
		String id = null;
		if((id = Cookies.getCookie(COOKIE_OPENED_PROJECT_ID)) != null)
			return new ProjectDetails(id, Cookies.getCookie(COOKIE_OPENED_PROJECT_PATH), Cookies.getCookie(COOKIE_OPENED_PROJECT_LOCALE));
		return null;
	}

	private void openExplicitedProject(final ProjectDetails details) {
		AsyncCallback<Project> callback = new AsyncCallback<Project>() {
			public void onFailure(Throwable caught) {
				History.newItem(HOME_PLACE);
			}
			public void onSuccess(Project result) {
				if(result == null)
					History.newItem(HOME_PLACE);
				else
					eventBus.fireEvent(new OpenProjectEvent(result));
			}
		};
		rpcActionFactory.<Project>create(callback, new RpcAttempt<Project>(){
			@Override
			public void call(AsyncCallback<Project> callback) {
				projectRpcService.get(details, callback);
			}
		}).attempt();
	}
	
	public Project getProject() {
		return project;
	}
	
	public void go() {
		
		editorRpcService.getParameters(new AsyncCallback<Parameters>() {

			@Override
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, "Initialization failed",caught);
				Window.alert(messages.unableToLoadApplication());
			}

			@Override
			public void onSuccess(Parameters result) {
				
				parametersProvider.setParameters(result);
				eventBus.fireEvent(new ParametersLoadedEvent(parametersProvider.getParameters()));
				
				initRootLogger(result.getUseInViewLogging(), result.getUseRemoteLogging());
				
				logger.fine("Version "+result.getApplicationVersion()+"-"+result.getApplicationBuild()+" build at "+result.getApplicationBuildDate());
				logger.fine("App controller initialization...");
				
				DOM.getElementById("loading").removeFromParent();
				RootPanel.get().add(container);
				
				logger.fine("Initialization finish");
				
				if("".equals(History.getToken())) History.newItem(HOME_PLACE);
				else History.fireCurrentHistoryState();
				
				if(result.getConnected())
					eventBus.fireEvent(new AuthenticatedEvent(result.getAuthor()));
			}
		
		});
		
		
	}
	
	private void initRootLogger(boolean useInview, boolean useRemote) {
		Logger root = Logger.getLogger("");
		
		if(useInview && container.getLoggingPanel() != null){
			root.addHandler(new HasWidgetsLogHandler(container.getLoggingPanel()));
		}
		if(!useRemote) {
			Handler[] handlers = root.getHandlers();
			for(Handler h : handlers){
				if(h.getClass().equals(SimpleRemoteLogHandler.class))
					root.removeHandler(h);
			}
		}
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		
		final String token = event.getValue();
		
		if(token != null) {
			
			if(presenter != null) {
				logger.fine("Found a presenter. Clean it before create a new one.");
				presenter.clear();
				presenter = null;
			}
			
			if(token.equals(HOME_PLACE)) {
				Window.setTitle(messages.appTitle());
				presenter = homePresenterProvider.get();
			}
			else if(token.equals(NEW_PROJECT_PLACE)) presenter = createProjectPresenterProvider.get();
			else if(token.equals(LIST_PROJECT_PLACE)) presenter = listProjectPresenterProvider.get();
			
			if(presenter != null)
				presenter.go(container.getContent());
			
			else if(token.equals(APP_PLACE)) {
				canPersistProject = true;
				if(project == null) {
					final ProjectDetails persisted = getPersisted();
					if(persisted == null) {
						History.newItem(HOME_PLACE);
						return;
					}
					openExplicitedProject(persisted);
				}else{
					doOpenProject(project);
				}
				
			}else {
				canPersistProject = false;
				
				String[] tokens = token.split("!");
				final String id = tokens[0];
				final String path = tokens.length > 1 ? tokens[1] : null;
				final String locale = Window.Location.getParameter("plocale");
				if(id != null && id.trim().length() != 0)
					openExplicitedProject(new ProjectDetails(id, path, locale));
				else
					History.newItem(HOME_PLACE);
			}
		}
	}
}
