package com.paraschool.editor.server;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.editor.shared.exception.security.UnauthenticatedException;


public class ApplicationManager {

	private Log logger = LogFactory.getLog(getClass());
	
	private final String usersDirectory;
	private final UserProvider userProvider;
	private final String exportDirectory;
	private final String publishDirectory;
	
	@Inject
	private ApplicationManager(@Named("users.directory") String usersDirectory, 
			@Named("publish.directory") String publishDirectory,
			@Named("exports.directory") String exportDirectory,
			UserProvider userProvider) {
		
		logger.debug("Set users directory to ["+usersDirectory+"]");
		logger.debug("Set publish directory to ["+publishDirectory+"]");
		logger.debug("Set export directory to ["+exportDirectory+"]");
		this.usersDirectory = usersDirectory;
		this.userProvider = userProvider;
		this.publishDirectory = publishDirectory;
		this.exportDirectory = exportDirectory;
	}
	
	public String getUsersDirectory() {
		return usersDirectory;
	}
	
	public User getUser() {
		User u = userProvider.getCurrentUser(); 
		if(u == null)
			throw new UnauthenticatedException();
		return u;
	}
	
	public String getUserAbsoluteDirectory() {
		return getUsersDirectory()+File.separator+getUser().getDirectoryPath();
	}
	
	public String getUserProjectModelsDirectory() {
		return getUsersDirectory()+File.separator+getUser().getDirectoryPath()+File.separator+"models";
	}
	
	public String getPublishDirectory() {
		return publishDirectory;
	}
	public String getExportDirectory() {
		return exportDirectory;
	}

}
