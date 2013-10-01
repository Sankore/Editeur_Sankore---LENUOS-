package com.paraschool.editor.client.event.page;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.commons.share.Page;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class PageEvent extends GwtEvent<PageEventHandler> {

	public static Type<PageEventHandler> TYPE = new Type<PageEventHandler>();

	private final Page page;
	
	public PageEvent(Page page) {
		this.page = page;
	}

	@Override
	public Type<PageEventHandler> getAssociatedType() {
		return TYPE;
	}

	public Page getPage() {
		return page;
	}
}
