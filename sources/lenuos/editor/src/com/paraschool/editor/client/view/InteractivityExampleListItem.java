package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.shared.SampleDetails;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 12 juil. 2010
 * By Didier Bathily
 */
public class InteractivityExampleListItem extends HTMLListItem {

	private static InteractivityExampleListItemUiBinder uiBinder = GWT.create(InteractivityExampleListItemUiBinder.class);

	interface InteractivityExampleListItemUiBinder extends	UiBinder<Widget, InteractivityExampleListItem> {
	}

	interface InteractivityExampleListItemCssResource extends CssResource {
		String interactivity();
		String name();
		String description();
		String thumbnail();
	}

	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		@Source({"css/Constants.css", "css/InteractivityExampleListItem.css"}) InteractivityExampleListItemCssResource css();
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}

	@UiField Image img;
	@UiField ParagraphElement name;
	@UiField ParagraphElement description;
	@UiField ParagraphElement interactivity;
	@UiField Button chooseButton;
	
	public InteractivityExampleListItem() {
		add(uiBinder.createAndBindUi(this));
	}
	
	public InteractivityExampleListItem(SampleDetails sample, String moduleName) {
		this();
		setImageUrl(AppUtil.makeSampleURL(sample));
		setName(sample.getName());
		setDescription(sample.getDescription());
		setInteractivity(moduleName);
	}
	
	public void setImageUrl(String url) {
		img.setUrl(url);
	}

	public void setName(String name) {
		this.name.setInnerHTML(name);
	}
	
	public void setDescription(String description) {
		this.description.setInnerHTML(description);
	}

	public void setInteractivity(String interactivity) {
		this.interactivity.setInnerHTML(interactivity);
	}
}
