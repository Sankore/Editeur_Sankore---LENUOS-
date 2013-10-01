package com.paraschool.editor.modules.segmenter.client.ui;

import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 4 nov. 2010
 * By bathily
 */
public interface SegmenterWidgetGenerator {
	
	interface GeneratorHandler {
		Widget onGenerate(int index, String separator, InlineLabel w);
	}
	
	void generate(String value, InsertPanel panel, GeneratorHandler handler);
}