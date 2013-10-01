package com.paraschool.editor.modules.segmenter.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableHTML;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableMedia;
import com.paraschool.editor.modules.commons.client.ui.edit.MediaButtonFactory;
import com.paraschool.editor.modules.segmenter.client.i18n.SegmenterModuleMessages;
import com.paraschool.editor.modules.segmenter.client.jso.SegmenterNodeContentJSO;

/*
 * Created at 16 oct. 2010
 * By bathily
 */
public class SegmenterEditNodeContent extends Composite implements NodeContent, ValueChangeHandler<String>, KeyUpHandler, SegmenterWidgetGenerator.GeneratorHandler {

	private static SegmenterEditNodeContentUiBinder uiBinder = GWT.create(SegmenterEditNodeContentUiBinder.class);
	interface SegmenterEditNodeContentUiBinder extends UiBinder<Widget, SegmenterEditNodeContent> {}
	
	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String separator();
	}
	
	private final EventBus eventBus;
	private final EditModuleContext context;
	@SuppressWarnings("unused")
	private final SegmenterModuleMessages messages;
	
	private final SegmenterWidgetGenerator generator;
	
	@UiField Button showStatementButton;
	@UiField Button hideStatementButton;
	@UiField Panel statementWrapper;
	@UiField Panel statementContainer;
	
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	
	@UiField(provided=true) Button addResource = MediaButtonFactory.createAddMediaButton();
	@UiField(provided=true) Button addSound = MediaButtonFactory.createAddSoundButton();
	@UiField(provided=true) Button addText = MediaButtonFactory.createAddTextButton();
	
	@UiField HTML advice;
	@UiField(provided=true) TextBoxBase source;
	@UiField FlowPanel cells;
	@UiField CssResource css;
	
	public SegmenterEditNodeContent(EventBus eventBus, EditModuleContext context, SegmenterModuleMessages messages, SegmenterWidgetGenerator generator, boolean useTextarea) {
		if(useTextarea)
			source = new TextArea();
		else
			source = new TextBox();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		this.generator = generator;
		
		advice.setHTML(messages.advice());
		source.addValueChangeHandler(this);
		source.addKeyUpHandler(this);
	}
	
	
	@UiHandler(value={"showStatementButton","hideStatementButton"})
	protected void toogleStatement(ClickEvent event) {
		boolean toShow = event.getSource().equals(showStatementButton);
		if(toShow){
			statementWrapper.getElement().getStyle().setOpacity(1);
			statementWrapper.setHeight("auto");
		}
			
		else{
			statementWrapper.getElement().getStyle().setOpacity(0);
			statementWrapper.setHeight("0");
		}
			
		showStatementButton.setVisible(!toShow);
		hideStatementButton.setVisible(toShow);
	}
	
	@Override
	public Widget widget(NodeJSO jso) {
		updateWidget(jso);
		return this;
	}

	private void updateWidget(NodeJSO jso) {
		new EditableMedia(eventBus, context, resource, addResource, null, ModuleObject.Type.IMAGE, ModuleObject.Type.VIDEO, ModuleObject.Type.SWF);
		new EditableMedia(eventBus, context, sound, addSound, null, ModuleObject.Type.SOUND);
		
		SegmenterNodeContentJSO contentJSO = null;
		if(jso != null && (contentJSO = jso.getContent().cast()) != null) {
			
			StatementJSO statementJSO = contentJSO.getStatement();
			if(statementJSO != null) {
				text.setHTML(statementJSO.getStatement());
				ModuleObject temp = null;
				
				String sId = statementJSO.getSound();
				if(sId != null && (temp = context.getObject(sId)) != null) sound.setMedia(temp, null);
				
				String rId = statementJSO.getResource();
				if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
				
				if(text.getText().trim().length() != 0 || sId != null || rId != null)
					Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
						@Override
						public void execute() {
							showStatementButton.click();
						}
					});
			}
			
			String sourceText = contentJSO.getSource();
			if(sourceText != null && sourceText.trim().length() != 0)
				source.setValue(sourceText, true);
			
		}	
		
		new EditableHTML(eventBus, context, text, addText);
		
		if(!ModuleUtils.canAddOrRemoveContent(context)) {
			showStatementButton.setVisible(false);
		}
	}
	
	@Override
	public NodeContentJSO getJSO() {
		SegmenterNodeContentJSO jso = SegmenterNodeContentJSO.createObject().cast();
		
		StatementJSO statementJSO = StatementJSO.createObject().cast();
		
		statementJSO.setStatement(text.getHTML());
		
		ModuleObject soundObject = sound.getObject();
		statementJSO.setSound(soundObject == null ? null : soundObject.getId());
		
		ModuleObject resourceObject = resource.getObject();
		statementJSO.setResource(resourceObject == null ? null : resourceObject.getId());
		
		jso.setStatement(statementJSO);
		jso.setSource(source.getText());
		
		return jso;
	}


	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		cells.clear();
		generator.generate(event.getValue(), cells, this);
	}


	@Override
	public void onKeyUp(KeyUpEvent event) {
		cells.clear();
		generator.generate(source.getValue(), cells, this);
	}


	@Override
	public Widget onGenerate(int index, String separator, InlineLabel w) {
		w.addStyleName(css.separator());
		return w;
	}
	
}
