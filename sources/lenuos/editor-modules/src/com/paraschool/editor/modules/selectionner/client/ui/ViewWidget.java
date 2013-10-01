package com.paraschool.editor.modules.selectionner.client.ui;

import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.NodesViewWidget;
import com.paraschool.editor.modules.selectionner.client.i18n.SelectionnerModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class ViewWidget extends NodesViewWidget<SelectionnerNodeContent> {

	class SelectionnerNodeContentProvider implements NodeContentFactory<SelectionnerNodeContent> {
		private final boolean isInverse;
		
		public SelectionnerNodeContentProvider(boolean isInverse) {
			this.isInverse = isInverse;
		}
		
		@Override
		public SelectionnerNodeContent get() {
			return new SelectionnerNodeContent(isInverse,eventBus,context,(SelectionnerModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
	}
	
	private boolean isInverse = false;
	private SelectionnerNodeContentProvider contentProvider;
	
	
	public ViewWidget(EventBus eventBus, ViewModuleContext context, SelectionnerModuleMessages messages) {
		super(eventBus, context, messages);
	}

	@Override
	protected NodeContentFactory<SelectionnerNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new SelectionnerNodeContentProvider(isInverse);
		return contentProvider;
	}

	@Override
	protected void proceedWithOption(OptionJSO jso){
		if(jso.getName().equals("switchNode") && jso.getValue() != null) {
			isInverse = true;
			return;
		}	
		super.proceedWithOption(jso);
	}
}
