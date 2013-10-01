package com.paraschool.editor.client.api;

import com.google.gwt.core.client.JavaScriptObject;


/*
 * Created at 7 nov. 2010
 * By bathily
 */
public class ResourceJSO extends JavaScriptObject {
	protected ResourceJSO(){}
	
	public final native String getDescription() /*-{return this.description;}-*/;
	public final native  void setDescription(String description) /*-{this.description = description;}-*/;
	
	public final native  void setId(String id) /*-{this.id = id;}-*/;
	public final native String getId() /*-{return this.id;}-*/;
	
	public final native String getUrl() /*-{return this.url;}-*/;
	public final native void setUrl(String url) /*-{this.url = url;}-*/;

	public final native String getName() /*-{return this.name;}-*/;
	public final native void setName(String name) /*-{this.name = name;}-*/;

	public final native String getMimetype() /*-{return this.mimetype;}-*/;
	public final native void setMimetype(String mimetype) /*-{this.mimetype = mimetype;}-*/;

	public final native String getThumbnail() /*-{return this.thumbnail;}-*/;
	public final native void setThumbnail(String thumbnail) /*-{this.thumbnail = thumbnail;}-*/;
}
