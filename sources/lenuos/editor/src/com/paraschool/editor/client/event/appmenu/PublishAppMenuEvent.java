package com.paraschool.editor.client.event.appmenu;

/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public class PublishAppMenuEvent extends AppMenuEvent {

	@Override
	protected void dispatch(AppMenuEventHandler handler) {
		handler.onPublish(this);
	}

}
