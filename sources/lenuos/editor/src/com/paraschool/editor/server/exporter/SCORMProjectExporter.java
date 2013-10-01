package com.paraschool.editor.server.exporter;

import java.util.Map;

import com.paraschool.editor.shared.ProjectExporterDescriptor;

public class SCORMProjectExporter extends ManifestProjectExporter {

	private final static ProjectExporterDescriptor descriptor = new ProjectExporterDescriptor("SCORM", "SCORM", "SCORM");
	
	@Override
	public ProjectExporterDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	protected String getTemplate(Map<String, String> options) {
		String version = null;
		if((version = options.get("scorm.version")) != null && isValidVersion(version))
			return String.format("scorm%s.vm", version);
		else
			return "scorm2004.vm";
	}

	private boolean isValidVersion(String version) {
		return version.equals("1.2") || version.equals("2004");
	}

	@Override
	protected String getManifestEntry() {
		return "imsmanifest.xml";
	}


}
