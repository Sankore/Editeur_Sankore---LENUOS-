package com.paraschool.editor.server.content;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.paraschool.commons.share.Identifiable;
import com.paraschool.editor.server.Marshaller;

public abstract class XMLContentProvider<T extends Identifiable> extends MapContentProvider<T> {

	protected Marshaller marshaller = new Marshaller();

	public XMLContentProvider() {
		super();
	}

	@SuppressWarnings("unchecked")
	protected T convert(InputStream source) {
		return (T) marshaller.fromXML(source);
	}
	
	protected String[] xmlList() {
		return null;
	}
	
	protected String getPathFor(String xml) {
		return xml;
	}

	public void scan() {
		String[] xmls = xmlList();
		if(xmls != null){
			logger.debug("Found "+xmls.length+" xml files");
			
			contents = new HashMap<String, T>();
			
			for(String xml : xmls) {
				try {
					InputStream input = new FileInputStream(getPathFor(xml));
					T content = convert(input);
					if(content != null)
						contents.put(content.getId(), content);
					input.close();
				} catch (FileNotFoundException ignore) {
					logger.error(ignore);
				} catch (IOException e) {
					logger.error(e);
				}
				
			}
		}
	}

}