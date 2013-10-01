package com.paraschool.editor.modules.ordonner.lettre.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.modules.commons.client.CommonsEditorModule;
import com.paraschool.editor.modules.commons.client.CommonsModuleResources;
import com.paraschool.editor.modules.ordonner.client.OrdonnerModuleWidget;
import com.paraschool.editor.modules.ordonner.lettre.client.i18n.OrdonnerModuleMessages;

public class OrdonnerModule extends CommonsEditorModule {

	interface Resources extends CommonsModuleResources {
		ImageResource thumbnail();
	}
	
	protected static String ID = "ordonnerlettre";
	protected static String VERSION = "1.0 Beta";
	
	
	public OrdonnerModule() {}
	
	@Override
	public ModuleWidget newWidget() {
		return new OrdonnerModuleWidget((OrdonnerModuleMessages)messages);
	}

	@Override
	public void init() {
		messages = GWT.create(OrdonnerModuleMessages.class);
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
