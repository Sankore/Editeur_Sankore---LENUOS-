package com.paraschool.editor.catalog.web.models.rest;

import java.util.ArrayList;
import java.util.List;

public class RestElement<E> {
	
	protected static final String ROOT_CATALOG = "catalog";
	
	private final List<E> list = new ArrayList<E>();
	private RestError error;

	public List<E> getList() {
		return this.list;
	}

	public RestError getError() {
		return error;
	}

	public void setError(RestError error) {
		this.error = error;
	}
	
}
