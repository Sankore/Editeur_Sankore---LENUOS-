package com.paraschool.viewer.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 8 sept. 2010
 * By bathily
 */
public class PagePanel extends Composite {

	interface PagePanelUiBinder extends UiBinder<Widget, PagePanel> {}
	private static PagePanelUiBinder uiBinder = GWT.create(PagePanelUiBinder.class);
	
	@UiField FlowPanel interactivities;

	public PagePanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void addInteractivity(String id, Widget w) {
		w.addStyleName(id);
		interactivities.add(w);
	}
	
	public void clearAllInteractivities() {
		interactivities.clear();
	}
}
