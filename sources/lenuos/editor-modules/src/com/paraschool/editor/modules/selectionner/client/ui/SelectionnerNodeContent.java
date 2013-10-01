package com.paraschool.editor.modules.selectionner.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.edit.Blocs;
import com.paraschool.editor.modules.selectionner.client.i18n.SelectionnerModuleMessages;
import com.paraschool.editor.modules.selectionner.client.jso.SelectionnerNodeContentJSO;

public class SelectionnerNodeContent extends Composite implements NodeContent {

	enum TYPE {
		RADIO,
		CHECKBOX
	}
	
	private static SelectionnerNodeContentUiBinder uiBinder = GWT.create(SelectionnerNodeContentUiBinder.class);
	interface SelectionnerNodeContentUiBinder extends UiBinder<Widget, SelectionnerNodeContent> {}

	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String part();
		String statement();
		String cells();
		String cell();
		String switched();
		String text();
		@ClassName("has-sound") String hasSound();
		@ClassName("has-text") String hasText();
		@ClassName("custom-input") String customInput();
		String checked();
	}
	
	private TYPE type = TYPE.CHECKBOX;
	private VerticalAlignmentConstant verticalAlignment = HasVerticalAlignment.ALIGN_BOTTOM;
	
	private final boolean isInverse;
	private final ViewModuleContext context;
	private final List<CheckBox> inputs = new ArrayList<CheckBox>();
	
	@UiField CssResource css;
	@UiField FlowPanel root;
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	@UiField Panel soundTextContainer;
	
	public SelectionnerNodeContent(boolean isInverse, EventBus eventBus, ViewModuleContext context, SelectionnerModuleMessages messages) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.isInverse = isInverse;
		this.context = context;
	}

	@Override
	public Widget widget(NodeJSO jso) {
		
		SelectionnerNodeContentJSO contentJSO = null;
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
			
			final JsArray<BlocJSO> blocJSO = contentJSO.getBlocs();
			if(blocJSO != null) {
				final Blocs blocs = new Blocs(4, -1);
				blocs.setStyleName(css.cells());
				proceedOptions(jso.getOptions());
				final String name = HTMLPanel.createUniqueId();
				
				Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
					int i=0;
					@Override
					public boolean execute() {
						createFromJSO(blocs, i, name, blocJSO.get(i));
						return ++i < blocJSO.length();
					}
				});
				if(isInverse)
					root.insert(blocs, 0);
				else
					root.add(blocs);
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
		for(int i=0 ; jsos != null && i<jsos.length() ; i++) {
			OptionJSO jso = jsos.get(i);
			if(jso != null){
				if("input".equals(jso.getName()) && jso.getValue() != null){
					type = TYPE.valueOf(jso.getValue().toUpperCase());
				}
				else if("resourceAlignement".equals(jso.getName()) && jso.getValue() != null){
					String value = jso.getValue();
					if("TOP".equals(value)) verticalAlignment = HasVerticalAlignment.ALIGN_TOP;
					else if("MIDDLE".equals(value)) verticalAlignment = HasVerticalAlignment.ALIGN_MIDDLE;
					else if("BOTTOM".equals(value)) verticalAlignment = HasVerticalAlignment.ALIGN_BOTTOM;
				}
			}
		}
	}

	private void createFromJSO(Blocs blocs, int column, String name, BlocJSO jso) {
		HTML text = new HTML();
		MediaContainer resource = new MediaContainer();
		MediaContainer sound = new MediaContainer();
		
		text.setStyleName(css.text());
		
		if(jso != null) {
			text.setHTML(jso.getText());
			ModuleObject temp = null;
			
			String rId = jso.getResource();
			if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
			
			String sId = jso.getSound();
			if(sId != null && (temp = context.getObject(sId)) != null) sound.setMedia(temp, null);
		}
		
		blocs.setWidget(0, column, resource);
		blocs.getCellFormatter().setVerticalAlignment(0, column, verticalAlignment);
		blocs.setWidget(1, column, sound);
		blocs.setWidget(2, column, text);
		
		CheckBox input = type.equals(TYPE.CHECKBOX) ? createCheckbox(name, column) : createRadio(name, column);
		inputs.add(input);
		blocs.setWidget(3, column, makeCustomized(input));
	}
	
	private RadioButton createRadio(String name, int j) {
		RadioButton radio = new RadioButton(name);
		radio.setFormValue(j+"");
		radio.setStyleName("cell");
		return radio;
	}
	
	private CheckBox createCheckbox(String name, int j) {
		CheckBox checkbox = new CheckBox();
		checkbox.setName(name);
		checkbox.setFormValue(j+"");
		checkbox.setStyleName("cell");
		return checkbox;
	}
	
	private Widget makeCustomized(final CheckBox input) {
		FlowPanel panel = new FlowPanel();
		
		final Button b = new Button();
		b.setStyleName(css.customInput());
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Button b = (Button)event.getSource();
				boolean checked = input.getValue();
				
				if(!checked && input instanceof RadioButton) {
					for(CheckBox other : inputs) {
						if(!other.equals(input)){
							other.setValue(checked, true);
						}
							
					}
				}
				
				input.setValue(!checked);
				
				if(checked)
					b.removeStyleName(css.checked());
				else
					b.addStyleName(css.checked());
			}
		});
		panel.add(b);
		
		input.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				boolean checked = event.getValue();
				if(!checked)
					b.removeStyleName(css.checked());
				else
					b.addStyleName(css.checked());
				
			}
		});
		panel.add(input);
		return panel;
	}
}
