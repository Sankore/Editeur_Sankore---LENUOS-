package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class PreviewPopupContent extends Composite {

	private static PreviewPopupContentUiBinder uiBinder = GWT.create(PreviewPopupContentUiBinder.class);
	interface PreviewPopupContentUiBinder extends UiBinder<Widget, PreviewPopupContent> {}
	 
	
	@UiField Button closeButton;
	@UiField Panel container;
	
	public PreviewPopupContent() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
