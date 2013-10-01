package com.paraschool.editor.modules.categoriser.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.modules.associer.client.AssocierModuleWidget;
import com.paraschool.editor.modules.categoriser.client.i18n.CategoriserModuleConstants;
import com.paraschool.editor.modules.categoriser.client.i18n.CategoriserModuleMessages;
import com.paraschool.editor.modules.commons.client.CommonsEditorModule;
import com.paraschool.editor.modules.commons.client.CommonsModuleResources;

public class CategoriserModule extends CommonsEditorModule {

	interface Resources extends CommonsModuleResources {
		ImageResource thumbnail();
	}
	
	protected static String ID = "associate";
	protected static String VERSION = "1.0 Beta";
	
	
	public CategoriserModule() {}
	
	@Override
	public ModuleWidget newWidget() {
		return new AssocierModuleWidget((CategoriserModuleMessages)messages, GWT.<CategoriserModuleConstants>create(CategoriserModuleConstants.class));
	}

	@Override
	public void init() {
		messages = GWT.create(CategoriserModuleMessages.class);
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
