package com.paraschool.editor.shared;

import java.io.Serializable;

import com.paraschool.commons.share.Identifiable;
import com.paraschool.commons.share.ProjectDetails;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("model")
public class ProjectModel implements Serializable, Identifiable {

	public enum Owner {
		APPLICATION,
		USER
	}
	
	private ProjectDetails details;
	private String location;
	
	public ProjectModel() {
		super();
	}

	public ProjectModel(ProjectDetails details, String location) {
		this();
		this.details = details;
		this.location = location;
	}
	
	public ProjectDetails getDetails() {
		return details;
	}
	public void setDetails(ProjectDetails details) {
		this.details = details;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String getId() {
		return details.getId();
	}
	
}
