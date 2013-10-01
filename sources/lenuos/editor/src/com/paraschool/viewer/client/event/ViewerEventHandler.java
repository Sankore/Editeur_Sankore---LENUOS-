package com.paraschool.viewer.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ViewerEventHandler extends EventHandler {
	void onFinish(FinishViewerEvent event);
	void onSave(SaveViewerEvent event);
}
