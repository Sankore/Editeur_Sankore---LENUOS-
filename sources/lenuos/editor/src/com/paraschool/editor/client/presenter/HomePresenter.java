package com.paraschool.editor.client.presenter;

import java.util.List;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.event.project.CreateProjectEvent;
import com.paraschool.editor.client.event.project.ListProjectEvent;
import com.paraschool.editor.client.event.project.OpenProjectEvent;
import com.paraschool.editor.client.i18n.AppConstants;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class HomePresenter extends DefaultPresenter {

	public interface Display extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getCreateProjectButton();
		HasClickHandlers getOpenProjectButton();
		HasClickHandlers getRecentList();
		int getClicked(ClickEvent event);
		void setData(List<ProjectDetails> data);
	}

	private List<ProjectDetails> recentProjects;
	
	@Inject private RpcActionFactory rpcActionFactory;
	@Inject private AppConstants constants;
	
	@Inject
	private HomePresenter(ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Display display) {
		super(null, projectRpcService, eventBus, ajaxHandler, display);
	}

	private void openCreateProjectView() {
		eventBus.fireEvent(new CreateProjectEvent(null));
	}

	private void openListProjectView() {
		eventBus.fireEvent(new ListProjectEvent(null));
	}

	@Override
	protected void bind() {
		registrations.add(
				((Display)display).getCreateProjectButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						openCreateProjectView();
					}
				})
		);

		registrations.add(
				((Display)display).getOpenProjectButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						openListProjectView();
					}
				})
		);

		registrations.add(
				((Display)display).getRecentList().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						int selected = ((Display)display).getClicked(event);
						if(selected >= 0){
							final ProjectDetails details = recentProjects.get(selected);
							AsyncCallback<Project> callback = new AsyncCallback<Project>() {
								public void onFailure(Throwable caught) {}
								public void onSuccess(Project result) {
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
					}
				})
		);

		registrations.add(
				Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

					public void onPreviewNativeEvent(NativePreviewEvent preview) {
						NativeEvent event = preview.getNativeEvent();
						if(AppUtil.isMeta(event)) {
							
							int keycode = event.getKeyCode();
							
							if(keycode == constants.newProjectKeyStroke()) {
								openCreateProjectView();
								preview.consume();
								event.preventDefault();
								return;
							}
							else if(keycode == constants.openProjectKeyStroke()) {
								openListProjectView();
								preview.consume();
								event.preventDefault();
								return;
							}
						}
					}
				})
		);

	}

	@Override
	public void go(final HasWidgets container) {
		super.go(container);
		
		AsyncCallback<List<ProjectDetails>> callback = new AsyncCallback<List<ProjectDetails>>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(List<ProjectDetails> result) {
				recentProjects = result;
				((Display)display).setData(result);
			}
		};
		
		rpcActionFactory.<List<ProjectDetails>>create(callback, new RpcAttempt<List<ProjectDetails>>() {
			@Override
			public void call(AsyncCallback<List<ProjectDetails>> callback) {
				projectRpcService.getRecentProjects(callback);
			}
		}).attempt();
		
	}
}
