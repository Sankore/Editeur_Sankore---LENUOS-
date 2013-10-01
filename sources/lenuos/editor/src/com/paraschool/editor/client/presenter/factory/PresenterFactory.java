package com.paraschool.editor.client.presenter.factory;

import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.presenter.Display;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public abstract class PresenterFactory {

	protected final EditorServiceAsync editorRpcService;
	protected final ProjectServiceAsync projectRpcService;
	protected final EventBus eventBus;
	protected final Display display;
	protected final AjaxHandler ajaxHandler;
	
	public PresenterFactory(EditorServiceAsync editorRpcService, ProjectServiceAsync projectRpcService,
			EventBus eventBus, AjaxHandler ajaxHandler, Display display) {
		this.editorRpcService = editorRpcService;
		this.projectRpcService = projectRpcService;
		this.eventBus = eventBus;
		this.ajaxHandler = ajaxHandler;
		this.display = display;
	}
}
