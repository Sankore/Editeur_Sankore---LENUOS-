package com.paraschool.editor.modules.segmenter.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterConstants;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterModuleMessages;
import com.paraschool.editor.modules.segmenter.client.ui.AbstractEditWidget;
import com.paraschool.editor.modules.segmenter.client.ui.EditWidget;
import com.paraschool.editor.modules.segmenter.client.ui.ViewWidget;
import com.paraschool.editor.modules.commons.client.EventBus;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class SegmenterModuleWidget implements ModuleWidget {

	protected AbstractEditWidget editWidget;
	protected ViewWidget viewWidget;
	
	protected final SegmenterModuleMessages messages;
	protected final SegmenterConstants constants;
	
	public SegmenterModuleWidget(SegmenterModuleMessages messages, SegmenterConstants constants) {
		super();
		this.messages = messages;
		this.constants = constants;
	}

	@Override
	public Widget editWidget(EditModuleContext context) {
		if(editWidget == null){
			editWidget = new EditWidget(new EventBus(), context, messages);
		}
		return editWidget;
	}
	
	@Override
	public Widget viewWidget(ViewModuleContext context) {
		viewWidget = new ViewWidget(new EventBus(), context, messages, constants);
		return viewWidget;
	}

	@Override
	public String getEditData() {
		return new JSONObject(editWidget.getData()).toString();
	}

	@Override
	public String getResultData() {
		return new JSONObject(viewWidget.getData()).toString();
	}

	@Override
	public Widget optionsWidget(EditModuleContext context) {
		return editWidget.getOptionsWidget();
	}
}
