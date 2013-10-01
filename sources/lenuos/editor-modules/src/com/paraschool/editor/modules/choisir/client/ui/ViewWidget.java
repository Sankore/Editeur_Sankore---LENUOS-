package com.paraschool.editor.modules.choisir.client.ui;

import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.choisir.client.i18n.ChoisirModuleMessages;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.NodesViewWidget;

/*
 * Created at 8 sept. 2010
 * By bathily
 */
public class ViewWidget extends NodesViewWidget<ChoisirNodeContent> {

	class ChoisirNodeContentFactory implements NodeContentFactory<ChoisirNodeContent> {
		private final boolean isInverse;
		private ChoisirNodeContentFactory(boolean isInverse) {
			this.isInverse = isInverse;
		}
		
		@Override
		public ChoisirNodeContent get() {
			return new ChoisirNodeContent(this.isInverse, eventBus, context, (ChoisirModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
	}
	
	ChoisirNodeContentFactory contentProvider;
	private boolean isInverse = false;
	
	public ViewWidget(EventBus eventBus, ViewModuleContext context, ChoisirModuleMessages messages) {
		super(eventBus, context, messages);
	}

	@Override
	protected NodeContentFactory<ChoisirNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new ChoisirNodeContentFactory(isInverse);
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
