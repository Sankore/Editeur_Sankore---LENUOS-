package com.paraschool.editor.server.exporter;

import java.util.Map;

import com.paraschool.editor.shared.ProjectExporterDescriptor;

public class W3CWidgetProjectExporter extends ManifestProjectExporter {

	private final static ProjectExporterDescriptor descriptor = new ProjectExporterDescriptor("W3CWidget", "W3CWidget", "W3CWidget");

	@Override
	public ProjectExporterDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	protected String getTemplate(Map<String, String> options) {
		return "wgt.vm";
	}

	@Override
	protected String getManifestEntry() {
		return "config.xml";
	}
	
	@Override
	protected String getExtension() {
		return ".wgt";
	}

}
