package com.paraschool.editor.client.event.page;

public class MovePageEvent extends PageNavigationEvent {

	private final int destination;
	
	public MovePageEvent(int index, int destination) {
		super(index);
		this.destination = destination;
	}

	@Override
	protected void dispatch(PageNavigationEventHandler handler) {
		handler.move(this);
	}

	public int getDestination() {
		return destination;
	}

}
