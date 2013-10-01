package com.paraschool.editor.server.content.models;

import com.paraschool.editor.server.content.ContentProvider;
import com.paraschool.editor.shared.ProjectModel;

public class UserProjectModelProviderFactoryImpl implements
		ProjectModelProviderFactory {

	@Override
	public ContentProvider<ProjectModel> get(String directory) {
		return new FileProjectModelProvider(directory);
	}

}
