package com.paraschool.editor.gip.client;

import com.google.inject.Inject;
import com.paraschool.editor.client.i18n.AppConstants;
import com.paraschool.editor.client.view.HomeView;

/*
 * Created at 5 nov. 2010
 * By bathily
 */
public class GIPHomeView extends HomeView {

	@Inject
	protected GIPHomeView(AppConstants constants) {
		super(constants);
		root.setVisible(false);
	}

}
