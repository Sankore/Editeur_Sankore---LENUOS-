package com.paraschool.editor.modules.ordonner.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 4 nov. 2010
 * By bathily
 */
public class SplitWidgetGenerator implements OrdonnerWidgetGenerator {

	private final String separator;
	
	public SplitWidgetGenerator(String separator) {
		this.separator = separator;
	}
	
	@Override
	public void generate(String value, InsertPanel panel, boolean random, GeneratorHandler handler) {
		String[] word = split(value, separator);
		
		int i=0;
		if(!random)
			for(String w : word) {
				generateFor(w, i++, value, panel, handler);
			}
		else {
			ArrayList<Integer> meeted = new ArrayList<Integer>();
			while(meeted.size() < word.length) {
				i = Random.nextInt(word.length);
				while (meeted.contains(i)) {
					i = Random.nextInt(word.length);
				}
				meeted.add(i);
				generateFor(word[i], i, value, panel, handler);
			}
		}
		
	}
	
	protected String[] split(String source, String separator) {
		return source.split(separator);
	}
	
	protected Widget makeWidget(String w, String source) {
		InlineLabel html  = new InlineLabel(w);
		return html;
	}
	
	private void generateFor(String w, int i, String source, InsertPanel panel, GeneratorHandler handler) {
		Widget widget  = makeWidget(w, source);
		if(handler != null)
			panel.add(handler.onGenerate(i, widget));
		else
			panel.add(widget);
	}

}
