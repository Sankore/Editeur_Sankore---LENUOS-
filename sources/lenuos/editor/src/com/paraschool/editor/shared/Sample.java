package com.paraschool.editor.shared;

import java.io.Serializable;
import java.util.Map;

import com.paraschool.commons.share.Identifiable;
import com.paraschool.commons.share.Resource;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("sample")
public class Sample implements Serializable , Identifiable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private SampleDetails details;
	private Map<String,Resource> resources;
	@XStreamAlias("package")
	private String files;
	
	@XStreamOmitField
	private String resourceLocation;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public SampleDetails getDetails() {
		return details;
	}
	public void setDetails(SampleDetails details) {
		this.details = details;
	}
	
	public Map<String,Resource> getResources() {
		return resources;
	}
	public void setResources(Map<String,Resource> resources) {
		this.resources = resources;
	}
	public String getFiles() {
		return files;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	
	public String getResourceLocation() {
		return resourceLocation;
	}
	public void setResourceLocation(String resourceLocation) {
		this.resourceLocation = resourceLocation;
	}
	
	@Override
	public String getId() {
		return details.getId();
	}
	
}
