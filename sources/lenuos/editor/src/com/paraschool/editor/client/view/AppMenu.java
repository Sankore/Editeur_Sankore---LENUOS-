package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public class AppMenu extends Composite {

	private static AppMenuUiBinder uiBinder = GWT.create(AppMenuUiBinder.class);

	interface AppMenuUiBinder extends UiBinder<HTMLList, AppMenu> {
	}
	
	@UiField HTMLListItem save;
	@UiField HTMLListItem preview;
	@UiField HTMLListItem publish;
	@UiField HTMLListItem export;
	@UiField HTMLListItem quit;
	
	public AppMenu() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public HasClickHandlers getSaveButton() {
		return save;
	}
	
	public HasClickHandlers getPreviewButton() {
		return preview;
	}
	
	public HasClickHandlers getPublishButton() {
		return publish;
	}
	
	public HasClickHandlers getExportButton() {
		return export;
	}
	
	public HasClickHandlers getQuitButton() {
		return quit;
	}
}
