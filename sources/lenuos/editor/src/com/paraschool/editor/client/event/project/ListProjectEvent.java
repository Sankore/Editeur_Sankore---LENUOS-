package com.paraschool.editor.client.event.project;

import com.paraschool.commons.share.Project;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class ListProjectEvent extends ProjectEvent {
	
	public ListProjectEvent(Project project) {
		super(project);
	}

	@Override
	protected void dispatch(ProjectEventHandler handler) {
		handler.onListProject(this);
	}
}
