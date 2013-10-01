package com.paraschool.editor.modules.segmenter.client.ui;

import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.NodesViewWidget;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterConstants;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class ViewWidget extends NodesViewWidget<SegmenterNodeContent> {

	class SegmenterNodeContentProvider implements NodeContentFactory<SegmenterNodeContent> {
		
		@Override
		public SegmenterNodeContent get() {
			return new SegmenterNodeContent(eventBus,context,(SegmenterModuleMessages)messages, constants, getGenerator());
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
	}
	
	private SegmenterNodeContentProvider contentProvider;
	private final SegmenterConstants constants;
	
	public ViewWidget(EventBus eventBus, ViewModuleContext context, SegmenterModuleMessages messages, SegmenterConstants constants) {
		super(eventBus, context, messages);
		this.constants = constants;
	}

	@Override
	protected NodeContentFactory<SegmenterNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new SegmenterNodeContentProvider();
		return contentProvider;
	}
	
	protected SegmenterWidgetGenerator getGenerator() {
		return new JoinWidgetGenerator(((SegmenterModuleMessages)messages).separator());
	}
}
