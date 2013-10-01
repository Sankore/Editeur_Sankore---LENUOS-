package com.paraschool.editor.modules.ordonnerimg.client.ui;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.ordonnerimg.client.i18n.OrdonnerModuleMessages;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.options.OptionChoice;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.edit.NodesEditWidget;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class EditWidget extends NodesEditWidget<OrdonnerEditNodeContent> {

	public interface OptionsResource extends com.paraschool.editor.modules.commons.client.ui.OptionsResource,  ClientBundle {
		@Source("images/options-hide-statement.png") ImageResource optionHideStatement();
		@Source("images/options-node-sequence.png") ImageResource optionNodeSequence();
		@Source("images/options-switch-node-statement.png") ImageResource optionSwitchStatement();
		
		@Source("images/options-random-cell.png") ImageResource optionRandomCell();
		
	}
	
	private class OrdonnerNodeContentFactory implements NodeContentFactory<OrdonnerEditNodeContent> {

		@Override
		public OrdonnerEditNodeContent get() {
			return new OrdonnerEditNodeContent(eventBus, context, (OrdonnerModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			OptionChoice randomChoice = new OptionChoice(((OptionsResource)optionsResource).optionRandomCell(), "random", ((OrdonnerModuleMessages)messages).optionRandomCell());
			Option random = new Option("random", Arrays.asList(randomChoice));
			random.setSelectedChoice(randomChoice);
			return new Option[] {random};
		}
		
	}
	
	private OrdonnerNodeContentFactory contentProvider;
	
	public EditWidget(EventBus eventBus, EditModuleContext context, OrdonnerModuleMessages messages) {
		super(eventBus, context, messages, null, GWT.<OptionsResource>create(OptionsResource.class));
	}

	@Override
	protected NodeContentFactory<OrdonnerEditNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new OrdonnerNodeContentFactory();
		return contentProvider;
	}
	
}
