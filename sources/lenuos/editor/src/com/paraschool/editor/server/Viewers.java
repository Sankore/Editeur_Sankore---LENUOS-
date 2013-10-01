package com.paraschool.editor.server;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.server.exporter.ViewerPackage;

public class Viewers {

	@Inject static Set<ViewerPackage> packages;
	@Inject static ServletContext servletContext;
	
	private Viewers(){}
	
	public static void initAll() {
		for(ViewerPackage viewerPackage : packages){
			viewerPackage.build(servletContext.getRealPath("/"));
		}
			
	}
	
	public static ViewerPackage getViewerPackage(String name) {
		for(ViewerPackage viewerPackage : packages)
			if(viewerPackage.getName().equals(name))
				return viewerPackage;
		return null;
	}
	
	public static File getPackage(String name) {
		for(ViewerPackage viewerPackage : packages)
			if(viewerPackage.getName().equals(name))
				return viewerPackage.getPackage();
		return null;
	}
	
	public static void completeSourceFolder(File source, List<Project> projects) {
		getViewerPackage(projects.get(0).getDetails().getViewer()).completeSourceFolder(source, projects);
	}
}
