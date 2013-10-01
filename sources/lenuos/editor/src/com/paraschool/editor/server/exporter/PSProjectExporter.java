package com.paraschool.editor.server.exporter;

import java.util.Map;

import com.paraschool.editor.shared.ProjectExporterDescriptor;

public class PSProjectExporter extends ManifestProjectExporter {
	
	private final static ProjectExporterDescriptor descriptor = new ProjectExporterDescriptor("PSPackage", "Item paraschool", "Paraschool's items packager");

	@Override
	public ProjectExporterDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	protected String getTemplate(Map<String, String> options) {
		return "ps.vm";
	}

	@Override
	protected String getManifestEntry() {
		return "psmanifest.xml";
	}

}
