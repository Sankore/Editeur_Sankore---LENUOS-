package com.paraschool.editor.client.event.app;

import com.google.gwt.event.shared.GwtEvent;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class AuthenticationEvent extends GwtEvent<AuthenticationEventHandler> {

	public static Type<AuthenticationEventHandler> TYPE = new Type<AuthenticationEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AuthenticationEventHandler> getAssociatedType() {
		return TYPE;
	}

}
