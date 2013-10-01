package com.paraschool.editor.modules.commons.client.event;

import com.paraschool.editor.modules.commons.client.ui.AbstractNode;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class MoveNodeEvent<T extends NodeContent> extends NodeEvent<T> {

	private final int previousIndex;
	
	public MoveNodeEvent(AbstractNode<T> node, int previousIndex) {
		super(node);
		this.previousIndex = previousIndex;
	}
	
	public int getPreviousIndex() {
		return previousIndex;
	}

	@Override
	protected void dispatch(NodeEventHandler handler) {
		handler.onMoved(this);
	}

}
