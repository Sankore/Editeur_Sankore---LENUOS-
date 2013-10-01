package com.paraschool.editor.client.config;

import com.google.inject.Inject;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.client.AppController;

/*
 * Created at 6 nov. 2010
 * By bathily
 */
public class ProjectProvider implements javax.inject.Provider<Project> {

	final AppController appController;
	@Inject 
	private ProjectProvider(AppController appController) {
		this.appController = appController;
	}
	
	@Override
	public Project get() {
		return appController.getProject();
	}

}
