package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.client.AppUtil;

/*
 * Created at 14 septembre 2010
 * By blamouret
 */
public class ConnectPopupContent extends Composite {

	private static ConnectPopupContentUiBinder uiBinder = GWT.create(ConnectPopupContentUiBinder.class);
	interface ConnectPopupContentUiBinder extends UiBinder<Widget, ConnectPopupContent> {}
	 
	public interface ConnectPopupContentCssResource extends CssResource {
		double width();
		
		String window();
		String dial_conn();
		String haut_dialConn();
		String bas_dialConn();
		String mil_dialConn();
		String intitule_dialConn();
		String mil_mil_dialConn();
		String close_dial();
		String titre();
		String sous_titre();
		String a_popClose2();
		String logo();
		
		String btn_dial();
		String loginfields();
		String loginMessage();
		String forget();
		String forgetMessage();
		String forgetButton();
		String forgetPanel();
		String unavailable();
		
		String row();
		String fieldLeft();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/ConnectPopupContentView.css", "css/Constants.css"}) ConnectPopupContentCssResource css();
		
		@Source("images/icn_conect_dial.png") ImageResource logo();
		@Source("images/dialConn_bas.png") ImageResource basDialConn();
		@Source("images/dialConn_haut.png") ImageResource hautDialConn();
		@Source("images/dialConn_mil.png") ImageResource milDialConn();
		@Source("images/btn_dial.png") ImageResource btn_dial();
		@Source("images/btn_close.png") ImageResource btn_close();
		
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}

	private HandlerRegistration loginPanelClickRegistration;
	
	@UiField HTMLPanel forgetButtonPanel;
	@UiField HTMLPanel forgetPanel;
	@UiField FocusPanel loginPanel;
	@UiField Button forgetButton;
	@UiField Button sendButton;
	@UiField TextBox loginTextBox;
	@UiField PasswordTextBox passwordTextBox;
	@UiField TextBox emailTextBox;
	@UiField Anchor closeLink;
	
	public ConnectPopupContent() {
		initWidget(uiBinder.createAndBindUi(this));

		this.displayPanels(false);

		this.forgetButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displayPanels(!forgetPanel.isVisible());
			}
		});
		
		AppUtil.bindEnterKeyInTextbox(loginTextBox, sendButton);
		AppUtil.bindEnterKeyInTextbox(passwordTextBox, sendButton);
		AppUtil.bindEnterKeyInTextbox(emailTextBox, sendButton);
	}
	
	
	
	private void displayPanels(boolean forgetPanelVisible) {
		this.forgetPanel.setVisible(forgetPanelVisible);
		
		if(forgetPanelVisible){
			this.loginPanel.addStyleName(Resources.INSTANCE.css().unavailable());
			loginTextBox.setEnabled(false);
			passwordTextBox.setEnabled(false);
			
			loginPanelClickRegistration =
				this.loginPanel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						displayPanels(false);
					}
				});
			
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					emailTextBox.setFocus(true);
				}
			});
		}else{
			
			this.loginPanel.removeStyleName(Resources.INSTANCE.css().unavailable());
			if(loginPanelClickRegistration != null){
				loginPanelClickRegistration.removeHandler();
				loginPanelClickRegistration = null;
			}
			
			loginTextBox.setEnabled(true);
			passwordTextBox.setEnabled(true);
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					loginTextBox.setFocus(true);
				}
			});
		}
	}
	
	void clear() {
		this.displayPanels(false);
		this.loginTextBox.setValue("");
		this.passwordTextBox.setValue("");
		this.emailTextBox.setValue("");
	}

}
