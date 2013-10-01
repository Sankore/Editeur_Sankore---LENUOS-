package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.i18n.CommonsModuleMessages;
import com.paraschool.editor.modules.commons.client.jso.ModuleJSO;
import com.paraschool.editor.modules.commons.client.jso.ModuleWithNodesJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;

/*
 * Created at 8 sept. 2010
 * By bathily
 */
public abstract class CommonsViewWidget extends Composite {

	interface CommonsViewWidgetUiBinder extends UiBinder<FlowPanel, CommonsViewWidget> {}
	private static CommonsViewWidgetUiBinder uiBinder = GWT.create(CommonsViewWidgetUiBinder.class);
	
	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String head();
		String content();
		String switched();
		@ClassName("has-thumbnail") String hasThumbnail();
	}
	
	protected boolean canHideStatement;
	protected boolean mustSwitchStatement;
	
	protected final StatementPanel statementPanel;
	protected @UiField FormPanel form;
	protected @UiField FlowPanel content;
	protected @UiField CssResource css;
	
	protected final EventBus eventBus;
	protected final ViewModuleContext context;
	protected final CommonsModuleMessages messages;
	
	public CommonsViewWidget(EventBus eventBus, ViewModuleContext context, CommonsModuleMessages messages) {
		FlowPanel root = uiBinder.createAndBindUi(this);
		
		statementPanel = new StatementPanel();
		statementPanel.setStyleName(css.head());
		
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		
		String data = context.getData();
		
		JSONObject json = null;
		
		if(data != null && data.trim().length() != 0) {
			try{
				json = JSONParser.parseStrict(data).isObject();
				
				if(json != null)  {
					ModuleWithNodesJSO object = json.getJavaScriptObject().cast();
					initFromData(object);
				}
				
			}catch (JSONException e) {}
			
		}
		
		
		if(mustSwitchStatement){
			root.add(statementPanel);
			root.addStyleName(css.switched());
		}else
			root.insert(statementPanel, 0);
		initWidget(root);
	}

	public abstract JavaScriptObject getData();
	
	public FormPanel getForm() {
		return form;
	}
	
	public FlowPanel getContent() {
		return content;
	}
	
	protected void initFromData(ModuleJSO jso) {
		
		proceedWithOptions(jso.getOptions());
		
		StatementJSO statementJSO = jso.getStatement(); 
		
		statementPanel.setCanHide(canHideStatement);
		
		// Thumbnail
		String thumbnailId = null;
		if((thumbnailId = statementJSO.getThumbnail()) != null){
			ModuleObject thumbnail = context.getObject(thumbnailId);
			if(thumbnail != null){
				statementPanel.thumbnail.setMedia(thumbnail, null);
				statementPanel.statementContainer.addStyleName(css.hasThumbnail());
			}
				
		}
		
		
		//Statement
		statementPanel.statement.setHTML(statementJSO.getStatement());
		
		//Sound
		String soundId = null;
		if((soundId = statementJSO.getSound()) != null) {
			ModuleObject sound = context.getObject(soundId);
			if(sound != null)
				statementPanel.sound.setMedia(sound, null);
		}
		
		//Resources
		String resourceId = null;
		if((resourceId = statementJSO.getResource()) != null) {
			ModuleObject resource = context.getObject(resourceId);
			if(resource != null)
				statementPanel.resource.setMedia(resource, null);
		}
		
	}
	
	protected void proceedWithOptions(JsArray<OptionJSO> jsos){
		for(int i = 0 ; jsos != null && i < jsos.length() ; i++) {
			OptionJSO jso = jsos.get(i);
			if(jso != null)
				proceedWithOption(jso);
		}
	}
	
	protected void proceedWithOption(OptionJSO jso) {
		if(jso.getName().equals("hide") && jso.getValue() != null){
			canHideStatement = true;
			return;
		}
		if(jso.getName().equals("switchStatement") && jso.getValue() != null){
			mustSwitchStatement = true;
			return;
		}
	}
}
