package com.paraschool.editor.modules.segmenter.phrase.client.ui;

import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterModuleMessages;

public class EditWidget extends
		com.paraschool.editor.modules.segmenter.client.ui.EditWidget {

	public EditWidget(EventBus eventBus, EditModuleContext context,
			SegmenterModuleMessages messages) {
		super(eventBus, context, messages);
	}

	@Override
	protected boolean useTextArea() {
		return true;
	}

	
}
