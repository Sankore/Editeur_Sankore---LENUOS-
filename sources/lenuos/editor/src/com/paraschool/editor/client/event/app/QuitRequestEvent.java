package com.paraschool.editor.client.event.app;


/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public class QuitRequestEvent extends AppRequestEvent {

	private final boolean confirm;

	public QuitRequestEvent(boolean confirm) {
		this.confirm = confirm;
	}
	

	public boolean isConfirm() {
		return confirm;
	}

	@Override
	protected void dispatch(AppRequestEventHandler handler) {
		handler.onQuitRequest(this);
	}

}
