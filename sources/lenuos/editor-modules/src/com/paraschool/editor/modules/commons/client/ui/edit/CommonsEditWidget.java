package com.paraschool.editor.modules.commons.client.ui.edit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.TextEditCallback;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.i18n.CommonsModuleConstants;
import com.paraschool.editor.modules.commons.client.i18n.CommonsModuleMessages;
import com.paraschool.editor.modules.commons.client.jso.ModuleJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.options.OptionChoice;
import com.paraschool.editor.modules.commons.client.ui.OptionsResource;
import com.paraschool.editor.modules.commons.client.ui.StatementPanel;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class CommonsEditWidget extends Composite {

	interface CommonsEditWidgetUiBinder extends UiBinder<Widget, CommonsEditWidget>{}
	private static CommonsEditWidgetUiBinder uiBinder = GWT.create(CommonsEditWidgetUiBinder.class);
	
	{
		com.paraschool.editor.modules.commons.client.ui.edit.Resources.INSTANCE.css().ensureInjected();
	}
	
	interface Resources extends ClientBundle {
		@Source("images/statement-thumbnail.png") ImageResource thumbnail();
	}
	
	private static Resources resources = GWT.create(Resources.class);
	
	private final Image defaultThumbnail = new Image(resources.thumbnail());
	

	protected final EventBus eventBus;
	protected final EditModuleContext context;
	protected final CommonsModuleMessages messages;
	protected final CommonsModuleConstants constants;
	
	protected final OptionsResource optionsResource;
	
	private final List<Option> optionList;
	private ModuleJSO initialJSO;
	private CommonsOptionsWidget optionsWidget;
	
	@UiField StatementPanel statementPanel;
	@UiField StatementControls statementControls;
	@UiField FlowPanel content;
	
	public CommonsEditWidget(EventBus eventBus, EditModuleContext context, CommonsModuleMessages messages, CommonsModuleConstants constants, OptionsResource optionsResource) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		this.constants = constants;
		this.optionsResource = optionsResource;
		
		this.optionList = new ArrayList<Option>();
		
		bind();
		
		makeStatementEditable();
		
		statementPanel.setThumbnail(null, defaultThumbnail);
		statementPanel.setStatement(null);
		statementPanel.setSound(null);
		statementPanel.setResource(null);
		
		String data = context.getData();
		JSONObject json = null;
		if(data != null && data.trim().length() != 0) {
			try{
				json = JSONParser.parseStrict(data).isObject();
				if(json != null) initFromData(json.getJavaScriptObject());
			}catch (JSONException e) {}
		}else {
			init();
		}

		initOptions();
		
		if(!ModuleUtils.canAddOrRemoveContent(context)){
			statementControls.addResource.setEnabled(false);
			statementControls.addSound.setEnabled(false);
		}
			
	}
	
	private void initStatement(final StatementJSO statementJso) {
		statementPanel.setStatement(statementJso.getStatement());
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				ModuleObject temp = null;
				
				String tId = statementJso.getThumbnail();
				if(tId != null && (temp = context.getObject(tId)) != null) statementPanel.setThumbnail(temp, defaultThumbnail);
				
				String sId = statementJso.getSound();
				if(sId != null && (temp = context.getObject(sId)) != null) statementPanel.setSound(temp);
				
				String rId = statementJso.getResource();
				if(rId != null && (temp = context.getObject(rId)) != null) statementPanel.setResource(temp);
			
			}
		});
	}
	
	private void makeStatementEditable() {
		context.makeEditable(statementPanel.getStatement(), new TextEditCallback() {
			@Override
			public void onEdit() {
				String html =statementPanel.getStatement().getText();
				if(html == null || html.trim().trim().length() == 0)
					statementPanel.setStatement(null);
			}
		});
		
		new EditableMedia(eventBus, context, statementPanel.getThumbnail(), null, defaultThumbnail);
		new EditableMedia(eventBus, context, statementPanel.getSound(), statementControls.addSound, null, ModuleObject.Type.SOUND);
		new EditableMedia(eventBus, context, statementPanel.getResource(), statementControls.addResource, null, ModuleObject.Type.IMAGE, ModuleObject.Type.VIDEO, ModuleObject.Type.SWF);
	}
	
	protected void bind() {}
	
	protected void init() {}
	
	protected void initFromData(JavaScriptObject jso) {
		initialJSO = jso.<ModuleJSO>cast();
		initStatement(initialJSO.getStatement());
	}
	
	protected FlowPanel getContent() {
		return content;
	}
	
	protected void initOptions() {		
		OptionChoice hide = new OptionChoice(optionsResource.optionHideStatement(), "hide", messages.optionHideStatement());
		OptionChoice switchStatement = new OptionChoice(optionsResource.optionSwitchStatement(), "switchStatement", messages.optionSwitchStatement());
		register(new Option("hide", Arrays.asList(hide)),
				new Option("switchStatement", Arrays.asList(switchStatement)));
	}
	
	protected void register(Option ... options) {
		JsArray<OptionJSO> optionsJSO = initialJSO != null ? initialJSO.getOptions() : null;
		
		if(options.length > 0 && optionsWidget == null)
			optionsWidget = new CommonsOptionsWidget();
		
		for(int i = 0; i < options.length ; i++) {
			Option option = options[i];
			for(int j = 0 ; optionsJSO != null && j < optionsJSO.length() ; j++) {
				OptionJSO ojso = optionsJSO.get(j);
				if(ojso != null && ojso.getName().equals(option.getName())){
					option.updateByJSO(ojso);
					break;
				}
			}
			
			optionList.add(option);
			
			List<OptionChoice> choices = option.getChoices();
			for(int j = 0 ; j < choices.size() ; j++)
				optionsWidget.add(new OptionWidget(option, j));
		}
	}
	
	public CommonsOptionsWidget getOptionsWidget() {
		if(!ModuleUtils.canEditOptions(context)) {
			return null;
		}
		return optionsWidget;
	}
	
	public ModuleJSO getData() {
		ModuleJSO object = ModuleJSO.createObject().cast();
		object.setStatement(statementPanel.getStatementJSO());
		
		JsArray<OptionJSO> optionsJSO = JsArray.createArray().cast();
		for(Option o : optionList)
			optionsJSO.push(o.getJSO());
		
		object.setOptions(optionsJSO);
		return object;
	}
	
}
