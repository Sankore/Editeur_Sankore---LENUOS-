package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.client.presenter.Menu;
import com.paraschool.htmllist.client.HTMLList;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
public class PageMenu extends Composite implements Menu {

	private static PageMenuUiBinder uiBinder = GWT.create(PageMenuUiBinder.class);
	interface PageMenuUiBinder extends UiBinder<Widget, PageMenu> {}
	
	interface DefaultMenuCssResource extends CssResource {
		String container();
		String head();
		String bottom();
		String content();
		String buttons();
		String last();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/DefaultMenu.css","css/Constants.css"}) DefaultMenuCssResource css();

		@Source("images/navFour_bg_haut.gif") ImageResource head();
		@Source("images/navFour_bg_mil.gif") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource content();
		@Source("images/navFour_bg_bas.gif") ImageResource bottom();
		
		@Source("images/inc_menuVfourFive_souris.png") ImageResource interactivity();
		@Source("images/inc_menuVfourFive_download.png") ImageResource export();
		@Source("images/inc_menuVfourFive_croix.png") ImageResource close();
		@Source("images/inc_menuVfourFive_roue.png") ImageResource preferences();
		@Source("images/inc_menuVfourFive_pen.png") ImageResource edit();
		@Source("images/inc_menuVfourFive_cle.png") ImageResource tools();
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@UiField MenuItem addInteractivityButton;
	@UiField MenuItem exportButton;
	@UiField MenuItem deleteButton;
	@UiField MenuItem preferenceButton;
	@UiField MenuItem quickEditButton;
	@UiField MenuItem toolsButton;
	
	@UiField FlowPanel menuContainer;
	@UiField HTMLList menuList;
	
	public PageMenu() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public HasClickHandlers getExportButton() {
		return exportButton;
	}

	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}

	public HasClickHandlers getPreferenceButton() {
		return preferenceButton;
	}

	public HasClickHandlers getQuickEditButton() {
		return quickEditButton;
	}

	public HasClickHandlers getToolsButton() {
		return toolsButton;
	}

	public HasClickHandlers getAddInteractivityButton() {
		return addInteractivityButton;
	}
	
	
	
	
}
