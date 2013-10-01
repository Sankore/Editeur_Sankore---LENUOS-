package com.paraschool.editor.modules.selectionner.client.ui;

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
import com.paraschool.editor.modules.selectionner.client.i18n.SelectionnerModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class EditWidget extends NodesEditWidget<SelectionnerEditNodeContent> {

	public interface OptionsResource extends com.paraschool.editor.modules.commons.client.ui.OptionsResource,  ClientBundle {
		@Source("images/options-hide-statement.png") ImageResource optionHideStatement();
		@Source("images/options-node-sequence.png") ImageResource optionNodeSequence();
		@Source("images/options-switch-node-statement.png") ImageResource optionSwitchStatement();
		@Source("images/options-switch-cell-node.png") ImageResource optionSwitchNodeStatement();
		
		@Source("images/options-checkbox.png") ImageResource optionCheckbox();
		@Source("images/options-radio.png") ImageResource optionRadio();
		
		@Source("images/options-hide-statement.png") ImageResource optionTopAlign();
		@Source("images/options-hide-statement.png") ImageResource optionMiddleAlign();
		@Source("images/options-hide-statement.png") ImageResource optionBottomAlign();
	}
	
	private class SelectionnerNodeContentFactory implements NodeContentFactory<SelectionnerEditNodeContent> {

		@Override
		public SelectionnerEditNodeContent get() {
			return new SelectionnerEditNodeContent(eventBus, context);
		}

		@Override
		public Option[] getOptions() {
			SelectionnerModuleMessages amessages = (SelectionnerModuleMessages)messages; 
			OptionChoice radio = new OptionChoice(((OptionsResource)optionsResource).optionRadio(), "radio", amessages.optionRadio());
			OptionChoice checkbox = new OptionChoice(((OptionsResource)optionsResource).optionCheckbox(), "checkbox", amessages.optionCheckbox());
			
			OptionChoice optionResourceAlignementTOP = new OptionChoice(((OptionsResource)optionsResource).optionTopAlign(), "TOP", amessages.optionTopAlign());
			OptionChoice optionResourceAlignementMIDDLE = new OptionChoice(((OptionsResource)optionsResource).optionMiddleAlign(), "MIDDLE", amessages.optionMiddleAlign());
			OptionChoice optionResourceAlignementBOTTOM = new OptionChoice(((OptionsResource)optionsResource).optionBottomAlign(), "BOTTOM", amessages.optionBottomAlign());
			
			Option option = new Option("input", Arrays.asList(radio, checkbox));
			option.setSelectedChoice(checkbox);
			Option alignement = new Option("resourceAlignement", Arrays.asList(optionResourceAlignementTOP, optionResourceAlignementMIDDLE, optionResourceAlignementBOTTOM));
			alignement.setSelectedChoice(optionResourceAlignementBOTTOM);
			
			return new Option[] {option, alignement};
		}
		
	}
	
	private SelectionnerNodeContentFactory contentProvider;
	
	public EditWidget(EventBus eventBus, EditModuleContext context, SelectionnerModuleMessages messages) {
		super(eventBus, context, messages, null, GWT.<OptionsResource>create(OptionsResource.class));
	}

	@Override
	protected NodeContentFactory<SelectionnerEditNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new SelectionnerNodeContentFactory();
		return contentProvider;
	}
	
	@Override
	protected void initOptions() {
		super.initOptions();
		OptionChoice switchNode = new OptionChoice(((OptionsResource)optionsResource).optionSwitchNodeStatement(), "switchNode", ((SelectionnerModuleMessages)messages).optionSwitchCellNode());
		register(new Option("switchNode", Arrays.asList(switchNode)));
	}

}
