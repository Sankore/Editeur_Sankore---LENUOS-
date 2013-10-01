package com.paraschool.editor.server.upload;

import java.util.List;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.paraschool.editor.server.ProjectManager;

@SuppressWarnings("serial")
@Singleton
public class IconUploadServlet extends ResourceUploadServlet {

	@Inject
	public IconUploadServlet(@Named("icon.upload.max.size") long sizeLimit, @Named("upload.sleep") int sleep) {
		super(sizeLimit, sleep);
	}

	@Override
	protected String processFileForProject(List<FileItem> items, ProjectManager projectManager) {
		return projectManager.processUploadedIconFileForProject(items);
	}

	
}
