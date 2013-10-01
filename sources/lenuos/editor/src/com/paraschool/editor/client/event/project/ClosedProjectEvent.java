package com.paraschool.editor.client.event.project;

import com.paraschool.commons.share.Project;

public class ClosedProjectEvent extends StateProjectEvent {

	public ClosedProjectEvent(Project project) {
		super(project);
	}

	@Override
	protected void dispatch(StateProjectEventHandler handler) {
		handler.onClosed(this);
	}

}
