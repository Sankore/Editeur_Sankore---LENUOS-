package com.paraschool.editor.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.commons.client.presenter.Display;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
public abstract class CompositeDisplayView extends Composite implements Display {

	public Widget asWidget() {
		return this;
	}
	
	public void clear() {
		
	}

}
