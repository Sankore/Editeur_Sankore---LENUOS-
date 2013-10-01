package com.paraschool.editor.modules.ordonnerimg.client.ui;

import java.util.ArrayList;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
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
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.ordonnerimg.client.i18n.OrdonnerModuleMessages;
import com.paraschool.editor.modules.ordonnerimg.client.jso.OrdonnerNodeContentJSO;

public class OrdonnerNodeContent extends Composite implements NodeContent {
	
	private static OrdonnerNodeContentUiBinder uiBinder = GWT.create(OrdonnerNodeContentUiBinder.class);
	interface OrdonnerNodeContentUiBinder extends UiBinder<Widget, OrdonnerNodeContent> {}

	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String part();
		String statement();
		String cells();
		String cell();
		String switched();
		@ClassName("has-sound") String hasSound();
		@ClassName("has-text") String hasText();
		String dropzone();
		String bin();
		String draggables();
		String draggable();
	}
	
	private boolean random = false;	
	private final ViewModuleContext context;
	
	@UiField CssResource css;
	@UiField AbsolutePanel boundary;
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	@UiField Panel soundTextContainer;
	
	private final PickupDragController dragController;
	
	public OrdonnerNodeContent(EventBus eventBus, ViewModuleContext context, OrdonnerModuleMessages messages) {
		initWidget(uiBinder.createAndBindUi(this));
		this.context = context;
		dragController = new PickupDragController(boundary, false);
		dragController.setBehaviorConstrainedToBoundaryPanel(true);
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
			
			
			
			final JsArray<BlocJSO> cellJSO = contentJSO.getBlocs();
			if(cellJSO != null) {
				final FlowPanel droppables = new FlowPanel();
				DropController drop = new FlowPanelDropController(droppables);
				dragController.registerDropController(drop);
				
				droppables.setStyleName(css.draggables());
				
				if(!random)
					Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
						int i=0;
						@Override
						public boolean execute() {
							createFromJSO(droppables, i, cellJSO.get(i));
							return ++i < cellJSO.length();
						}
					});
				else
					Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
						ArrayList<Integer> meeted = new ArrayList<Integer>(); 
						
						@Override
						public boolean execute() {
							int next = Random.nextInt(cellJSO.length());
							while (meeted.contains(next)) {
								next = Random.nextInt(cellJSO.length());
							}
							createFromJSO(droppables, next, cellJSO.get(next));
							meeted.add(next);
							return meeted.size() < cellJSO.length();
						}
					});
				boundary.add(droppables);
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
	
	private void createFromJSO(FlowPanel droppables, int index, BlocJSO jso) {
		HTML text = new HTML();
		MediaContainer resource = new MediaContainer();
		
		if(jso != null) {
			text.setHTML(jso.getText());
			ModuleObject temp = null;
			
			String rId = jso.getResource();
			if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
		}
		
		FocusPanel draggable = new Draggable(index);
		FlowPanel wrapper = new FlowPanel();
		draggable.add(wrapper);
		wrapper.add(resource);
		wrapper.add(text);
		
		draggable.setStyleName(css.draggable());
		dragController.makeDraggable(draggable);
		droppables.add(draggable);
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
