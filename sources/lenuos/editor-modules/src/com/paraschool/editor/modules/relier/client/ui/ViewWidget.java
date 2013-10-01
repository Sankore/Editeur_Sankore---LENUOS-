package com.paraschool.editor.modules.relier.client.ui;

import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.NodesViewWidget;
import com.paraschool.editor.modules.relier.client.i18n.RelierModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class ViewWidget extends NodesViewWidget<RelierNodeContent> {

	class RelierNodeContentProvider implements NodeContentFactory<RelierNodeContent> {
		private final boolean isInverse;
		
		public RelierNodeContentProvider(boolean isInverse) {
			this.isInverse = isInverse;
		}
		
		@Override
		public RelierNodeContent get() {
			return new RelierNodeContent(isInverse,eventBus,context,(RelierModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
	}
	
	private boolean isInverse = false;
	private RelierNodeContentProvider contentProvider;
	
	
	public ViewWidget(EventBus eventBus, ViewModuleContext context, RelierModuleMessages messages) {
		super(eventBus, context, messages);
	}

	@Override
	protected NodeContentFactory<RelierNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new RelierNodeContentProvider(isInverse);
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
