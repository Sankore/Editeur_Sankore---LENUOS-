package com.paraschool.editor.server.rpc;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.ProjectManagerProvider;
import com.paraschool.editor.server.assets.FileStream;

/*
 * Created at 20 juil. 2010
 * By Didier Bathily
 */
@Singleton
@SuppressWarnings("serial")
public class InProjectServlet extends AbstractFileServlet {

	@Inject ProjectManagerProvider projectManagerProvider;

	private ProjectManager getProjectManagerFromRequestParameters(HttpServletRequest req) {
		String project = req.getParameter("project");
		if(project != null) {
			String[] tokens = project.split("!");
			String path = tokens.length > 1 ? tokens[1] : null; 
			String locale = req.getParameter("plocale");
			return projectManagerProvider.getProjectManager(new ProjectDetails(tokens[0], path, locale));
		}
		return null;
	}
	
	@Override
	protected boolean forceDownload(HttpServletRequest request,
			HttpServletResponse response) {
		try{
			return Boolean.parseBoolean(request.getParameter("download"));
		}catch(Exception ignore) {}
		return super.forceDownload(request, response);
	}


	@Override
	protected FileStream getResource(HttpServletRequest req,
			HttpServletResponse resp) {
		ProjectManager projectManager = getProjectManagerFromRequestParameters(req);
		if(projectManager != null){
			// Get requested file by path info.
	        String requestedFile = req.getPathInfo();
	        if (requestedFile == null) {
	        	return null;
	        }
	        try {
	        	String path = URLDecoder.decode(requestedFile, "UTF-8");
	        	logger.debug("Access in project file ["+path+"]");
	        	return projectManager.getInProjectResource(path);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}
