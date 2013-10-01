package com.paraschool.editor.client.presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.AppController;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.event.app.InfoMessageEvent;
import com.paraschool.editor.client.event.app.WarningMessageEvent;
import com.paraschool.editor.client.event.project.CreateProjectEvent;
import com.paraschool.editor.client.event.project.OpenProjectEvent;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.ProjectService.Sort;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;
import com.paraschool.editor.shared.Page;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class ListProjectPresenter extends DefaultPresenter {

	private static final Logger logger = Logger.getLogger(ListProjectPresenter.class.getName());
	
	public interface Display extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getOpenButton();
		HasClickHandlers getDeleteButton();
		HasClickHandlers getBackButton();
		HasClickHandlers getCreateButton();
		HasClickHandlers getFetchButton();
		
		Sort getDesiredSort();
		int getDesiredPage();
		
		int getClicked(ClickEvent event);
		void remove(int index);
		void setData(Page<ProjectDetails> page);
	}
	
	private Page<ProjectDetails> page;
	@Inject private RpcActionFactory rpcActionFactory;
	@Inject AppMessages messages;
	
	@Inject
	public ListProjectPresenter(ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Display display) {
		super(null, projectRpcService, eventBus, ajaxHandler, display);
	}
	
	@Override
	protected void bind() {
		
		registrations.add(
				((Display)display).getFetchButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						fetchProjects();
					}
				})
			);
			
		
		registrations.add(
			((Display)display).getOpenButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					int selected = ((Display)display).getClicked(event);
					if(selected >= 0) {
						final ProjectDetails selectedProject = page.getData().get(selected);
						AsyncCallback<Project> callback = new AsyncCallback<Project>() {
							public void onFailure(Throwable caught) {}
							public void onSuccess(Project result) {
								eventBus.fireEvent(new OpenProjectEvent(result));
							}
						};
						
						rpcActionFactory.<Project>create(callback, new RpcAttempt<Project>() {
							@Override
							public void call(AsyncCallback<Project> callback) {
								projectRpcService.get(selectedProject, callback);
							}
						
						}).attempt();
						
					}
					
				}
			})
		);
		
		registrations.add(
				((Display)display).getDeleteButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						final int selected = ((Display)display).getClicked(event);
						if(selected >= 0 && Window.confirm(messages.confirmDeletion())) {
							final ProjectDetails selectedProject = page.getData().get(selected);
							AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
								public void onFailure(Throwable caught) {}
								public void onSuccess(Boolean result) {
									if(result) {
										page.getData().remove(selected);
										((Display)display).remove(selected);
										eventBus.fireEvent(new InfoMessageEvent(messages.projectDeletionSuccess(selectedProject.getName())));
									}
									else
										eventBus.fireEvent(new WarningMessageEvent(messages.projectDeletionError(selectedProject.getName())));
								}
							};
							
							rpcActionFactory.<Boolean>create(callback, new RpcAttempt<Boolean>(){
								@Override
								public void call(AsyncCallback<Boolean> callback) {
									projectRpcService.delete(selectedProject, callback);
								}
								
							}).attempt();
							
						}
						
					}
				})
			);
		
		registrations.add(
			((Display)display).getCreateButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					eventBus.fireEvent(new CreateProjectEvent(null));
				}
			})
		);
		
		registrations.add(
			((Display)display).getBackButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					History.newItem(AppController.HOME_PLACE);
				}
			})
		);
	}
	
	private void fetchProjects() {
		AsyncCallback<Page<ProjectDetails>> callback = new AsyncCallback<Page<ProjectDetails>>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Page<ProjectDetails> result) {
				if(logger.isLoggable(Level.FINE))
					logger.fine("Current page:"+result.getCurrent()+" , Total page:"+result.getTotalPageCount()+" , Size:"+result.getSize());
				page = result;
				((Display)display).setData(result);
			}
		};
		
		rpcActionFactory.<Page<ProjectDetails>>create(callback, new RpcAttempt<Page<ProjectDetails>>() {
			@Override
			public void call(AsyncCallback<Page<ProjectDetails>> callback) {
				projectRpcService.getProjects(((Display)display).getDesiredPage(), ((Display)display).getDesiredSort(), callback);
			}
		}).attempt();
	}

	@Override
	public void go(final HasWidgets container) {
		super.go(container);
		fetchProjects();
	}

}
