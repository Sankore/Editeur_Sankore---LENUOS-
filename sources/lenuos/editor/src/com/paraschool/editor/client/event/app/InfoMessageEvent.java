package com.paraschool.editor.client.event.app;

public class InfoMessageEvent extends NotificationEvent {

	public InfoMessageEvent(String message) {
		super(message);
	}

	@Override
	protected void dispatch(NotificationEventHandler handler) {
		handler.onInfo(this);
	}

}
