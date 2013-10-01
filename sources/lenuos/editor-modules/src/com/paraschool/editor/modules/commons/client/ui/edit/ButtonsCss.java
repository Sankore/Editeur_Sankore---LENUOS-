package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.resources.client.CssResource;

/*
 * Created at 17 oct. 2010
 * By bathily
 */
public interface ButtonsCss extends CssResource {

	@ClassName("huge-plus") String hugePlus();
	@ClassName("plus") String plus();
	@ClassName("plus-active") String plusActive();
	String delete();
	@ClassName("tiny-delete") String tinyDelete();
	
	@ClassName("puce-number") String puceNumber();
	
	@ClassName("add-resource") String addResource();
	@ClassName("add-sound") String addSound();
	@ClassName("add-text") String addText();
	
	@ClassName("add-resource-tiny") String addTinyResource();
	@ClassName("add-text-tiny") String addTinyText();
}
