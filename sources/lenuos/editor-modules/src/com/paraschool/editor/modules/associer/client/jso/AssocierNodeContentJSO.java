package com.paraschool.editor.modules.associer.client.jso;

import com.google.gwt.core.client.JsArray;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class AssocierNodeContentJSO extends NodeContentJSO {
	protected AssocierNodeContentJSO() {}
	
	public final native StatementJSO getStatement() /*-{return this.statement;}-*/;
	public final native void setStatement(StatementJSO statement) /*-{this.statement = statement;}-*/;
	
	public final native JsArray<BlocJSO> getBlocs() /*-{return this.blocs;}-*/;
	public final native void setBlocs(JsArray<BlocJSO> blocs) /*-{this.blocs = blocs;}-*/;
	
	public final native JsArray<BlocJSO> getCells() /*-{return this.cells;}-*/;
	public final native void setCells(JsArray<BlocJSO> cells) /*-{this.cells = cells;}-*/;
}
