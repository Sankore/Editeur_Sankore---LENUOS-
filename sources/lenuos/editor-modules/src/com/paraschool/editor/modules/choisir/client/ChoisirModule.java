package com.paraschool.editor.modules.choisir.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.modules.choisir.client.i18n.ChoisirModuleMessages;
import com.paraschool.editor.modules.commons.client.CommonsEditorModule;
import com.paraschool.editor.modules.commons.client.CommonsModuleResources;

public class ChoisirModule extends CommonsEditorModule {

	interface Resources extends CommonsModuleResources {
		ImageResource thumbnail();
	}
	
	protected static String ID = "quizz";
	protected static String VERSION = "1.0 Beta";
	
	
	public ChoisirModule() {}
	
	@Override
	public ModuleWidget newWidget() {
		return new ChoisirModuleWidget((ChoisirModuleMessages)messages);
	}

	@Override
	public void init() {
		messages = GWT.create(ChoisirModuleMessages.class);
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
