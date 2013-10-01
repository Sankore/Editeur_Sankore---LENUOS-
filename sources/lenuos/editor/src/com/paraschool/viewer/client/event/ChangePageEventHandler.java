package com.paraschool.viewer.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ChangePageEventHandler extends EventHandler {
	void previous(ChangePageEvent event);
	void next(ChangePageEvent event);
	void to(ChangePageToIndexEvent event);
}
