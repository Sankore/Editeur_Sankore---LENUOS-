package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;

public class ExportResultPopupContent extends Composite {

	private static ExportResultPopupContentUiBinder uiBinder = GWT.create(ExportResultPopupContentUiBinder.class);
	interface ExportResultPopupContentUiBinder extends UiBinder<Widget, ExportResultPopupContent> {}
	 
	
	@UiField Button closeButton;
	@UiField Panel container;
	@UiField Panel waitingPanel;
	@UiField Panel urlsPanel;
	
	@UiField HTMLList urls;
	
	public ExportResultPopupContent() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void addUrl(String url) {
		waitingPanel.setVisible(false);
		urlsPanel.setVisible(true);
		Anchor a = new Anchor(url,url,"_blank");
		HTMLListItem item = new HTMLListItem();
		item.add(a);
		urls.add(item);
	}
	
	public void clear() {
		urls.clear();
		urlsPanel.setVisible(false);
		waitingPanel.setVisible(true);
	}
}
