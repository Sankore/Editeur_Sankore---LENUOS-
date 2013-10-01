package com.paraschool.editor.client.event.app;


/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public class ExportRequestEvent extends AppRequestEvent {

	@Override
	protected void dispatch(AppRequestEventHandler handler) {
		handler.onExportRequest(this);

	}

}
