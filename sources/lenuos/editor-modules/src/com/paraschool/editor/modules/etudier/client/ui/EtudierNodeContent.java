package com.paraschool.editor.modules.etudier.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.etudier.client.i18n.EtudierModuleMessages;
import com.paraschool.editor.modules.etudier.client.jso.EtudierNodeContentJSO;

public class EtudierNodeContent extends Composite implements NodeContent {

	private static EtudierNodeContentUiBinder uiBinder = GWT.create(EtudierNodeContentUiBinder.class);
	interface EtudierNodeContentUiBinder extends UiBinder<Widget, EtudierNodeContent> {}

	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String part();
		@ClassName("has-sound") String hasSound();
		@ClassName("has-text") String hasText();
	}
	
	private final ViewModuleContext context;
	
	@UiField CssResource css;
	@UiField FlowPanel root;
	@UiField Panel soundTextContainer;
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	
	public EtudierNodeContent(EventBus eventBus, ViewModuleContext context, EtudierModuleMessages messages) {
		initWidget(uiBinder.createAndBindUi(this));
		this.context = context;
	}

	@Override
	public Widget widget(NodeJSO jso) {
		
		EtudierNodeContentJSO contentJSO = null;
		if(jso != null && (contentJSO = jso.getContent().cast()) != null) {
			StatementJSO statementJSO = contentJSO.getStatement();
			if(statementJSO != null) {
				text.setHTML(statementJSO.getStatement());
				if(text.getText().trim().length() != 0)
					soundTextContainer.addStyleName(css.hasText());
				
				ModuleObject temp = null;
				
				String sId = statementJSO.getSound();
				if(sId != null && (temp = context.getObject(sId)) != null) {
					sound.setMedia(temp, null);
					soundTextContainer.addStyleName(css.hasSound());
				}
				
				String rId = statementJSO.getResource();
				if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);

			}
		}
		
		return this;
	}

	@Override
	public NodeContentJSO getJSO() {
		// TODO Create result data
		return null;
	}

	

}
