package com.paraschool.editor.client.event.project;

import com.paraschool.commons.share.Project;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class CreateProjectEvent extends ProjectEvent {
	
	public CreateProjectEvent(Project project) {
		super(project);
	}

	@Override
	protected void dispatch(ProjectEventHandler handler) {
		handler.onCreateProject(this);
	}
}
