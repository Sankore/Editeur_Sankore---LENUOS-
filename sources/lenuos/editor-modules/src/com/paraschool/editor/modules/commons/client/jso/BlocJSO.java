package com.paraschool.editor.modules.commons.client.jso;

import com.google.gwt.core.client.JavaScriptObject;


/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class BlocJSO extends JavaScriptObject {
	protected BlocJSO() {}
	
	public final native String getResource() /*-{return this.resource;}-*/;
	public final native void setResource(String resource) /*-{this.resource = resource;}-*/;
	
	public final native String getSound() /*-{return this.sound;}-*/;
	public final native void setSound(String sound) /*-{this.sound = sound;}-*/;
	
	public final native String getText() /*-{return this.text;}-*/;
	public final native void setText(String text) /*-{this.text = text;}-*/;
}
