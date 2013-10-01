package com.paraschool.editor.modules.choisir.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.TextEditCallback;
import com.paraschool.editor.modules.choisir.client.event.RemoveCellEvent;
import com.paraschool.editor.modules.choisir.client.i18n.ChoisirModuleMessages;
import com.paraschool.editor.modules.choisir.client.jso.CellJSO;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.event.MoveCellEvent;
import com.paraschool.editor.modules.commons.client.ui.edit.ContentMoverPopUpPanel;
import com.paraschool.editor.modules.commons.client.ui.edit.ContentMoverPopUpPanel.MoverCallback;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class Cell extends HTMLListItem {

	interface CellUiBinder extends UiBinder<Widget, Cell> {}
	private static CellUiBinder uiBinder = GWT.create(CellUiBinder.class);
	
	private final ChoisirEditNodeContent nodeContent;
	private final EventBus eventBus;
	@SuppressWarnings("unused")
	private final EditModuleContext context;
	private final  ChoisirModuleMessages messages;

	@UiField InlineLabel number;
	@UiField HTML content;
	@UiField Button remove;
	
	public Cell(ChoisirEditNodeContent nodeContent, int id, EventBus eventBus, EditModuleContext context, ChoisirModuleMessages messages) {
		add(uiBinder.createAndBindUi(this));
		
		this.nodeContent = nodeContent;
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		
		setContent(null);
		
		updateUI(id);
		
		number.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new ContentMoverPopUpPanel(number, getIndex(), new MoverCallback() {
					@Override
					public void onMove(int source, int destination) {
						HTMLList parent = (HTMLList)getParent();
						if(destination >= 0 && destination < parent.getWidgetCount()) {
							int previousIndex = getIndex();
							parent.insert(Cell.this, destination + (destination > previousIndex ? 1 : 0));
							Cell.this.nodeContent.onCellMoved(previousIndex, destination);
							Cell.this.eventBus.fireEvent(new MoveCellEvent(Cell.this, previousIndex));
						}
						
					}
				});
				
			}
		});
		
		context.makeEditable(content, new TextEditCallback() {
			@Override
			public void onEdit() {
				String html = content.getHTML();
				if(html == null || html.trim().trim().length() == 0)
					setContent(null);
			}
		});
	}
	
	public Cell(ChoisirEditNodeContent node, int id, EventBus eventBus, EditModuleContext context, ChoisirModuleMessages messages, CellJSO jso) {
		this(node, id, eventBus, context, messages);
		initFromData(jso);
	}
	
	private void initFromData(final CellJSO jso) {
		setContent(jso.getContent());
	}
	
	private void setContent(String content) {
		String html = content;
		if(html == null || (html = html.trim()).trim().length() == 0)
			html = messages.editContent();
		this.content.setHTML(html);
	}
	
	private String getContent() {
		String html = content.getHTML();
		if(messages.editContent().equals(html))
			html = null;
		return html;
	}

	public void updateUI(int id) {
		this.number.setText(messages.cellIdPrefix()+(id+1));
	}
	
	public int getId() {
		return getIndex();
	}
	
	public ChoisirEditNodeContent getNodeContent() {
		return this.nodeContent;
	}
	
	public CellJSO getJSO() {
		CellJSO jso = CellJSO.createObject().cast();
		jso.setContent(getContent());
		return jso;
	}
	
	@UiHandler("remove")
	public void removeNode(ClickEvent e) {
		int id = getId();
		removeFromParent();
		nodeContent.onCellRemoved(id);
		eventBus.fireEvent(new RemoveCellEvent(this, id));
	}
}
