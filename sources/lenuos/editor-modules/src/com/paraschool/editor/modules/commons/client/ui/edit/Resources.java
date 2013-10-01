package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/*
 * Created at 17 oct. 2010
 * By bathily
 */
public interface Resources extends ClientBundle {
	Resources INSTANCE = GWT.create(Resources.class);
	
	@Source("Buttons.css") ButtonsCss css();
	
	@Source("images/ibutton-huge-plus.png") ImageResource hugePlusButton();
	@Source("images/ibutton-plus.png") ImageResource plusButton();
	@Source("images/ibutton-plus-active.png") ImageResource plusActiveButton();
	@Source("images/ibutton-delete.png") ImageResource deleteButton();
	@Source("images/ibutton-tiny-delete.png") ImageResource tinyDeleteButton();
	
	@Source("images/puce.png") ImageResource puce();
	
	@Source("images/button-resource.png") ImageResource resourceButton();
	@Source("images/button-sound.png") ImageResource soundButton();
	@Source("images/button-text.png") ImageResource textButton();
	
	@Source("images/button-resource-tiny.png") ImageResource resourceTinyButton();
	@Source("images/button-text-tiny.png") ImageResource textTinyButton();
}
