package com.paraschool.editor.modules.segmenter.client.ui;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.InsertPanel;

/*
 * Created at 4 nov. 2010
 * By bathily
 */
public class JoinWidgetGenerator implements SegmenterWidgetGenerator {

	private final String separator;
	
	public JoinWidgetGenerator(String separator) {
		this.separator = separator;
	}
	
	@Override
	public void generate(String value, InsertPanel panel, GeneratorHandler handler) {
		
		JsArrayString result = matches(value, separator);
		String[] split = value.split(separator);
		
		int resultLenght = result.length();
		int j=0;
		
		for(int i=0 ; split != null && i<split.length ; i++){
			String splitted = split[i];
			int splittedLenght = splitted.length();
			for(int k=0 ; k < splittedLenght ; k++) {
				panel.add(new InlineLabel(splitted.charAt(k)+""));
				boolean canAddSeparator = false;
				String sep = null;
				if(k < splittedLenght-1) {
					canAddSeparator = true;
				}else if(i < resultLenght) {
					canAddSeparator = true;
					sep=result.get(i);
				}
				
				if(canAddSeparator){
					if(handler != null)
						panel.add(handler.onGenerate(j, sep, new InlineLabel("|")));
					else
						panel.add(new InlineLabel("|"));
					j++;
				}
			}
			if(i < resultLenght)
				j+=result.get(i).length();
		}
	}
	
	protected String[] split(String source, String separator) {
		JsArrayString result = matches(source, separator);
		String[] split = source.split(separator);
		for(int i=0 ; result != null && i<result.length() ; i++)
			split[i] = split[i] + result.get(i);
		return split;
	}
	
	protected final native JsArrayString matches(String source, String separator) /*-{
		return source.match(new RegExp(separator,"g"));
	}-*/;
}
