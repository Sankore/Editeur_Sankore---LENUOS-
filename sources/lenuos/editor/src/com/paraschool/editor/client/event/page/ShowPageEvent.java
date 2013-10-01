package com.paraschool.editor.client.event.page;

public class ShowPageEvent extends PageNavigationEvent {

	public ShowPageEvent(int index) {
		super(index);
	}

	@Override
	protected void dispatch(PageNavigationEventHandler handler) {
		handler.show(this);
	}

}
