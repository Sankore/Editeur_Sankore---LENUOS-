package com.paraschool.editor.modules.choisir.client.ui;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.choisir.client.i18n.ChoisirModuleMessages;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.i18n.ModuleWithNodesMessage;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.options.OptionChoice;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.edit.NodesEditWidget;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class EditWidget extends NodesEditWidget<ChoisirEditNodeContent> {
	
	class ChoisirNodeContentFactory implements NodeContentFactory<ChoisirEditNodeContent> {
		@Override
		public ChoisirEditNodeContent get() {
			return new ChoisirEditNodeContent(eventBus, context, (ChoisirModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			ChoisirModuleMessages amessages = (ChoisirModuleMessages)messages;
			
			OptionChoice radio = new OptionChoice(NodeOptionsResource.INSTANCE.optionRadio(), "radio", amessages.optionRadio());
			OptionChoice checkbox = new OptionChoice(NodeOptionsResource.INSTANCE.optionCheckbox(), "checkbox", amessages.optionCheckbox());
			OptionChoice select = new OptionChoice(NodeOptionsResource.INSTANCE.optionSelect(), "select", amessages.optionSelect());
			Option option = new Option("input", Arrays.asList(radio, checkbox, select));
			option.setSelectedChoice(checkbox);
			
			return new Option[] {option};
		}
	}
	
	public interface OptionsResource extends com.paraschool.editor.modules.commons.client.ui.OptionsResource, ClientBundle {
		@Source("images/options-hide-statement.png") ImageResource optionHideStatement();
		@Source("images/options-node-sequence.png") ImageResource optionNodeSequence();
		@Source("images/options-switch-node-statement.png") ImageResource optionSwitchStatement();
		
		@Source("images/options-switch-cell-node.png") ImageResource optionSwitchNodeStatement();
	}
	
	ChoisirNodeContentFactory contentProvider;
	
	
	public EditWidget(EventBus eventBus, EditModuleContext context, ChoisirModuleMessages messages) {
		super(eventBus, context, messages, null, GWT.<OptionsResource>create(OptionsResource.class));
	}
	
	@Override
	protected NodeContentFactory<ChoisirEditNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new ChoisirNodeContentFactory();
		return contentProvider;
	}
	
	@Override
	protected void initOptions() {
		super.initOptions();
		OptionChoice switchNode = new OptionChoice(((OptionsResource)optionsResource).optionSwitchNodeStatement(), "switchNode", ((ModuleWithNodesMessage)messages).optionSwitchNodeStatement());
		register(new Option("switchNode", Arrays.asList(switchNode)));
	}
	
}
