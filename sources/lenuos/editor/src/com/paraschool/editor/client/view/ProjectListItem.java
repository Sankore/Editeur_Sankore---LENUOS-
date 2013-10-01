package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 12 juil. 2010
 * By Didier Bathily
 */
public class ProjectListItem extends HTMLListItem {

	private static ProjectListItemUiBinder uiBinder = GWT.create(ProjectListItemUiBinder.class);
	interface ProjectListItemUiBinder extends	UiBinder<Widget, ProjectListItem> {}

	@UiField Panel boxPanel;
	@UiField Panel descriptionPanel;
	
	@UiField HasText name;
	@UiField HasText date;
	@UiField HasText objectif;
	@UiField HasText description;

	@UiField HasClickHandlers open;
	@UiField HasClickHandlers detail;
	@UiField HasClickHandlers delete;
	
	public ProjectListItem() {
		add(uiBinder.createAndBindUi(this));
		detail.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				toogleDetail();
			}
		});
	}
	
	public ProjectListItem(String name, String date, String objectif) {
		this();
		setName(name);
		setDate(date);
		setObjectif(objectif);
	}

	public HasText getName() {
		return name;
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public HasText getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date.setText(date);
	}

	public HasText getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif.setText(objectif);
	}

	public HasText getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description.setText(description);
	}

	public HasClickHandlers getOpen() {
		return open;
	}

	public HasClickHandlers getDetail() {
		return detail;
	}
	
	public HasClickHandlers getDelete() {
		return delete;
	}

	protected void toogleDetail() {
		this.descriptionPanel.setVisible(!this.descriptionPanel.isVisible());
		
		String style = ListProjectView.Resources.INSTANCE.css().opened();
		
		if(this.descriptionPanel.isVisible())
			this.boxPanel.addStyleName(style);
		else 
			this.boxPanel.removeStyleName(style);
		
	}
}
