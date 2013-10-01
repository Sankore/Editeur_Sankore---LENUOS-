package com.paraschool.editor.modules.commons.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ModuleObject;

/*
 * Created at 29 sept. 2010
 * By bathily
 */
public class EditMediaEvent extends GwtEvent<EditMediaEventHandler> {
	public static Type<EditMediaEventHandler> TYPE = new Type<EditMediaEventHandler>();

	private final Widget media;
	private final ModuleObject object;
	
	public EditMediaEvent(Widget media, ModuleObject object) {
		this.media = media;
		this.object = object;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<EditMediaEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EditMediaEventHandler handler) {
		handler.onEditMedia(this);
	}

	public Widget getMedia() {
		return media;
	}

	public ModuleObject getObject() {
		return object;
	}

}
