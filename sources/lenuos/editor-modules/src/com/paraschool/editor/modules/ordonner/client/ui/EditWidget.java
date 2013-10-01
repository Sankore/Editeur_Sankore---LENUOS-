package com.paraschool.editor.modules.ordonner.client.ui;

import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.ordonner.client.i18n.OrdonnerModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class EditWidget extends AbstractEditWidget {
	
	public EditWidget(EventBus eventBus, EditModuleContext context, OrdonnerModuleMessages messages) {
		super(eventBus, context, messages);
	}
	
	@Override
	protected OrdonnerWidgetGenerator getGenerator() {
		return new SplitWidgetGenerator(((OrdonnerModuleMessages)messages).separator());
	}

	@Override
	protected boolean useTextArea() {
		return false;
	}
}
