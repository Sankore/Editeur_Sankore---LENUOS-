package com.paraschool.commons.share;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*
 * Created at 15 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
@XStreamAlias("resource")
public class Resource implements Serializable{
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String url;
	private String name;
	@XStreamAsAttribute
	private String mimetype;
	@XStreamAsAttribute
	private long size;
	@XStreamAsAttribute
	private String thumbnail;
	private String description;
	@XStreamAsAttribute
	private String locale;
	
	public Resource() {
		super();
	}

	public Resource(String id, String url, String name, long size, String mimetype, String thumbnail, String description, String locale) {
		super();
		this.description = description;
		this.id = id;
		this.url = url;
		this.name = name;
		this.mimetype = mimetype;
		this.size = size;
		this.thumbnail = thumbnail;
		this.setLocale(locale);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		return locale;
	}

	public ResourceDetails details() {
		return new ResourceDetails(id, url, name, size, mimetype, thumbnail);
	}

}
