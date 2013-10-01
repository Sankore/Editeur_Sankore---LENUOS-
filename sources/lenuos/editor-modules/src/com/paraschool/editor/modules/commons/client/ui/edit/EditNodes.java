package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.modules.commons.client.EventBus;
import com.paraschool.editor.modules.commons.client.ModuleUtils;
import com.paraschool.editor.modules.commons.client.event.MoveNodeEvent;
import com.paraschool.editor.modules.commons.client.event.NodeEvent;
import com.paraschool.editor.modules.commons.client.event.NodeEventHandler;
import com.paraschool.editor.modules.commons.client.event.RemoveNodeEvent;
import com.paraschool.editor.modules.commons.client.i18n.ModuleWithNodesMessage;
import com.paraschool.editor.modules.commons.client.jso.NodeJSO;
import com.paraschool.editor.modules.commons.client.options.Option;
import com.paraschool.editor.modules.commons.client.ui.AbstractNode;
import com.paraschool.editor.modules.commons.client.ui.NodeContent;
import com.paraschool.editor.modules.commons.client.ui.NodeContentFactory;
import com.paraschool.htmllist.client.HTMLList;

/*
 * Created at 5 sept. 2010
 * By bathily
 */
public class EditNodes<T extends NodeContent> extends Composite {

	@SuppressWarnings("rawtypes")
	interface ContentUiBinder extends UiBinder<Widget, EditNodes>{}
	private static ContentUiBinder uiBinder = GWT.create(ContentUiBinder.class);
	
	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String disabled();
	}
	
	protected final  ModuleWithNodesMessage messages;
	protected final EventBus eventBus;
	protected final EditModuleContext context;
	protected final NodeContentFactory<T> contentProvider;
	
	@UiField HTMLList nodes;
	@UiField CommonsAddNodeButton add;
	@UiField CssResource css;
	
	public EditNodes(EventBus eventBus, EditModuleContext context, ModuleWithNodesMessage messages, NodeContentFactory<T> contentProvider) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.eventBus = eventBus;
		this.context = context;
		this.messages = messages;
		this.contentProvider = contentProvider;
		
		add.setNextIdPrefix(messages.editNodeIdPrefix());
		add.setHTML(messages.addNodeButton());
		add.setNextId(1);
		
		bind();
		
		if(!ModuleUtils.canAddOrRemoveContent(context)){
			add.setEnabled(false);
			add.addStyleName(css.disabled());
		}
			
	}
	
	
	protected void bind() {
		
		eventBus.addHandler(NodeEvent.TYPE, new NodeEventHandler() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onRemoved(RemoveNodeEvent event) {
				
				if(event.getNode() == null) return;
				
				int count = nodes.getWidgetCount();
				int removed = event.getPreviousIndex();
				
				for(int i = removed ; i < count; i++){
					@SuppressWarnings("unchecked")
					EditNode<T> n = (EditNode<T>)nodes.getWidget(i);
					n.updateUI(i);
				}
				add.setNextId(count+1);
			}
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void onMoved(MoveNodeEvent event) {
				AbstractNode<T> node = null;
				if((node = (AbstractNode<T>)event.getNode()) == null) return;
				
				int source = event.getPreviousIndex();
				int destination = node.getId();
				
				for(int i = Math.min(source, destination) ; i <= Math.max(source, destination) ; i++) {
					EditNode<T> aNode = (EditNode<T>)nodes.getWidget(i);
					aNode.updateUI(i);
				}
				
			}
		});
	}
	
	@UiFactory
	public HTMLList createNodes() {
		return new HTMLList(HTMLList.ListType.ORDERED);
	}
	
	@UiHandler("add")
	public void addNode(ClickEvent e) {
		addNodeFor(null);
	}
	
	public void addNodeFor(NodeJSO jso) {
		int count = nodes.getWidgetCount();
		EditNode<T> n = new EditNode<T>(count, eventBus, context, messages, contentProvider.get(), jso);
		Option[] options = contentProvider.getOptions();
		if(options != null)
			n.register(options);
		nodes.add(n);
		add.setNextId(count + 2);
	}

	@SuppressWarnings("unchecked")
	public JsArray<NodeJSO> getJSO() {
		JsArray<NodeJSO> nodesJSO = JsArray.createArray().cast();
		for(int i = 0 ; i < nodes.getWidgetCount() ; i++) {
			nodesJSO.push(((AbstractNode<T>)nodes.getWidget(i)).getJSO());
		}
		return nodesJSO;
	}
}
