package com.paraschool.editor.client.event.app;

public class ErrorMessageEvent extends NotificationEvent {

	public ErrorMessageEvent(String message) {
		super(message);
	}

	@Override
	protected void dispatch(NotificationEventHandler handler) {
		handler.onError(this);
	}

}
