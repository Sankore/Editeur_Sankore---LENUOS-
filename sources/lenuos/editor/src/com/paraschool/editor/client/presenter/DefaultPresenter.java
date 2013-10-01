package com.paraschool.editor.client.presenter;

import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.presenter.AbstractPresenter;
import com.paraschool.commons.client.presenter.Display;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 12 juil. 2010
 * By Didier Bathily
 */
public abstract class DefaultPresenter extends AbstractPresenter {

	protected final EditorServiceAsync editorRpcService;
	protected final ProjectServiceAsync projectRpcService;
	protected final AjaxHandler ajaxHandler;
	
	public DefaultPresenter(EditorServiceAsync editorRpcService, ProjectServiceAsync projectRpcService,
			EventBus eventBus, AjaxHandler ajaxHandler, Display display) {
		
		super(eventBus, display);
		
		this.editorRpcService = editorRpcService;
		this.projectRpcService = projectRpcService;
		this.ajaxHandler = ajaxHandler;
		
		
		
	}

}
