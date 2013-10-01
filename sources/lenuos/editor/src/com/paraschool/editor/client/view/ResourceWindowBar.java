package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;

public class ResourceWindowBar extends Composite {

	interface ResourceWindowBarUiBinder extends UiBinder<FlowPanel, ResourceWindowBar> {}
	private static ResourceWindowBarUiBinder uiBinder = GWT.create(ResourceWindowBarUiBinder.class);
	
	@UiField Button catalogButton;
	@UiField Button createLinkButton;
	@UiField(provided=true) ResourceUpload uploader;
	
	@Inject
	private ResourceWindowBar(ResourceUpload uploader) {
		this.uploader = uploader;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
}
