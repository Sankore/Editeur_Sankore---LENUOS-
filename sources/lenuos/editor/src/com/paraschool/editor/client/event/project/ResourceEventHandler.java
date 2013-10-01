package com.paraschool.editor.client.event.project;

import com.google.gwt.event.shared.EventHandler;
import com.paraschool.editor.client.event.page.DeletedResourceEvent;
import com.paraschool.editor.client.event.page.UploadedResourceEvent;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public interface ResourceEventHandler extends EventHandler {
	void onUploaded(UploadedResourceEvent event);
	void onDeleted(DeletedResourceEvent event);
	void onChange(ChangeResourceEvent event);
}
