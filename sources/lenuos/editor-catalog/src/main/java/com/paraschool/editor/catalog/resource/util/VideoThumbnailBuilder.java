package com.paraschool.editor.catalog.resource.util;

import java.io.File;


public class VideoThumbnailBuilder extends ImageThumbnailBuilder implements
		ThumbnailBuilder {

	@Override
	public File make(String mediaURL, File destinationDirectory) throws Throwable, NoClassDefFoundError {
		File media = new File(mediaURL);
		DecodeAndCaptureFrames decoder = new DecodeAndCaptureFrames(media.getAbsolutePath(),null, 1);
		return super.make(decoder.getFiles().get(0).getAbsolutePath(), destinationDirectory);
	}

	
}
