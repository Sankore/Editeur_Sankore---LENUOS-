package com.paraschool.editor.client.event.app;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public interface AppRequestEventHandler extends EventHandler {
	void onSaveRequest(AppRequestEvent event);
	void onPublishRequest(AppRequestEvent event);
	void onExportRequest(AppRequestEvent event);
	void onPreviewRequest(AppRequestEvent event);
	void onQuitRequest(QuitRequestEvent event);
	void onCreateModelRequest(AppRequestEvent event);
}
