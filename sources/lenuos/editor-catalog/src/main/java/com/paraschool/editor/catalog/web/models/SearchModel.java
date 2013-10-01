package com.paraschool.editor.catalog.web.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="catalog")
public class SearchModel {

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
