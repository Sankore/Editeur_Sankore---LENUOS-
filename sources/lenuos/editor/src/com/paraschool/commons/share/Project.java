package com.paraschool.commons.share;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
@XStreamAlias("project")
public class Project implements Serializable{

	private ProjectDetails details;
	
	@XStreamImplicit(itemFieldName="page")
	private ArrayList<Page> pages;
	
	private Map<String,Resource> resources;
	
	@Deprecated
	@XStreamImplicit(itemFieldName="locale")
	private ArrayList<String> locales;
	
	public Project() {
		super();
	}
	
	public Project(ProjectDetails details) {
		this.details = details;
	}
	
	public void setDetails(ProjectDetails details) {
		this.details = details;
	}

	public ProjectDetails getDetails() {
		return details;
	}

	public void setPages(ArrayList<Page> pages) {
		this.pages = pages;
	}

	public Map<String, Resource> getResources() {
		if(resources == null)
			resources = new HashMap<String, Resource>();
		return resources;
	}

	public void setResources(Map<String, Resource> resources) {
		this.resources = resources;
	}

	@Deprecated
	public void setLocales(ArrayList<String> locales) {
		this.locales = null;
	}

	@Deprecated
	public ArrayList<String> getLocales() {
		return locales;
	}

	public ArrayList<Page> getPages() {
		if(pages == null)
			pages = new ArrayList<Page>();
		return pages;
	}
	
	public void addPage(Page page) {
		addPage(page, -1);
	}

	public void addPage(Page page, int index) {
		if(index == -1) getPages().add(page);
		else getPages().add(index, page);
	}
	
	public int removePage(Page page) {
		int index = getPageIndex(page);
		if(index >= 0)
			getPages().remove(index);
		return index;
	}
	
	public int getPageIndex(Page page) {
		return getPages().indexOf(page);
	}
	
	public void addResource(Resource resource) {
		getResources().put(resource.getId(), resource);
	}
	
	public Resource removeResource(Resource resource) {
		return getResources().remove(resource.getId());
	}
	
	public Project copy() {
		Project copy = new Project(getDetails());
		copy.setPages(getPages());
		copy.setResources(getResources());
		return copy;
	}
}
