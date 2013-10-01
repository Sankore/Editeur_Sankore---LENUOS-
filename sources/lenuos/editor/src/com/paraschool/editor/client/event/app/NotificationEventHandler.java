package com.paraschool.editor.client.event.app;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public interface NotificationEventHandler extends EventHandler {
	void onInfo(InfoMessageEvent event);
	void onWarning(WarningMessageEvent event);
	void onError(ErrorMessageEvent event);
}
