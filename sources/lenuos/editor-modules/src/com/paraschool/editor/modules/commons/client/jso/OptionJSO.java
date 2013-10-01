package com.paraschool.editor.modules.commons.client.jso;

import com.google.gwt.core.client.JavaScriptObject;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class OptionJSO extends JavaScriptObject {

	protected OptionJSO(){}
	
	public final native String getName() /*-{return this.name;}-*/;
	public final native void setName(String name) /*-{this.name = name;}-*/;
	public final native String getValue() /*-{return this.value;}-*/;
	public final native void setValue(String value) /*-{this.value = value;}-*/;
	
}
