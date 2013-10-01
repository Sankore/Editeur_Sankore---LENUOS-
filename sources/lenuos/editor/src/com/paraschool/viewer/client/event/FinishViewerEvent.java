package com.paraschool.viewer.client.event;

public class FinishViewerEvent extends ViewerEvent {

	@Override
	protected void dispatch(ViewerEventHandler handler) {
		handler.onFinish(this);
	}

}
