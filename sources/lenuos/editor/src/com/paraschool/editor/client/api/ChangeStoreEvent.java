package com.paraschool.editor.client.api;


/*
 * Created at 7 nov. 2010
 * By bathily
 */
public class ChangeStoreEvent extends StoreEvent {

	private final ResourceStoreCallback store;
	
	public ChangeStoreEvent(ResourceStoreCallback store) {
		super();
		this.store = store;
	}

	@Override
	protected void dispatch(StoreEventHandler handler) {
		handler.onChangeStore(this);
	}

	public ResourceStoreCallback getStore() {
		return store;
	}

}
