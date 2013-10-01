package com.paraschool.editor.modules.etudier.client.jso;

import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class EtudierNodeContentJSO extends NodeContentJSO {
	protected EtudierNodeContentJSO() {}

	public final native StatementJSO getStatement() /*-{return this.statement;}-*/;
	public final native void setStatement(StatementJSO statement) /*-{this.statement = statement;}-*/;
}
