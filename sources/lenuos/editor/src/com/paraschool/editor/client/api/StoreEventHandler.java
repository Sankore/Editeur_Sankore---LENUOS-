package com.paraschool.editor.client.api;

import com.google.gwt.event.shared.EventHandler;

/*
 * Created at 7 nov. 2010
 * By bathily
 */
public interface StoreEventHandler extends EventHandler {
	void onChangeStore(ChangeStoreEvent event);
	void onSetResource(SetResourceEvent event);
}
