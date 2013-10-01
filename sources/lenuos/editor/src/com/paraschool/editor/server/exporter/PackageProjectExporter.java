package com.paraschool.editor.server.exporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.server.Viewers;
import com.paraschool.editor.server.utils.Zip;

/*
 * Created at 19 août 2010
 * By bathily
 */
public abstract class PackageProjectExporter implements ProjectExporter {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@Inject @Named("exports.prefix") String exportsPrefix;
	@Inject @Named("exports.suffix") String exportsSuffix;
	
	@Inject @Named("exports.directory") String exportsDirectory;
	@Inject @Named("publish.directory") String publishDirectory;
	
	protected abstract String getExtension();
	protected abstract void completeZIP(List<Project> projects, Map<String, String> options, File source, ZipArchiveOutputStream zipOutput) throws Exception;
	
	public String getDestinationName(ProjectDetails details) {
		return exportsPrefix+details.getId()+exportsSuffix+getExtension();
	}
	
	private void createExportZIP(List<Project> projects, Map<String, String> options, File source, OutputStream destination, File viewerZIP)
	throws Exception {
		ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(destination);

		importZip(zipOutput, viewerZIP);
		
		Viewers.completeSourceFolder(source, projects);
		
		File[] files = source.listFiles();
		
		for(int i = 0 ; i < files.length ; i++)
			Zip.addEntry(zipOutput, files[i], null);
		
		completeZIP(projects, options, source, zipOutput);
		
		zipOutput.finish();
		zipOutput.close();
	}	

	private void importZip(ZipArchiveOutputStream zipOutput, File zip) throws ZipException,
	IOException {
		FileInputStream fileStream = new FileInputStream(zip);
		ZipArchiveInputStream zipInput = new ZipArchiveInputStream(fileStream);

		ZipArchiveEntry entry;
		while((entry = zipInput.getNextZipEntry()) != null){
			zipOutput.putArchiveEntry(entry);
			IOUtils.copy(zipInput, zipOutput);
			zipOutput.closeArchiveEntry();
		}
		zipInput.close();
		fileStream.close();
	}

	@Override
	public void exportTo(List<Project> projects, Map<String, String> options, File source, OutputStream destination)
	throws Exception {
		Project p = projects.get(0);
		ProjectDetails details = p.getDetails();
		logger.debug("Export project ["+details.getId()+"]");
		File viewer = Viewers.getPackage(details.getViewer());
		createExportZIP(projects, options, source, destination, viewer);
	}

}