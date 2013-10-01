package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 13 juil. 2010
 * By Didier Bathily
 */
public class MenuItem extends HTMLListItem {

	private static MenuItemUiBinder uiBinder = GWT
			.create(MenuItemUiBinder.class);

	interface MenuItemUiBinder extends UiBinder<ButtonElement, MenuItem> {
	}

	@UiField ButtonElement button;
	@UiField ImageElement image;
	
	public MenuItem() {
		getElement().appendChild(uiBinder.createAndBindUi(this));
	}
	
	public MenuItem(String imageUrl) {
		this();
		setImageUrl(imageUrl);
	}

	public void setImageUrl(String url) {
		image.setSrc(url);
	}
	
	public Button getButton() {
		return Button.wrap(button);
	}
}
