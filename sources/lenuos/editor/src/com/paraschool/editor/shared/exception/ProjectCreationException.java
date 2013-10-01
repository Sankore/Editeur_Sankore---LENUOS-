package com.paraschool.editor.shared.exception;

import com.paraschool.commons.share.ProjectDetails;


/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
public class ProjectCreationException extends ProjectException {

	public ProjectCreationException() {}
	
	public ProjectCreationException(ProjectDetails details) {
		super(details);
	}

}
