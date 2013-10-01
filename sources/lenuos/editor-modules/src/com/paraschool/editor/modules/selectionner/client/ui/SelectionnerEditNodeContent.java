package com.paraschool.editor.modules.selectionner.client.ui;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeContentJSO;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.StatementJSO;
import com.paraschool.editor.modules.commons.client.ui.BlocObject;
import com.paraschool.editor.modules.commons.client.ui.MediaContainer;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.edit.Blocs;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableHTML;
import com.paraschool.editor.modules.commons.client.ui.edit.EditableMedia;
import com.paraschool.editor.modules.commons.client.ui.edit.MediaButtonFactory;
import com.paraschool.editor.modules.selectionner.client.jso.SelectionnerNodeContentJSO;

/*
 * Created at 16 oct. 2010
 * By bathily
 */
public class SelectionnerEditNodeContent extends Composite implements NodeContent {

	private static SelectionnerEditNodeContentUiBinder uiBinder = GWT.create(SelectionnerEditNodeContentUiBinder.class);
	interface SelectionnerEditNodeContentUiBinder extends UiBinder<Widget, SelectionnerEditNodeContent> {}
	
	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String disabled();
	}
	
	private final EventBus eventBus;
	private final EditModuleContext context;
	private final LinkedList<BlocObject> objects;
	
	@UiField Button showStatementButton;
	@UiField Button hideStatementButton;
	@UiField Panel statementWrapper;
	@UiField Panel statementContainer;
	
	@UiField MediaContainer resource;
	@UiField MediaContainer sound;
	@UiField HTML text;
	
	@UiField(provided=true) Button addResource = MediaButtonFactory.createAddMediaButton();
	@UiField(provided=true) Button addSound = MediaButtonFactory.createAddSoundButton();
	@UiField(provided=true) Button addText = MediaButtonFactory.createAddTextButton();
	
	@UiField Blocs blocs;
	@UiField Widget addBlocContainer;
	@UiField Button addBloc;
	
	@UiField CssResource css;
	
	public SelectionnerEditNodeContent(EventBus eventBus, EditModuleContext context) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		this.context = context;
		
		objects = new LinkedList<BlocObject>();
		blocs.setAddBlocButton(addBloc);
		initBlocs();
	}
	
	private void initBlocs() {
		blocs.setAddBlocCallback(new Blocs.AddBlocCallback() {
			@Override
			public void onAddBloc(int column, Blocs blocs) {
				makeBloc(blocs, column, null);
			}
		});
		
		blocs.setRemoveBlocCallback(new Blocs.RemoveBlocCallback() {
			@Override
			public void onRemoveBloc(int column, Blocs blocs) {
				objects.remove(column);
				if(column == 0)
					addBloc.click();
			}
		});
		
		blocs.setMoveBlocCallback(new Blocs.MoveBlocCallback() {
			@Override
			public void onMove(int from, int to) {
				objects.add(to, objects.get(from));
				objects.remove(from);
			}
		});
	}
	
	@UiHandler(value={"showStatementButton","hideStatementButton"})
	protected void toogleStatement(ClickEvent event) {
		boolean toShow = event.getSource().equals(showStatementButton);
		if(toShow){
			statementWrapper.getElement().getStyle().setOpacity(1);
			statementWrapper.setHeight("auto");
		}
			
		else{
			statementWrapper.getElement().getStyle().setOpacity(0);
			statementWrapper.setHeight("0");
		}
			
		showStatementButton.setVisible(!toShow);
		hideStatementButton.setVisible(toShow);
	}
	
	@Override
	public Widget widget(NodeJSO jso) {
		updateWidget(jso);
		return this;
	}

	private void updateWidget(NodeJSO jso) {
		new EditableMedia(eventBus, context, resource, addResource, null, ModuleObject.Type.IMAGE, ModuleObject.Type.VIDEO, ModuleObject.Type.SWF);
		new EditableMedia(eventBus, context, sound, addSound, null, ModuleObject.Type.SOUND);
		
		SelectionnerNodeContentJSO contentJSO = null;
		if(jso != null && (contentJSO = jso.getContent().cast()) != null) {
			
			StatementJSO statementJSO = contentJSO.getStatement();
			if(statementJSO != null) {
				text.setHTML(statementJSO.getStatement());
				ModuleObject temp = null;
				
				String sId = statementJSO.getSound();
				if(sId != null && (temp = context.getObject(sId)) != null) sound.setMedia(temp, null);
				
				String rId = statementJSO.getResource();
				if(rId != null && (temp = context.getObject(rId)) != null) resource.setMedia(temp, null);
				
				if(text.getText().trim().length() != 0 || sId != null || rId != null)
					Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
						@Override
						public void execute() {
							showStatementButton.click();
						}
					});
			}
			
			final JsArray<BlocJSO> blocJSO = contentJSO.getBlocs();
			if(blocJSO != null && blocJSO.length() > 0)
				Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
					int i=0;
					@Override
					public boolean execute() {
						blocs.prepareBlocForEditing(i);
						makeBloc(blocs, i, blocJSO.get(i));
						return ++i < blocJSO.length();
					}
				});
			else
				addDefaultBloc();
			
		}
		else
			addDefaultBloc();
		
		new EditableHTML(eventBus, context, text, addText);
		
		if(!ModuleUtils.canAddOrRemoveContent(context)) {
			showStatementButton.setVisible(false);
			addBloc.setEnabled(false);
			addBlocContainer.addStyleName(css.disabled());
		}
	}

	private void addDefaultBloc() {
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				addBloc.click();
			}
		});
	}
	
	@Override
	public NodeContentJSO getJSO() {
		SelectionnerNodeContentJSO jso = SelectionnerNodeContentJSO.createObject().cast();
		
		StatementJSO statementJSO = StatementJSO.createObject().cast();
		
		statementJSO.setStatement(text.getHTML());
		
		ModuleObject soundObject = sound.getObject();
		statementJSO.setSound(soundObject == null ? null : soundObject.getId());
		
		ModuleObject resourceObject = resource.getObject();
		statementJSO.setResource(resourceObject == null ? null : resourceObject.getId());
		
		jso.setStatement(statementJSO);
		
		JsArray<BlocJSO> blocsJSO = BlocJSO.createArray().cast();
		jso.setBlocs(blocsJSO);

		for(BlocObject obj : objects)
			blocsJSO.push(obj.toJSO());
		
		return jso;
	}

	private void makeBloc(Blocs blocs, int column, BlocJSO jso) {
		BlocObject object = createFromJSO(context);
		makeEditable(blocs, column, object, jso);
		objects.add(object);
	}
	
	private BlocObject createFromJSO(EditModuleContext context) {
		HTML text = new HTML();
		MediaContainer resource = new MediaContainer();
		MediaContainer sound = new MediaContainer();
		return new  BlocObject(resource, sound, text);
	}
	
	private void makeEditable(Blocs blocs, int column, BlocObject object, BlocJSO jso) {
		Button resource = MediaButtonFactory.createAddMediaButton();
		new EditableMedia(eventBus, context, object.resource, resource, null, ModuleObject.Type.IMAGE, ModuleObject.Type.VIDEO, ModuleObject.Type.SWF);
		FlowPanel resourcePanel = new FlowPanel();
		resourcePanel.add(object.resource);
		resourcePanel.add(resource);
		
		Button sound = MediaButtonFactory.createAddSoundButton();
		new EditableMedia(eventBus, context, object.sound, sound, null, ModuleObject.Type.SOUND);
		FlowPanel soundPanel = new FlowPanel();
		soundPanel.add(object.sound);
		soundPanel.add(sound);
		
		Button text = MediaButtonFactory.createAddTextButton();
		EditableHTML editableHTML = new EditableHTML(eventBus, context, object.text, text);
		FlowPanel textPanel = new FlowPanel();
		textPanel.add(object.text);
		textPanel.add(text);
		
		blocs.setWidget(1, column, resourcePanel);
		blocs.setWidget(2, column, soundPanel);
		blocs.setWidget(3, column, textPanel);
		
		initBloc(object, jso);
		editableHTML.update();
	}
	
	private void initBloc(BlocObject object, BlocJSO jso) {
		if(jso != null) {
			object.text.setHTML(jso.getText());
			ModuleObject temp = null;
			
			String rId = jso.getResource();
			if(rId != null && (temp = context.getObject(rId)) != null) object.resource.setMedia(temp, null);
			
			String sId = jso.getSound();
			if(sId != null && (temp = context.getObject(sId)) != null) object.sound.setMedia(temp, null);
		}
	}
}
