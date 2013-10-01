package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;

/*
 * Created at 4 oct. 2010
 * By bathily
 */
public interface NodeContent {
	Widget widget(NodeJSO jso);
	NodeContentJSO getJSO();
}