package com.paraschool.editor.modules.ordonner.client.ui;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
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
import com.paraschool.editor.modules.ordonner.client.i18n.OrdonnerModuleMessages;
import com.paraschool.editor.modules.ordonner.client.jso.OrdonnerNodeContentJSO;

public class OrdonnerNodeContent extends Composite implements NodeContent {
	
	private static OrdonnerNodeContentUiBinder uiBinder = GWT.create(OrdonnerNodeContentUiBinder.class);
	interface OrdonnerNodeContentUiBinder extends UiBinder<Widget, OrdonnerNodeContent> {}

	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String part();
		String statement();
		@ClassName("has-sound") String hasSound();
		@ClassName("has-text") String hasText();
		String dropzone();
		String bin();
		String draggables();
		String draggable();
	}
	
	private boolean random = false;	
	private final ViewModuleContext context;
	private final OrdonnerWidgetGenerator generator;
	
	@UiField CssResource css;
	@UiField AbsolutePanel boundary;
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	@UiField Panel soundTextContainer;
	@UiField FlowPanel cells;
	
	private final PickupDragController dragController;
	
	public OrdonnerNodeContent(EventBus eventBus, ViewModuleContext context, OrdonnerModuleMessages messages, OrdonnerWidgetGenerator generator) {
		initWidget(uiBinder.createAndBindUi(this));
		this.context = context;
		this.generator = generator;
		
		dragController = new PickupDragController(boundary, false);
		dragController.setBehaviorConstrainedToBoundaryPanel(true);
		DropController drop = new FlowPanelDropController(cells);
		dragController.registerDropController(drop);
		
	}

	@Override
	public Widget widget(NodeJSO jso) {
		
		OrdonnerNodeContentJSO contentJSO = null;
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
				generator.generate(source, cells, random, new OrdonnerWidgetGenerator.GeneratorHandler() {
					@Override
					public Widget onGenerate(int index, Widget w) {
						FocusPanel draggable = new Draggable(index);
						draggable.add(w);
						draggable.setStyleName(css.draggable());
						dragController.makeDraggable(draggable);
						return draggable;
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
		for(int i=0 ; jsos != null && i<jsos.length() ; i++) {
			OptionJSO jso = jsos.get(i);
			if(jso != null){
				if("random".equals(jso.getName()) && jso.getValue() != null){
					random = true;
					return;
				}
			}
				
		}
	}
	
	private class Draggable extends FocusPanel {
		@SuppressWarnings("unused")
		final int index;
		Draggable(int index) {
			super();
			this.index = index;
		}
	}

}
