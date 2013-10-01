package com.paraschool.editor.client.event.appmenu;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public interface AppMenuEventHandler extends EventHandler {
	void onSave(AppMenuEvent event);
	void onPreview(AppMenuEvent event);
	void onPublish(AppMenuEvent event);
	void onExport(AppMenuEvent event);
	void onQuit(QuitAppMenuEvent event);
	void onDisconnectRequest(AppMenuEvent event);
}
