package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

/*
 * Created at 3 oct. 2010
 * By bathily
 */
public class ContentMoverPopUpPanel extends PopupPanel {
	
	public interface MoverCallback {
		void onMove(int source, int destination);
	}
	
	private static final int TIMER_ELAPSE = 5000;
	
	private final InlineLabel target;
	private final TextBox destinationText;
	private final Timer hideTimer;
	
	public ContentMoverPopUpPanel(final InlineLabel target, final int source, final MoverCallback callback) {
		super(true);
		
		this.target = target;
		
		destinationText = new TextBox();
		destinationText.setText((source+1)+"");
		destinationText.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				hideTimer.schedule(TIMER_ELAPSE);
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					event.preventDefault();
					close();
					int destination = -1;
					try{
						destination = Integer.parseInt(destinationText.getText().trim()) - 1;
					}catch (NumberFormatException ignore) {}
					callback.onMove(source, destination);
				}
				if(event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE){
					event.preventDefault();
					close();
				}
					
			}
		});
		
		add(destinationText);
		
		setPosition();
		show();
		

		hideTimer = new Timer() {
			@Override
			public void run() {
				hide();
			}
		};
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				destinationText.setFocus(true);
				destinationText.setSelectionRange(0, destinationText.getText().length());
			}
		});
	}
	
	private void setPosition() {			
		setPopupPosition(target.getAbsoluteLeft(), target.getAbsoluteTop());
		setSize(target.getOffsetWidth()+"px", target.getOffsetHeight()+"px");
	}
	
	private void close() {
		hideTimer.cancel();
		hide();
	}
	
}