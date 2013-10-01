package com.paraschool.editor.client.event.project;

import com.paraschool.commons.share.Resource;

/*
 * Created at 12 nov. 2010
 * By bathily
 */
public class ChangeResourceEvent extends ResourceEvent {

	public ChangeResourceEvent(Resource resource) {
		super(resource);
	}

	@Override
	protected void dispatch(ResourceEventHandler handler) {
		handler.onChange(this);
	}

}
