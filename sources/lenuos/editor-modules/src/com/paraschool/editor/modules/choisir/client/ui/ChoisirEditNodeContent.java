package com.paraschool.editor.modules.choisir.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.TextEditCallback;
import com.paraschool.editor.modules.choisir.client.i18n.ChoisirModuleMessages;
import com.paraschool.editor.modules.choisir.client.jso.CellJSO;
import com.paraschool.editor.modules.choisir.client.jso.ChoisirNodeContentJSO;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableMedia;
import com.paraschool.editor.modules.commons.client.ui.edit.MediaButtonFactory;
import com.paraschool.htmllist.client.HTMLList;

/*
 * Created at 4 oct. 2010
 * By bathily
 */
public class ChoisirEditNodeContent extends Composite implements NodeContent {

	interface ChoisirEditNodeContentUiBinder extends UiBinder<Widget, ChoisirEditNodeContent>{}
	private static ChoisirEditNodeContentUiBinder uiBinder = GWT.create(ChoisirEditNodeContentUiBinder.class);

	private final EventBus eventBus;
	private final EditModuleContext context;
	private final ChoisirModuleMessages messages;

	@UiField HTML statement;
	@UiField HTMLList cells;

	@UiField MediaContainer resource;
	@UiField(provided=true) Button addResource = MediaButtonFactory.createAddMediaButton();
	
	public ChoisirEditNodeContent(EventBus eventBus, EditModuleContext context,	ChoisirModuleMessages messages) {
		initWidget(uiBinder.createAndBindUi(this));

		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;

		setStatement(null);
		bind();
	}

	private void bind() {
		context.makeEditable(statement, new TextEditCallback() {
			@Override
			public void onEdit() {
				String html = statement.getHTML();
				if(html == null || html.trim().trim().length() == 0)
					setStatement(null);
			}
		});
	}
	
	public void onCellRemoved(int index) {
		for(int i = index ; i < cells.getWidgetCount(); i++){
			Cell c = (Cell)cells.getWidget(i);
			c.updateUI(i);
		}
	}
	
	public void onCellMoved(int source, int destination) {
		for(int i = Math.min(source, destination) ; i <= Math.max(source, destination) ; i++) {
			Cell acell = (Cell)cells.getWidget(i);
			acell.updateUI(i);
		}
	}

	@Override
	public Widget widget(NodeJSO jso) {
		updateWidget(jso);	
		return this;
	}

	
	private void updateWidget(NodeJSO jso) {
		new EditableMedia(eventBus, context, resource, addResource, null, ModuleObject.Type.IMAGE, ModuleObject.Type.VIDEO, ModuleObject.Type.SWF);
		
		cells.clear();
		
		ChoisirNodeContentJSO contentJSO;
		
		if(jso != null && (contentJSO = jso.getContent().cast()) != null) {
			setStatement(contentJSO.getStatement());
			String rId = contentJSO.getResource();
			ModuleObject temp = null;
			if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
			
			final JsArray<CellJSO> cellsJSO = contentJSO.getCells();
			if(cellsJSO.length() > 0)
				Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
					int i = 0;
					@Override
					public boolean execute() {
						CellJSO cellJSO = cellsJSO.get(i);			
						cells.add(new Cell(ChoisirEditNodeContent.this, i, eventBus, context, messages, cellJSO));
						return ++i < cellsJSO.length();
					}
				});
			else
				addCell(null);
		}else
			addCell(null);
	}

	@Override
	public NodeContentJSO getJSO() {
		ChoisirNodeContentJSO jso = ChoisirNodeContentJSO.createObject().cast();
		
		jso.setStatement(getStatement());
		
		JsArray<CellJSO> cjsos = JsArray.createArray().cast();
		for(int i = 0 ; i < cells.getWidgetCount() ; i++) {
			CellJSO cjso = ((Cell)cells.getWidget(i)).getJSO();
			cjsos.push(cjso);
		}
		jso.setCells(cjsos);
		
		ModuleObject resourceObject = resource.getObject();
		jso.setResource(resourceObject == null ? null : resourceObject.getId());
		
		return jso;
	}

	
	private void setStatement(String statement) {
		String html = statement;
		if(html == null || (html = html.trim()).trim().length() == 0)
			html = messages.editStatement();
		this.statement.setHTML(html);
	}
	

	private String getStatement() {
		String html = statement.getHTML();
		if(messages.editStatement().equals(html))
			html = null;
		return html;
	}

	@UiHandler("add")
	protected void addCell(ClickEvent event) {
		Cell c = new Cell(this, cells.getWidgetCount(), eventBus, context, (ChoisirModuleMessages)messages);
		cells.add(c);
	}
}