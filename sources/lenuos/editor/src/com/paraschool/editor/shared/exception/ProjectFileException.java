package com.paraschool.editor.shared.exception;

import com.paraschool.commons.share.ProjectDetails;

@SuppressWarnings("serial")
public class ProjectFileException extends ProjectCreationException {

	public ProjectFileException() {
		super();
	}

	public ProjectFileException(ProjectDetails details) {
		super(details);
	}

	
}
