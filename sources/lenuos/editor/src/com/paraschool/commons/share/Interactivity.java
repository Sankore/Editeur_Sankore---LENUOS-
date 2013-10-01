package com.paraschool.commons.share;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/*
 * Created at 28 juil. 2010
 * By bathily
 */
@SuppressWarnings("serial")
@XStreamAlias("interactivity")
public class Interactivity implements Serializable {
	
	private String id;
	private String content;
	
	public Interactivity() {
		this(null);
	}
	
	public Interactivity(String id) {
		this(id, null);
	}
	
	public Interactivity(String id, String content) {
		super();
		this.id = id;
		this.content = content;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
