package com.paraschool.editor.client.view;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.editor.client.presenter.ConnectPresenter;
/*
 * Created at 14 septembre 2010
 * By blamouret
 */
public class ConnectView extends AppPopupPanel implements ConnectPresenter.Display {

	
	private final ConnectPopupContent content;
	
	@Inject
	private ConnectView(ConnectPopupContent content) {
		super(true, true);
		this.content = content;
		window.add(content);
		
		double wwidth = ConnectPopupContent.Resources.INSTANCE.css().width(); 
		Style style = window.getElement().getStyle();
		style.setWidth(wwidth, Unit.PX);
		style.setMarginLeft(-wwidth/2, Unit.PX);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void hide() {
		super.hide();
		this.content.clear();
	}

	@Override
	public void show() {
		super.show();
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				content.loginTextBox.setFocus(true);
			}
		});
	}
	
	@Override
	public HasClickHandlers getSendButton() {
		return content.sendButton;
	}

	@Override
	public String getLogin() {
		return this.content.loginTextBox.getValue();
	}

	@Override
	public String getPassword() {
		return this.content.passwordTextBox.getValue();
	}

	@Override
	public String getEmail() {
		return this.content.emailTextBox.getValue();
	}

	@Override
	public boolean isLogging() {
		return !this.content.forgetPanel.isVisible();
	}

	@Override
	public HasClickHandlers getCloseLink() {
		return this.content.closeLink;
	}

	@Override
	public void setCanRetrieveLogin(boolean canRetrieveLogin) {
		content.forgetButtonPanel.setVisible(canRetrieveLogin);
	}

}
