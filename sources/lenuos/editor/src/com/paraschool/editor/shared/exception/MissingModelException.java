package com.paraschool.editor.shared.exception;

import com.paraschool.commons.share.ProjectDetails;

@SuppressWarnings("serial")
public class MissingModelException extends ProjectCreationException {

	
	private String name;
	
	public MissingModelException() {
		super();
	}

	public MissingModelException(String name, ProjectDetails details) {
		super(details);
		setName(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	
}
