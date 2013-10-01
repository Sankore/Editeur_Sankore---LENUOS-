package com.paraschool.editor.client.event.project;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public interface StateProjectEventHandler extends EventHandler {
	void onOpened(OpenedProjectEvent event);
	void onClosed(ClosedProjectEvent event);
	
}
