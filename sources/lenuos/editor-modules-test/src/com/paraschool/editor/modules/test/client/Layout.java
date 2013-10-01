package com.paraschool.editor.modules.test.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class Layout extends Composite {

	private static LayoutUiBinder uiBinder = GWT.create(LayoutUiBinder.class);

	interface LayoutUiBinder extends UiBinder<Widget, Layout> {
	}

	@UiField FlowPanel content;
	@UiField Tree tree;
	
	public Layout() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
