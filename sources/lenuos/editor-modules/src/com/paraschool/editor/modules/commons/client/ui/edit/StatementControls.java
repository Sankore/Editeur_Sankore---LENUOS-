package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 29 sept. 2010
 * By bathily
 */
public class StatementControls extends Composite {

	interface StatementControlsUiBinder extends	UiBinder<Widget, StatementControls> {}
	private static StatementControlsUiBinder uiBinder = GWT.create(StatementControlsUiBinder.class);
	
	@UiField Button addSound;
	@UiField Button addResource;

	public StatementControls() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
