package com.paraschool.viewer.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.paraschool.commons.share.AudioInfo;
import com.paraschool.commons.share.AudioResource;
import com.paraschool.commons.share.Author;
import com.paraschool.commons.share.DocumentResource;
import com.paraschool.commons.share.ImageInfo;
import com.paraschool.commons.share.ImageResource;
import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.LinkResource;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.SWFInfo;
import com.paraschool.commons.share.SWFResource;
import com.paraschool.commons.share.TemplateDetails;
import com.paraschool.commons.share.VideoInfo;
import com.paraschool.commons.share.VideoResource;

public class DefaultXMLProjectSerializer extends XMLProjectDeserializer {

	private ProjectDetails getProjectDetails(Node detailsNode) {
		ProjectDetails details = new ProjectDetails();
		
		if(detailsNode != null) {
			Element detailsElement = (Element) detailsNode;
			details.setId(detailsElement.getElementsByTagName("id").item(0).getFirstChild().getNodeValue());
			details.setName(detailsElement.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
			try {details.setDescription(detailsElement.getElementsByTagName("description").item(0).getFirstChild().getNodeValue());} catch (Exception e) {}
			try {details.setObjectif(detailsElement.getElementsByTagName("objectif").item(0).getFirstChild().getNodeValue());} catch (Exception e) {}
			try {details.setViewer(detailsElement.getElementsByTagName("viewer").item(0).getFirstChild().getNodeValue());} catch (Exception e) {}
			
			details.setAuthor(getAuthor(detailsElement.getElementsByTagName("author").item(0)));
			
			try {
				details.setDate(DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL).parse(detailsElement.getElementsByTagName("date").item(0).getFirstChild().getNodeValue()));
			} catch (Exception e) {
			}
			details.setWidth(Integer.parseInt(detailsElement.getElementsByTagName("width").item(0).getFirstChild().getNodeValue()));
			details.setHeight(Integer.parseInt(detailsElement.getElementsByTagName("height").item(0).getFirstChild().getNodeValue()));
		}
		
		return details;
	}
	
	private Author getAuthor(Node authorNode) {
		Author author = new Author();
		if(authorNode != null) {
			Element authorElement = (Element) authorNode;
			try {author.setEmail(authorElement.getElementsByTagName("email").item(0).getFirstChild().getNodeValue());} catch (Exception e) {}
			try {author.setName(authorElement.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());} catch (Exception e) {}
			try {author.setHref(authorElement.getElementsByTagName("href").item(0).getFirstChild().getNodeValue());} catch (Exception e) {}
			try {author.setCompany(authorElement.getElementsByTagName("company").item(0).getFirstChild().getNodeValue());} catch (Exception e) {}
		}
		return author;
	}
	
	private ArrayList<Page> getPages(NodeList pageList) {
		ArrayList<Page> pages = new ArrayList<Page>();
		for(int i=0 ; pageList != null && i < pageList.getLength() ; i++) {
			pages.add(getPage(pageList.item(i)));
		}
		return pages;
	}
	
	private Page getPage(Node pageNode) {
		Page page = new Page();
		
		Element pageElement = (Element) pageNode;
		try{
			page.setStatement(pageElement.getElementsByTagName("statement").item(0).getFirstChild().getNodeValue());
		}catch (Exception e) {}
		
		page.setTemplateDetails(getTemplateDetails(pageElement.getElementsByTagName("templateDetails").item(0)));
		Node interactivities = pageElement.getElementsByTagName("interactivities").item(0);
		if(interactivities != null)
			page.setInteractivities(getInteractivities(interactivities));
		
		return page;
	}
	
	private TemplateDetails getTemplateDetails(Node templateNode) {
		TemplateDetails template = new TemplateDetails();
		
		Element templateElement = (Element) templateNode;
		template.setId((templateElement.getElementsByTagName("id").item(0).getFirstChild().getNodeValue()));
		template.setUrl((templateElement.getElementsByTagName("url").item(0).getFirstChild().getNodeValue()));
		try {template.setName((templateElement.getElementsByTagName("name").item(0).getFirstChild().getNodeValue()));} catch (Exception e) {}
		try {template.setDescription((templateElement.getElementsByTagName("description").item(0).getFirstChild().getNodeValue()));} catch (Exception e) {}
		template.setLocales(getTemplateLocales(templateElement.getElementsByTagName("locale")));
		
		return template;
	}
	
	private HashSet<String> getTemplateLocales(NodeList localesList) {
		HashSet<String> locales = new HashSet<String>();
		for(int j = 0 ; localesList != null && j < localesList.getLength(); j++) {
			locales.add(localesList.item(j).getFirstChild().getNodeValue());
		}
		return locales;
	}
	
	private ArrayList<Interactivity> getInteractivities(Node interactivitesNode) {
		return getInteractivities(((Element) interactivitesNode).getElementsByTagName("interactivity"));
	}
	
	private ArrayList<Interactivity> getInteractivities(NodeList interactivityList) {
		ArrayList<Interactivity> interactivities = new ArrayList<Interactivity>();
		for(int j = 0 ; interactivityList != null && j < interactivityList.getLength(); j++) {
			interactivities.add(getInteractivity(interactivityList.item(j)));
		}
		return interactivities;
	}
	
