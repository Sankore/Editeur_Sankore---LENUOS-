package com.paraschool.editor.server.content.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.editor.server.content.FileContentProvider;
import com.paraschool.editor.shared.ProjectModel;

public class FileProjectModelProvider extends FileContentProvider<ProjectModel> {

	@Inject
	public FileProjectModelProvider(@Named("project.model.directory") String path) {
		super(path);
	}

	@Override
	protected ProjectModel convert(InputStream source) {
		ProjectModel projectModel = super.convert(source);
		projectModel.setLocation(getPath()+File.separator+projectModel.getLocation());
		return projectModel;
	}

	@Override
	public InputStream get(ProjectModel t) {
		try {
			return new FileInputStream(new File(getPath()+File.separator+t.getLocation()));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public boolean delete(String id) {
		super.delete(id);
		return new File(getPath()+File.separator+id+".xml").delete();
	}
	
}
