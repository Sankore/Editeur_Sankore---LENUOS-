package com.paraschool.commons.share;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
@SuppressWarnings("serial")
public class ProjectDetails implements Serializable {

	public static int DEFAULT_WIDTH = 964;
	public static int DEFAULT_HEIGTH = 654;
	
	private String id;
	private String path;
	private String name;
	private String description;
	private String objectif;
	private String version;
	
	private Date date;
	private int width;
	private int height;
	
	private String icon;
	
	private String viewer;
	
	private Author author;
	
	private String locale;
	
	@XStreamOmitField
	private String localeName;
	
	public ProjectDetails() {}
	
	/*
	public ProjectDetails(String id, String path) {
		this(id, path, null);
	}
	*/
	
	public ProjectDetails(String id, String path, String locale) {
		this(id, path, locale, null, null, null, null, null, null);
	}
	
	public ProjectDetails(String name, String description, String objectif, String version, String viewer, Author author) {
		this(null, null, null, name, description, objectif, version, viewer, author);
	}
	
	public ProjectDetails(String id, String path, String locale, String name, String description, String objectif, String version, String viewer, Author author) {
		this(id, path, locale, name, description, objectif, version, viewer, author, new Date(), DEFAULT_WIDTH, DEFAULT_HEIGTH);
	}
	
	public ProjectDetails(String id, String path, String locale, String name, String description, String objectif, String version, String viewer, Author author, Date date, int width, int height) {
		this();
		this.id = id;
		this.path = path;
		this.locale = locale;
		this.name = name;
		this.description = description;
		this.objectif = objectif;
		this.version = version;
		this.viewer = viewer;
		this.author = author;
		this.setDate(date);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setViewer(String viewer) {
		this.viewer = viewer;
	}

	public String getViewer() {
		return viewer;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Author getAuthor() {
		return author;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		return locale;
	}
	
	public String getLocaleName() {
		return this.localeName;
	}
	
	public void setLocaleName(String name) {
		this.localeName = name;
	}
}
