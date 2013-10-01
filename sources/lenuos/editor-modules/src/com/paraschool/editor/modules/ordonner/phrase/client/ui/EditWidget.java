package com.paraschool.editor.modules.ordonner.phrase.client.ui;

import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.ordonner.client.i18n.OrdonnerModuleMessages;
import com.paraschool.editor.modules.ordonner.client.ui.AbstractEditWidget;
import com.paraschool.editor.modules.ordonner.client.ui.OrdonnerWidgetGenerator;

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
		return true;
	}
	
	
}
