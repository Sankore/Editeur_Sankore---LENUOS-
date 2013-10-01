package com.paraschool.editor.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Author;
import com.paraschool.editor.client.config.ParametersProvider;
import com.paraschool.editor.client.event.app.AuthenticatedEvent;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.i18n.ConnectPopupMessages;
import com.paraschool.editor.client.presenter.ConnectPresenter;
import com.paraschool.editor.client.rpc.AuthenticationServiceAsync;
import com.paraschool.editor.shared.exception.MailException;
import com.paraschool.editor.shared.exception.UserNotFoundException;
import com.paraschool.editor.shared.exception.security.ApplicationSecurityException;

public class SecuredRpcAction<T> extends RpcAction<T> {

	final AuthenticationServiceAsync service;
	final RpcAttempt<T> attempt;
	
	private final EventBus eventBus;
	private final ConnectPresenter connectPresenter;
	private final ParametersProvider parametersProvider;
	private final AppMessages appMessages;
	private final ConnectPopupMessages messages;

	
	@Inject //TODO use future gin 'assisted'
	public SecuredRpcAction(EventBus eventBus, ConnectPresenter connectPresenter, AuthenticationServiceAsync service,
			AjaxHandler handler, AsyncCallback<T> asyncCallBack, RpcAttempt<T> attempt, ParametersProvider parametersProvider,
			AppMessages appMessages, ConnectPopupMessages messages) {
		this(eventBus, connectPresenter, 0, service, handler, asyncCallBack, attempt, parametersProvider, appMessages, messages);
	}

	public SecuredRpcAction(EventBus eventBus, ConnectPresenter connectPresenter, int retryCount, AuthenticationServiceAsync service, 
			AjaxHandler handler,AsyncCallback<T> asyncCallBack, RpcAttempt<T> attempt, ParametersProvider parametersProvider,
			AppMessages appMessages, ConnectPopupMessages messages) {
		super(retryCount, handler, asyncCallBack);
		this.eventBus = eventBus;
		this.service = service;
		this.attempt = attempt;
		this.parametersProvider = parametersProvider;
		this.connectPresenter = connectPresenter;
		this.appMessages = appMessages;
		this.messages = messages;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		
		if(caught instanceof ApplicationSecurityException) {
			handler.onComplete();
			displayConnectView();
		}else
			super.onFailure(caught);
	}
	
	private void displayConnectView() {
		
		this.connectPresenter.go(null, new LoginCallback() {
			@Override
			public void login(String login, String password) {
				attemptLogin(login, password);
			}

			@Override
			public void sendLoginPass(String email) {
				send(email);
			}
		}
		);
		
	}
	
	protected void attemptLogin(String username, String password) {

		AsyncCallback<Author> callback = new AsyncCallback<Author>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(messages.loginError());
			}

			@Override
			public void onSuccess(Author result) {
				connectPresenter.clear();
				attempt();
				eventBus.fireEvent(new AuthenticatedEvent(result));
			}
			
		};
		service.login(username, password, true, callback);
	}
	
	protected void send(String email) {
		String url = parametersProvider.getParameters().getRetrieveLoginExternalServiceUrl();
		String lang = LocaleInfo.getCurrentLocale().getLocaleName();
		
		if (url != null && url.trim().length() != 0) {
			this.sendByUrl(url, email, lang);
		} else {
			this.sendByRpc(email, lang);
		}
	}
	private void sendByUrl(final String url, final String email, final String lang) {
		
		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
			builder.setHeader("Content-type", "application/x-www-form-urlencoded");
			builder.sendRequest(URL.encode("email="+email+"&lang="+lang), new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					Window.alert(messages.sendMailSuccessfully());
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert(messages.sendMailError());
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
	private void sendByRpc(final String email, final String lang) {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof UserNotFoundException) {
					Window.alert(messages.unexistedEmail());
				} else if (caught instanceof MailException) {
					Window.alert(messages.errorInSendedMail());
				} else {
					Window.alert(messages.sendMailError());
				}
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert(messages.sendMailSuccessfully());
			}
			
		};
		service.sendLogin(email, lang, callback);
	}

	@Override
	public void attempt() {
		attempt.call(this);
	}

	@Override
	protected String retryMessage() {
		return appMessages.rpcRetryMessage();
	}

}
