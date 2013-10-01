package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.i18n.ModuleWithNodesMessage;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;

public class Node<T extends NodeContent> extends AbstractNode<T> {

	private static NodeUiBinder uiBinder = GWT.create(NodeUiBinder.class);
	@SuppressWarnings("rawtypes")
	interface NodeUiBinder extends UiBinder<Widget, Node> {}

	public Node(int id, EventBus eventBus, ViewModuleContext context,  ModuleWithNodesMessage messages, T nodeContent, NodeJSO jso) {
		super(nodeContent);
		add(uiBinder.createAndBindUi(this));
		
		this.number.setText(messages.nodeIdPrefix()+(id+1));
		Widget w = null;
		if(nodeContent != null && (w = nodeContent.widget(jso)) != null)
			content.add(w);
	}
	
	@Override
	public NodeJSO getJSO() {
		NodeJSO jso = NodeJSO.createObject().cast();
		jso.setContent(nodeContent.getJSO());
		return jso;
	}
}
