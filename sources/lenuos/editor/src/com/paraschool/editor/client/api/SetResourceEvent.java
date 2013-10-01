package com.paraschool.editor.client.api;


/*
 * Created at 7 nov. 2010
 * By bathily
 */
public class SetResourceEvent extends StoreEvent {

	private final String requestId;
	private final ResourceJSO jso;
	
	public SetResourceEvent(String requestId, ResourceJSO jso) {
		super();
		this.requestId = requestId;
		this.jso = jso;
	}

	@Override
	protected void dispatch(StoreEventHandler handler) {
		handler.onSetResource(this);
	}

	public String getRequestId() {
		return requestId;
	}

	public ResourceJSO getJso() {
		return jso;
	}

}
