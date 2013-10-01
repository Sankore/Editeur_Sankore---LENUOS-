package com.paraschool.editor.modules.ordonnerimg.client.ui;

import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.NodesViewWidget;
import com.paraschool.editor.modules.ordonnerimg.client.i18n.OrdonnerModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class ViewWidget extends NodesViewWidget<OrdonnerNodeContent> {

	class OrdonnerNodeContentProvider implements NodeContentFactory<OrdonnerNodeContent> {
		
		@Override
		public OrdonnerNodeContent get() {
			return new OrdonnerNodeContent(eventBus,context,(OrdonnerModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
	}
	
	private OrdonnerNodeContentProvider contentProvider;
	
	
	public ViewWidget(EventBus eventBus, ViewModuleContext context, OrdonnerModuleMessages messages) {
		super(eventBus, context, messages);
	}

	@Override
	protected NodeContentFactory<OrdonnerNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new OrdonnerNodeContentProvider();
		return contentProvider;
	}
}
