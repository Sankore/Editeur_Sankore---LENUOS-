package com.paraschool.editor.interactivity.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 28 juil. 2010
 * By bathily
 */
public class InteractivityView extends FocusPanel {

	private static InteractivityViewUiBinder uiBinder = GWT.create(InteractivityViewUiBinder.class);
	interface InteractivityViewUiBinder extends UiBinder<HTMLPanel, InteractivityView> {}

	interface InteractivityContainerCssResource extends CssResource {
		String container();
		
		String wrapper();
		
		String head();
		String title();
		String menus();
		String buttons();
		
		String optionsWrapper();
		String options();
		String icon();
		String optionsList();
		
		String content();
		
		String ibutton();
		String ibuttonactive();
		String plus();
		String delete();
	}
	
	public interface Resources extends ClientBundle {
		
		Resources INSTANCE = GWT.create(Resources.class);
		@Source("InteractivityView.css") InteractivityContainerCssResource css();
		
		@Source("interactivity-loading.gif") ImageResource loading();
		@Source("interactivity-title-icon.png") ImageResource title();
		
		@Source("options-icon.png") ImageResource icon();
		
		@Source("ibutton-plus.png") ImageResource plusbg();
		@Source("ibutton-plus-active.png") ImageResource plusActivebg();
		@Source("ibutton-delete.png") ImageResource deletebg();
	}
	
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@UiField FlowPanel content;
	@UiField Widget loadingIndicator;
	@UiField FocusPanel moveButton;

	@UiField Button preferenceButton;
	@UiField Button hidePreferenceButton;
	@UiField Button deleteButton;
	
	@UiField HTML title;
	@UiField HTMLPanel menuContainer;
	
	@UiField Panel optionsWrapper;
	@UiField Panel optionsContainer;
	@UiField FlowPanel options;
	
	public InteractivityView() {
		setWidget(uiBinder.createAndBindUi(this));
		setStyleName(Resources.INSTANCE.css().container());
		toogleOptions(null);
	}

	public Widget asWidget() {
		return this;
	}

	public void setInteractivityWidget(Widget widget, Widget optionsWidget) {
		loadingIndicator.setVisible(false);
		
		options.clear();
		if(optionsWidget != null)
			options.add(optionsWidget);
		else{
			preferenceButton.setEnabled(false);
			preferenceButton.setVisible(false);
		}
		content.add(widget);
	}
	
	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}
	
	public FocusPanel getMoveInteractivityButton() {
		return moveButton;
	}

	public HasHTML getModuleTitle() {
		return title;
	}
	
	public void setCanDelete(boolean canDelete) {
		deleteButton.setEnabled(canDelete);
	}
	
	@UiHandler(value={"preferenceButton","hidePreferenceButton"})
	protected void toogleOptions(ClickEvent event) {
		int height = optionsContainer.getOffsetHeight();
		boolean toShow = event != null ? event.getSource().equals(preferenceButton) : false;
		hidePreferenceButton.setVisible(toShow);
		preferenceButton.setVisible(!toShow);
		optionsWrapper.setHeight(toShow ? height+"px" : "0");
	}
	
	
}
