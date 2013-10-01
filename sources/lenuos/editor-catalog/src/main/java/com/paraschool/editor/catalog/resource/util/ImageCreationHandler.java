package com.paraschool.editor.catalog.resource.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import com.paraschool.editor.catalog.models.Resource;
import com.paraschool.editor.catalog.models.TechnicalData;

public class ImageCreationHandler implements ResourceCreationHandler {
	
	private final String fileSeparator = System.getProperty("file.separator");
	private final String rootname;
	private final String pathname = ResourceUtil.getPath(Calendar.getInstance());
	
	public ImageCreationHandler(String rootname) {
		super();
		this.rootname = rootname;
	}

	private Set<TechnicalData> makeImageInfo(File file) {
		
		Set<TechnicalData> technicalDataSet = new HashSet<TechnicalData>();
		TechnicalData technicalData;
		
		BufferedImage image;
		try {
			image = ImageIO.read(file);
			technicalData = new TechnicalData("Width", new Integer(image.getWidth(null)).toString());
			technicalDataSet.add(technicalData);
			
			technicalData = new TechnicalData("Height", new Integer(image.getHeight(null)).toString());
			technicalDataSet.add(technicalData);
			
			//return new ImageInfo(image.getWidth(null), image.getHeight(null));
			return technicalDataSet;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void onCreate(Resource resource) {
		
		File dir = new File(rootname + pathname);
		String source = rootname + resource.getFileUrl();
		File thumbnail = null;
		try {
			ThumbnailBuilder thumbnailBuilder = new ImageThumbnailBuilder();
			thumbnail = thumbnailBuilder.make(source, dir);
			if (thumbnail != null){			
				resource.setThumbnailUrl(pathname + fileSeparator + thumbnail.getName());
			}
			resource.setTechnicalDatas(makeImageInfo(new File(source)));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}

}
