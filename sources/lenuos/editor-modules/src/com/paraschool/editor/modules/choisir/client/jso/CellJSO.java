package com.paraschool.editor.modules.choisir.client.jso;

import com.google.gwt.core.client.JavaScriptObject;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class CellJSO extends JavaScriptObject {
	protected CellJSO() {}
	public final native String getContent() /*-{return this.content;}-*/;
	public final native void setContent(String content) /*-{this.content = content;}-*/;
}
