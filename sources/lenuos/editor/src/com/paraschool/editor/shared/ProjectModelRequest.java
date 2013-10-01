package com.paraschool.editor.shared;

import java.io.Serializable;

import com.paraschool.editor.shared.ProjectModel.Owner;

@SuppressWarnings("serial")
public class ProjectModelRequest implements Serializable {

	private String id;
	private ProjectModel.Owner owner;
	
	public ProjectModelRequest() {
		super();
	}

	public ProjectModelRequest(String id, Owner owner) {
		this();
		this.id = id;
		this.owner = owner;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ProjectModel.Owner getOwner() {
		return owner;
	}
	public void setOwner(ProjectModel.Owner owner) {
		this.owner = owner;
	}
	
	
}
