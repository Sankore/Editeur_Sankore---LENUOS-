package com.paraschool.editor.client.event.page;

import com.paraschool.commons.share.Page;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
public class RemovePageEvent extends PageEvent {

	public RemovePageEvent(Page page) {
		super(page);
	}

	@Override
	protected void dispatch(PageEventHandler handler) {
		handler.onRemovePage(this);
	}

}
