package com.paraschool.editor.catalog.resource.util;

import java.io.File;

public interface ThumbnailBuilder {

	File make(String mediaURL, File destinationDirectory) throws Throwable;
	
}
