package com.paraschool.editor.server.content.models;

import java.util.Set;

import com.google.inject.Inject;
import com.paraschool.editor.server.content.ContentProvider;
import com.paraschool.editor.shared.ProjectModel;

public class ProjectModels {

	@Inject static Set<ContentProvider<ProjectModel>> models;
	
	private ProjectModels() {};
	
	public static void initAll() {
		for(ContentProvider<ProjectModel> model : models)
			model.list();
	}
}
