package com.paraschool.editor.server.exporter;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.shared.ProjectExporterDescriptor;

public interface ProjectExporter {

	ProjectExporterDescriptor getDescriptor();
	String getDestinationName(ProjectDetails details);
	void exportTo(List<Project> list, Map<String, String> options, File source, OutputStream destination) throws Exception;
}
