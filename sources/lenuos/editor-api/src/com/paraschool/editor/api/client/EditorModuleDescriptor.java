package com.paraschool.editor.api.client;

import com.google.gwt.resources.client.ImageResource;

/*
 * Created at 14 août 2010
 * By bathily
 */
public interface EditorModuleDescriptor {
	String getId();
	String getVersion();
	String getFamily();
	String getName();
	String getDescription();
	String getAutor();
	String getCompany();
	ImageResource getThumbnail();
}
