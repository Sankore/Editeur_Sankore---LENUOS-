package com.paraschool.editor.server.exporter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.shared.ProjectExporterDescriptor;

public class CompositePackageProjectExporter extends PackageProjectExporter {

	private final static ProjectExporterDescriptor descriptor = new ProjectExporterDescriptor("PACK", "", "");
	
	private final Set<PackageProjectExporter> exporters;
	private final List<String> exportersId;
	private final String extension;
	
	@Inject
	private CompositePackageProjectExporter(@Assisted List<String> exportersId, Set<PackageProjectExporter> exporters, @Named("exports.ext") String extension) {
		this.exporters = exporters;
		this.exportersId = exportersId;
		this.extension = extension;
	}
	
	@Override
	public ProjectExporterDescriptor getDescriptor() {
		if(exportersId != null && exportersId.size() == 1)
			for(ProjectExporter exporter : exporters)
				if(exporter.getDescriptor().getId().equals(exportersId.get(0)))
					return exporter.getDescriptor();
		return descriptor;
	}


	@Override
	protected void completeZIP(List<Project> projects, Map<String, String> options, File source, ZipArchiveOutputStream zipOutput) throws Exception {
		for(PackageProjectExporter exporter : exporters)
			if(exportersId != null && exportersId.contains(exporter.getDescriptor().getId()))
				exporter.completeZIP(projects, options, source, zipOutput);
	}

	@Override
	protected String getExtension() {
		return extension;
	}
}
