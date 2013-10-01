package com.paraschool.editor.client.event.project;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.commons.share.Project;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class ProjectEvent extends GwtEvent<ProjectEventHandler> {

	public static Type<ProjectEventHandler> TYPE = new Type<ProjectEventHandler>();
	
	private final Project project;
	
	public ProjectEvent(Project project) {
		this.project = project;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ProjectEventHandler> getAssociatedType() {
		return TYPE;
	}

	public Project getProject() {
		return project;
	}

}
