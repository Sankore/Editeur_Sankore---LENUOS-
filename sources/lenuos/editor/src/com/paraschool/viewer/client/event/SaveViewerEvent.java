package com.paraschool.viewer.client.event;

public class SaveViewerEvent extends ViewerEvent {

	@Override
	protected void dispatch(ViewerEventHandler handler) {
		handler.onSave(this);
	}

}
