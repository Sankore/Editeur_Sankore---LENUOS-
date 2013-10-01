package com.paraschool.editor.modules.choisir.client.jso;

import com.google.gwt.core.client.JsArray;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class ChoisirNodeContentJSO extends NodeContentJSO {
	protected ChoisirNodeContentJSO() {}
	public final native String getStatement() /*-{return this.statement;}-*/;
	public final native void setStatement(String statement) /*-{this.statement = statement;}-*/;
	public final native JsArray<CellJSO> getCells() /*-{return this.cells;}-*/;
	public final native void setCells(JsArray<CellJSO> cells) /*-{this.cells = cells;}-*/;
	public final native String getResource() /*-{return this.resource;}-*/;
	public final native void setResource(String resource) /*-{this.resource = resource;}-*/;
}
