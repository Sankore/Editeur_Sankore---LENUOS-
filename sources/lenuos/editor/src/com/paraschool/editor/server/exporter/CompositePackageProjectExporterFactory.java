package com.paraschool.editor.server.exporter;

import java.util.List;


public interface CompositePackageProjectExporterFactory {
	CompositePackageProjectExporter create(List<String> exportersID);
}
