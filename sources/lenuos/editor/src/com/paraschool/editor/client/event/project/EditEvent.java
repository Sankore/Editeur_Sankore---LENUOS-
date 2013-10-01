package com.paraschool.editor.client.event.project;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.editor.api.client.ObjectEditCallback;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class EditEvent extends GwtEvent<EditEventHandler> {

	public static Type<EditEventHandler> TYPE = new Type<EditEventHandler>();
	
	private final ObjectEditCallback callback;
	
	public EditEvent(ObjectEditCallback callback) {
		this.callback = callback;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<EditEventHandler> getAssociatedType() {
		return TYPE;
	}

	public ObjectEditCallback getCallback() {
		return callback;
	}

}
