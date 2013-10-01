package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class PageContentPanel extends FlowPanel implements HasWidgets {

	private static PageContentPanelUiBinder uiBinder = GWT.create(PageContentPanelUiBinder.class);
	interface PageContentPanelUiBinder extends UiBinder<Widget, PageContentPanel> {}

	protected @UiField HTML enonce;
	protected @UiField AbsolutePanel wrapper;
	protected @UiField InteractivityPanel panel;
	
	public PageContentPanel() {
		add(uiBinder.createAndBindUi(this));
	}
	
	public Panel getInteractivityPanel() {
		return panel;
	}

	public HTML getEnonce() {
		return enonce;
	}
}
