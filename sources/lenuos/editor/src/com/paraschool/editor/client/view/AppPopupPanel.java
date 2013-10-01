package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class AppPopupPanel extends PopupPanel implements com.google.gwt.user.client.Window.ScrollHandler {

	@UiTemplate("AppPopupPanel.ui.xml")
	interface AppPopupPanelUiBinder extends UiBinder<Widget, AppPopupPanel> {}
	private static AppPopupPanelUiBinder uiBinder = GWT.create(AppPopupPanelUiBinder.class);
	
	interface AppPopupPanelCssResource extends CssResource {
		int windowTopPosition();
		
		String calque();
		String container();
		String window();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/AppPopupPanel.css","css/Constants.css"}) AppPopupPanelCssResource css();
	}
	
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@UiField HTMLPanel calque;
	@UiField FlowPanel window;

	public AppPopupPanel(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		add(uiBinder.createAndBindUi(this));
		com.google.gwt.user.client.Window.addWindowScrollHandler(this);
		getElement().getStyle().setProperty("width", "100%");
		getElement().getStyle().setProperty("height", "100%");
	}
	
	private void setCalquePosition(int top, int left) {
		/*
		Style style = calque.getElement().getStyle();
		style.setTop(top, Unit.PX);
		style.setLeft(left, Unit.PX);
		*/
	}
	
	@Override
	public void onWindowScroll(
			com.google.gwt.user.client.Window.ScrollEvent event) {
		setCalquePosition(event.getScrollTop(), event.getScrollLeft());
	}
	
	@Override
	public void show() {
		super.show();
		/*
		Style style = window.getElement().getStyle();		
		style.setTop(com.google.gwt.user.client.Window.getScrollTop() + Resources.INSTANCE.css().windowTopPosition(), Unit.PX);
		setCalquePosition(com.google.gwt.user.client.Window.getScrollTop(),com.google.gwt.user.client.Window.getScrollLeft());
		*/
	}

}
