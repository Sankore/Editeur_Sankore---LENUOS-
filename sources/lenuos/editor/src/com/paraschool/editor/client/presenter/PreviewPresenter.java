package com.paraschool.editor.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.presenter.Presenter;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 15 août 2010
 * By bathily
 */
public class PreviewPresenter extends DefaultPresenter implements Presenter {

	public interface Display extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getCloseButton();
		HasWidgets getViewerContainer();
		
		void show();
		void hide();
		
		void viewProject(Project project, int page);
	}

	@Inject private RpcActionFactory rpcActionFactory;
	
	@Inject
	private PreviewPresenter(EditorServiceAsync editorRpcService,
			ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Display display) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, display);

	}

	@Override
	protected void bind() {
		registrations.add(
				((Display)display).getCloseButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						((Display)display).hide();
						clear();
					}
				})
		);
	}

	@Override
	public void go(HasWidgets container) {	
		bind();
		((Display)display).show();
	}

	public void previewProject(final Project project, final int page) {
		
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {}
			
			@Override
			public void onSuccess(Boolean result) {
				if(result)
					((Display)display).viewProject(project, page);
			}
		};
		
		rpcActionFactory.<Boolean>create(callback, new RpcAttempt<Boolean>() {
			@Override
			public void call(AsyncCallback<Boolean> callback) {
				projectRpcService.preview(project, callback);
			}
		}).attempt();
	}

}
