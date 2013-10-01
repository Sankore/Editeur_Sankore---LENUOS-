package com.paraschool.editor.catalog.web.models.rest;


public class RestError {

	private static final long serialVersionUID = -8232774670169168784L;
	
	private String code;

	public RestError() {
		super();
	}

	public RestError(String code) {
		this();
		this.code = code;
	}

	public String toString() {
		return code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
