package com.paraschool.editor.server.rpc;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.ProjectManagerProvider;
import com.paraschool.editor.server.assets.FileStream;

/*
 * Created at 20 juil. 2010
 * By Didier Bathily
 */
@Singleton
@SuppressWarnings("serial")
public class PublishServlet extends AbstractFileServlet {

	@Inject ProjectManagerProvider projectManagerProvider;
	
	@Override
	protected boolean forceDownload(HttpServletRequest request,
			HttpServletResponse response) {
		return true;
	}

	@Override
	protected FileStream getResource(HttpServletRequest req,
			HttpServletResponse resp) {
		ProjectManager projectManager = projectManagerProvider.getProjectManager(null);
		// Get requested file by path info.
        String requestedFile = req.getPathInfo();
        if (requestedFile == null) {
        	return null;
        }
        try {
        	logger.debug("Access publication ["+requestedFile+"]");
        	String path = URLDecoder.decode(requestedFile, "UTF-8");
        	return projectManager.getPublicationResource(path);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	
}
