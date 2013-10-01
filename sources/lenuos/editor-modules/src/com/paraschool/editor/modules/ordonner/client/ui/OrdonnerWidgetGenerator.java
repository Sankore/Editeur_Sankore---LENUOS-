package com.paraschool.editor.modules.ordonner.client.ui;

import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 4 nov. 2010
 * By bathily
 */
public interface OrdonnerWidgetGenerator {
	
	interface GeneratorHandler {
		Widget onGenerate(int index, Widget w);
	}
	
	void generate(String value, InsertPanel panel, boolean random, GeneratorHandler handler);
}