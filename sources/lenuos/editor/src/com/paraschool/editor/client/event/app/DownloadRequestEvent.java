package com.paraschool.editor.client.event.app;

import com.google.gwt.event.shared.GwtEvent;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class DownloadRequestEvent extends GwtEvent<DownloadRequestEventHandler> {

	public static Type<DownloadRequestEventHandler> TYPE = new Type<DownloadRequestEventHandler>();
	private final String url;
	
	public DownloadRequestEvent(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DownloadRequestEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DownloadRequestEventHandler handler) {
		handler.download(this);
	}

}
