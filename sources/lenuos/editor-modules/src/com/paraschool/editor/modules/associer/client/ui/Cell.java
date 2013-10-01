package com.paraschool.editor.modules.associer.client.ui;

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
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.modules.associer.client.i18n.AssocierModuleMessages;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.edit.ContentMoverPopUpPanel;
import com.paraschool.editor.modules.commons.client.ui.edit.ContentMoverPopUpPanel.MoverCallback;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableHTML;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableMedia;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class Cell extends HTMLListItem {

	interface CellUiBinder extends UiBinder<Widget, Cell> {}
	private static CellUiBinder uiBinder = GWT.create(CellUiBinder.class);
	
	private final AssocierEditNodeContent nodeContent;
	private final EventBus eventBus;
	private final EditModuleContext context;
	private final  AssocierModuleMessages messages;

	@UiField InlineLabel number;
	@UiField Button remove;
	
	@UiField MediaContainer resource;
	@UiField HTML text;
	@UiField Button addCellResource;
	@UiField Button addCellText;
	
	public Cell(AssocierEditNodeContent nodeContent, int id, EventBus eventBus, EditModuleContext context, AssocierModuleMessages messages) {
		this(nodeContent, id, eventBus, context, messages, null);
	}
	
	public Cell(AssocierEditNodeContent nodeContent, int id, EventBus eventBus, EditModuleContext context, AssocierModuleMessages messages, BlocJSO jso) {
		add(uiBinder.createAndBindUi(this));
		
		this.nodeContent = nodeContent;
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		
		number.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new ContentMoverPopUpPanel(number, getIndex(), new MoverCallback() {
					@Override
					public void onMove(int source, int destination) {
						HTMLList parent = (HTMLList)getParent();
						if(destination >= 0 && destination < parent.getWidgetCount()-1) {
							int previousIndex = getIndex();
							parent.insert(Cell.this, destination + (destination > previousIndex ? 1 : 0));
							Cell.this.nodeContent.onCellMoved(previousIndex, destination);
						}
						
					}
				});
				
			}
		});
		
		updateUI(id);
		
		initFromData(jso);
		
		remove.setEnabled(ModuleUtils.canAddOrRemoveContent(context));
	}
	
	private void initFromData(final BlocJSO jso) {
		new EditableMedia(eventBus, context, resource, addCellResource, null, ModuleObject.Type.IMAGE);
		
		if(jso != null) {
			text.setHTML(jso.getText());
			ModuleObject temp = null;
			
			String rId = jso.getResource();
			if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
			
		}
		
		new EditableHTML(eventBus, context, text, addCellText).update();
	}
	
	public void updateUI(int id) {
		this.number.setText(messages.cellIdPrefix()+(id+1));
	}
	
	public int getId() {
		return getIndex();
	}
	
	public AssocierEditNodeContent getNodeContent() {
		return this.nodeContent;
	}
	
	public BlocJSO getJSO() {
		BlocJSO jso = BlocJSO.createObject().cast();
		ModuleObject temp;
		if((temp = resource.getObject()) != null)
			jso.setResource(temp.getId());
		jso.setText(text.getHTML());
		return jso;
	}
	
	@UiHandler("remove")
	public void removeNode(ClickEvent e) {
		int id = getId();
		removeFromParent();
		nodeContent.onCellRemoved(id);
	}
}
