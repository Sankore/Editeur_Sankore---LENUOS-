package com.paraschool.editor.client.event.page;

public class HidePageEvent extends PageNavigationEvent {

	public HidePageEvent(int index) {
		super(index);
	}

	@Override
	protected void dispatch(PageNavigationEventHandler handler) {
		handler.hide(this);
	}

}
