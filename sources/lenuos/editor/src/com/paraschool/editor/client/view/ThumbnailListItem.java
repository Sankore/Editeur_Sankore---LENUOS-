package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
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
public class ThumbnailListItem extends HTMLListItem {

	private static ThumbnailListItemUiBinder uiBinder = GWT.create(ThumbnailListItemUiBinder.class);

	interface ThumbnailListItemUiBinder extends	UiBinder<Widget, ThumbnailListItem> {
	}

	interface ThumbnailListItemCssResource extends CssResource {
		String tpl();
		String selected();
		String thumbnail();
		String title();
		String mask();
		String icon();
		String closeItem();
		String closeButton();
	}

	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);

		@Source({"css/ThumbnailListItem.css","css/Constants.css"}) ThumbnailListItemCssResource css();
		@Source("images/masque_icn_ress.png") ImageResource mask();
		@Source("images/bg_select_icn_ress.png") ImageResource hover();
		@Source("images/hover_vignette_focus.png") ImageResource selected();
		@Source("images/vignette_icn_close.png") ImageResource closeItemBg();
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}

	@UiField Button closeButton;
	@UiField Image img;
	@UiField ParagraphElement description;
	@UiField ParagraphElement name;

	public ThumbnailListItem() {
		add(uiBinder.createAndBindUi(this));
		setStyleName(Resources.INSTANCE.css().tpl());
	}
	
	public ThumbnailListItem(String url, String name){
		this();
		setImageUrl(url);
		setName(name);
	}
	
	public ThumbnailListItem(String url, String name, String description) {
		this(url, name);
		setDescription(description);
	}
	
	public void setImageUrl(String url) {
		img.setUrl(url);
	}

	public void setName(String name) {
		this.name.setInnerText(name);
	}

	public void setDescription(String description) {
		this.description.setInnerText(description);
	}

}
