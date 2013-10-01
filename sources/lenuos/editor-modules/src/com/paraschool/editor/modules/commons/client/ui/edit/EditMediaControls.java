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
public class EditMediaControls extends Composite {

	interface EditMediaControlsUiBinder extends	UiBinder<Widget, EditMediaControls> {}
	private static EditMediaControlsUiBinder uiBinder = GWT.create(EditMediaControlsUiBinder.class);
	
	@UiField Button change;
	@UiField Button delete;
	
	public EditMediaControls() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
