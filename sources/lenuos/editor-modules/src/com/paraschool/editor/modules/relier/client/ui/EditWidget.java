package com.paraschool.editor.modules.relier.client.ui;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.options.OptionChoice;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.edit.NodesEditWidget;
import com.paraschool.editor.modules.relier.client.i18n.RelierModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class EditWidget extends NodesEditWidget<RelierEditNodeContent> {

	public interface OptionsResource extends com.paraschool.editor.modules.commons.client.ui.OptionsResource,  ClientBundle {
		@Source("images/options-hide-statement.png") ImageResource optionHideStatement();
		@Source("images/options-node-sequence.png") ImageResource optionNodeSequence();
		@Source("images/options-switch-node-statement.png") ImageResource optionSwitchStatement();
		@Source("images/options-switch-cell-node.png") ImageResource optionSwitchNodeStatement();
		
		@Source("images/options-random-cell.png") ImageResource optionRandomCell();
	}
	
	private class RelierNodeContentFactory implements NodeContentFactory<RelierEditNodeContent> {

		@Override
		public RelierEditNodeContent get() {
			return new RelierEditNodeContent(eventBus, context, (RelierModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			RelierModuleMessages amessages = (RelierModuleMessages)messages; 
			OptionChoice random = new OptionChoice(((OptionsResource)optionsResource).optionRandomCell(), "random", amessages.optionRandomCell());
			Option option = new Option("random", Arrays.asList(random));
			option.setSelectedChoice(random);
			return new Option[] {option};
		}
		
	}
	
	private RelierNodeContentFactory contentProvider;
	
	public EditWidget(EventBus eventBus, EditModuleContext context, RelierModuleMessages messages) {
		super(eventBus, context, messages, null, GWT.<OptionsResource>create(OptionsResource.class));
	}

	@Override
	protected NodeContentFactory<RelierEditNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new RelierNodeContentFactory();
		return contentProvider;
	}
	
	@Override
	protected void initOptions() {
		super.initOptions();
		OptionChoice switchNode = new OptionChoice(((OptionsResource)optionsResource).optionSwitchNodeStatement(), "switchNode", ((RelierModuleMessages)messages).optionSwitchCellNode());
		register(new Option("switchNode", Arrays.asList(switchNode)));
	}

}
