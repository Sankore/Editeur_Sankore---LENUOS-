package com.paraschool.editor.modules.selectionner.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.modules.commons.client.CommonsEditorModule;
import com.paraschool.editor.modules.commons.client.CommonsModuleResources;
import com.paraschool.editor.modules.selectionner.client.i18n.SelectionnerModuleMessages;

public class SelectionnerModule extends CommonsEditorModule {

	interface Resources extends CommonsModuleResources {
		ImageResource thumbnail();
	}
	
	protected static String ID = "select";
	protected static String VERSION = "1.0 Beta";
	
	
	public SelectionnerModule() {}
	
	@Override
	public ModuleWidget newWidget() {
		return new SelectionnerModuleWidget((SelectionnerModuleMessages)messages);
	}

	@Override
	public void init() {
		messages = GWT.create(SelectionnerModuleMessages.class);
		resources = GWT.create(Resources.class);
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}

}
