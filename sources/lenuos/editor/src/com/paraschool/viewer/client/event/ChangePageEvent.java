package com.paraschool.viewer.client.event;

import com.google.gwt.event.shared.GwtEvent;


public abstract class ChangePageEvent extends GwtEvent<ChangePageEventHandler> {

	public static Type<ChangePageEventHandler> TYPE = new Type<ChangePageEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ChangePageEventHandler> getAssociatedType() {
		return TYPE;
	}
}
