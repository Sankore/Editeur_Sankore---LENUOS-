package com.paraschool.editor.modules.relier.client.jso;

import com.google.gwt.core.client.JsArray;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class RelierNodeContentJSO extends NodeContentJSO {
	protected RelierNodeContentJSO() {}
	
	public final native StatementJSO getStatement() /*-{return this.statement;}-*/;
	public final native void setStatement(StatementJSO statement) /*-{this.statement = statement;}-*/;
	
	public final native JsArray<BlocJSO> getLeftBlocs() /*-{return this.leftBlocs;}-*/;
	public final native void setLeftBlocs(JsArray<BlocJSO> leftBlocs) /*-{this.leftBlocs = leftBlocs;}-*/;
	
	public final native JsArray<BlocJSO> getRightBlocs() /*-{return this.rightBlocs;}-*/;
	public final native void setRightBlocs(JsArray<BlocJSO> rightBlocs) /*-{this.rightBlocs = rightBlocs;}-*/;
	
}
