package com.paraschool.editor.client.event.interactivity;

import com.google.gwt.event.shared.EventHandler;

public interface InteractivityEventHandler extends EventHandler {
	void onAddinteractivity(AddInteractivityEvent event);
	void onRemoveinteractivity(RemoveInteractivityEvent event);
}
