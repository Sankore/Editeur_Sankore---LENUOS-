package com.paraschool.editor.server.content;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;

import com.paraschool.commons.share.Identifiable;


public class FileContentProvider<T extends Identifiable> extends XMLContentProvider<T> {

	protected File files;
	protected String path;
	protected long lastModified = 0;

	public FileContentProvider(String path) {
		super();
		setPath(path);
	}

	protected boolean needScan() {
		return getFile().lastModified() > lastModified;
	}

	@Override
	public void scan() {
		if(needScan()){
			super.scan();
			lastModified = getFile().lastModified();
		}
	}

	@Override
	public ArrayList<T> list() {
		scan();
		return super.list();
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return getFile().getAbsolutePath();
	}

	protected File getFile() {
		if(files == null) {
			files = new File(path);
			if(!files.isAbsolute()) {
				URL url = getClass().getResource("/"+path);
				if(url != null)
					files= new File(url.getFile());
				else
					logger.warn("Content not found at ["+path+"]");
			}
			logger.info("Set path to ["+files.getAbsolutePath()+"]");
		}
		return files;
	}

	@Override
	protected String getPathFor(String xml) {
		return getFile().getAbsolutePath()+File.separator+xml;
	}

	@Override
	protected String[] xmlList() {
		if(getFile() != null && getFile().isDirectory()) {
			logger.info("Scan directory "+getFile().getAbsolutePath());

			String[] xmls = getFile().list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}
			});
			return xmls;
		}else
			logger.warn(getFile().getAbsolutePath()+" not found");
		return super.xmlList();
	}


}
