package com.paraschool.editor.client.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface AppLayout extends IsWidget {

	FlowPanel getContent();
	HasWidgets getLoggingPanel();

}