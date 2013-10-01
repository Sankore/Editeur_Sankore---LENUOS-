package com.paraschool.editor.modules.etudier.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.modules.commons.client.CommonsEditorModule;
import com.paraschool.editor.modules.commons.client.CommonsModuleResources;
import com.paraschool.editor.modules.etudier.client.i18n.EtudierModuleMessages;

public class EtudierModule extends CommonsEditorModule {

	interface Resources extends CommonsModuleResources {
		ImageResource thumbnail();
	}
	
	protected static String ID = "consult";
	protected static String VERSION = "1.0 Beta";
	
	
	public EtudierModule() {}
	
	@Override
	public ModuleWidget newWidget() {
		return new EtudierModuleWidget((EtudierModuleMessages)messages);
	}

	@Override
	public void init() {
		messages = GWT.create(EtudierModuleMessages.class);
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
