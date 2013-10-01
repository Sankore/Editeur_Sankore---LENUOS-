package com.paraschool.editor.client.event.project;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.commons.share.Resource;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class ResourceEvent extends GwtEvent<ResourceEventHandler> {

	public static Type<ResourceEventHandler> TYPE = new Type<ResourceEventHandler>();
	
	private final Resource resource;
	
	public ResourceEvent(Resource resource) {
		this.resource = resource;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ResourceEventHandler> getAssociatedType() {
		return TYPE;
	}

	public Resource getResource() {
		return resource;
	}

}
