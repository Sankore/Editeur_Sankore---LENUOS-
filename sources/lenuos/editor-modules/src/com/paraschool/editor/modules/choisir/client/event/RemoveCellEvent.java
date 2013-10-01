package com.paraschool.editor.modules.choisir.client.event;

import com.paraschool.editor.modules.choisir.client.ui.Cell;
import com.paraschool.editor.modules.commons.client.event.MoveCellEvent;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class RemoveCellEvent extends MoveCellEvent {

	
	public RemoveCellEvent(Cell cell, int previousIndex) {
		super(cell, previousIndex);
	}

	@Override
	protected void dispatch(CellEventHandler handler) {
		handler.onRemoved(this);
	}

}
