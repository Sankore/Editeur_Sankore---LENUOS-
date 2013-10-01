package com.paraschool.editor.server.content;

import java.io.File;

import javax.servlet.ServletContext;

import com.paraschool.commons.share.Identifiable;

public class WebAppContentProvider<T extends Identifiable> extends FileContentProvider<T> {

	private ServletContext servletContext;
	
	public WebAppContentProvider(String path, ServletContext servletContext) {
		super(path);
		this.servletContext = servletContext;
	}

	@Override
	protected boolean needScan() {
		if(servletContext != null)
			return super.needScan();
		return false;
	}

	@Override
	protected File getFile() {
		if(files == null) {
			setPath(servletContext.getRealPath("/")+path);
		}
		return super.getFile();
	}
	
}
