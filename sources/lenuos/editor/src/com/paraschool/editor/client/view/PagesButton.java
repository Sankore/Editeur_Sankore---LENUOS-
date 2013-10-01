package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 13 juil. 2010
 * By Didier Bathily
 */
public class PagesButton extends HTMLListItem {

	private static PagesButtonUiBinder uiBinder = GWT
			.create(PagesButtonUiBinder.class);

	interface PagesButtonUiBinder extends UiBinder<Button, PagesButton> {
	}

	@UiField Button button;
	
	public PagesButton() {
		super();
		add(uiBinder.createAndBindUi(this));
		button.setStyleName(ProjectView.Resources.INSTANCE.css().addPage());
	}
	
	public PagesButton(int index) {
		this();
		setIndex(index);
	}
	
	public void setIndex(int index) {
		button.setText((index+1)+"");
		button.setStyleName(ProjectView.Resources.INSTANCE.css().pageButton());
	}
	
	public Button getButton() {
		return button;
	}

}
