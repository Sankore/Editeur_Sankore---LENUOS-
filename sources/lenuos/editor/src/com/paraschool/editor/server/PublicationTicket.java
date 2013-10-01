package com.paraschool.editor.server;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * Created at 31 oct. 2010
 * By bathily
 */
@XStreamAlias("ticket")
public class PublicationTicket {
	private long date;
	private String username;
	
	public PublicationTicket(long date, String username) {
		super();
		this.date = date;
		this.username = username;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public long getDate() {
		return date;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	
}