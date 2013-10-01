package com.paraschool.editor.client.event.interactivity;

import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.Page;
import com.paraschool.editor.shared.SampleDetails;

public class AddInteractivityEvent extends InteractivityEvent {

	private final SampleDetails sample;
	
	public AddInteractivityEvent(Interactivity interactivity, Page page, SampleDetails sample) {
		super(interactivity, page);
		this.sample = sample;
	}
	
	public SampleDetails getSample() {
		return sample;
	}

	@Override
	protected void dispatch(InteractivityEventHandler handler) {
		handler.onAddinteractivity(this);
	}

}
