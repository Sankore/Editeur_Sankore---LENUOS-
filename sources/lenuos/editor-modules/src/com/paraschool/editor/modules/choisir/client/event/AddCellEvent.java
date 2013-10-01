package com.paraschool.editor.modules.choisir.client.event;

import com.paraschool.editor.modules.choisir.client.ui.Cell;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class AddCellEvent extends CellEvent {

	public AddCellEvent(Cell cell) {
		super(cell);
	}

	@Override
	protected void dispatch(CellEventHandler handler) {
		handler.onAdded(this);
	}

}
