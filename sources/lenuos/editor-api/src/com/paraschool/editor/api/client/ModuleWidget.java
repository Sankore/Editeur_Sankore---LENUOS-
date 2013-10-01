package com.paraschool.editor.api.client;

import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 15 août 2010
 * By bathily
 */
public interface ModuleWidget {

	Widget editWidget(EditModuleContext context);
	Widget optionsWidget(EditModuleContext context);
	
	Widget viewWidget(ViewModuleContext context);
	
	String getEditData();
	String getResultData();
}
