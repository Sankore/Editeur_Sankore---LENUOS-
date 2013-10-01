package com.paraschool.editor.server.content.models;

import com.paraschool.editor.server.content.ContentProvider;
import com.paraschool.editor.shared.ProjectModel;

public interface ProjectModelProviderFactory {

	ContentProvider<ProjectModel> get(String directory);
	
}
