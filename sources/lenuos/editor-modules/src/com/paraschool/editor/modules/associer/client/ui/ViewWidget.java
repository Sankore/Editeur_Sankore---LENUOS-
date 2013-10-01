package com.paraschool.editor.modules.associer.client.ui;

import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleMessages;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.NodesViewWidget;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class ViewWidget extends NodesViewWidget<AssocierNodeContent> {

	class AssocierNodeContentProvider implements NodeContentFactory<AssocierNodeContent> {
		
		@Override
		public AssocierNodeContent get() {
			return new AssocierNodeContent(eventBus,context,(AssocierModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
	}
	
	private AssocierNodeContentProvider contentProvider;
	
	
	public ViewWidget(EventBus eventBus, ViewModuleContext context, AssocierModuleMessages messages) {
		super(eventBus, context, messages);
	}

	@Override
	protected NodeContentFactory<AssocierNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new AssocierNodeContentProvider();
		return contentProvider;
	}
}
