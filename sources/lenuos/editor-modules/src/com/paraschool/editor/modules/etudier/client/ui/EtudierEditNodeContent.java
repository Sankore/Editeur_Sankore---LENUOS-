package com.paraschool.editor.modules.etudier.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.event.EditMediaEvent;
import com.paraschool.editor.modules.commons.client.event.EditMediaEventHandler;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableHTML;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableMedia;
import com.paraschool.editor.modules.commons.client.ui.edit.MediaButtonFactory;
import com.paraschool.editor.modules.etudier.client.jso.EtudierNodeContentJSO;

/*
 * Created at 16 oct. 2010
 * By bathily
 */
public class EtudierEditNodeContent extends Composite implements NodeContent, EditMediaEventHandler {

	private static EtudierEditNodeContentUiBinder uiBinder = GWT.create(EtudierEditNodeContentUiBinder.class);
	interface EtudierEditNodeContentUiBinder extends UiBinder<Widget, EtudierEditNodeContent> {}
	
	private final EventBus eventBus;
	private final EditModuleContext context;
	
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	
	@UiField Panel buttons;
	@UiField(provided=true) Button addResource = MediaButtonFactory.createAddMediaButton();
	@UiField(provided=true) Button addSound = MediaButtonFactory.createAddSoundButton();
	@UiField(provided=true) Button addText = MediaButtonFactory.createAddTextButton();
	
	public EtudierEditNodeContent(EventBus eventBus, EditModuleContext context) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		this.context = context;
		
		eventBus.addHandler(EditMediaEvent.TYPE, this);
	}
	
	
	@Override
	public Widget widget(NodeJSO jso) {
		updateWidget(jso);
		return this;
	}

	private void updateWidget(NodeJSO jso) {
		new EditableMedia(eventBus, context, resource, addResource, null, ModuleObject.Type.IMAGE, ModuleObject.Type.VIDEO, ModuleObject.Type.SWF);
		new EditableMedia(eventBus, context, sound, addSound, null, ModuleObject.Type.SOUND);
		
		EtudierNodeContentJSO contentJSO = null;
		if(jso != null && (contentJSO = jso.getContent().cast()) != null) {
			
			StatementJSO statementJSO = contentJSO.getStatement();
			if(statementJSO != null) {
				text.setHTML(statementJSO.getStatement());
				
				ModuleObject temp = null;
				
				String sId = statementJSO.getSound();
				if(sId != null && (temp = context.getObject(sId)) != null) sound.setMedia(temp, null);
				
				String rId = statementJSO.getResource();
				if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);

			}
		}
		
		new EditableHTML(eventBus, context, text, addText);
	}
	
	@Override
	public NodeContentJSO getJSO() {
		EtudierNodeContentJSO jso = EtudierNodeContentJSO.createObject().cast();
		
		StatementJSO statementJSO = StatementJSO.createObject().cast();
		
		statementJSO.setStatement(text.getHTML());
		
		ModuleObject soundObject = sound.getObject();
		statementJSO.setSound(soundObject == null ? null : soundObject.getId());
		
		ModuleObject resourceObject = resource.getObject();
		statementJSO.setResource(resourceObject == null ? null : resourceObject.getId());
		
		jso.setStatement(statementJSO);
		
		return jso;
	}


	@Override
	public void onEditMedia(EditMediaEvent event) {
		Widget media = event.getMedia();
		if(media.equals(resource) || media.equals(sound)){
			buttons.setVisible(event.getObject() == null);
		}else if(media.equals(text)) {
			buttons.setVisible(text.getText().trim().length() == 0);
		}
	}
}
