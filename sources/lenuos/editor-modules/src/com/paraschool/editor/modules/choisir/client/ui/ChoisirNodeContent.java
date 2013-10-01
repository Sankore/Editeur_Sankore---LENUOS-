package com.paraschool.editor.modules.choisir.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.choisir.client.i18n.ChoisirModuleMessages;
import com.paraschool.editor.modules.choisir.client.jso.CellJSO;
import com.paraschool.editor.modules.choisir.client.jso.ChoisirNodeContentJSO;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;

public class ChoisirNodeContent extends Composite implements NodeContent{

	enum TYPE {
		RADIO,
		CHECKBOX,
		SELECT
	}
	
	private static ChoisirNodeContentUiBinder uiBinder = GWT.create(ChoisirNodeContentUiBinder.class);
	interface ChoisirNodeContentUiBinder extends UiBinder<Widget, ChoisirNodeContent> {}

	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String part();
		String statement();
		String cells();
		String switched();
	}
	
	/*
	private final EventBus eventBus;
	
	private final ChoisirModuleMessages messages;
	*/
	private final ViewModuleContext context;
	
	private TYPE type = TYPE.CHECKBOX;
	
	private HTML statement;
	@UiField FlowPanel root;
	@UiField FlowPanel cells;
	@UiField MediaContainer resource;
	@UiField CssResource css;
	
	public ChoisirNodeContent(boolean isInverse, EventBus eventBus, ViewModuleContext context, ChoisirModuleMessages messages) {
		initWidget(uiBinder.createAndBindUi(this));
		statement = new HTML();
		statement.setStyleName(css.statement());
		if(isInverse) {
			root.addStyleName(css.switched());
			root.add(statement);
		}else
			root.insert(statement, 0);
		
		this.context = context;
		
		/*
		this.eventBus = eventBus;
		
		this.messages = messages;
		*/
	}

	@Override
	public Widget widget(NodeJSO jso) {
		updateWidget(jso);
		return this;
	}

	protected void proceedOptions(JsArray<OptionJSO> jsos) {
		for(int i=0 ; jsos != null && i<jsos.length() ; i++) {
			OptionJSO jso = jsos.get(i);
			if(jso != null && "input".equals(jso.getName())){
				type = TYPE.valueOf(jso.getValue().toUpperCase());
				return;
			}
		}
	}
	
	private void updateWidget(NodeJSO jso) {
		if(jso == null) return;
		
		proceedOptions(jso.getOptions());
		
		ChoisirNodeContentJSO nodeContentJSO = (ChoisirNodeContentJSO) jso.getContent();
		statement.setHTML(nodeContentJSO.getStatement());
		
		final JsArray<CellJSO> cellsJSO = nodeContentJSO.getCells();
		final String name = HTMLPanel.createUniqueId();
		if(cellsJSO.length() > 0) {
			
			switch (type) {
				case RADIO:
					Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
						int j = 0;
						@Override
						public boolean execute() {
							cells.add(createRadio(name, j, cellsJSO.get(j)));
							return ++j < cellsJSO.length();
						}
					});
					break;
				case CHECKBOX:
					Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
						int j = 0;
						@Override
						public boolean execute() {
							cells.add(createCheckbox(name, j, cellsJSO.get(j)));
							return ++j < cellsJSO.length();
						}
					});
					break;
				case SELECT:
					final ListBox list = new ListBox();
					list.setStyleName("cell");
					list.setName(name);
					cells.add(list);
					Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
						int j = 0;
						@Override
						public boolean execute() {
							list.addItem(new HTML(cellsJSO.get(j).getContent()).getText(), j+"");
							return ++j < cellsJSO.length();
						}
					});
					break;
			}
		}
		
		String rId = nodeContentJSO.getResource();
		ModuleObject temp = null;
		if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
	}

	@Override
	public NodeContentJSO getJSO() {
		// TODO Create result data
		return null;
	}

	
	private Widget createRadio(String name, int j, CellJSO jso) {
		RadioButton radio = new RadioButton(name, jso.getContent(),true);
		radio.setFormValue(j+"");
		radio.setStyleName("cell");
		return radio;
	}
	
	private Widget createCheckbox(String name, int j, CellJSO jso) {
		CheckBox checkbox = new CheckBox(jso.getContent(),true);
		checkbox.setName(name);
		checkbox.setFormValue(j+"");
		checkbox.setStyleName("cell");
		return checkbox;
	}
}
