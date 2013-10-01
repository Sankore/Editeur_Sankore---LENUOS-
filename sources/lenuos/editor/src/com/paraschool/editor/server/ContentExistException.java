package com.paraschool.editor.server;

/*
 * Created at 1 août 2010
 * By bathily
 */
@SuppressWarnings("serial")
public class ContentExistException extends RuntimeException {

	private final String id;
	
	public ContentExistException(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
