package com.paraschool.editor.client.view;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.interactivity.client.InteractivityView;

/*
 * Created at 4 sept. 2010
 * By bathily
 */
public class EditorInteractivityPanel extends InteractivityPanel {

	private PickupDragController dragController;

	public EditorInteractivityPanel() {
		super();
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		dragController = new PickupDragController((AbsolutePanel)getParent(), false);
		FlowPanelDropController dropController = new InteractivityDropController(this);
		dragController.registerDropController(dropController);
	}

	@Override
	public void add(Widget w) {
		assert w instanceof InteractivityView;
		super.add(w);
		InteractivityView iView = (InteractivityView)w;
		dragController.makeDraggable(iView, iView.getMoveInteractivityButton());
	}
	
	public DragController getDragController() {
		return dragController;
	}
	
}
