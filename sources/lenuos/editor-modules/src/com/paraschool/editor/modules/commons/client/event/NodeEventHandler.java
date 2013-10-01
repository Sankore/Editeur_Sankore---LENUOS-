package com.paraschool.editor.modules.commons.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public interface NodeEventHandler extends EventHandler {
	<T extends NodeContent> void onMoved(MoveNodeEvent<T> event);
	<T extends NodeContent> void onRemoved(RemoveNodeEvent<T> event);
}
