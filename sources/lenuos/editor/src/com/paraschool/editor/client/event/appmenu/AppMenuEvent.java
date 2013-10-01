package com.paraschool.editor.client.event.appmenu;

import com.google.gwt.event.shared.GwtEvent;

/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public abstract class AppMenuEvent extends GwtEvent<AppMenuEventHandler> {

	public static Type<AppMenuEventHandler> TYPE = new Type<AppMenuEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AppMenuEventHandler> getAssociatedType() {
		return TYPE;
	}
}
