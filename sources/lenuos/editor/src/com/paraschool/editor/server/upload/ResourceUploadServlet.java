package com.paraschool.editor.server.upload;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.ProjectManagerProvider;

/*
 * Created at 28 août 2010
 * By bathily
 */
@SuppressWarnings("serial")
@Singleton
public class ResourceUploadServlet extends UploadServlet {

	@Inject	private ProjectManagerProvider projectManagerProvider;
	
	@Inject
	public ResourceUploadServlet(@Named("upload.max.size") long sizeLimit,
			@Named("upload.sleep") int sleep) {
		super(sizeLimit, sleep);
	}
	
	@Override
	protected String executeAction(HttpServletRequest request, List<FileItem> items) {

		String response = "<response status=\"ok\">";
		
		ProjectDetails details = new ProjectDetails(getFormParameterInItems(items, "project"), getFormParameterInItems(items, "path"), getFormParameterInItems(items, "locale"));
		ProjectManager projectManager = projectManagerProvider.getProjectManager(details);
		
		logger.debug("Execute action for project ["+details.getId()+"]");
		String tmp = processFileForProject(items, projectManager);
		logger.debug(tmp);
		response += tmp;
		return response+"</response>";
	}
	
	protected String processFileForProject(List<FileItem> items, ProjectManager projectManager) {
		return projectManager.processUploadedResourceFileForProject(items);
	}
}
