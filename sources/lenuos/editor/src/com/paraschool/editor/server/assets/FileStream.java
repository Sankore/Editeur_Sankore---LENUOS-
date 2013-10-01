package com.paraschool.editor.server.assets;

import java.io.InputStream;

public class FileStream {
	private InputStream inputStream;
	private String path;
	private String name;
	private long size;
	private long lastModified;
	
	public FileStream(String name, String path, InputStream input, long size, long lastModified) {
		this.name = name;
		this.path = path;
		this.inputStream = input;
		this.size = size;
		this.lastModified = lastModified;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream input) {
		this.inputStream = input;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
