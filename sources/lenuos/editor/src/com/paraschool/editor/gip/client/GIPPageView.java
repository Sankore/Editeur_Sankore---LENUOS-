package com.paraschool.editor.gip.client;

import com.google.inject.Inject;
import com.paraschool.editor.client.view.EditorPageView;

/*
 * Created at 5 nov. 2010
 * By bathily
 */
public class GIPPageView extends EditorPageView {

	@Inject
	private GIPPageView() {
		super();
		menu.setVisible(false);
	}
}
