package com.paraschool.editor.modules.choisir.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.paraschool.editor.modules.choisir.client.ui.Cell;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public abstract class CellEvent extends GwtEvent<CellEventHandler> {

	public static Type<CellEventHandler> TYPE = new Type<CellEventHandler>();
	
	private final Cell cell;
	
	public CellEvent(Cell cell) {
		super();
		this.cell = cell;
	}

	public Cell getCell() {
		return cell;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CellEventHandler> getAssociatedType() {
		return TYPE;
	}

}
