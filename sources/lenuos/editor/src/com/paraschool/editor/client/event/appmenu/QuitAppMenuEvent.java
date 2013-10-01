package com.paraschool.editor.client.event.appmenu;

/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public class QuitAppMenuEvent extends AppMenuEvent {

	private final boolean confirm;

	public QuitAppMenuEvent(boolean confirm) {
		this.confirm = confirm;
	}
	

	public boolean isConfirm() {
		return confirm;
	}

	@Override
	protected void dispatch(AppMenuEventHandler handler) {
		handler.onQuit(this);
	}

}
