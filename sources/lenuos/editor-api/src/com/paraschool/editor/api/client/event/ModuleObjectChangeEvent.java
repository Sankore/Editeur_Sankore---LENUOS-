package com.paraschool.editor.api.client.event;

import com.paraschool.editor.api.client.ModuleObject;

/*
 * Created at 22 août 2010
 * By bathily
 */
public class ModuleObjectChangeEvent extends ModuleObjectEvent {

	public ModuleObjectChangeEvent(ModuleObject object) {
		super(object);
	}

	@Override
	protected void dispatch(ModuleObjectEventHandler handler) {
		handler.onChange(this);
	}

}
