package com.paraschool.editor.shared.exception;

import com.paraschool.commons.share.ProjectDetails;

@SuppressWarnings("serial")
public class ProjectException extends RuntimeException {

	private ProjectDetails details;

	public ProjectException() {
		super();
	}

	public ProjectException(ProjectDetails details) {
		this();
		setDetails(details);
	}
	
	public void setDetails(ProjectDetails details) {
		this.details = details;
	}

	public ProjectDetails getDetails() {
		return details;
	}

}