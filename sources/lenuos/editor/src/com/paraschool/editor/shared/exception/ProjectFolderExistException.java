package com.paraschool.editor.shared.exception;

import com.paraschool.commons.share.ProjectDetails;

@SuppressWarnings("serial")
public class ProjectFolderExistException extends ProjectCreationException {

	public ProjectFolderExistException() {
		super();
	}

	public ProjectFolderExistException(ProjectDetails details) {
		super(details);
	}

	
}
