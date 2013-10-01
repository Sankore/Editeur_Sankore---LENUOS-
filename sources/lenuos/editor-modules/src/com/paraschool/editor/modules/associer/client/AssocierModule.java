package com.paraschool.editor.modules.associer.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleConstants;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleMessages;
import com.paraschool.editor.modules.commons.client.CommonsEditorModule;
import com.paraschool.editor.modules.commons.client.CommonsModuleResources;

public class AssocierModule extends CommonsEditorModule {

	interface Resources extends CommonsModuleResources {
		ImageResource thumbnail();
	}
	
	protected static String ID = "categorize";
	protected static String VERSION = "1.0 Beta";
	
	
	public AssocierModule() {}
	
	@Override
	public ModuleWidget newWidget() {
		return new AssocierModuleWidget((AssocierModuleMessages)messages, GWT.<AssocierModuleConstants>create(AssocierModuleConstants.class));
	}

	@Override
	public void init() {
		messages = GWT.create(AssocierModuleMessages.class);
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
