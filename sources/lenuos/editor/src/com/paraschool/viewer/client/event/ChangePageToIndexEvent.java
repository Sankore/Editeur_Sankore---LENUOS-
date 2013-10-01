package com.paraschool.viewer.client.event;

/*
 * Created at 13 oct. 2010
 * By bathily
 */
public class ChangePageToIndexEvent extends ChangePageEvent {

	private final int destination;
	
	public ChangePageToIndexEvent(int destination) {
		super();
		this.destination = destination;
	}

	@Override
	protected void dispatch(ChangePageEventHandler handler) {
		handler.to(this);
	}

	public int getDestination() {
		return destination;
	}

}
