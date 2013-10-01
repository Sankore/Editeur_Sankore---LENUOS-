package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.modules.commons.client.i18n.StatementMessages;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;

public class StatementPanel extends Composite {

	interface StatementPanelUiBinder extends UiBinder<Widget, StatementPanel> {}
	private static StatementPanelUiBinder uiBinder = GWT.create(StatementPanelUiBinder.class);
	
	private static final StatementMessages messages = GWT.create(StatementMessages.class);
	
	@UiField Button hide;
	@UiField Panel statementContainer;
	@UiField MediaContainer thumbnail;
	@UiField MediaContainer sound;
	@UiField MediaContainer resource;
	@UiField HTML statement;
	
	public StatementPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public StatementJSO getStatementJSO() {
		StatementJSO statementJSO = StatementJSO.createObject().cast();
		statementJSO.setStatement(getStatementHTML());
		
		ModuleObject thumbnailObject = thumbnail.getObject();
		statementJSO.setThumbnail(thumbnailObject == null ? null : thumbnailObject.getId());
		
		ModuleObject soundObject = sound.getObject();
		statementJSO.setSound(soundObject == null ? null : soundObject.getId());
		
		ModuleObject resourceObject = resource.getObject();
		statementJSO.setResource(resourceObject == null ? null : resourceObject.getId());
		
		return statementJSO;
	}

	public MediaContainer getThumbnail() {
		return thumbnail;
	}
	
	public MediaContainer getSound() {
		return sound;
	}
	
	public MediaContainer getResource() {
		return resource;
	}
	
	public HTML getStatement() {
		return statement;
	}
	
	public void setStatement(String statement) {
		String html = messages.statement();
		if(statement != null && statement.trim().trim().length() != 0) {
			html = statement;
		}
		getStatement().setHTML(html);
	}
	
	public String getStatementHTML() {
		String html = getStatement().getHTML();
		if(messages.statement().equals(html))
			html = null;
		return html;
	}
	
	public void setThumbnail(ModuleObject object, Image defaultThumbnail) {
		getThumbnail().setMedia(object, defaultThumbnail);
	}
	
	public void setSound(ModuleObject object) {
		getSound().setMedia(object, null);
	}
	
	public void setResource(ModuleObject object) {
		getResource().setMedia(object, null);
	}
	
	@UiHandler("hide")
	public void hideStatement(ClickEvent event) {
		statementContainer.setVisible(!statementContainer.isVisible());
	}
	
	public void setCanHide(boolean canHide) {
		hide.setVisible(canHide);
		statementContainer.setVisible(!canHide);
	}
}
