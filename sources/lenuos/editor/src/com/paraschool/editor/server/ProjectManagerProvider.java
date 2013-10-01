package com.paraschool.editor.server;

import com.paraschool.commons.share.ProjectDetails;

public interface ProjectManagerProvider {
	ProjectManager getProjectManager(ProjectDetails details);
}
