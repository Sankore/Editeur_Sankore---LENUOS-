package com.paraschool.editor.modules.ordonner.phrase.client;

import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.ordonner.client.i18n.OrdonnerModuleMessages;
import com.paraschool.editor.modules.ordonner.phrase.client.ui.EditWidget;
import com.paraschool.editor.modules.ordonner.phrase.client.ui.ViewWidget;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class OrdonnerModuleWidget extends com.paraschool.editor.modules.ordonner.client.OrdonnerModuleWidget {

	public OrdonnerModuleWidget(OrdonnerModuleMessages messages) {
		super(messages);
		
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
		if(viewWidget == null) {
			viewWidget = new ViewWidget(new EventBus(), context, messages);
		}
		return viewWidget;
	}
}
