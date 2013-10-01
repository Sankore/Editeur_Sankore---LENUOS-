package com.paraschool.editor.server;

import java.util.ArrayList;

/*
 * Created at 1 août 2010
 * By bathily
 */
public class User {

	private String username;
	private String authorName;
	private String directoryPath;
	private ArrayList<String> authorizedTemplate;
	private ArrayList<String> authorizedModel;
	
	public User(String username, String authorName, ArrayList<String> authorizedTemplate, ArrayList<String> authorizedModel) {
		this(username, authorName, username, authorizedTemplate, authorizedModel);
	}
	
	public User(String username, String authorName, String directoryPath, ArrayList<String> authorizedTemplate, ArrayList<String> authorizedModel) {
		super();
		this.username = username;
		this.authorName = authorName;
		this.directoryPath = directoryPath;
		this.authorizedTemplate = authorizedTemplate;
		this.setAuthorizedModel(authorizedModel);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	public ArrayList<String> getAuthorizedTemplate() {
		return authorizedTemplate;
	}
	public void setAuthorizedTemplate(ArrayList<String> authorizedTemplate) {
		this.authorizedTemplate = authorizedTemplate;
	}

	public void setAuthorizedModel(ArrayList<String> authorizedModel) {
		this.authorizedModel = authorizedModel;
	}

	public ArrayList<String> getAuthorizedModel() {
		return authorizedModel;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
}
