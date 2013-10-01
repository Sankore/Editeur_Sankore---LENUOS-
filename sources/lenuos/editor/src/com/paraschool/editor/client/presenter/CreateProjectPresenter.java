package com.paraschool.editor.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Author;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.ProjectFieldVerifier;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.AppController;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.config.ParametersProvider;
import com.paraschool.editor.client.event.project.OpenProjectEvent;
import com.paraschool.editor.client.i18n.LocalizableResource;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;
import com.paraschool.editor.shared.ProjectModel.Owner;
import com.paraschool.editor.shared.ProjectModelRequest;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class CreateProjectPresenter extends DefaultPresenter {

	public interface Display extends ProjectFormDisplay {
		FormPanel getUploadModelForm();
		FileUpload getModelFileUpload();
		Button getDeleteModelButton();
		
		void setCanUploadModel(boolean canUpload);
		void setModels(HashMap<Owner, ArrayList<ProjectDetails>> modelMap);
		ProjectModelRequest getModelRequest();
		
	}

	@Inject private RpcActionFactory rpcActionFactory;
	@Inject ParametersProvider parameters;
	@Inject LocalizableResource resources;
	
	@Inject
	private CreateProjectPresenter(EditorServiceAsync editorRpcService, ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Display display) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, display);
	}
	
	@Override
	protected void bind() {
		registrations.add(
			((Display)display).getCancelButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					History.newItem(AppController.HOME_PLACE);
				}
			})
		);
		
		registrations.add(
			((Display)display).getSubmitButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					
					final ProjectDetails details = ((Display)display).getDetails();
					
					if(ProjectFieldVerifier.isValidProjectDetail(details)){
						AsyncCallback<Project> callback = new AsyncCallback<Project>() {
							public void onFailure(Throwable caught) {}
							public void onSuccess(Project result) {
								eventBus.fireEvent(new OpenProjectEvent(result));
							}
						};

						rpcActionFactory.<Project>create(callback, new RpcAttempt<Project>() {
							@Override
							public void call(AsyncCallback<Project> callback) {
								projectRpcService.create(details, ((Display)display).getModelRequest(), callback);
							}
						}).attempt();
					}else
						Window.alert(resources.projectFormError());
				}
			})
		);
		
		registrations.add(
			((Display)display).getModelFileUpload().addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					((Display)display).getUploadModelForm().submit();
				}
			})
		);
		registrations.add(
			((Display)display).getUploadModelForm().addSubmitHandler(new SubmitHandler() {
				@Override
				public void onSubmit(SubmitEvent event) {
					
				}
			})
		);
		registrations.add(
			((Display)display).getUploadModelForm().addSubmitCompleteHandler(new SubmitCompleteHandler() {
				@Override
				public void onSubmitComplete(SubmitCompleteEvent event) {
					((Display)display).getUploadModelForm().reset();
					setModels();
				}
			})
		);

		registrations.add(
			((Display)display).getDeleteModelButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
						@Override
						public void onFailure(Throwable caught) {}
						@Override
						public void onSuccess(Boolean result) {
							setModels();
						}
					};
					
					rpcActionFactory.<Boolean>create(callback, new RpcAttempt<Boolean>() {
						@Override
						public void call(AsyncCallback<Boolean> callback) {
							ProjectModelRequest request = ((Display)display).getModelRequest();
							if(request != null && request.getOwner().equals(Owner.USER))
								editorRpcService.deleteModel(request.getId(), callback);
						}
					}).attempt();
				}
			})
		);
		
	}

	private void setHiddenAttribute() {
		AsyncCallback<Author> callback = new AsyncCallback<Author>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Author result) {
				if(result != null){
					ProjectDetails details = new ProjectDetails();
					details.setVersion("1.0");
					details.setAuthor(result);
					((Display)display).setDetails(details);
				}
			}
		};

		rpcActionFactory.<Author>create(callback, new RpcAttempt<Author>() {
			@Override
			public void call(AsyncCallback<Author> callback) {
				editorRpcService.getCurrentAutor(callback);
			}
		}).attempt();
	}
	
	private void setModels() {
		AsyncCallback<HashMap<Owner, ArrayList<ProjectDetails>>> callback = new AsyncCallback<HashMap<Owner, ArrayList<ProjectDetails>>>() {
			@Override
			public void onFailure(Throwable caught) {
				((Display)display).setModels(null);
			}

			@Override
			public void onSuccess(HashMap<Owner, ArrayList<ProjectDetails>> result) {
				((Display)display).setModels(result);
			}
		};
		
		rpcActionFactory.<HashMap<Owner, ArrayList<ProjectDetails>>>create(callback, new RpcAttempt<HashMap<Owner, ArrayList<ProjectDetails>>>() {
			@Override
			public void call(AsyncCallback<HashMap<Owner, ArrayList<ProjectDetails>>> callback) {
				editorRpcService.getModels(callback);
				
			}
		}).attempt();
	}
	
	@Override
	public void go(HasWidgets container) {
		super.go(container);
		((Display)display).setCanUploadModel(parameters.getParameters().getCanUploadModel());
		setHiddenAttribute();
		setModels();
	}
	
	
}
