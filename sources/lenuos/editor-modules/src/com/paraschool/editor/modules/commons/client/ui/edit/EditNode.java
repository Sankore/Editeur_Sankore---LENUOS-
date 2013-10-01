package com.paraschool.editor.modules.commons.client.ui.edit;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.event.MoveNodeEvent;
import com.paraschool.editor.modules.commons.client.event.RemoveNodeEvent;
import com.paraschool.editor.modules.commons.client.i18n.ModuleWithNodesMessage;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.jso.OptionJSO;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.options.OptionChoice;
import com.paraschool.editor.modules.commons.client.ui.AbstractNode;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.htmllist.client.HTMLList;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class EditNode<T extends NodeContent> extends AbstractNode<T> {

	@SuppressWarnings("rawtypes")
	interface EditNodeUiBinder extends UiBinder<Widget, EditNode>{}
	private static EditNodeUiBinder uiBinder = GWT.create(EditNodeUiBinder.class);
	
	protected final EventBus eventBus;
	protected final EditModuleContext context;
	protected final ModuleWithNodesMessage messages;
	protected final List<Option> optionList;
	
	protected NodeJSO initialJSO;
	
	@UiField Panel root;
	
	@UiField Button preferenceButton;
	@UiField Button hidePreferenceButton;
	@UiField Button deleteButton;
	
	@UiField Panel optionsWrapper;
	@UiField Panel optionsContainer;
	@UiField CommonsOptionsWidget options;
	
	public EditNode(int id, EventBus eventBus, EditModuleContext context,  ModuleWithNodesMessage messages, T contentProvider) {
		this(id, eventBus, context, messages, contentProvider, null);
		
	}
	
	public EditNode(int id, EventBus eventBus, EditModuleContext context, ModuleWithNodesMessage messages, T contentProvider, NodeJSO jso) {
		super(contentProvider);
		add(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		this.optionList = new ArrayList<Option>();
		this.initialJSO = jso;
		
		Widget w = null;
		if(nodeContent != null && (w = nodeContent.widget(jso)) != null)
			content.add(w);
		
		toogleOptions(null);
		
		updateUI(id);
		bind();
		
		if(!ModuleUtils.canAddOrRemoveContent(context)) {
			this.deleteButton.setEnabled(false);
			this.number.setVisible(false);
		}
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				getElement().getStyle().setOpacity(1);
			}
		});
	}
	
	protected void bind() {
		number.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new ContentMoverPopUpPanel(number, getIndex(), new ContentMoverPopUpPanel.MoverCallback() {
					
					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void onMove(int source, int destination) {
						HTMLList parent = (HTMLList)getParent();
						if(destination >= 0 && destination < parent.getWidgetCount()) {
							int previousIndex = getIndex();
							parent.insert(EditNode.this, destination + (destination > previousIndex ? 1 : 0));
							eventBus.fireEvent(new MoveNodeEvent((EditNode<T>)EditNode.this, previousIndex));
						}
					}
				});
				
			}
		});
		
	}
	
	public void updateUI(int id) {
		this.number.setText(messages.editNodeIdPrefix()+(id+1));
	}
	
	@Override
	public NodeJSO getJSO() {
		NodeJSO jso = NodeJSO.createObject().cast();
		jso.setContent(nodeContent.getJSO());
		
		JsArray<OptionJSO> optionsJSO = JsArray.createArray().cast();
		for(Option o : optionList)
			optionsJSO.push(o.getJSO());
		
		jso.setOptions(optionsJSO);
		
		return jso;
	}
	
	public void register(Option ... options) {
		
		JsArray<OptionJSO> optionsJSO = initialJSO != null ? initialJSO.getOptions() : null;
		
		preferenceButton.setEnabled(true);
		preferenceButton.setVisible(true);
		
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
				this.options.add(new OptionWidget(option, j));
		}
		
		if(!ModuleUtils.canEditOptions(context)) {
			this.options.removeFromParent();
			this.preferenceButton.setEnabled(false);
		}
		
	}
	
	@UiFactory
	protected HTMLList createCells() {
		return new HTMLList(HTMLList.ListType.ORDERED);
	}
	
	@UiHandler(value={"preferenceButton","hidePreferenceButton"})
	protected void toogleOptions(ClickEvent event) {
		int height = optionsContainer.getOffsetHeight();
		boolean toShow = event != null ? event.getSource().equals(preferenceButton) : false;
		hidePreferenceButton.setVisible(toShow);
		preferenceButton.setVisible(!toShow && optionList.size()>0);
		optionsWrapper.setHeight(toShow ? height+"px" : "0");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@UiHandler("deleteButton")
	public void removeNode(ClickEvent e) {
		int id = getIndex();
		removeFromParent();
		eventBus.fireEvent(new RemoveNodeEvent(this,id));
	}
		
}
