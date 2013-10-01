package com.paraschool.editor.server.exporter;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.server.Viewers;

public abstract class ManifestProjectExporter extends PackageProjectExporter {

	
	protected abstract String getTemplate(Map<String, String> options);
	protected abstract String getManifestEntry();
	
	@Override
	protected void completeZIP(List<Project> projects, Map<String, String> options, File source, ZipArchiveOutputStream zipOutput)
			throws Exception {
		
				VelocityContext context = new VelocityContext();
				context.put("projects", projects);
				
				Map<String, String> entries = new HashMap<String, String>(projects.size());
				for(Project p : projects){
					ProjectDetails details = p.getDetails();
					String locale = details.getLocale(); 
					entries.put(locale == null ? "":locale, Viewers.getViewerPackage(details.getViewer()).getEntry(locale));
				}
				context.put("entries", entries);
					
				Template wgtTemplate = Velocity.getTemplate(getTemplate(options));
				StringWriter st = new StringWriter();
				wgtTemplate.merge(context, st);
				
				ZipArchiveEntry entry = new ZipArchiveEntry(getManifestEntry());
				zipOutput.putArchiveEntry(entry);
				StringInputStream stream = new StringInputStream(st.toString()); 
				IOUtils.copy(stream, zipOutput);
				zipOutput.closeArchiveEntry();
				stream.close();
			}

	@Override
	protected String getExtension() {
		return ".zip";
	}
}