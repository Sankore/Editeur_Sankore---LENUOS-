package com.paraschool.editor.client.view;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.commons.client.presenter.Display;

public class Window extends AppPopupPanel implements Display {


	protected final WindowContent content;
	
	interface WindowCssResource extends CssResource {
		
		String windowHead();
		String windowContent();
		String windowBottom();
		
		String windowContentLeft();
		String windowContentRight();
		String windowContentRightHead();
		String windowContentRightBottom();
		String close();
		
		String title();
		String windowTitle();
		String windowDescription();
		
		String bar();
		String leftBar();
		String rightBar();
		String barWrapper();
		String rightBarLeft();
		String rightBarContent();
		String rightBarRight();
		
		String viewLoading();
		String hide();
	}
	
	interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		@Source("images/view-loading.gif") ImageResource loading();
		
		@Source(value={"css/Window.css","css/Constants.css"}) WindowCssResource windowCss();
		@Source("images/bg_haut_popup_gabarit.png") ImageResource windowHead();
		@Source("images/bg_bas_popup_gabarit.png") ImageResource windowBottom();
		@Source("images/bg_mil_popup_gabarit.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical)  ImageResource windowContent();
		
		@Source("images/bg_mil_popup_gabarit.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource bar();
		@Source("images/gch_barNavNoir.gif") ImageResource rightBarLeft();
		@Source("images/mil_barNavNoir.gif") @ImageOptions(repeatStyle=RepeatStyle.Horizontal) ImageResource rightBarContent();
		@Source("images/dt_barNavNoir.gif") ImageResource rightBarRight();
	}
	
	{
		Resources.INSTANCE.windowCss().ensureInjected();
	}
	
	@Inject
	protected Window(WindowContent content) {
		super(false, false);
		this.content = content;
		window.add(content);
	}

	@Override
	public Widget asWidget() {
		return this;
	}
	
	public void setWindowImageUrl(String url) {
		this.content.windowImage.setUrl(url);
	}
	
	public void setWindowTitle(String title) {
		this.content.windowTitle.setInnerText(title);
	}

	public void setWindowDescription(String description) {
		this.content.windowDescription.setInnerText(description);
	}

	public HasClickHandlers getCloseButton() {
		return this.content.closeButton;
	}
	
	public boolean barIsVisible() {
		return this.content.bar.isVisible();
	}
	
	public void showBar() {
		this.content.bar.setVisible(true);
	}
	
	public void hideBar() {
		this.content.bar.setVisible(false);
	}
	
	public void addToContent(Widget widget) {
		this.content.content.add(widget);
	}
	
	public void addToLeftContent(Widget widget) {
		this.content.leftContent.add(widget);
	}
	
	public void addToLeftBar(Widget widget) {
		this.content.leftBarContent.add(widget);
	}
	
	public void addToRightBar(Widget widget) {
		this.content.rightBarContent.add(widget);
	}
	
	public void hideContent() {
		this.content.content.addStyleName(Resources.INSTANCE.windowCss().hide());
	}
	
	public void showContent() {
		this.content.content.removeStyleName(Resources.INSTANCE.windowCss().hide());
	}
	
}
