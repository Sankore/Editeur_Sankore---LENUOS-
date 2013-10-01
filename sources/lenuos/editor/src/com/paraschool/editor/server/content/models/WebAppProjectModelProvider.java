package com.paraschool.editor.server.content.models;

import java.io.File;
import java.io.InputStream;

import javax.annotation.Nullable;
import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.editor.server.content.WebAppContentProvider;
import com.paraschool.editor.shared.ProjectModel;

public class WebAppProjectModelProvider extends WebAppContentProvider<ProjectModel> {

	@Inject
	public WebAppProjectModelProvider(@Named("webapp.project.model.directory") String path,@Nullable ServletContext servletContext) {
		super(path, servletContext);
	}
	
	@Override
	protected ProjectModel convert(InputStream source) {
		ProjectModel projectModel = super.convert(source);
		projectModel.setLocation(getPath()+File.separator+projectModel.getLocation());
		return projectModel;
	}
}
