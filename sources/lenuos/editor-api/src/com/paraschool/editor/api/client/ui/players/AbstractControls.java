package com.paraschool.editor.api.client.ui.players;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

public abstract class AbstractControls extends Composite {

	protected interface CssResource extends com.google.gwt.resources.client.CssResource {
		@ClassName("native-media")			String nativeMedia();
		@ClassName("state-playing")			String statePlaying();
		@ClassName("state-pausing")			String statePausing();
		@ClassName("state-loading")			String stateLoading();
	}

	@UiField
	protected CssResource css;
	@UiField
	protected FlowPanel root;
	@UiField
	protected Button play;
	@UiField
	protected Button pause;
	@UiField
	protected Label elapse;
	@UiField
	protected Label duration;
	@UiField
	protected FocusPanel slider;
	
	private final Timer progressTimer;
	
	public AbstractControls() {
		super();
		progressTimer = new Timer() {
			@Override
			public void run() {
				try{
					updateUI();
				}catch (Throwable e) {
					cancel();
				}

			}
		};
	}
	
	protected void stopTimer() {
		progressTimer.cancel();
	}
	
	protected void launchTimer() {
		progressTimer.scheduleRepeating(500);
	}
	
	@UiHandler(value={"duration", "elapse"})
	protected void toogleDuration(ClickEvent event) {
		boolean toShow = elapse.isVisible();
		elapse.setVisible(!toShow);
		duration.setVisible(toShow);
	}
	
	@UiHandler("slider")
	protected void playTo(ClickEvent event) {
		int x = event.getNativeEvent().getClientX() - slider.getAbsoluteLeft() + 1;
		int w = slider.getParent().getOffsetWidth();
		play((double)x/w);
		
	}
	
	protected abstract void play(double position);
	
	protected abstract void updateUI();

}