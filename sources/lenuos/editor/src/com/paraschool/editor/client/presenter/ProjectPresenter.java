package com.paraschool.editor.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.Resource2ModuleObject;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.ProjectFieldVerifier;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.Template;
import com.paraschool.commons.share.TemplateDetails;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ObjectEditCallback;
import com.paraschool.editor.api.client.event.ModuleObjectChangeEvent;
import com.paraschool.editor.api.client.event.ModuleObjectDeleteEvent;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.ResourceStore;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.event.app.AppRequestEvent;
import com.paraschool.editor.client.event.app.AppRequestEventHandler;
import com.paraschool.editor.client.event.app.CreateModelRequestEvent;
import com.paraschool.editor.client.event.app.DownloadRequestEvent;
import com.paraschool.editor.client.event.app.ErrorMessageEvent;
import com.paraschool.editor.client.event.app.ExportRequestEvent;
import com.paraschool.editor.client.event.app.InfoMessageEvent;
import com.paraschool.editor.client.event.app.PreviewRequestEvent;
import com.paraschool.editor.client.event.app.PublishRequestEvent;
import com.paraschool.editor.client.event.app.QuitRequestEvent;
import com.paraschool.editor.client.event.app.SaveRequestEvent;
import com.paraschool.editor.client.event.app.WarningMessageEvent;
import com.paraschool.editor.client.event.page.AddPageEvent;
import com.paraschool.editor.client.event.page.DeletedResourceEvent;
import com.paraschool.editor.client.event.page.HidePageEvent;
import com.paraschool.editor.client.event.page.MovePageEvent;
import com.paraschool.editor.client.event.page.PageEvent;
import com.paraschool.editor.client.event.page.PageEventHandler;
import com.paraschool.editor.client.event.page.PageNavigationEvent;
import com.paraschool.editor.client.event.page.PageNavigationEventHandler;
import com.paraschool.editor.client.event.page.ShowPageEvent;
import com.paraschool.editor.client.event.page.UploadedResourceEvent;
import com.paraschool.editor.client.event.project.ChangeResourceEvent;
import com.paraschool.editor.client.event.project.CloseProjectEvent;
import com.paraschool.editor.client.event.project.EditEvent;
import com.paraschool.editor.client.event.project.EditEventHandler;
import com.paraschool.editor.client.event.project.EditObjectRequestEvent;
import com.paraschool.editor.client.event.project.ResourceEvent;
import com.paraschool.editor.client.event.project.ResourceEventHandler;
import com.paraschool.editor.client.i18n.AppConstants;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.i18n.LocalizableResource;
import com.paraschool.editor.client.presenter.factory.PagePresenterFactory;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;
import com.paraschool.editor.client.view.PageNavigation;
import com.paraschool.editor.shared.LocaleDTO;
import com.paraschool.editor.shared.ProjectExporterDescriptor;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class ProjectPresenter extends DefaultPresenter {

	private static final Logger logger = Logger.getLogger(ProjectPresenter.class.getName());
	private static final Map<String, String> exportOptions = new HashMap<String, String>();
	{
		exportOptions.put("scorm.version", "2004");
	}
	
	
	public interface ExportCallback {
		void doExport(List<String> exporterIds);
	}
	
	public class SaveAsyncCallback implements AsyncCallback<Boolean> {
		AsyncCallback<Boolean> callback;
		public SaveAsyncCallback(AsyncCallback<Boolean> callback) {
			this.callback = callback;
		}
		@Override
		public void onFailure(Throwable caught) {
			eventBus.fireEvent(new WarningMessageEvent(messages.projectSaveError(project.getDetails().getName())));
			onFailure(caught);
		}
		@Override
		public void onSuccess(Boolean result) {
			logger.fine("Save finish with result ["+result+"]");
			if(result) {
				eventBus.fireEvent(new InfoMessageEvent(messages.projectSaveSuccess(project.getDetails().getName())));
			}else{
				eventBus.fireEvent(new WarningMessageEvent(messages.projectSaveError(project.getDetails().getName())));
			}
			callback.onSuccess(result);
		}
	}
	
	public interface Display extends com.paraschool.commons.client.presenter.Display {
		PageNavigation getNavigation();
		
		Button getAddPageButton();
		Button getShowResourcesButton();

		HasClickHandlers getShowProjectFormButton();
		HasClickHandlers getHideProjectFormButton();
		HasWidgets getPagesContainer();
		HasText getProjectName();
		HasWidgets getFormContainer();
		
		void pageAdded(int index);
		void pageRemoved(int index);
		void pageMoved(int source, int destination);
		
		int currentPageIndex();
		
		void showPage(int index);
		void hidePage(int index);
		
		void showForm();
		void hideForm();
	}
	
	public interface ProjectFormDisplay extends com.paraschool.commons.client.presenter.Display, com.paraschool.editor.client.presenter.ProjectFormDisplay {
		void setProjectLocales(List<LocaleDTO> locales);
		void setAvailableLocales(List<LocaleDTO> locales);
		
		String getLocaleForChange();
		String getLocaleForCreation();
		String getLocaleForDeletion();
		Button createProjectLocaleButton();
		Button changeProjectLocaleButton();
		Button deleteProjectLocaleButton();
		HasClickHandlers exportAllPagesButton();
		HasClickHandlers cleanButton();
		
		void clear();
		void removeLocale(String locale);
	}
	
	public interface ExportResultDisplay extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getCloseButton();
		void setExportResult(ProjectDetails details, List<String> urls);
		void show();
		void hide();
	}

	private final Project project;
	private final ProjectFormDisplay form;
	private final ExportResultDisplay exportResultDisplay;
	private final Map<String, Template> usedTemplates;

	private ArrayList<PagePresenter> pagePresenters;
	
	@Inject private RpcActionFactory rpcActionFactory;
	@Inject AppMessages messages;
	@Inject LocalizableResource resources;
	@Inject PagePresenterFactory pagePresenterFactory;
	@Inject AppConstants constants;
	@Inject ResourceStore resourceStore;
	@Inject TemplateListPresenter templateListPresenter;
	@Inject PreviewPresenter previewPresenter;

	@Inject
	private ProjectPresenter(Project project, EditorServiceAsync editorRpcService, 
			ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Display display, ProjectFormDisplay form, ExportResultDisplay exportResultDisplay) {

		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, display);
		
		this.project = project;
		
		//TODO Remove when project width & height can be edited
		this.project.getDetails().setWidth(ProjectDetails.DEFAULT_WIDTH);
		this.project.getDetails().setHeight(ProjectDetails.DEFAULT_HEIGTH);
		
		this.form = form;
		this.exportResultDisplay = exportResultDisplay;
		usedTemplates = new HashMap<String, Template>();
		pagePresenters = new ArrayList<PagePresenter>();
		
	}

	private void setProjectName() {
		ProjectDetails details = project.getDetails(); 
		String name = details.getName() + (details.getLocale() == null ? "" : " ("+details.getLocaleName()+")");
		((Display)display).getProjectName().setText(name);
		Window.setTitle(name);
	}

	private void addPage(final Page page) {
		project.addPage(page);
		getTemplateForPageAndGoPresenter(page,project.getPages().size()-1, false);
	}

	private void goPagePresenter(final Template template, final Page page, final int index, final boolean openNext) {
		PageNavigation nav = ((Display)display).getNavigation();
		
		int tmp = (tmp = nav.getSelectedPage()) == -1 ? index - 1: tmp;
		nav.unselectPage(tmp);
		
		addPagePresenter(page, template, index);
		nav.selectPage(index);
		
		if(openNext && index < project.getPages().size() - 1)
			getTemplateForPageAndGoPresenter(project.getPages().get(index+1), index+1, openNext);
	}
	
	private void getTemplateForPageAndGoPresenter(final Page page, final int index, final boolean openNext) {
		Template template;
		if((template = usedTemplates.get(page.getTemplateDetails().getId())) != null){
			goPagePresenter(template, page, index, openNext);
			
		}else{
			AsyncCallback<Template> callback = new AsyncCallback<Template>() {
				@Override
				public void onFailure(Throwable caught) {}
				@Override
				public void onSuccess(Template result) {
					if(result != null) {
						usedTemplates.put(result.getDetails().getId(), result);
						goPagePresenter(result, page, index, openNext);
					}
				}
			};

			rpcActionFactory.<Template>create(callback, new RpcAttempt<Template>() {
				@Override
				public void call(AsyncCallback<Template> callback) {
					editorRpcService.getTemplate(page.getTemplateDetails().getId(), callback);
				}
			}).attempt();
		}
	}

	private void addPagePresenter(Page page, Template template, int index) {
		PagePresenter pagePresenter = pagePresenterFactory.create(page, project); 
		pagePresenters.add(pagePresenter);
		pagePresenter.go(((Display)display).getPagesContainer());
		((Display)display).pageAdded(index);
	}

	private int removePage(Page page) {
		int index = project.removePage(page);
		((Display)display).pageRemoved(index);
		PagePresenter pagePresenter = pagePresenters.get(index);
		pagePresenter.clear();
		
		((Display)display).getNavigation().selectPage(index == 0 && project.getPages().size() > 0 ? 0 : index - 1);
		
		return index;
	}

	private void saveAllPage() {
		for(PagePresenter presenter : pagePresenters) {
			presenter.save();
		}
	}
	
	private void saveProject(AsyncCallback<Boolean> callback) {
		eventBus.fireEvent(new InfoMessageEvent(messages.projectOnSave(project.getDetails().getName())));
		logger.fine("Save project");
		try{
			saveAllPage();
			SaveAsyncCallback saveCallback = new SaveAsyncCallback(callback);
			rpcActionFactory.<Boolean>create(saveCallback, new RpcAttempt<Boolean>() {
				@Override
				public void call(AsyncCallback<Boolean> callback) {
					projectRpcService.save(project, callback);
				}
			}).attempt();
		}catch (Throwable e) {
			e.printStackTrace();
			eventBus.fireEvent(new ErrorMessageEvent(e.getClass()+""));
		}
	}

	private void saveProject(final boolean andQuit) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(Boolean result) {
				if(result)
					if(andQuit) eventBus.fireEvent(new CloseProjectEvent(project, null));
			}
		};
		saveProject(callback);
	}
	
	private void saveProjectAndCreateLocale(final String locale) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(Boolean result) {
				if(result)
					createLocale(locale);
			}
		};
		saveProject(callback);
	}
	
	private void saveProjectAndChangeToLocale(final String locale) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(Boolean result) {
				if(result)
					changeToLocale(locale);
			}
		};
		saveProject(callback);
	}
	
	private void createLocale(final String locale) {
		logger.fine("Send new locale project request");
		AsyncCallback<Project> callback = new AsyncCallback<Project>() {
			private void failed() {
				String msg = resources.failedToCreateLocale(locale);
				logger.fine(msg);
				Window.alert(msg);
			}
			@Override
			public void onFailure(Throwable caught) {
				failed();
			}

			@Override
			public void onSuccess(Project result) {
				if(result == null){
					failed();
					return;
				}
				logger.fine("Locale creation for project succeed.");
				eventBus.fireEvent(new CloseProjectEvent(project, result));
			}
		};
		rpcActionFactory.<Project>create(callback, new RpcAttempt<Project>() {
			@Override
			public void call(AsyncCallback<Project> callback) {
				projectRpcService.create(project.getDetails(), locale, callback);
			}
		}).attempt();
	}
	
	private void changeToLocale(final String locale) {
		logger.fine("Get project for locale "+locale);
		AsyncCallback<Project> callback = new AsyncCallback<Project>() {
			private void failed() {
				String msg = resources.failedToGetLocale(locale);
				logger.fine(msg);
				Window.alert(msg);
			}
			@Override
			public void onFailure(Throwable caught) {
				failed();
			}
			@Override
			public void onSuccess(Project result) {
				if(result == null) {
					failed();
					return;
				}
				logger.fine("Successful retrieve project for locale "+locale);
				eventBus.fireEvent(new CloseProjectEvent(project, result));
			}
		};
		rpcActionFactory.<Project>create(callback, new RpcAttempt<Project>() {
			@Override
			public void call(AsyncCallback<Project> callback) {
				projectRpcService.get(project.getDetails(), locale, callback);
			}
		}).attempt();
	}
	
	private void deleteProjectLocale(final String locale) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			private void failed() {
				String msg = resources.failedToDeleteLocale(locale);
				logger.fine(msg);
				Window.alert(msg);
			}
			@Override
			public void onFailure(Throwable caught) {
				failed();
			}

			@Override
			public void onSuccess(Boolean result) {
				if(!result)
					failed();
				else
					form.removeLocale(locale);
			}
		};
		rpcActionFactory.<Boolean>create(callback, new RpcAttempt<Boolean>() {
			@Override
			public void call(AsyncCallback<Boolean> callback) {
				projectRpcService.deleteLocale(project.getDetails(), locale, callback);
			}
		}).attempt();
	}
	
	//TODO quand il y aura une fenètre de choix d'exporter améliorer cette implémentation.
	protected void getExporters(final ExportCallback exportCallback) {
		AsyncCallback<ArrayList<ProjectExporterDescriptor>> callback = new AsyncCallback<ArrayList<ProjectExporterDescriptor>>() {
			@Override
			public void onFailure(Throwable caught) {}

			@Override
			public void onSuccess(ArrayList<ProjectExporterDescriptor> result) {
				final ArrayList<String> exportersId = new ArrayList<String>();
				for(final ProjectExporterDescriptor descriptor : result) {
					exportersId.add(descriptor.getId());
				}
				exportCallback.doExport(exportersId);
			}
		};

		rpcActionFactory.<ArrayList<ProjectExporterDescriptor>>create(callback, new RpcAttempt<ArrayList<ProjectExporterDescriptor>>() {
			@Override
			public void call(AsyncCallback<ArrayList<ProjectExporterDescriptor>> callback) {
				editorRpcService.getExporters(callback);
			}
		}).attempt();
		
	}
	
	protected void doExport(final List<String> exportersId) {
		if(!checkProjectMandatoryDetailsBeforeExport()) return;
		
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(String result) {
				if(result != null){
					eventBus.fireEvent(new DownloadRequestEvent(AppUtil.makeExportURL(project.getDetails(), result)));
					eventBus.fireEvent(new InfoMessageEvent(messages.projectExportSuccess(project.getDetails().getName())));
				}else
					eventBus.fireEvent(new WarningMessageEvent(messages.projectExportError(project.getDetails().getName())));
			}
		};

		rpcActionFactory.<String>create(callback, new RpcAttempt<String>() {
			@Override
			public void call(AsyncCallback<String> callback) {
				projectRpcService.export(project, exportOptions, exportersId, callback);
			}
		}).attempt();
	}
	
	protected void doExportAPage(final List<String> exportersId, final Page page) {
		if(!checkProjectMandatoryDetailsBeforeExport()) return;
		
		final int pageIndex = project.getPageIndex(page);
		
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(String result) {
				if(result != null){
					eventBus.fireEvent(new DownloadRequestEvent(AppUtil.makeExportURL(project.getDetails(), result)));
					eventBus.fireEvent(new InfoMessageEvent(messages.projectExportSuccess(project.getDetails().getName())));
				}else
					eventBus.fireEvent(new WarningMessageEvent(messages.projectExportError(project.getDetails().getName())));
			}
		};

		rpcActionFactory.<String>create(callback, new RpcAttempt<String>() {
			@Override
			public void call(AsyncCallback<String> callback) {
				projectRpcService.exportPageToProject(project, exportOptions, exportersId, pageIndex, callback);
			}
		}).attempt();
	}
	
	protected void doExportAllPage(final List<String> exportersId) {
		if(!checkProjectMandatoryDetailsBeforeExport()) return;
		
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(List<String> result) {
				exportResultDisplay.setExportResult(project.getDetails(), result);
			}
		};

		rpcActionFactory.<List<String>>create(callback, new RpcAttempt<List<String>>() {
			@Override
			public void call(AsyncCallback<List<String>> callback) {
				projectRpcService.exportAfterPageSplitted(project, exportOptions, exportersId, callback);
			}
		}).attempt();
		hideProjectForm();
		exportResultDisplay.show();
	}
	
	
	protected void doPublish() {
		logger.fine("Publication requested");
		if(!checkProjectMandatoryDetailsBeforeExport()) return;
		
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(String result) {
				if(result != null)
					eventBus.fireEvent(new InfoMessageEvent(messages.projectPublishSuccess(project.getDetails().getName())));
				else
					eventBus.fireEvent(new WarningMessageEvent(messages.projectPublishError(project.getDetails().getName())));
			}
		};

		rpcActionFactory.<String>create(callback, new RpcAttempt<String>() {
			@Override
			public void call(AsyncCallback<String> callback) {
				projectRpcService.publish(project, exportOptions, callback);
			}
		}).attempt();
	}
	
	private boolean checkProjectMandatoryDetailsBeforeExport() {
		ProjectDetails details = project.getDetails();
		if(details.getObjectif() == null || details.getObjectif().trim().length() == 0 ||
			details.getDescription() == null || details.getDescription().trim().length() == 0){
			Window.alert(messages.someProjectDetailsAreMissing());
			showProjectForm();
			return false;
		}
		return true;
	}
	
	private void showResources(ObjectEditCallback callback, ModuleObject.Type ...types) {
		resourceStore.get(callback, types);
	}
	
	private void createProjectModel() {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(Boolean result) {
				if(result) {
					eventBus.fireEvent(new InfoMessageEvent(messages.modelCreationSucceed(project.getDetails().getName())));
				}else{
					eventBus.fireEvent(new WarningMessageEvent(messages.modelCreationError(project.getDetails().getName())));
				}
			}
		};
		rpcActionFactory.<Boolean>create(callback, new RpcAttempt<Boolean>() {
			@Override
			public void call(AsyncCallback<Boolean> callback) {
				projectRpcService.createModel(project.getDetails(), callback);
			}
		}).attempt();

	}
	
	private void showProjectForm() {
		form.setDetails(project.getDetails());
		((Display)display).showForm();
		AsyncCallback<List<LocaleDTO>> callback = new AsyncCallback<List<LocaleDTO>>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(List<LocaleDTO> result) {
				form.setProjectLocales(result);
			}
		};
		rpcActionFactory.<List<LocaleDTO>>create(callback, new RpcAttempt<List<LocaleDTO>>() {
			@Override
			public void call(AsyncCallback<List<LocaleDTO>> callback) {
				projectRpcService.getLocales(project.getDetails(), callback);
			}
		}).attempt();
		
		AsyncCallback<List<LocaleDTO>> callback2 = new AsyncCallback<List<LocaleDTO>>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(List<LocaleDTO> result) {
				form.setAvailableLocales(result);
			}
		};
		rpcActionFactory.<List<LocaleDTO>>create(callback2, new RpcAttempt<List<LocaleDTO>>() {
			@Override
			public void call(AsyncCallback<List<LocaleDTO>> callback) {
				editorRpcService.supportedLocales(callback);
			}
		}).attempt();
	}
	
	private void hideProjectForm() {
		((Display)display).hideForm();
	}
	
	private void fetchTemplates() {
		AsyncCallback<ArrayList<TemplateDetails>> callback = new AsyncCallback<ArrayList<TemplateDetails>>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(ArrayList<TemplateDetails> result) {
				if(result.size() == 1){
					Page page = new Page(result.get(0));
					eventBus.fireEvent(new AddPageEvent(page));
				}else{
					templateListPresenter.clear();
					templateListPresenter.setDate(result);
					templateListPresenter.go(null);
				}
			}
		};
		
		rpcActionFactory.<ArrayList<TemplateDetails>>create(callback, new RpcAttempt<ArrayList<TemplateDetails>>() {
			@Override
			public void call(AsyncCallback<ArrayList<TemplateDetails>> callback) {
				editorRpcService.getTemplates(callback);
			}
		}).attempt();
	}
	
	private void clean() {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Boolean result) {
				Window.alert(messages.cleanThanks());
			}
		};
		
		rpcActionFactory.<Boolean>create(callback, new RpcAttempt<Boolean>() {
			@Override
			public void call(AsyncCallback<Boolean> callback) {
				projectRpcService.clean(project.getDetails(), callback);
			}
		}).attempt();
	}
	
	
	@Override
	protected void bind() {
		
		registrations.add(
				form.getSubmitButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						ProjectDetails newDetails = form.getDetails();
						if(ProjectFieldVerifier.isValidProjectDetail(newDetails)){
							ProjectDetails details = project.getDetails();
							
							newDetails.setId(details.getId());
							newDetails.setPath(details.getPath());
							newDetails.setDate(details.getDate());
							
							//TODO
							newDetails.setWidth(details.getWidth());
							newDetails.setHeight(details.getHeight());
							
							project.setDetails(newDetails);
							setProjectName();
							hideProjectForm();
						}
					}
				})
		);
		
		registrations.add(
				form.getCancelButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						hideProjectForm();
					}
				})
		);
		
		registrations.add(
				form.createProjectLocaleButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String locale = form.getLocaleForCreation();
						logger.fine("Create new version for locale "+locale);
						String msg = project.getDetails().getLocale() == null ? resources.performSaveForCreateLocaleDefaultMessage(locale) :
							resources.performSaveForCreateLocaleMessage(locale, project.getDetails().getLocale());
						
						if(Window.confirm(msg)){
							hideProjectForm();
							saveProjectAndCreateLocale(locale);
						}else {
							hideProjectForm();
							createLocale(locale);
						}
					}
				})
		);
		
		registrations.add(
				form.changeProjectLocaleButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String locale = form.getLocaleForChange();
						logger.fine("Change project to locale "+locale);
						String msg = locale == null ? resources.performSaveForChangeLocaleDefaultMessage() :
							resources.performSaveForChangeLocaleMessage(locale);
						
						if(Window.confirm(msg)){
							hideProjectForm();
							saveProjectAndChangeToLocale(locale);
						}else{
							hideProjectForm();
							changeToLocale(locale);
						}
							
					}
				})
		);
		
		registrations.add(
				form.deleteProjectLocaleButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String locale = form.getLocaleForChange();
						logger.fine("Delete project to locale "+locale);
						if(Window.confirm(resources.confirmLocaleDeletion(locale)))
							deleteProjectLocale(locale);
					}
				})
		);
		
		registrations.add(
				form.exportAllPagesButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						getExporters(new ProjectPresenter.ExportCallback() {
							@Override
							public void doExport(List<String> exporterIds) {
								doExportAllPage(exporterIds);
							}
						});
					}
				})
		);
		
		registrations.add(
				form.cleanButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						clean();
					}
				})
		);
		
		registrations.add(
				exportResultDisplay.getCloseButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						exportResultDisplay.hide();
					}
				})
		);
		
		registrations.add(
				((Display)display).getAddPageButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						fetchTemplates();
					}
				})
		);

		registrations.add(
				((Display)display).getShowResourcesButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						showResources(null);
					}
				})
		);
		
		registrations.add(
				((Display)display).getShowProjectFormButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						showProjectForm();
					}
				})
		);
		
		registrations.add(
				((Display)display).getHideProjectFormButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						hideProjectForm();
					}
				})
		);

		registrations.add(
				eventBus.addHandler(PageEvent.TYPE, new PageEventHandler() {
					@Override
					public void onRemovePage(PageEvent event) {
						removePage(event.getPage());
					}
					@Override
					public void onAddPage(PageEvent event) {
						addPage(event.getPage());
					}
					@Override
					public void onExportPage(final PageEvent event) {
						getExporters(new ProjectPresenter.ExportCallback() {
							@Override
							public void doExport(List<String> exporterIds) {
								doExportAPage(exporterIds, event.getPage());
							}
						});
					}
				})
		);
		
		registrations.add(
				eventBus.addHandler(PageNavigationEvent.TYPE, new PageNavigationEventHandler() {
					@Override
					public void show(ShowPageEvent event) {
						((Display)display).showPage(event.getIndex());
					}

					@Override
					public void hide(HidePageEvent event) {
						((Display)display).hidePage(event.getIndex());
					}

					@Override
					public void move(MovePageEvent event) {
						int source = event.getIndex();
						int destination = event.getDestination();
						
						ArrayList<Page> pages = project.getPages();
						Page p = pages.get(source);
						pages.remove(p);
						
						if(destination == pages.size())
							pages.add(p);
						else
							pages.add( source < destination ? destination - 1 : destination, p);
						
						((Display)display).pageMoved(source, destination);
						((Display)display).getNavigation().selectPage(destination);
					}
				})
		);
		
		registrations.add(
				eventBus.addHandler(ResourceEvent.TYPE, new ResourceEventHandler() {
					@Override
					public void onUploaded(UploadedResourceEvent event) {
						Resource resource = event.getResource();
						
						if(LogConfiguration.loggingIsEnabled() && logger.isLoggable(Level.FINE))
							logger.fine("Receive new UploadResourceEvent. Add the resource ["+resource.getUrl()+"] with id ["+resource.getId()+"] to the project");
						
						project.addResource(resource);
					}

					@Override
					public void onDeleted(DeletedResourceEvent event) {
						Resource resource = event.getResource();
						project.removeResource(resource);
						ModuleObject object = Resource2ModuleObject.toModuleObject(resource);
						eventBus.fireEvent(new ModuleObjectDeleteEvent(object));
					}

					@Override
					public void onChange(ChangeResourceEvent event) {
						Resource resource = event.getResource();
						project.addResource(resource);
						ModuleObject object = Resource2ModuleObject.toModuleObject(resource);
						eventBus.fireEvent(new ModuleObjectChangeEvent(object));
					}
				})
		);

		registrations.add(
				eventBus.addHandler(AppRequestEvent.TYPE, new AppRequestEventHandler() {

					@Override
					public void onSaveRequest(AppRequestEvent event) {
						saveProject(false);
					}

					@Override
					public void onPublishRequest(AppRequestEvent event) {
						doPublish();
					}

					@Override
					public void onPreviewRequest(AppRequestEvent event) {
						saveAllPage();
						previewPresenter.go(null);
						previewPresenter.previewProject(project, ((Display)display).currentPageIndex());
					}

					@Override
					public void onExportRequest(AppRequestEvent event) {
						getExporters(new ProjectPresenter.ExportCallback() {
							@Override
							public void doExport(List<String> exporterIds) {
								ProjectPresenter.this.doExport(exporterIds);
							}
						});
					}

					@Override
					public void onQuitRequest(QuitRequestEvent event) {
						if(event.isConfirm() && Window.confirm(resources.performSaveMessage()))
							saveProject(true);
						else
							eventBus.fireEvent(new CloseProjectEvent(project, null));
					}

					@Override
					public void onCreateModelRequest(AppRequestEvent event) {
						createProjectModel();
					}

				})
		);
		
		registrations.add(
				eventBus.addHandler(EditEvent.TYPE, new EditEventHandler() {
					@Override
					public void onObjectEdited(EditObjectRequestEvent event) {
						showResources(event.getCallback(), event.getType());
					}
				})
		);

		registrations.add(
				Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

					private void consume(NativePreviewEvent preview) {
						preview.consume();
						preview.getNativeEvent().preventDefault();
					}

					@Override
					public void onPreviewNativeEvent(NativePreviewEvent preview) {
						NativeEvent event = preview.getNativeEvent();


						if(AppUtil.isMeta(event)) {
							int keycode = event.getKeyCode();

							AppRequestEvent appRequestEvent = null;
							
							if( !event.getAltKey()) {
								if(keycode == constants.saveProjectKeyStroke()) {
									appRequestEvent = new SaveRequestEvent();
								}else if(keycode == constants.previewProjectKeyStroke()) {
									appRequestEvent = new PreviewRequestEvent();
								}else if(keycode == constants.exportProjectKeyStroke()) {
									appRequestEvent = new ExportRequestEvent();
								}else if(keycode == constants.publishProjectKeyStroke()) {
									appRequestEvent = new PublishRequestEvent();
								}else if(keycode == constants.quitProjectKeyStroke()) {
									appRequestEvent = new QuitRequestEvent(true);
								}else if(keycode == constants.newPageKeyStroke()) {
									consume(preview);
									((Display)display).getAddPageButton().click();
									return;
								}
							}else{
								if(keycode == constants.resourceKeyStroke()) {
									consume(preview);
									((Display)display).getShowResourcesButton().click();
									return;
								}else if(keycode == constants.createModelKeyStroke()) {
									appRequestEvent = new CreateModelRequestEvent();
								}
							}	

							if(appRequestEvent != null) {
								consume(preview);
								eventBus.fireEvent(appRequestEvent);
								return;
							}

						}else if(event.getAltKey() && preview.getTypeInt() == 128) {
							int keycode = event.getKeyCode();
							switch (keycode) {
							case KeyCodes.KEY_LEFT:
								((Display)display).getNavigation().showPreviousPage();
								consume(preview);
								break;
							case KeyCodes.KEY_RIGHT:
								((Display)display).getNavigation().showNextPage();
								consume(preview);
								break;
							default:
								break;
							}
						}

					}
				})
		);
	}
	
	@Override
	public void go(final HasWidgets container) {
		super.go(container);
		hideProjectForm();
		((Display)display).getFormContainer().add(form.asWidget());
		if(project != null){
			setProjectName();
			if(project.getPages().size() > 0) {
				getTemplateForPageAndGoPresenter(project.getPages().get(0), 0, true);
			}
		}
	}

	@Override
	public void clear() {
		form.clear();
		super.clear();
	}
	
	

}
