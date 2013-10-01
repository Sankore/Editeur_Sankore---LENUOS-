package com.paraschool.editor.client.event.interactivity;

import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.Page;

public class RemoveInteractivityEvent extends InteractivityEvent {

	public RemoveInteractivityEvent(Interactivity interactivity, Page page) {
		super(interactivity, page);
	}

	@Override
	protected void dispatch(InteractivityEventHandler handler) {
		handler.onRemoveinteractivity(this);
	}

}
