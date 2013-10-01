package com.paraschool.editor.client.view;

import com.google.gwt.resources.client.CssResource;

/*
 * Created at 21 juil. 2010
 * By Didier Bathily
 */
public interface AppCssResource extends CssResource {
	
	String textColor();
	String otherColor();
	
	int appWidth();
	int contentHeight();
	int menuHeight();
	int menuContentHeight();
	
	@ClassName("drag-positioner") String dragPositioner();
	
	String flotL();
	String flotR();
	String clear();
	String clearfix();
	String gray();
	String darkGray();
	String clickable();
	String hide();
	String button();
	String close();
	
	String form();
	String row();
	String labelContainer();
	String labelLeft();
	String labelRight();
	String field();
	String fieldLeft();
	String fieldRight();
	String textRow();
	
}
