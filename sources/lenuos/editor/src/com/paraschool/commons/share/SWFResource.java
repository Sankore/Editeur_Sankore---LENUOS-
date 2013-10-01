package com.paraschool.commons.share;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*
 * Created at 28 août 2010
 * By bathily
 */
@SuppressWarnings("serial")
@XStreamAlias("swf")
public class SWFResource extends Resource implements HasInfo<SWFInfo>, IsFile {

	private SWFInfo info;

	public SWFResource() {
		super();
	}

	public SWFResource(String id, String url, String name, long size,
			String mimetype, String thumbnail, String description, String locale, SWFInfo info) {
		super(id, url, name, size, mimetype, thumbnail, description, locale);
		this.info = info;
	}

	public SWFInfo getInfo() {
		return info;
	}

	public void setInfo(SWFInfo info) {
		this.info = info;
	}

}
