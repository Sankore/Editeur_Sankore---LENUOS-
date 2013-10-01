package com.paraschool.commons.client;

import com.google.gwt.event.shared.SimpleEventBus;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public class EventBus extends SimpleEventBus {

	private static EventBus instance;
	
	private EventBus() {}

	public static EventBus getInstance() {
		if(instance == null)
			instance = new EventBus();
		return instance;
	}
}
