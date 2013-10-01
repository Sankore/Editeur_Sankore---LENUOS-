package com.paraschool.editor.gip.client;

import com.google.inject.Inject;
import com.paraschool.editor.client.view.PageNavigation;
import com.paraschool.editor.client.view.ProjectView;

public class GIPProjectView extends ProjectView {

	@Inject
	private GIPProjectView(PageNavigation pagesButtons) {
		super(pagesButtons);
		arrow.setVisible(false);
		head.setVisible(false);
		nav.setVisible(false);
	}

	
}
