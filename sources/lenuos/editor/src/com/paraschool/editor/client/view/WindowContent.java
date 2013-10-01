package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class WindowContent extends Composite {
	interface WindowContentUiBinder extends UiBinder<Widget, WindowContent> {}
	private static WindowContentUiBinder uiBinder = GWT.create(WindowContentUiBinder.class);
	
	@UiField Button closeButton;
	
	@UiField FlowPanel leftContent;
	@UiField FlowPanel rightContent;
	@UiField FlowPanel headPanel;
	@UiField FlowPanel content;
	
	@UiField FlowPanel bar;
	@UiField FlowPanel leftBarContent;
	@UiField FlowPanel rightBarContent;
	
	@UiField Image windowImage;
	@UiField ParagraphElement windowTitle;
	@UiField ParagraphElement windowDescription;
	
	@UiField Widget loadingIndicator;
	
	public WindowContent() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
}
