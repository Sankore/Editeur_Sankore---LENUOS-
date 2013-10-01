package com.paraschool.commons.share;

import java.io.Serializable;

/*
 * Created at 15 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
public class ResourceDetails implements Serializable{
	
	private String id;
	private String url;
	private String name;
	private String mimetype;
	private long size;
	private String thumbnail;
	
	public ResourceDetails() {
		super();
	}
	
	public ResourceDetails(String id, String url, String name, long size, String mimetype, String thumbnail) {
		this();
		this.id = id;
		this.url = url;
		this.name = name;
		this.size = size;
		this.mimetype = mimetype;
		this.thumbnail = thumbnail;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}
	
}
