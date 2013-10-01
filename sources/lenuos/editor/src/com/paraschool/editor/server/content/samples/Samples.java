package com.paraschool.editor.server.content.samples;

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
import com.paraschool.editor.server.content.ContentProvider;
import com.paraschool.editor.server.utils.Zip;
import com.paraschool.editor.shared.Sample;

public class Samples {
	
	private static final Log logger = LogFactory.getLog(Samples.class);
	
	@Inject static Set<ContentProvider<Sample>> samples;
	@Inject static ServletContext servletContext;
	
	private Samples(){}
	
	public static File getApplicationSamplesDirectory() {
		File applicationSamplesDirectory = new File(servletContext.getRealPath("/samples"));
		if(!applicationSamplesDirectory.exists()) applicationSamplesDirectory.mkdir();
		return applicationSamplesDirectory;
	}
	
	public static File getLocationForSample(String id) {
		return new File(getApplicationSamplesDirectory(), id);
	}
	
	public static void initAll() {
		logger.info("Clean application's samples directory");
		try {
			FileUtils.cleanDirectory(getApplicationSamplesDirectory());
		} catch (IOException e) {
			logger.error("Failed to clean directory. "+e);
		}
		
		for(ContentProvider<Sample> sample : samples){
			ArrayList<Sample> samplesList = sample.list();
			
			for(Sample s : samplesList) {
				String locale = s.getDetails().getLocale();
				if(locale != null)
					locale = "_"+locale;
				else
					locale = "";
				
				logger.info("Install sample ["+s.getId()+"] with locale ["+locale+"] from ["+s.getFiles()+"]");
				if(s.getFiles() != null)
					try{
						File destination = getLocationForSample(s.getId()+locale);
						destination.mkdir();
						s.setResourceLocation(destination.getAbsolutePath());
						FileInputStream input = new FileInputStream(new File(s.getFiles())); 
						Zip.unzip(input, destination);
						input.close();
					}catch (Exception e) {
						logger.error("Failed to install samples's ["+s.getId()+"]. "+e);
						sample.unregister(s.getId());
					}
			}
		}
			
	}
}
