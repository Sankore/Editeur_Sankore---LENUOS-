package com.paraschool.viewer.client.event;

/*
 * Created at 13 oct. 2010
 * By bathily
 */
public class ChangePageToNextEvent extends ChangePageEvent {

	@Override
	protected void dispatch(ChangePageEventHandler handler) {
		handler.next(this);
	}

}
