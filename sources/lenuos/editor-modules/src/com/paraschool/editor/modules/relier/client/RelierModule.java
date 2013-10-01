package com.paraschool.editor.modules.relier.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.modules.commons.client.CommonsEditorModule;
import com.paraschool.editor.modules.commons.client.CommonsModuleResources;
import com.paraschool.editor.modules.relier.client.i18n.RelierModuleMessages;

public class RelierModule extends CommonsEditorModule {

	interface Resources extends CommonsModuleResources {
		ImageResource thumbnail();
	}
	
	protected static String ID = "relier";
	protected static String VERSION = "1.0 Beta";
	
	
	public RelierModule() {}
	
	@Override
	public ModuleWidget newWidget() {
		return new RelierModuleWidget((RelierModuleMessages)messages);
	}

	@Override
	public void init() {
		messages = GWT.create(RelierModuleMessages.class);
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
