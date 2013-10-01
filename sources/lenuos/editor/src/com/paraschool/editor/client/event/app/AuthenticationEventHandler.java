package com.paraschool.editor.client.event.app;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public interface AuthenticationEventHandler extends EventHandler {
	void onAuthenticated(AuthenticatedEvent event);
	void onDeauthenticated(AuthenticationEvent event);
}
