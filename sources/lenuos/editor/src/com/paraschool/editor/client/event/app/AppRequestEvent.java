package com.paraschool.editor.client.event.app;

import com.google.gwt.event.shared.GwtEvent;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class AppRequestEvent extends GwtEvent<AppRequestEventHandler> {

	public static Type<AppRequestEventHandler> TYPE = new Type<AppRequestEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AppRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

}
