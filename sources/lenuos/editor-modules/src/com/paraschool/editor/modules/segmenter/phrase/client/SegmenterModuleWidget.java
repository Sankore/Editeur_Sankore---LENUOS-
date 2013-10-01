package com.paraschool.editor.modules.segmenter.phrase.client;

import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterConstants;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterModuleMessages;
import com.paraschool.editor.modules.segmenter.phrase.client.ui.EditWidget;


public class SegmenterModuleWidget extends
		com.paraschool.editor.modules.segmenter.client.SegmenterModuleWidget {

	public SegmenterModuleWidget(SegmenterModuleMessages messages,
			SegmenterConstants constants) {
		super(messages, constants);
	}

	@Override
	public Widget editWidget(EditModuleContext context) {
		if(editWidget == null){
			editWidget = new EditWidget(new EventBus(), context, messages);
		}
		return editWidget;
	}

	
}
