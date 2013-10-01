package com.paraschool.editor.modules.commons.client.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class ModuleJSO extends JavaScriptObject {

	protected ModuleJSO(){}
	
	public final native JsArray<OptionJSO> getOptions() /*-{return this.options;}-*/;
	public final native void setOptions(JsArray<OptionJSO> options) /*-{this.options = options;}-*/;
	public final native StatementJSO getStatement() /*-{return this.statement;}-*/;
	public final native void setStatement(StatementJSO statement) /*-{this.statement = statement;}-*/;
	
}
