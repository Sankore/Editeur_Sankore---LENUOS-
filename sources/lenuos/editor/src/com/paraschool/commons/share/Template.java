package com.paraschool.commons.share;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
@XStreamAlias("template")
public class Template implements Serializable, Identifiable {

	private TemplateDetails details;
	private String location;
	
	public Template() {}
	
	public Template(TemplateDetails details, String location) {
		this.setDetails(details);
		this.setLocation(location);
	}

	public void setDetails(TemplateDetails details) {
		this.details = details;
	}

	public TemplateDetails getDetails() {
		return details;
	}

	@Override
	public String getId() {
		return getDetails().getId();
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}
	
}
