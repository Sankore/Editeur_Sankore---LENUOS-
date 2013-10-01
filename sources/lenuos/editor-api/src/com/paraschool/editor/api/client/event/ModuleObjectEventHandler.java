package com.paraschool.editor.api.client.event;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 22 août 2010
 * By bathily
 */
public interface ModuleObjectEventHandler extends EventHandler {
	void onChange(ModuleObjectChangeEvent event);
	void onDelete(ModuleObjectDeleteEvent event);
}
