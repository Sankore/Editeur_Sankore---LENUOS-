package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ui.UIUtils;

/*
 * Created at 29 sept. 2010
 * By bathily
 */
public class MediaContainer extends FlowPanel implements HasValueChangeHandlers<ModuleObject> {

	private Widget media;
	private ModuleObject object;
	
	private final FlowPanel mediaPanel;
	
	public MediaContainer() {
		mediaPanel = new FlowPanel();
		add(mediaPanel);
	}
	
	public MediaContainer( ModuleObject object) {
		this();
		setMedia(object, null);
	}

	public Widget getMedia() {
		return media;
	}
	
	public ModuleObject getObject() {
		return this.object;
	}
	
	public void setMedia(ModuleObject object, Widget defaultWidget) {
		mediaPanel.clear();
		this.object = object;
		if(object == null){
			this.media = null;
			if(defaultWidget != null)
				mediaPanel.add(defaultWidget);
		}else{
			media = UIUtils.getWidgetForObject(object);
			if(media != null)
				mediaPanel.add(media);
		}

		ValueChangeEvent.fire(this, object);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ModuleObject> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
	

}
