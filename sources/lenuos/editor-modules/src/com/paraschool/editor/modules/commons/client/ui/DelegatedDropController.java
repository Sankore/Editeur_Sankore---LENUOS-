package com.paraschool.editor.modules.commons.client.ui;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.Widget;

public class DelegatedDropController implements DropController {

	private final DropController sourceDropController;
	private final DropController targetDropController;
	
	public DelegatedDropController(DropController sourceDropController,
			DropController targetDropController) {
		this.sourceDropController = sourceDropController;
		this.targetDropController = targetDropController;
	}

	@Override
	public Widget getDropTarget() {
		return sourceDropController.getDropTarget();
	}

	@Override
	public void onDrop(DragContext context) {
		try{
			targetDropController.onDrop(context);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnter(DragContext context) {
		sourceDropController.onEnter(context);
	}

	@Override
	public void onLeave(DragContext context) {
		sourceDropController.onLeave(context);
	}

	@Override
	public void onMove(DragContext context) {
		sourceDropController.onMove(context);
	}

	@Override
	public void onPreviewDrop(DragContext context) throws VetoDragException {
		sourceDropController.onPreviewDrop(context);
	}
	
}
