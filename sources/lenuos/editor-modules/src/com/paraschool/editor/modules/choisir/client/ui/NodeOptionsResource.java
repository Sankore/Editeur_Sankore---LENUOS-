package com.paraschool.editor.modules.choisir.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface NodeOptionsResource extends ClientBundle {
	NodeOptionsResource INSTANCE = GWT.create(NodeOptionsResource.class);
	
	@Source("images/options-checkbox.png") ImageResource optionCheckbox();
	@Source("images/options-radio.png") ImageResource optionRadio();
	@Source("images/options-select.png") ImageResource optionSelect();
	
}