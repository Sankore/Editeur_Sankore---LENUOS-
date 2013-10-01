package com.paraschool.editor.client.api;

/*
 * Created at 7 nov. 2010
 * By bathily
 */
import com.google.gwt.event.shared.GwtEvent;

public abstract class StoreEvent extends GwtEvent<StoreEventHandler> {

	public static final Type<StoreEventHandler> TYPE = new Type<StoreEventHandler>();

	public StoreEvent() {
		super();
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StoreEventHandler> getAssociatedType() {
		return TYPE;
	}

}