package com.paraschool.editor.modules.segmenter.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterConstants;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterModuleMessages;
import com.paraschool.editor.modules.segmenter.client.jso.SegmenterNodeContentJSO;

public class SegmenterNodeContent extends Composite implements NodeContent {
	
	private static SegmenterNodeContentUiBinder uiBinder = GWT.create(SegmenterNodeContentUiBinder.class);
	interface SegmenterNodeContentUiBinder extends UiBinder<Widget, SegmenterNodeContent> {}

	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String part();
		String statement();
		@ClassName("has-sound") String hasSound();
		@ClassName("has-text") String hasText();
		String separator();
		String activated();
		String replaced();
	}
	
	private final ViewModuleContext context;
	private final SegmenterWidgetGenerator generator;
	private final ArrayList<Integer> activated;
	private final SegmenterModuleMessages messages;
	private final SegmenterConstants constants;
	
	@UiField CssResource css;
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	@UiField Panel soundTextContainer;
	@UiField FlowPanel cells;
	
	public SegmenterNodeContent(EventBus eventBus, ViewModuleContext context, SegmenterModuleMessages messages, SegmenterConstants constants,
			SegmenterWidgetGenerator generator) {
		initWidget(uiBinder.createAndBindUi(this));
		this.context = context;
		this.generator = generator;
		this.messages = messages;
		this.constants = constants;
		activated = new ArrayList<Integer>();
	}

	@Override
	public Widget widget(NodeJSO jso) {
		
		SegmenterNodeContentJSO contentJSO = null;
		if(jso != null && (contentJSO = jso.getContent().cast()) != null) {
			
			StatementJSO statementJSO = contentJSO.getStatement();
			if(statementJSO != null) {
				text.setHTML(statementJSO.getStatement());
				if(text.getText().trim().length() != 0) soundTextContainer.addStyleName(css.hasText());
				
				ModuleObject temp = null;
				
				String sId = statementJSO.getSound();
				if(sId != null && (temp = context.getObject(sId)) != null) {
					sound.setMedia(temp, null);
					soundTextContainer.addStyleName(css.hasSound());
				}
				
				String rId = statementJSO.getResource();
				if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
				
			}
			
			proceedOptions(jso.getOptions());
			
			String source = contentJSO.getSource();
			
			if(source !=null && source.trim().length() != 0) {
				generator.generate(source, cells, new SegmenterWidgetGenerator.GeneratorHandler() {
					@Override
					public Widget onGenerate(final int index, final String separator, final InlineLabel w) {
						w.setStyleName(css.separator());
						w.setTitle(messages.separateTitle());
						w.addClickHandler(new ClickHandler() {
							String temp = w.getText();
							@Override
							public void onClick(ClickEvent event) {
								if(activated.contains(index)){
									activated.remove(new Integer(index));
									w.removeStyleName(css.activated());
									w.setText(temp);
									w.setTitle(messages.separateTitle());
								}else{
									activated.add(index);
									String[] replaceValues = constants.replaceValues();
									if(replaceValues != null && replaceValues.length > 0) {
										if(replaceValues.length == 1 && !replaceValues[0].trim().isEmpty()) {
											w.setText(replaceValues[0]);
											w.addStyleName(css.replaced());
										}else if (replaceValues.length > 1) {
											//TODO prompt choice
										}
									}
									w.addStyleName(css.activated());
									w.setTitle(messages.joinTitle());
								}
							}
						});
					
						return w;
					}
				});
			}
		}
		
		return this;
	}

	@Override
	public NodeContentJSO getJSO() {
		// TODO Create result data
		return null;
	}
	
	protected void proceedOptions(JsArray<OptionJSO> jsos) {
		
	}
}
