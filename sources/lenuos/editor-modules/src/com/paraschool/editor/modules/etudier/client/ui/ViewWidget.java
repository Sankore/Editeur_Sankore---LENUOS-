package com.paraschool.editor.modules.etudier.client.ui;

import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.NodesViewWidget;
import com.paraschool.editor.modules.etudier.client.i18n.EtudierModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class ViewWidget extends NodesViewWidget<EtudierNodeContent> {

	class EtudierNodeContentProvider implements NodeContentFactory<EtudierNodeContent> {
		
		@Override
		public EtudierNodeContent get() {
			return new EtudierNodeContent(eventBus,context,(EtudierModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
	}
	
	private EtudierNodeContentProvider contentProvider;
	
	
	public ViewWidget(EventBus eventBus, ViewModuleContext context, EtudierModuleMessages messages) {
		super(eventBus, context, messages);
	}

	@Override
	protected NodeContentFactory<EtudierNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new EtudierNodeContentProvider();
		return contentProvider;
	}

}
