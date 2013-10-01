package com.paraschool.editor.modules.segmenter.client.jso;

import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class SegmenterNodeContentJSO extends NodeContentJSO {
	protected SegmenterNodeContentJSO() {}
	
	public final native StatementJSO getStatement() /*-{return this.statement;}-*/;
	public final native void setStatement(StatementJSO statement) /*-{this.statement = statement;}-*/;
	
	public final native String getSource() /*-{return this.source;}-*/;
	public final native void setSource(String source) /*-{this.source = source;}-*/;
}
