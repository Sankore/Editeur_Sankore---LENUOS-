package com.paraschool.commons.client.presenter;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.paraschool.commons.client.EventBus;

public abstract class AbstractPresenter implements Presenter {

	protected final EventBus eventBus;
	protected final Display display;
	protected ArrayList<HandlerRegistration> registrations;

	public AbstractPresenter(EventBus eventBus, Display display) {
		this.eventBus = eventBus;
		this.display = display;
		registrations = new ArrayList<HandlerRegistration>();
	}

	protected void bind() {
		
	}

	public void clear() {
		for(HandlerRegistration registration : registrations)
			registration.removeHandler();
		registrations.clear();
	}

	public void go(final HasWidgets container) {
		bind();
		if(container != null) {
			container.clear();
			container.add(display.asWidget());
		}
	}

}