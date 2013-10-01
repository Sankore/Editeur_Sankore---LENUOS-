package com.paraschool.editor.modules.ordonner.phrase.client.ui;

import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.ordonner.client.i18n.OrdonnerModuleMessages;
import com.paraschool.editor.modules.ordonner.client.ui.OrdonnerWidgetGenerator;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class ViewWidget extends com.paraschool.editor.modules.ordonner.client.ui.ViewWidget {
	
	public ViewWidget(EventBus eventBus, ViewModuleContext context,OrdonnerModuleMessages messages) {
		super(eventBus, context, messages);
	}

	protected OrdonnerWidgetGenerator getGenerator() {
		return new SplitWidgetGenerator(((OrdonnerModuleMessages)messages).separator());
	}
}
