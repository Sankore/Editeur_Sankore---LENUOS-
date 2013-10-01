package com.paraschool.editor.client.event.project;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public interface ProjectEventHandler extends EventHandler {
	void onCreateProject(CreateProjectEvent event);
	void onCloseProject(CloseProjectEvent event);
	void onListProject(ListProjectEvent event);
	void onOpenProject(OpenProjectEvent event);
}
