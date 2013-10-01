package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.paraschool.editor.modules.commons.client.i18n.ButtonsMessages;

public class MediaButtonFactory {

	private final static ButtonsCss css = Resources.INSTANCE.css();
	private final static ButtonsMessages messages = GWT.create(ButtonsMessages.class);
	
	public static Button createAddMediaButton() {
		Button b = new Button(messages.addResourceButton());
		b.setStyleName(css.addResource());
		b.setTitle(messages.addResourceTitle());
		return b;
	}
	
	public static Button createTinyAddMediaButton() {
		Button b = new Button(messages.addResourceButton());
		b.setStyleName(css.addTinyResource());
		b.setTitle(messages.addResourceTitle());
		return b;
	}
	
	public static Button createAddSoundButton() {
		Button b = new Button(messages.addSoundButton());
		b.setStyleName(css.addSound());
		b.setTitle(messages.addSoundTitle());
		return b;
	}
	public static Button createAddTextButton() {
		Button b = new Button(messages.addTextButton());
		b.setStyleName(css.addText());
		b.setTitle(messages.addTextTitle());
		return b;
	}
}
