package com.paraschool.editor.catalog.web.models.rest;

import java.io.Serializable;

public class RestFile implements Serializable {

	private static final long serialVersionUID = 8709222468292307073L;
	private String contentType;
	private byte[] binary;
	private String filename;
	
	public RestFile() {
		super();
	}

	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getBinary() {
		return binary;
	}
	public void setBinary(byte[] binary) {
		this.binary = binary;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
}
