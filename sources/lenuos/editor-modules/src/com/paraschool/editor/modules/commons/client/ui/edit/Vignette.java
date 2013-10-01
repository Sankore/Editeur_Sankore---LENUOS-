package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.edit.ContentMoverPopUpPanel.MoverCallback;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class Vignette extends Composite {

	interface CellUiBinder extends UiBinder<Widget, Vignette> {}
	private static CellUiBinder uiBinder = GWT.create(CellUiBinder.class);
	
	public interface RemoveCallback {
		void remove(Vignette v);
	}
	
	private final EventBus eventBus;
	private final EditModuleContext context;
	private final RemoveCallback removeCallback;

	private int id;
	private String numberPrefix;
	
	@UiField InlineLabel number;
	@UiField Button remove;
	
	@UiField MediaContainer resource;
	@UiField HTML text;
	@UiField Button addCellResource;
	@UiField Button addCellText;
	
	public Vignette(int id, EventBus eventBus, EditModuleContext context, String numberPrefix,
			final MoverCallback moverCallback, final RemoveCallback removeCallback) {
		this(id, null, eventBus, context, numberPrefix, moverCallback, removeCallback);
	}
	
	public Vignette(int id, BlocJSO jso, EventBus eventBus, EditModuleContext context, String numberPrefix,
			final MoverCallback moverCallback, final RemoveCallback removeCallback) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		this.context = context;
		this.numberPrefix = numberPrefix;
		this.removeCallback = removeCallback;
		
		setId(id);
		
		if(moverCallback != null)
			number.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					new ContentMoverPopUpPanel(number, getId(), moverCallback);
				}
			});
		
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
		this.number.setText(numberPrefix+(id+1));
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
		updateUI(id);
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
		if(removeCallback != null)
			removeCallback.remove(this);
	}
}
