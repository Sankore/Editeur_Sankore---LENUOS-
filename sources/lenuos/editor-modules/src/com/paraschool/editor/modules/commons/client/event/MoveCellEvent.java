package com.paraschool.editor.modules.commons.client.event;

import com.paraschool.editor.modules.choisir.client.event.CellEvent;
import com.paraschool.editor.modules.choisir.client.event.CellEventHandler;
import com.paraschool.editor.modules.choisir.client.ui.Cell;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class MoveCellEvent extends CellEvent {

	private final int previousIndex;
	
	public MoveCellEvent(Cell cell, int previousIndex) {
		super(cell);
		this.previousIndex = previousIndex;
	}
	
	public final int getPreviousIndex() {
		return previousIndex;
	}

	@Override
	protected void dispatch(CellEventHandler handler) {
		handler.onMoved(this);
	}

}
