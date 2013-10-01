package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.ModulePresenter;
import com.paraschool.editor.interactivity.client.InteractivityView;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class ModuleView extends InteractivityView implements ModulePresenter.Display {

	@Inject AppMessages messages;
	
	protected interface Css extends CssResource {
		String error();
	}
	
	protected interface Resources extends ClientBundle {
		Resources INSTANCE = GWT.create(Resources.class);
		@Source("css/ModuleView.css") Css css();
		@Source("images/module_error.png") ImageResource bg();
	}
	
	static {
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@Override
	public void setMissingModuleWidget() {
		HTML error = new HTML(messages.missingModuleWidget());
		error.setStyleName(Resources.INSTANCE.css().error());
		setInteractivityWidget(error, null);
	}

}
