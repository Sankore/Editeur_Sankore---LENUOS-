package com.paraschool.editor.client.event.page;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public interface PageNavigationEventHandler extends EventHandler {
	void show(ShowPageEvent event);
	void hide(HidePageEvent event);
	void move(MovePageEvent event);
}
