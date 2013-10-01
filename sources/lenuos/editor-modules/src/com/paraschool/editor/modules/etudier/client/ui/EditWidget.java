package com.paraschool.editor.modules.etudier.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.edit.NodesEditWidget;
import com.paraschool.editor.modules.etudier.client.i18n.EtudierModuleMessages;

/*
 * Created at 1 oct. 2010
 * By bathily
 */
public class EditWidget extends NodesEditWidget<EtudierEditNodeContent> {

	public interface OptionsResource extends com.paraschool.editor.modules.commons.client.ui.OptionsResource,  ClientBundle {
		@Source("images/options-hide-statement.png") ImageResource optionHideStatement();
		@Source("images/options-node-sequence.png") ImageResource optionNodeSequence();
		@Source("images/options-switch-node-statement.png") ImageResource optionSwitchStatement();
	}
	
	private class EtudierNodeContentFactory implements NodeContentFactory<EtudierEditNodeContent> {

		@Override
		public EtudierEditNodeContent get() {
			return new EtudierEditNodeContent(eventBus, context);
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
		
	}
	
	private EtudierNodeContentFactory contentProvider;
	
	public EditWidget(EventBus eventBus, EditModuleContext context, EtudierModuleMessages messages) {
		super(eventBus, context, messages, null, GWT.<OptionsResource>create(OptionsResource.class));
	}

	@Override
	protected NodeContentFactory<EtudierEditNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new EtudierNodeContentFactory();
		return contentProvider;
	}


}
