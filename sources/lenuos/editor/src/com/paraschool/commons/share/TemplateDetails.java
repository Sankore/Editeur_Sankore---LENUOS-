package com.paraschool.commons.share;

import java.io.Serializable;
import java.util.HashSet;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
public class TemplateDetails implements Serializable{

	private String url;
	private String id;
	private String name;
	private String description;
	@XStreamImplicit(itemFieldName="locale")
	private HashSet<String> locales;
	
	public TemplateDetails() {}
	
	public TemplateDetails(String id, String name, String description,String url) {
		super();
		this.id = id;
		this.url = url;
		this.name = name;
		this.description = description;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	public void setLocales(HashSet<String> locales) {
		this.locales = locales;
	}

	public HashSet<String> getLocales() {
		return locales;
	}
}
