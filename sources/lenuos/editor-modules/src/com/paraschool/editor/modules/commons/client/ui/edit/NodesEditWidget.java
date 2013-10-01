package com.paraschool.editor.modules.commons.client.ui.edit;

import java.util.Arrays;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.i18n.CommonsModuleConstants;
import com.paraschool.editor.modules.commons.client.i18n.ModuleWithNodesMessage;
import com.paraschool.editor.modules.commons.client.jso.ModuleJSO;
import com.paraschool.editor.modules.commons.client.jso.ModuleWithNodesJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.options.OptionChoice;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.OptionsResource;

/*
 * Created at 3 oct. 2010
 * By bathily
 */
public abstract class NodesEditWidget<T extends NodeContent> extends CommonsEditWidget {

	protected EditNodes<T> nodes;
	
	public NodesEditWidget(EventBus eventBus, EditModuleContext context, ModuleWithNodesMessage messages, CommonsModuleConstants constants, OptionsResource optionsResource) {
		super(eventBus, context, messages, constants, optionsResource);
	}

	private void createNodes() {
		nodes = new EditNodes<T>(eventBus, context, (ModuleWithNodesMessage)messages, getNodeContentProvider());
		getContent().add(nodes);
	}
	
	@Override
	protected void init() {
		createNodes();
		nodes.addNodeFor(null);	
	}
	
	@Override
	public ModuleJSO getData() {
		ModuleWithNodesJSO moduleJSO = super.getData().cast();
		moduleJSO.setNodes(nodes.getJSO());
		return moduleJSO;
	}

	protected abstract NodeContentFactory<T> getNodeContentProvider();

	@Override
	protected void initFromData(JavaScriptObject jso) {
		super.initFromData(jso);
		
		createNodes();
		
		ModuleWithNodesJSO object = jso.cast();
		final JsArray<NodeJSO> nodesJSO = object.getNodes();
		if(nodesJSO.length() > 0)
			Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
				int i = 0;
				@Override
				public boolean execute() {
					nodes.addNodeFor(nodesJSO.get(i));
					return ++i < nodesJSO.length();
				}
			});
		else
			nodes.addNodeFor(null);
	}

	@Override
	protected void initOptions() {
		super.initOptions();
		OptionChoice sequence = new OptionChoice(optionsResource.optionNodeSequence(), "true", ((ModuleWithNodesMessage)messages).optionNodeSequence());
		register(new Option("sequence", Arrays.asList(sequence)));
	}
}
