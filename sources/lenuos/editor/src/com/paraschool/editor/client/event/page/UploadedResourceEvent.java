package com.paraschool.editor.client.event.page;

import com.paraschool.commons.share.Resource;
import com.paraschool.editor.client.event.project.ResourceEvent;
import com.paraschool.editor.client.event.project.ResourceEventHandler;

/*
 * Created at 19 juil. 2010
 * By Didier Bathily
 */
public class UploadedResourceEvent extends ResourceEvent {

	public UploadedResourceEvent(Resource resource) {
		super(resource);
	}

	@Override
	protected void dispatch(ResourceEventHandler handler) {
		handler.onUploaded(this);

	}

}
