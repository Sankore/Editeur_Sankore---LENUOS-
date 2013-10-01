package com.paraschool.editor.modules.commons.client.jso;

import com.google.gwt.core.client.JsArray;

/*
 * Created at 3 oct. 2010
 * By bathily
 */
public class ModuleWithNodesJSO extends ModuleJSO {

	protected ModuleWithNodesJSO() {}
	
	public final native JsArray<NodeJSO> getNodes() /*-{return this.nodes;}-*/;
	public final native void setNodes(JsArray<NodeJSO> nodes) /*-{this.nodes = nodes;}-*/;
}
