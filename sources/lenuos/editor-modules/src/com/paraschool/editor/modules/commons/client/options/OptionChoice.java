package com.paraschool.editor.modules.commons.client.options;

import com.google.gwt.resources.client.ImageResource;

public class OptionChoice {
	
	private final ImageResource thumbnail;
	private final String description;
	private final String value;
	
	public OptionChoice(ImageResource thumbnail, String value, String description) {
		super();
		this.thumbnail = thumbnail;
		this.value = value;
		this.description = description;
	}
	
	public ImageResource getThumbnail() {
		return thumbnail;
	}
	public String getDescription() {
		return description;
	}

	public String getValue() {
		return value;
	}
	
}
