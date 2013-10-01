package com.paraschool.editor.modules.segmenter.phrase.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.modules.commons.client.CommonsEditorModule;
import com.paraschool.editor.modules.commons.client.CommonsModuleResources;
import com.paraschool.editor.modules.segmenter.phrase.client.i18n.SegmenterConstants;
import com.paraschool.editor.modules.segmenter.phrase.client.i18n.SegmenterModuleMessages;

public class SegmenterModule extends CommonsEditorModule {

	interface Resources extends CommonsModuleResources {
		ImageResource thumbnail();
	}
	
	protected static String ID = "segmenterphrase";
	protected static String VERSION = "1.0 Beta";
	
	
	public SegmenterModule() {}
	
	@Override
	public ModuleWidget newWidget() {
		return new SegmenterModuleWidget((SegmenterModuleMessages)messages,GWT.<SegmenterConstants>create(SegmenterConstants.class));
	}

	@Override
	public void init() {
		messages = GWT.create(SegmenterModuleMessages.class);
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
