package com.paraschool.commons.share;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("picture")
public class ImageResource extends Resource implements HasInfo<ImageInfo>, IsFile {

	private ImageInfo info;
	
	public ImageResource() {
		super();
	}

	public ImageResource(String id, String url, String name, long size,
			String mimetype, String thumbnail, String description, String locale, ImageInfo info) {
		super(id, url, name, size, mimetype, thumbnail, description, locale);
		this.setInfo(info);
	}

	public void setInfo(ImageInfo info) {
		this.info = info;
	}

	public ImageInfo getInfo() {
		return info;
	}	
	
}
