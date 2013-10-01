package com.paraschool.editor.client.event.interactivity;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.Page;

public abstract class InteractivityEvent extends GwtEvent<InteractivityEventHandler> {

	public static Type<InteractivityEventHandler> TYPE = new Type<InteractivityEventHandler>();
	
	private Interactivity interactivity;
	private Page page;
	
	public InteractivityEvent(Interactivity interactivity, Page page) {
		this.interactivity = interactivity;
		this.page = page;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<InteractivityEventHandler> getAssociatedType() {
		return TYPE;
	}

	public Interactivity getInteractivity() {
		return interactivity;
	}

	public Page getPage() {
		return page;
	}

}
