package com.paraschool.editor.client.event.project;

import com.paraschool.commons.share.Project;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class CloseProjectEvent extends ProjectEvent {
	
	private final Project projectToOpen;
	
	public CloseProjectEvent(Project project, Project toOpen) {
		super(project);
		this.projectToOpen = toOpen;
	}

	@Override
	protected void dispatch(ProjectEventHandler handler) {
		handler.onCloseProject(this);
	}

	public Project getProjectToOpen() {
		return projectToOpen;
	}

}
