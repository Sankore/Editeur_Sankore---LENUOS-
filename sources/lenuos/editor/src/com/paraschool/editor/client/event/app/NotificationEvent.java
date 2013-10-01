package com.paraschool.editor.client.event.app;

import com.google.gwt.event.shared.GwtEvent;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class NotificationEvent extends GwtEvent<NotificationEventHandler> {

	public static Type<NotificationEventHandler> TYPE = new Type<NotificationEventHandler>();
	
	private String message;
	
	public NotificationEvent(String message) {
		this.setMessage(message);
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NotificationEventHandler> getAssociatedType() {
		return TYPE;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	

}
