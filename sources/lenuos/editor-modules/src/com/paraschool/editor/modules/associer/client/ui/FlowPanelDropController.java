package com.paraschool.editor.modules.associer.client.ui;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 21 déc. 2010
 * By bathily
 */
public class FlowPanelDropController extends
		com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController {

	public FlowPanelDropController(FlowPanel dropTarget) {
		super(dropTarget);
	}

	@Override
	public void onDrop(DragContext context) {
		for (Widget widget : context.selectedWidgets) {
			dropTarget.add(widget);
		}
	}

	@Override
	protected Widget newPositioner(DragContext context) {
		Widget positioner =  super.newPositioner(context);
		positioner.setWidth(context.draggable.getOffsetWidth()+"px");
		positioner.setHeight(context.draggable.getOffsetHeight()+"px");
		return positioner;
	}

	
}
