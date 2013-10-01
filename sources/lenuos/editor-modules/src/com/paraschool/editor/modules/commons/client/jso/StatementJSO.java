package com.paraschool.editor.modules.commons.client.jso;

import com.google.gwt.core.client.JavaScriptObject;

/*
 * Created at 29 sept. 2010
 * By bathily
 */
public class StatementJSO extends JavaScriptObject {

	protected StatementJSO(){}
	
	public final native String getThumbnail() /*-{return this.thumbnail;}-*/;
	public final native void setThumbnail(String thumbnail) /*-{this.thumbnail = thumbnail;}-*/;
	
	public final native String getStatement() /*-{return this.statement;}-*/;
	public final native void setStatement(String statement) /*-{this.statement = statement;}-*/;
	
	public final native String getSound() /*-{return this.sound;}-*/;
	public final native void setSound(String sound) /*-{this.sound = sound;}-*/;
	
	public final native String getResource() /*-{return this.resource;}-*/;
	public final native void setResource(String resource) /*-{this.resource = resource;}-*/;
	
}
