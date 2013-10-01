package com.paraschool.editor.api.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.editor.api.client.ModuleObject;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public abstract class ModuleObjectEvent extends GwtEvent<ModuleObjectEventHandler> {

	public static Type<ModuleObjectEventHandler> TYPE = new Type<ModuleObjectEventHandler>();
	
	private final ModuleObject object;
	
	public ModuleObjectEvent(ModuleObject object) {
		this.object = object;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ModuleObjectEventHandler> getAssociatedType() {
		return TYPE;
	}

	public ModuleObject getObject() {
		return object;
	}

}
