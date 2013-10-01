package com.paraschool.editor.modules.associer.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleMessages;
import com.paraschool.editor.modules.associer.client.jso.AssocierNodeContentJSO;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.DelegatedDropController;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.edit.Blocs;

public class AssocierNodeContent extends Composite implements NodeContent, DragHandler {
	
	private static AssocierNodeContentUiBinder uiBinder = GWT.create(AssocierNodeContentUiBinder.class);
	interface AssocierNodeContentUiBinder extends UiBinder<Widget, AssocierNodeContent> {}

	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String part();
		String statement();
		String cells();
		String cell();
		String text();
		
		String switched();
		@ClassName("has-sound") String hasSound();
		@ClassName("has-text") String hasText();
		String dropzone();
		String bin();
		String box();
		@ClassName("draggables-wrapper") String draggablesWrapper();
		@ClassName("draggables-count") String draggablesCount();
		String draggables();
		String draggable();
		
		String onPreview();
		String onDrag();
		
		String opened();
		String empty();
		@ClassName("has-draggable") String hasDraggable();
		
		String viewport();
		String sequenced();
	}
	
	private boolean random = false;
	private VerticalAlignmentConstant verticalAlignment = HasVerticalAlignment.ALIGN_BOTTOM;
	
	private boolean cellSequence = false;
	private int cellPosition = 0;
	private final ViewModuleContext context;
	
	@UiField CssResource css;
	@UiField AbsolutePanel boundary;
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	@UiField Panel soundTextContainer;
	@UiField Panel navigationContainer;
	@UiField Panel droppablesWrapper;
	
	private final Panel viewport = new SimplePanel();
	private final FlowPanel droppables = new FlowPanel();
	
	private final List<FlowPanel> dropZones = new ArrayList<FlowPanel>();
	private final PickupDragController dragController;
	
	public AssocierNodeContent(EventBus eventBus, ViewModuleContext context, AssocierModuleMessages messages) {
		initWidget(uiBinder.createAndBindUi(this));
		this.context = context;
		
		droppables.setStyleName(css.draggables());
		viewport.setStyleName(css.viewport());
		viewport.add(droppables);
		droppablesWrapper.add(viewport);
	
		boundary.add(droppablesWrapper);
		
		dragController = new PickupDragController(boundary, false);
		dragController.setBehaviorConstrainedToBoundaryPanel(true);
		dragController.addDragHandler(this);
	}

	@Override
	public Widget widget(NodeJSO jso) {
		
		AssocierNodeContentJSO contentJSO = null;
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
			
			final JsArray<BlocJSO> cellJSO = contentJSO.getCells();
			if(cellJSO != null) {
				
				
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
				
				DropController viewPortDropController = new SimpleDropController(viewport);
				DropController drop = new FlowPanelDropController(droppables);
				dragController.registerDropController(new DelegatedDropController(viewPortDropController, drop));
			}
			
			if(cellSequence)
				makeCellSequenced();
			
			final JsArray<BlocJSO> blocJSO = contentJSO.getBlocs();
			if(blocJSO != null) {
				final Blocs blocs = new Blocs(4, -1);
				blocs.setStyleName(css.cells());
				if(blocJSO.length() > 0)
					Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
						int i=0;
						@Override
						public boolean execute() {
							createFromJSO(blocs, i, blocJSO.get(i));
							return ++i < blocJSO.length();
						}
					});
				boundary.insert(blocs, 0);
			}
		}
		
		return this;
	}

	private void makeCellSequenced() {
		droppablesWrapper.addStyleName(css.sequenced());
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
				}
				else if("cellSequence".equals(jso.getName())  && jso.getValue() != null){
					cellSequence = true;
				}
				else if("resourceAlignement".equals(jso.getName())){
					String value = jso.getValue();
					if("TOP".equals(value)) verticalAlignment = HasVerticalAlignment.ALIGN_TOP;
					else if("MIDDLE".equals(value)) verticalAlignment = HasVerticalAlignment.ALIGN_MIDDLE;
					else if("BOTTOM".equals(value)) verticalAlignment = HasVerticalAlignment.ALIGN_BOTTOM;
				}
			}
				
		}
	}

	private void createFromJSO(Blocs blocs, int column, BlocJSO jso) {
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
		
		FlowPanel dropzone = new FlowPanel();
		dropzone.setStyleName(css.bin());
		
		blocs.setWidget(0, column, resource);
		blocs.getCellFormatter().setVerticalAlignment(0, column, verticalAlignment);
		blocs.setWidget(1, column, sound);
		blocs.setWidget(2, column, text);
		blocs.setWidget(3, column, dropzone);
		
		blocs.getCellFormatter().setStyleName(3, column, css.dropzone());
		
		DropController drop = new FlowPanelDropController(dropzone);
		dragController.registerDropController(drop);
		dropZones.add(dropzone);
	}
	
	private void createFromJSO(FlowPanel droppables, int index, BlocJSO jso) {
		HTML text = new HTML();
		MediaContainer resource = new MediaContainer();
		text.setStyleName(css.text());
		
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
		//badge.setText((index+1)+"");
	}
	
	private class Draggable extends FocusPanel {
		@SuppressWarnings("unused")
		final int index;
		Draggable(int index) {
			super();
			this.index = index;
		}
	}

	@Override
	public void onDragEnd(DragEndEvent event) {
		removeStyleName(css.onDrag());
		int count = droppables.getWidgetCount();
		//badge.setText(count+"");
		
		if(count == 0){
			boundary.addStyleName(css.empty());
			droppablesWrapper.addStyleName(css.empty());
		}else{
			boundary.removeStyleName(css.empty());
			droppablesWrapper.removeStyleName(css.empty());
		}
			
		
		for(FlowPanel dropZone : dropZones) {
			if(dropZone.getWidgetCount() == 0) {
				dropZone.removeStyleName(css.hasDraggable());
			}else
				dropZone.addStyleName(css.hasDraggable());
		}
	}

	@Override
	public void onDragStart(DragStartEvent event) {
		addStyleName(css.onDrag());
	}

	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		removeStyleName(css.onPreview());
	}

	@Override
	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
		addStyleName(css.onPreview());
		
	}
	
	@UiHandler("next")
	protected void scrollCellToRight(ClickEvent event) {
		if(cellPosition >= droppables.getWidgetCount() - 1)
			cellPosition = -1;
		cellPosition++;
		scrollCellToPosition();
	}
	
	@UiHandler("previous")
	protected void scrollCellToLeft(ClickEvent event) {
		if(cellPosition == 0)
			cellPosition = 1;
		cellPosition--;
		scrollCellToPosition();
	}
	
	private void scrollCellToPosition() {
		droppables.getElement().getStyle().setMarginLeft(cellPosition * (-viewport.getOffsetWidth()), Unit.PX);
	}
}
