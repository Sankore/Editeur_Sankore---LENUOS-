package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.htmllist.client.HTMLListItem;

public abstract class AbstractNode<T extends NodeContent> extends HTMLListItem {

	protected final T nodeContent;
	
	@UiField public InlineLabel number;
	@UiField public FlowPanel content;

	public AbstractNode(T nodeContent) {
		super();
		this.nodeContent = nodeContent;
	}

	/*
	protected void initFromData(final NodeJSO jso) {
		if(nodeContent != null && jso != null)
			nodeContent.updateWidget(jso);
	}
	*/
	
	public int getId() {
		return getIndex();
	}

	public NodeJSO getJSO() {
		return null;
	}

}