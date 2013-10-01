package com.paraschool.editor.server.content.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.paraschool.commons.share.ImageInfo;
import com.paraschool.commons.share.ImageResource;
import com.paraschool.editor.server.ProjectManager;
import com.paraschool.editor.server.thumbnail.ImageThumbnailBuilder;
import com.paraschool.editor.server.thumbnail.ThumbnailBuilder;

public class ImageCreationHandler implements ResourceCreationHandler<ImageResource> {
	
	private static Log logger = LogFactory.getLog(ImageCreationHandler.class);
	
	private ImageInfo makeImageInfo(InputStream stream) {
		BufferedImage image;
		try {
			image = ImageIO.read(stream);
			return new ImageInfo(image.getWidth(null), image.getHeight(null));
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return null;
	}

	@Override
	public void onCreate(ImageResource resource, ProjectManager projectManager) {
		
		InputStream resourceStream = projectManager.getResource(resource);
		InputStream thumbnail = null;
		if(resourceStream != null)
			try {
				//TODO Inject
				ThumbnailBuilder thumbnailBuilder = new ImageThumbnailBuilder();
				thumbnail = thumbnailBuilder.make(resourceStream);
				projectManager.addThumbnailForResource(resource, thumbnail, thumbnail.available());
				thumbnail.close();
				resource.setInfo(makeImageInfo(resourceStream));
			} catch (Throwable e) {
				e.printStackTrace();
			}finally {
				try {
					resourceStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
	}
	
}
