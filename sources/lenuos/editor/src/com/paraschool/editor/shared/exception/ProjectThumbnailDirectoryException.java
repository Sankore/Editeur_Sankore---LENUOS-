package com.paraschool.editor.shared.exception;

import com.paraschool.commons.share.ProjectDetails;

@SuppressWarnings("serial")
public class ProjectThumbnailDirectoryException extends
		ProjectStructureException {

	public ProjectThumbnailDirectoryException() {
		super();
	}

	public ProjectThumbnailDirectoryException(ProjectDetails details) {
		super(details);
	}
}
