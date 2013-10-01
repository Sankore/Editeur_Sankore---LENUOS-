package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;


public interface AppResources extends ClientBundle {
	
	AppResources INSTANCE = GWT.create(AppResources.class);
	
	@Source(value={"css/Constants.css", "css/App.css"}) AppCssResource appCss();
	
	@Source("images/view-loading.gif") ImageResource loading();
	@Source("images/btn_valider.png") ImageResource button();
	@Source("images/close.png") ImageResource close();
	@Source("images/link-icon.png") ImageResource linkIcon();
	@Source("images/document-icon.png") ImageResource documentIcon();
	
	@Source("images/bg_gch_lab.png") ImageResource rLabelLeft();
	@Source("images/bg_dt_lab.png") @ImageOptions(repeatStyle=RepeatStyle.Horizontal) ImageResource rLabelRight();
	@Source("images/bg_gch_labTxtA.png") ImageResource tLabelLeft();
	@Source("images/bg_dt_labTxtA.png") @ImageOptions(repeatStyle=RepeatStyle.Horizontal) ImageResource tLabelRight();
	@Source("images/bg_gch_inp.png") @ImageOptions(repeatStyle=RepeatStyle.Horizontal) ImageResource rFieldLeft();
	@Source("images/bg_dt_inp.png") ImageResource rFieldRight();
	@Source("images/bg_gch_inpTxtA.png") @ImageOptions(repeatStyle=RepeatStyle.Horizontal) ImageResource tFieldLeft();
	@Source("images/bg_dt_inpTxtA.png") ImageResource tFieldRight();
}
