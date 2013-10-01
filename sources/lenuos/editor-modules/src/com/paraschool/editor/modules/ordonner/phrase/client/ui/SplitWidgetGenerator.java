package com.paraschool.editor.modules.ordonner.phrase.client.ui;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/*
 * Created at 4 nov. 2010
 * By bathily
 */
public class SplitWidgetGenerator extends com.paraschool.editor.modules.ordonner.client.ui.SplitWidgetGenerator {

	public SplitWidgetGenerator(String separator) {
		super(separator);
	}

	@Override
	protected Widget makeWidget(String w, String source) {
		return new Label(w);
	}

	@Override
	protected String[] split(String source, String separator) {
		JsArrayString result = matches(source, separator);
		String[] split = super.split(source, separator);
		for(int i=0 ; result != null && i<result.length() ; i++)
			split[i] = split[i] + result.get(i);
		return split;
	}
	
	protected final native JsArrayString matches(String source, String separator) /*-{
		return source.match(new RegExp(separator,"g"));
	}-*/;
	
}
