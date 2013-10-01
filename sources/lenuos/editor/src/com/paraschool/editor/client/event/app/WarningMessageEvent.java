package com.paraschool.editor.client.event.app;

public class WarningMessageEvent extends NotificationEvent {

	public WarningMessageEvent(String message) {
		super(message);
	}

	@Override
	protected void dispatch(NotificationEventHandler handler) {
		handler.onWarning(this);
	}

}
