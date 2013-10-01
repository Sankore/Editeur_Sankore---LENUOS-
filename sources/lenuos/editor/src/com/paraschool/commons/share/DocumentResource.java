package com.paraschool.commons.share;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * Created at 4 sept. 2010
 * By bathily
 */
@SuppressWarnings("serial")
@XStreamAlias("document")
public class DocumentResource extends Resource implements IsFile {

	public DocumentResource() {
		super();
	}

	public DocumentResource(String id, String url, String name, long size,
			String mimetype, String thumbnail, String description, String locale) {
		super(id, url, name, size, mimetype, thumbnail, description, locale);
	}

}
