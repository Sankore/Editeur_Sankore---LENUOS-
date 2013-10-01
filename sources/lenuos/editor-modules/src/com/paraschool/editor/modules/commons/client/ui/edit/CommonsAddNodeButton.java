package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class CommonsAddNodeButton extends Composite implements HasClickHandlers, HasText, HasHTML {
	interface CommonsAddNodeButtonUiBinder extends UiBinder<Button, CommonsAddNodeButton>{}
	private static CommonsAddNodeButtonUiBinder uiBinder = GWT.create(CommonsAddNodeButtonUiBinder.class);

	
	@UiField SpanElement nextIdSpan;
	@UiField SpanElement titleSpan;
	
	private final Button button;
	private String prefix = "";
	
	public CommonsAddNodeButton() {
		button = uiBinder.createAndBindUi(this);
		initWidget(button);
	}
	
	public CommonsAddNodeButton(String html) {
		this();
		setHTML(html);
	}
	
	public void setNextIdPrefix(String prefix) {
		this.prefix = prefix; 
	}
	
	public void setNextId(int id) {
		nextIdSpan.setInnerHTML(prefix + id);
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return button.addClickHandler(handler);
	}

	@Override
	public String getHTML() {
		return titleSpan.getInnerHTML();
	}

	@Override
	public void setHTML(String html) {
		titleSpan.setInnerHTML(html);
	}

	@Override
	public String getText() {
		return titleSpan.getInnerText();
	}

	@Override
	public void setText(String text) {
		titleSpan.setInnerHTML(text);
	}
	
	public void setEnabled(boolean enabled) {
		button.setEnabled(enabled);
	}
	
	public boolean isEnabled() {
		return button.isEnabled();
	}
 }
