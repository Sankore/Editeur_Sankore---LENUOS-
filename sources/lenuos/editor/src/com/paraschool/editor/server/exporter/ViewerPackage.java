package com.paraschool.editor.server.exporter;

import java.io.File;
import java.util.List;

import com.paraschool.commons.share.Project;

public interface ViewerPackage {
	String getName();
	String getEntry(String locale);
	File build(String viewerDirectory);
	File getPackage();
	void completeSourceFolder(File source, List<Project> projects);
}
