package com.paraschool.editor.modules.commons.client.event;

import com.paraschool.editor.modules.commons.client.ui.AbstractNode;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class RemoveNodeEvent<T extends NodeContent> extends MoveNodeEvent<T> {

	
	public RemoveNodeEvent(AbstractNode<T> node, int previousIndex) {
		super(node, previousIndex);
	}

	@Override
	protected void dispatch(NodeEventHandler handler) {
		handler.onRemoved(this);
	}

}
