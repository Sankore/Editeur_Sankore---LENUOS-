package com.paraschool.commons.share;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("link")
public class LinkResource extends Resource {

	public LinkResource() {
		super();
	}

	public LinkResource(String id, String url, String name, String thumbnail, String locale, String description) {
		super(id, url, name, -1, "", thumbnail, description, locale);
	}

	
}
