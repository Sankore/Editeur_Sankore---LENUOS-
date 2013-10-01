package com.paraschool.editor.modules.relier.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
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
import com.paraschool.editor.modules.relier.client.i18n.RelierModuleMessages;
import com.paraschool.editor.modules.relier.client.jso.RelierNodeContentJSO;

public class RelierNodeContent extends Composite implements NodeContent {

	
	private static RelierNodeContentUiBinder uiBinder = GWT.create(RelierNodeContentUiBinder.class);
	interface RelierNodeContentUiBinder extends UiBinder<Widget, RelierNodeContent> {}

	class SelectablePin extends Pin {
		private int column;
		private SelectablePin link;
		SelectablePin(int id, int column) {
			super(id);
			this.column = column;
		}
	}
	
	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String part();
		String statement();
		String cells();
		String cell();
		String switched();
		String text();
		@ClassName("has-sound") String hasSound();
		@ClassName("has-text") String hasText();
		
		String activated();
		String linked();
		String pin();
		String left();
		String right();
	}
	
	private final boolean isInverse;
	private boolean random = false;
	private final ViewModuleContext context;
	private SelectablePin selected;
	
	@UiField CssResource css;
	@UiField FlowPanel root;
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	@UiField Panel soundTextContainer;
	@UiField AbsolutePanel blocsContainer;
	@UiField HTMLTable blocs;
	
	public RelierNodeContent(boolean isInverse, EventBus eventBus, ViewModuleContext context, RelierModuleMessages messages) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.isInverse = isInverse;
		this.context = context;
	}

	@Override
	public Widget widget(NodeJSO jso) {
		
		RelierNodeContentJSO contentJSO = null;
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
			
			createFromJSOS(contentJSO.getLeftBlocs(), RelierEditNodeContent.LEFT_BLOC_COLUMN, random);
			createFromJSOS(contentJSO.getRightBlocs(), RelierEditNodeContent.RIGHT_BLOC_COLUMN, random);
			
			if(isInverse)
				root.insert(blocsContainer, 0);
	
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
				}
			}
		}
	}
	
	private void createFromJSOS(final JsArray<BlocJSO> jsos, final int column, boolean random) {
		if(jsos != null) {
			if(!random)
				Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
					int i=0;
					@Override
					public boolean execute() {
						createFromJSO(i, column, i, jsos.get(i));
						return ++i < jsos.length();
					}
				});
			else
				Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
					ArrayList<Integer> meeted = new ArrayList<Integer>(); 
					
					@Override
					public boolean execute() {
						int next = Random.nextInt(jsos.length());
						while (meeted.contains(next)) {
							next = Random.nextInt(jsos.length());
						}
						createFromJSO(meeted.size(), column, next, jsos.get(next));
						meeted.add(next);
						return meeted.size() < jsos.length();
					}
				});
		}
	}

	private void createFromJSO(int row, int column, int id, BlocJSO jso) {
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
		
		FlowPanel bloc = new FlowPanel();
		bloc.addStyleName(css.cell());
		bloc.add(resource);
		bloc.add(sound);
		bloc.add(text);
		
		blocs.setWidget(row, column, bloc);
		blocs.setWidget(row, column == RelierEditNodeContent.LEFT_BLOC_COLUMN ? RelierEditNodeContent.LEFT_BLOC_COLUMN + 1 : RelierEditNodeContent.RIGHT_BLOC_COLUMN - 1, createPin(id, column));
	}
	
	private SelectablePin createPin(final int id, final int column) {
		final SelectablePin p = new SelectablePin(id, column);
		p.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				pinClicked(p);
			}
		});
		p.addStyleName(css.pin());
		p.addStyleName(column == RelierEditNodeContent.LEFT_BLOC_COLUMN ? css.left() : css.right());
		return p;
	}
	
	private void pinClicked(SelectablePin p) {
		if(p.link != null) {
			unlink(p);
			return;
		}
		
		if(selected != null){
			selected.removeStyleName(css.activated());
			if(selected.column != p.column){
				link(selected, p);
				selected = null;
				return;
			}
		}
		
		if(selected != p) {
			p.addStyleName(css.activated());
			selected = p;
		}else
			selected = null;
	}
	
	private void link(SelectablePin source, SelectablePin destination) {
		source.addStyleName(css.linked());
		destination.addStyleName(css.linked());
		source.link = destination;
		destination.link = source;
	}
	
	private void unlink(SelectablePin p) {
		p.link.link = null;
		p.link.removeStyleName(css.linked());
		p.link = null;
		p.removeStyleName(css.linked());
	}
}
