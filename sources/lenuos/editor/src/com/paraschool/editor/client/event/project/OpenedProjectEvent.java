package com.paraschool.editor.client.event.project;

import com.paraschool.commons.share.Project;

public class OpenedProjectEvent extends StateProjectEvent {

	public OpenedProjectEvent(Project project) {
		super(project);
	}

	@Override
	protected void dispatch(StateProjectEventHandler handler) {
		handler.onOpened(this);
	}

}
