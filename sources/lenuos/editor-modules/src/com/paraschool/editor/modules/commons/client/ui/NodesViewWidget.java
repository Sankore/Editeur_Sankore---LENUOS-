package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.i18n.ModuleWithNodesMessage;
import com.paraschool.editor.modules.commons.client.jso.ModuleJSO;
import com.paraschool.editor.modules.commons.client.jso.ModuleWithNodesJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;

public abstract class NodesViewWidget<T extends NodeContent> extends CommonsViewWidget {

	protected ViewNodes<T> nodes;
	private boolean isSequence;
	
	public NodesViewWidget(EventBus eventBus, ViewModuleContext context, ModuleWithNodesMessage messages) {
		super(eventBus, context, messages);
		init();
	}

	protected void init() {
		nodes = new ViewNodes<T>(isSequence,
				eventBus, context, (ModuleWithNodesMessage)messages, getNodeContentProvider());
		getContent().add(nodes);
	}
	
	protected abstract NodeContentFactory<T> getNodeContentProvider();

	@Override
	public JavaScriptObject getData() {
		ModuleWithNodesJSO object = ModuleWithNodesJSO.createObject().cast();
		object.setNodes(nodes.getJSO());
		return object;
	}

	@Override
	protected void initFromData(ModuleJSO jso) {
		super.initFromData(jso);
		
		ModuleWithNodesJSO object = jso.cast();
		
		final JsArray<NodeJSO> nodesJSO = object.getNodes();
		if(nodesJSO != null && nodesJSO.length() > 0)
			Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
				int i = 0;
				@Override
				public boolean execute() {
					nodes.addNodeFor(nodesJSO.get(i));
					return ++i < nodesJSO.length();
				}
			});
	}
	
	@Override
	protected void proceedWithOption(OptionJSO jso){
		
		if(jso.getName().equals("sequence") && jso.getValue() != null){
			isSequence = true;
			return;
		}
		else
			super.proceedWithOption(jso);
		
	}
}
