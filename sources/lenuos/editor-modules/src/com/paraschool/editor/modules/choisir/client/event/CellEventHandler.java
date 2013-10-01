package com.paraschool.editor.modules.choisir.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.paraschool.editor.modules.commons.client.event.MoveCellEvent;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public interface CellEventHandler extends EventHandler {
	void onAdded(AddCellEvent event);
	void onMoved(MoveCellEvent event);
	void onRemoved(RemoveCellEvent event);
}
