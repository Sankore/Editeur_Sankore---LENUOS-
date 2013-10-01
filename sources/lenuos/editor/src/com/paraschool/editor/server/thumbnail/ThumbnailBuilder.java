package com.paraschool.editor.server.thumbnail;

import java.io.InputStream;

public interface ThumbnailBuilder {

	InputStream make(InputStream media) throws Throwable;
	
}
