package com.paraschool.editor.server.exporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.server.utils.Zip;

public class HTMLViewerPackage implements ViewerPackage {

	private static final String INDEX_HTML = "index.html";
	private static final String ZIP_DESTINATION = "/WEB-INF/viewer.zip";
	private static final String CONTENT_FOLDER = "viewer";
	private static final String TEMPLATES_FOLDER = "templates";
	
	private Log logger = LogFactory.getLog(getClass());
	private File viewerPackage;
	private String viewerDirectory;
	
	public HTMLViewerPackage() {
	}
	
	@Override
	public File build(String viewerDirectory) {
		logger.info("Build package from directory "+viewerDirectory);
		
		this.viewerDirectory = viewerDirectory;
		
		viewerPackage = new File(viewerDirectory+ZIP_DESTINATION);
		try{
			ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(viewerPackage);
			File htmlFile = new File(viewerDirectory+INDEX_HTML);
			File js = new File(viewerDirectory+CONTENT_FOLDER);

			File icon = new File(viewerDirectory+"images/icon.png");
			File loading = new File(viewerDirectory+"images/ajax-loader.gif");

			Zip.addEntry(zipOutput, htmlFile, null);
			Zip.addEntry(zipOutput, js, null);

			Zip.addEntry(zipOutput, icon,"images/", null);
			Zip.addEntry(zipOutput, loading,"images/", null);

			zipOutput.finish();
			zipOutput.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return viewerPackage;
	}

	@Override
	public File getPackage() {
		return viewerPackage;
	}

	@Override
	public String getName() {
		return "HTMLViewer";
	}
	
	@Override
	public String getEntry(String locale) {
		return INDEX_HTML+ (locale == null ? "":"?plocale="+locale);
	}

	@Override
	public void completeSourceFolder(File source, List<Project> projects) {
		ArrayList<String> ids = new ArrayList<String>();
		File templatesFolder = new File(source, TEMPLATES_FOLDER);
		if(templatesFolder.exists()) templatesFolder.delete();
		templatesFolder.mkdir();
		
		String templates = viewerDirectory+File.separator+TEMPLATES_FOLDER+File.separator;
		logger.debug("Complete source folder with templates located at ["+templates+"]");
		
		for(Project project : projects){
			for(Page p : project.getPages()) {
				String id = p.getTemplateDetails().getId();
				File template = new File(templates+id);
				if(!ids.contains(id) && template.exists() && template.isDirectory()){
					File templateDestination = new File(templatesFolder, id);
					templateDestination.mkdir();
					try {
						FileUtils.copyDirectory(template, templateDestination);
					} catch (IOException e) {
						e.printStackTrace();
					}
					ids.add(id);
				}
			}
		}
	}

}
