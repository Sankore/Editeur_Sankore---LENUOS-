package com.paraschool.editor.server.content.templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.paraschool.commons.share.Template;
import com.paraschool.editor.server.content.ContentProvider;
import com.paraschool.editor.server.utils.Zip;

public class Templates {
	
	private static final Log logger = LogFactory.getLog(Templates.class);
	
	@Inject static Set<ContentProvider<Template>> templates;
	@Inject static ServletContext servletContext;
	
	private Templates(){}
	
	public static void initAll() {
		
		File applicationTemplatesDirectory = new File(servletContext.getRealPath("/templates"));
		if(!applicationTemplatesDirectory.exists()) applicationTemplatesDirectory.mkdir();
		
		logger.info("Clean application's templates directory");
		try {
			FileUtils.cleanDirectory(applicationTemplatesDirectory);
		} catch (IOException e) {
			logger.error("Failed to clean directory. "+e);
		}
		
		for(ContentProvider<Template> template : templates){
			ArrayList<Template> templatesList = template.list();
			
			for(Template t : templatesList) {
				
				logger.info("Install template ["+t.getId()+"] from ["+t.getLocation()+"]");
				
				try{
					File destination = new File(applicationTemplatesDirectory, t.getId());
					destination.mkdir();
					FileInputStream input = new FileInputStream(new File(t.getLocation())); 
					Zip.unzip(input, destination);
					input.close();
				}catch (Exception e) {
					logger.error("Failed to install template's ["+t.getId()+"]. "+e);
					template.unregister(t.getId());
				}
			}
		}
			
	}
}
