package com.paraschool.editor.shared.exception;

import com.paraschool.commons.share.ProjectDetails;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
public class ProjectNotFoundException extends ProjectException {

	public ProjectNotFoundException() {}	
	public ProjectNotFoundException(ProjectDetails details) {
		super(details);
	}
	@Override
	public String getMessage() {
		ProjectDetails details = getDetails();
		return "Project ["+details.getId()+"] not found at location ["+details.getPath()+"]";
	}
	
	
}
