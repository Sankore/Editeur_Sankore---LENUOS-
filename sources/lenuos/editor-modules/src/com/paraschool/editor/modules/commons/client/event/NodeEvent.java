package com.paraschool.editor.modules.commons.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.editor.modules.commons.client.ui.AbstractNode;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public abstract class NodeEvent<T extends NodeContent> extends GwtEvent<NodeEventHandler> {

	public static Type<NodeEventHandler> TYPE = new Type<NodeEventHandler>();
	
	private final AbstractNode<T> node;
	
	public NodeEvent(AbstractNode<T> node) {
		super();
		this.node = node;
	}

	public AbstractNode<T> getNode() {
		return node;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NodeEventHandler> getAssociatedType() {
		return TYPE;
	}

}
