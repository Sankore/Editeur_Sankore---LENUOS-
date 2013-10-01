package com.paraschool.editor.client.event.app;

/*
 * Created at 25 oct. 2010
 * By bathily
 */
public class DeauthenticatedEvent extends AuthenticationEvent {

	@Override
	protected void dispatch(AuthenticationEventHandler handler) {
		handler.onDeauthenticated(this);
	}

}
