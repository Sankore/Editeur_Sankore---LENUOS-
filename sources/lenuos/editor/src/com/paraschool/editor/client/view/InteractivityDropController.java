package com.paraschool.editor.client.view;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class InteractivityDropController extends FlowPanelDropController {

	public InteractivityDropController(FlowPanel dropTarget) {
		super(dropTarget);
	}

	@Override
	  protected Widget newPositioner(DragContext context) {
	    HTML positioner = new HTML("");
	    positioner.addStyleName("drag-positioner");
	    return positioner;
	  }
	
}
