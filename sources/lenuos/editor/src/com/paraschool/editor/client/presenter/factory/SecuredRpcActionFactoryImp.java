package com.paraschool.editor.client.presenter.factory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.RpcAction;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.SecuredRpcAction;
import com.paraschool.editor.client.config.ParametersProvider;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.i18n.ConnectPopupMessages;
import com.paraschool.editor.client.presenter.ConnectPresenter;
import com.paraschool.editor.client.rpc.AuthenticationServiceAsync;

public class SecuredRpcActionFactoryImp implements RpcActionFactory {

	private final EventBus eventBus;
	private final AuthenticationServiceAsync service;
	private final AjaxHandler ajaxHandler;
	private final ConnectPresenter connectPresenter;
	private final ParametersProvider parametersProvider;
	private final AppMessages appMessages;
	private final ConnectPopupMessages messages;
	
	@Inject
	public SecuredRpcActionFactoryImp(EventBus eventBus, ConnectPresenter connectPresenter, AuthenticationServiceAsync service,
			AjaxHandler ajaxHandler, ParametersProvider parametersProvider, AppMessages appMessages, ConnectPopupMessages messages) {
		this.eventBus = eventBus;
		this.service = service;
		this.ajaxHandler = ajaxHandler;
		this.parametersProvider = parametersProvider;
		this.connectPresenter = connectPresenter;
		this.appMessages = appMessages;
		this.messages = messages;
	}
	
	@Override
	public <T> RpcAction<T> create(AsyncCallback<T> callBack, RpcAttempt<T> attempt) {
		return new SecuredRpcAction<T>(eventBus, connectPresenter, service, ajaxHandler, callBack, attempt, parametersProvider, appMessages, messages);
	}

}
