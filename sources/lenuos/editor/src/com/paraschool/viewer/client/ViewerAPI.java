package com.paraschool.viewer.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

import com.paraschool.commons.client.EventBus;
import com.paraschool.viewer.client.event.ChangePageToIndexEvent;
import com.paraschool.viewer.client.event.ChangePageToNextEvent;
import com.paraschool.viewer.client.event.ChangePageToPreviousEvent;

/*
 * Created at 30 oct. 2010
 * By bathily
 */
@Export
public class ViewerAPI implements Exportable {

	EventBus eventBus = EventBus.getInstance();
	
	public void next() {
		eventBus.fireEvent(new ChangePageToNextEvent());
	}
	
	public void previous() {
		eventBus.fireEvent(new ChangePageToPreviousEvent());
	}
	
	public void show(int page) {
		eventBus.fireEvent(new ChangePageToIndexEvent(page));
	}
}
