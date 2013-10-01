package com.paraschool.editor.modules.segmenter.client.ui;

/*
 * Created at 4 nov. 2010
 * By bathily
 */
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.i18n.ModuleWithNodesMessage;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.editor.modules.commons.client.ui.edit.NodesEditWidget;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterModuleMessages;

public abstract class AbstractEditWidget extends NodesEditWidget<SegmenterEditNodeContent> {

	public interface OptionsResource extends com.paraschool.editor.modules.commons.client.ui.OptionsResource,  ClientBundle {
		@Source("images/options-hide-statement.png") ImageResource optionHideStatement();
		@Source("images/options-node-sequence.png") ImageResource optionNodeSequence();
		@Source("images/options-switch-node-statement.png") ImageResource optionSwitchStatement();
	}

	protected class SegmenterNodeContentFactory implements NodeContentFactory<SegmenterEditNodeContent> {

		@Override
		public SegmenterEditNodeContent get() {
			return new SegmenterEditNodeContent(eventBus, context, (SegmenterModuleMessages)messages, getGenerator(), useTextArea());
		}

		@Override
		public Option[] getOptions() {
			return null;
		}
		
	}
	
	private SegmenterNodeContentFactory contentProvider;

	public AbstractEditWidget(EventBus eventBus, EditModuleContext context,
			ModuleWithNodesMessage messages) {
		super(eventBus, context, messages, null, GWT.<OptionsResource>create(OptionsResource.class));
	}

	@Override
	protected NodeContentFactory<SegmenterEditNodeContent> getNodeContentProvider() {
		if(contentProvider == null)
			contentProvider = new SegmenterNodeContentFactory();
		return contentProvider;
	}

	protected abstract SegmenterWidgetGenerator getGenerator();
	protected abstract boolean useTextArea();
}