package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 12 juil. 2010
 * By Didier Bathily
 */
public class InteractivityListItem extends HTMLListItem {

	private static InteractivityListItemUiBinder uiBinder = GWT.create(InteractivityListItemUiBinder.class);

	interface InteractivityListItemUiBinder extends	UiBinder<Widget, InteractivityListItem> {
	}

	interface InteractivityListItemCssResource extends CssResource {
		String item();
		String name();
		String description();
		String left();
		String center();
	}

	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);

		@Source({"css/Constants.css", "css/InteractivityListItem.css"}) InteractivityListItemCssResource css();
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}

	@UiField Image img;
	@UiField ParagraphElement name;
	@UiField ParagraphElement description;
	@UiField Button chooseButton;
	@UiField Button examplesButton;
	
	public InteractivityListItem() {
		add(uiBinder.createAndBindUi(this));
	}
	
	public InteractivityListItem(String url, String name, String description) {
		this();
		setImageUrl(url);
		setName(name);
		setDesciption(description);
	}
	
	public void setImageUrl(String url) {
		img.setUrl(url);
	}

	public void setName(String name) {
		this.name.setInnerHTML(name);
	}

	public void setDesciption(String description) {
		this.description.setInnerHTML(description);
	}
}
