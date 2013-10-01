package com.paraschool.editor.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.presenter.AbstractPresenter;
import com.paraschool.editor.client.LoginCallback;
import com.paraschool.editor.client.config.ParametersProvider;

/*
 * Created at 14 septembre 2010
 * By blamouret
 */
public class ConnectPresenter extends AbstractPresenter {

	public interface Display extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getSendButton();
		HasClickHandlers getCloseLink();
		
		String getLogin();
		String getPassword();
		String getEmail();
		boolean isLogging();
		
		void setCanRetrieveLogin(boolean canRetrieveLogin);
		
		void show();
		void hide();
		
	}
	
	private final ParametersProvider parametersProvider;
	private LoginCallback loginCallback;

	@Inject
	private ConnectPresenter(EventBus eventBus, Display display, ParametersProvider parametersProvider) {
		super(eventBus, display);
		this.parametersProvider = parametersProvider;
	}

	@Override
	protected void bind() {
		registrations.add(
				((Display)display).getSendButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						sendButtonAction();
					}
				})
		);
		registrations.add(
				((Display)display).getCloseLink().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						clear();
					}
				})
		);
		
		((Display)display).setCanRetrieveLogin(parametersProvider.getParameters().getCanRetrieveLogin());
	}
	
	public void clear() {
		((Display)display).hide();
		super.clear();
	}

	@Override
	public void go(HasWidgets container) {	
		bind();
		((Display)display).show();
	}

	public void go(HasWidgets container, LoginCallback loginCallback) {
		go(container);
		this.loginCallback = loginCallback;
	}
	
	private void sendButtonAction() {
		Display display = (Display)super.display;
		if (display.isLogging()) {
			String login = display.getLogin();
			String pass = display.getPassword();
			loginCallback.login(login,pass);
		} else {
			String email = display.getEmail();
			loginCallback.sendLoginPass(email);
		}
	}
}
