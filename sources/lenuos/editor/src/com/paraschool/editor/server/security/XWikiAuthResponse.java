package com.paraschool.editor.server.security;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * Created at 28 févr. 2012
 * By bathily
 */
@XStreamAlias("authentication")
public class XWikiAuthResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -762189393313501337L;

	public static enum Status {
		SUCCESS,
		FAILURE
	}
	
	private Status status;
	private String username;
	@XStreamAlias("displayname")
	private String displayName;
	private String url;
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "XWikiAuthResponse [status=" + status + ", username=" + username
				+ ", displayName=" + displayName + ", url=" + url + "]";
	}
}
