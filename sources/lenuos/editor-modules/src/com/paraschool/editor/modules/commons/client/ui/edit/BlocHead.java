package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class BlocHead extends Composite {

	private static BlocHeadUiBinder uiBinder = GWT.create(BlocHeadUiBinder.class);
	interface BlocHeadUiBinder extends UiBinder<Widget, BlocHead> {}

	@UiField InlineLabel number;
	@UiField Button remove;
	
	public BlocHead(boolean canRemove) {
		initWidget(uiBinder.createAndBindUi(this));
		remove.setEnabled(canRemove);
	}

}
