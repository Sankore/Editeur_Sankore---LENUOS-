package com.paraschool.editor.client.event.page;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public interface PageEventHandler extends EventHandler {
	void onAddPage(PageEvent event);
	void onRemovePage(PageEvent event);
	void onExportPage(PageEvent event);
}