	private Interactivity getInteractivity(Node interactivityNode) {
		Interactivity interactivity = new Interactivity();
		Element interactivityElement = (Element) interactivityNode;
		interactivity.setId(interactivityElement.getElementsByTagName("id").item(0).getFirstChild().getNodeValue());
		try{
			interactivity.setContent(interactivityElement.getElementsByTagName("content").item(0).getFirstChild().getNodeValue());
		}catch (Exception e) {}
		return interactivity;
	}
	
	private Map<String, Resource> getResourcesMap(NodeList entries) {
		Map<String, Resource> resources = new HashMap<String, Resource>();
		
		for(int i=0 ; entries != null && i < entries.getLength() ; i++) {
			Resource resource = getResource((Element) entries.item(i));
			resources.put(resource.getId(), resource);
		}
		
		return resources;
	}
	
	private Resource getResource(Element resourceEntry) {
		
		Element resourceElement = (Element)resourceEntry.getElementsByTagName("resource").item(0);
		Resource resource = null;
		
		if(resourceElement != null) resource = new Resource();
		else if((resourceElement = (Element)resourceEntry.getElementsByTagName("video").item(0)) != null){
			resource = new VideoResource();
			
			Element info = (Element)resourceElement.getElementsByTagName("info").item(0);
			if(info != null) {
				try{
					((VideoResource)resource).setInfo(new VideoInfo(Long.parseLong(info.getAttribute("duration")),
							Integer.parseInt(info.getAttribute("width")),
							Integer.parseInt(info.getAttribute("height")),
							info.getAttribute("codec"),
							info.getAttribute("format")));
				}catch (Exception ignore) {}
			}
		}else if((resourceElement = (Element)resourceEntry.getElementsByTagName("picture").item(0)) != null){
			resource = new ImageResource();
			
			Element info = (Element)resourceElement.getElementsByTagName("info").item(0);
			if(info != null) {
				try{
					((ImageResource)resource).setInfo(new ImageInfo(
							Integer.parseInt(info.getAttribute("width")),
							Integer.parseInt(info.getAttribute("height"))));
				}catch (Exception ignore) {}
			}
		}else if((resourceElement = (Element)resourceEntry.getElementsByTagName("swf").item(0)) != null){
			resource = new SWFResource();
			
			Element info = (Element)resourceElement.getElementsByTagName("info").item(0);
			if(info != null) {
				try{
					((SWFResource)resource).setInfo(new SWFInfo(
							Integer.parseInt(info.getAttribute("width")),
							Integer.parseInt(info.getAttribute("height")),
							Integer.parseInt(info.getAttribute("version")),
							Integer.parseInt(info.getAttribute("frame")),
							Float.parseFloat(info.getAttribute("rate")),
							Boolean.parseBoolean(info.getAttribute("as3")),
							Boolean.parseBoolean(info.getAttribute("network")),
							Boolean.parseBoolean(info.getAttribute("gpu"))));
				}catch (Exception ignore) {}
			}
		}else if((resourceElement = (Element)resourceEntry.getElementsByTagName("audio").item(0)) != null){
			resource = new AudioResource();
			
			Element info = (Element)resourceElement.getElementsByTagName("info").item(0);
			if(info != null) {
				try{
					((AudioResource)resource).setInfo(new AudioInfo(Long.parseLong(info.getAttribute("duration")),
							info.getAttribute("codec"),
							info.getAttribute("format")));
				}catch (Exception ignore) {}
			}
		}else if((resourceElement = (Element)resourceEntry.getElementsByTagName("link").item(0)) != null){
			resource = new LinkResource();
			
		}else if((resourceElement = (Element)resourceEntry.getElementsByTagName("document").item(0)) != null){
			resource = new DocumentResource();
		}
			
		String id = resourceElement.getAttribute("id");
		String url = resourceElement.getAttribute("url");
		String type = resourceElement.getAttribute("mimetype");
		String thumbnail = resourceElement.getAttribute("thumbnail");
		long size = 0;
		try{
			size = Long.parseLong(resourceElement.getAttribute("size"));
		}catch (NumberFormatException e) {}
		
		Node temp = null;
		String name =  (temp = resourceElement.getElementsByTagName("name").item(0).getFirstChild()) != null ? temp.getNodeValue() : null;
		String description = null;
		try{
			description = resourceElement.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
		}catch (Exception ignore) {}
		
		resource.setId(id);
		resource.setUrl(url);
		resource.setName(name);
		resource.setSize(size);
		resource.setMimetype(type);
		resource.setThumbnail(thumbnail);
		resource.setDescription(description);
		
		return resource;
	}

	@Override
	protected Project parseDocument(Document root) {
		Project project = new Project();
		project.setDetails(getProjectDetails(root.getElementsByTagName("details").item(0)));
		project.setPages(getPages(root.getElementsByTagName("page")));
		Element resources = ((Element)root.getElementsByTagName("resources").item(0));
		if(resources != null)
			project.setResources(getResourcesMap(resources.getElementsByTagName("entry")));
		return project;
	}
}
