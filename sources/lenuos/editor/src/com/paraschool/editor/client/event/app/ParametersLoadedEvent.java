package com.paraschool.editor.client.event.app;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.editor.client.event.app.ParametersLoadedEvent.Handler;
import com.paraschool.editor.shared.Parameters;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class ParametersLoadedEvent extends GwtEvent<Handler> {

	public static interface Handler extends EventHandler {
		void onLoad(ParametersLoadedEvent event);
	}
	
	public static Type<Handler> TYPE = new Type<Handler>();
	
	private final Parameters parameters;
	
	public ParametersLoadedEvent(final Parameters parameters) {
		super();
		this.parameters = parameters;
	}

	public Parameters getParameters() {
		return parameters;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onLoad(this);
	}
	
}
