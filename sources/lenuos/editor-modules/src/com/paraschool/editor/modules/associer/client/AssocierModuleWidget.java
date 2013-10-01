package com.paraschool.editor.modules.associer.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleConstants;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleMessages;
import com.paraschool.editor.modules.associer.client.ui.EditWidget;
import com.paraschool.editor.modules.associer.client.ui.ViewWidget;
import com.paraschool.editor.modules.commons.client.EventBus;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class AssocierModuleWidget implements ModuleWidget {

	private EditWidget editWidget;
	private ViewWidget viewWidget;
	
	private AssocierModuleMessages messages;
	private AssocierModuleConstants constants;
	
	public AssocierModuleWidget(AssocierModuleMessages messages, AssocierModuleConstants constants) {
		super();
		this.messages = messages;
		this.constants = constants;
	}

	@Override
	public Widget editWidget(EditModuleContext context) {
		if(editWidget == null){
			editWidget = new EditWidget(new EventBus(), context, messages, constants);
		}
		return editWidget;
	}

	@Override
	public Widget viewWidget(ViewModuleContext context) {
		viewWidget = new ViewWidget(new EventBus(), context, messages);
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
