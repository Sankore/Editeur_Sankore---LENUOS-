package com.paraschool.viewer.client.event;

import com.google.gwt.event.shared.GwtEvent;

public abstract class ViewerEvent extends GwtEvent<ViewerEventHandler> {

	public static Type<ViewerEventHandler> TYPE = new Type<ViewerEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ViewerEventHandler> getAssociatedType() {
		return TYPE;
	}

}
