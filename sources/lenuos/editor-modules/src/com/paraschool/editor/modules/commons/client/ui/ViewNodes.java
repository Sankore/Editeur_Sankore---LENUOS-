package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.i18n.ModuleWithNodesMessage;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.htmllist.client.HTMLList;

public class ViewNodes<T extends NodeContent> extends Composite {

	@SuppressWarnings("rawtypes")
	interface ContentUiBinder extends UiBinder<Widget, ViewNodes>{}
	private static ContentUiBinder uiBinder = GWT.create(ContentUiBinder.class);
	
	protected final  ModuleWithNodesMessage messages;
	protected final EventBus eventBus;
	protected final ViewModuleContext context;
	protected final NodeContentFactory<T> contentProvider;
	
	private final boolean isSequence;
	private int currentNode = -1;
	
	@UiField HTMLList nodes;
	@UiField HTMLPanel navigation;
	@UiField Button previous;
	@UiField Button next;
	
	public ViewNodes(boolean isSequence, EventBus eventBus, ViewModuleContext context, ModuleWithNodesMessage messages, NodeContentFactory<T> nodeContentProvider) {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		this.isSequence = isSequence;
		
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		this.contentProvider = nodeContentProvider;
		
		previous.setVisible(false);
		next.setVisible(true);
		navigation.setVisible(isSequence);
	}
	
	public void addNodeFor(NodeJSO jso) {
		
		int count = nodes.getWidgetCount();
		Node<T> n = new Node<T>(count, eventBus, context, messages, contentProvider.get(), jso);
		nodes.add(n);
		
		n.setVisible(!isSequence);
		
		if(count == 0)
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				
				@Override
				public void execute() {
					next.click();
				}
			});
		
	}

	@SuppressWarnings("unchecked")
	public JsArray<NodeJSO> getJSO() {
		JsArray<NodeJSO> nodesJSO = JsArray.createArray().cast();
		for(int i = 0 ; i < nodes.getWidgetCount() ; i++) {
			nodesJSO.push(((Node<T>)nodes.getWidget(i)).getJSO());
		}
		return nodesJSO;
	}
	
	
	@UiHandler("previous")
	protected void prev(ClickEvent event) {
		int count = nodes.getWidgetCount();
		int prevNode = currentNode-1;
		
		if(prevNode >= 0 && prevNode < count) {
			if(currentNode >= 0 && currentNode < count) {
				Widget current = nodes.getWidget(currentNode);
				current.setVisible(false);
			}
			
			Widget prev = nodes.getWidget(prevNode);
			prev.setVisible(true);
			currentNode = prevNode;
			if(currentNode < count - 1)
				next.setVisible(true);
			
			
		}
		if(currentNode == 0)
			previous.setVisible(false);
	}
	
	@UiHandler("next")
	protected void next(ClickEvent event) {
		int count = nodes.getWidgetCount();
		int nextNode = currentNode+1;
		if(nextNode >= 0 && nextNode < count) {
			if(currentNode >= 0 && currentNode < count) {
				Widget current = nodes.getWidget(currentNode);
				current.setVisible(false);
			}
			
			Widget next = nodes.getWidget(nextNode);
			next.setVisible(true);
			currentNode = nextNode;
			if(currentNode > 0)
				previous.setVisible(true);
			
			
		}
		if(currentNode == count-1)
			next.setVisible(false);
	}

}
