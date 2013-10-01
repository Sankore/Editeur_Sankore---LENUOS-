package com.paraschool.editor.client.presenter.factory;

import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.ResourceUploadPresenter;
import com.paraschool.editor.client.presenter.ResourceUploadPresenter.Display;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public class ResourceUploadPresenterFactoryImp extends PresenterFactory implements
		ResourceUploadPresenterFactory {
	
	private Project project;
	private AppMessages messages;
	private RpcActionFactory rpcActionFactory;
	
	@Inject
	public ResourceUploadPresenterFactoryImp(Project project, EditorServiceAsync editorRpcService, ProjectServiceAsync projectRpcService, 
			EventBus eventBus, AjaxHandler ajaxHandler, AppMessages messages, RpcActionFactory rpcActionFactory) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, null);
		
		this.project = project;
		this.messages = messages;
		this.rpcActionFactory = rpcActionFactory;
	}
	
	@Override
	public ResourceUploadPresenter create(Display display) {
		return new ResourceUploadPresenter(project, editorRpcService, projectRpcService,
				eventBus, ajaxHandler,messages, rpcActionFactory, display);
	}

}
