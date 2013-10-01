package com.paraschool.editor.shared.exception;

import com.paraschool.commons.share.ProjectDetails;

@SuppressWarnings("serial")
public class ProjectResourceDirectoryException extends
		ProjectStructureException {

	public ProjectResourceDirectoryException() {
		super();
	}

	public ProjectResourceDirectoryException(ProjectDetails details) {
		super(details);
	}
}
