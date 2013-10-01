package com.paraschool.editor.modules.segmenter.client.ui;

import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class EditWidget extends AbstractEditWidget {
	
	public EditWidget(EventBus eventBus, EditModuleContext context, SegmenterModuleMessages messages) {
		super(eventBus, context, messages);
	}
	
	@Override
	protected SegmenterWidgetGenerator getGenerator() {
		return new JoinWidgetGenerator(((SegmenterModuleMessages)messages).separator());
	}

	@Override
	protected boolean useTextArea() {
		return false;
	}
}
