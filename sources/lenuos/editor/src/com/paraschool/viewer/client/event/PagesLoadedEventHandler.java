package com.paraschool.viewer.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface PagesLoadedEventHandler extends EventHandler {
	void onPageLoaded(PageLoadedEvent event);
}
