package com.paraschool.editor.client.event.project;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.commons.share.Project;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class StateProjectEvent extends GwtEvent<StateProjectEventHandler> {

	public static Type<StateProjectEventHandler> TYPE = new Type<StateProjectEventHandler>();
	
	private final Project project;
	
	public StateProjectEvent(Project project) {
		this.project = project;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StateProjectEventHandler> getAssociatedType() {
		return TYPE;
	}

	public Project getProject() {
		return project;
	}

}
