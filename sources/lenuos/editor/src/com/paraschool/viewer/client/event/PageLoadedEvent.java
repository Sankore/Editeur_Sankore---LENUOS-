package com.paraschool.viewer.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class PageLoadedEvent extends GwtEvent<PagesLoadedEventHandler> {

	public static Type<PagesLoadedEventHandler> TYPE = new Type<PagesLoadedEventHandler>();
	
	private final int index;
	
	public PageLoadedEvent(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	@Override
	protected void dispatch(PagesLoadedEventHandler handler) {
		handler.onPageLoaded(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PagesLoadedEventHandler> getAssociatedType() {
		return TYPE;
	}

}
