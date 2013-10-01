package com.paraschool.editor.modules.associer.client.ui;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleConstants;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleMessages;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.options.OptionChoice;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.edit.NodesEditWidget;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class EditWidget extends NodesEditWidget<AssocierEditNodeContent> {

	public interface OptionsResource extends com.paraschool.editor.modules.commons.client.ui.OptionsResource,  ClientBundle {
		@Source("images/options-hide-statement.png") ImageResource optionHideStatement();
		@Source("images/options-node-sequence.png") ImageResource optionNodeSequence();
		@Source("images/options-switch-node-statement.png") ImageResource optionSwitchStatement();
		
		@Source("images/options-random-cell.png") ImageResource optionRandomCell();
		@Source("images/options-cell-sequence.png") ImageResource optionCellSequence();
		
		@Source("images/options-random-cell.png") ImageResource optionTopAlign();
		@Source("images/options-random-cell.png") ImageResource optionMiddleAlign();
		@Source("images/options-random-cell.png") ImageResource optionBottomAlign();
	}
	
	private class AssocierNodeContentFactory implements NodeContentFactory<AssocierEditNodeContent> {

		private final int maxBloc;
		
		private AssocierNodeContentFactory(int maxBloc) {
			this.maxBloc = maxBloc;
		}
		
		@Override
		public AssocierEditNodeContent get() {
			return new AssocierEditNodeContent(eventBus, context, maxBloc, (AssocierModuleMessages)messages);
		}

		@Override
		public Option[] getOptions() {
			AssocierModuleMessages amessages = (AssocierModuleMessages)messages;
			OptionChoice randomChoice = new OptionChoice(((OptionsResource)optionsResource).optionRandomCell(), "random", ((AssocierModuleMessages)messages).optionRandomCell());
			OptionChoice cellSequence = new OptionChoice(((OptionsResource)optionsResource).optionCellSequence(), "cellSequence", amessages.optionCellSequence());
			
			OptionChoice optionResourceAlignementTOP = new OptionChoice(((OptionsResource)optionsResource).optionTopAlign(), "TOP", amessages.optionTopAlign());
			OptionChoice optionResourceAlignementMIDDLE = new OptionChoice(((OptionsResource)optionsResource).optionMiddleAlign(), "MIDDLE", amessages.optionMiddleAlign());
			OptionChoice optionResourceAlignementBOTTOM = new OptionChoice(((OptionsResource)optionsResource).optionBottomAlign(), "BOTTOM", amessages.optionBottomAlign());
			
			Option random = new Option("random", Arrays.asList(randomChoice));
			random.setSelectedChoice(randomChoice);
			
			Option cell = new Option("cellSequence", Arrays.asList(cellSequence));
			Option alignement = new Option("resourceAlignement", Arrays.asList(optionResourceAlignementTOP, optionResourceAlignementMIDDLE, optionResourceAlignementBOTTOM));
			alignement.setSelectedChoice(optionResourceAlignementBOTTOM);
			
			return new Option[] {random, cell, alignement};
		}
		
	}
	
	private AssocierNodeContentFactory contentProvider;
	
	public EditWidget(EventBus eventBus, EditModuleContext context, AssocierModuleMessages messages, AssocierModuleConstants constants) {
		super(eventBus, context, messages, constants, GWT.<OptionsResource>create(OptionsResource.class));
	}

	@Override
	protected NodeContentFactory<AssocierEditNodeContent> getNodeContentProvider() {
		if(contentProvider == null){
			contentProvider = new AssocierNodeContentFactory(((AssocierModuleConstants)constants).maxBloc());
		}
		return contentProvider;
	}
	
}
