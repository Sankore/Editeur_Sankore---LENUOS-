package com.paraschool.commons.share;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Author implements Serializable {

	private String name;
	private String email;
	private String href;
	private String company;
	
	public Author() {}
	
	public Author(String name, String email, String href, String company) {
		super();
		this.name = name;
		this.email = email;
		this.href = href;
		this.company = company;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	
}
