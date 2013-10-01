package com.paraschool.editor.client.event.page;

import com.google.gwt.event.shared.GwtEvent;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class PageNavigationEvent extends GwtEvent<PageNavigationEventHandler> {

	public static Type<PageNavigationEventHandler> TYPE = new Type<PageNavigationEventHandler>();

	private final int index;
	
	public PageNavigationEvent(int index) {
		super();
		this.index = index;
	}

	@Override
	public Type<PageNavigationEventHandler> getAssociatedType() {
		return TYPE;
	}

	public int getIndex() {
		return index;
	}

}
