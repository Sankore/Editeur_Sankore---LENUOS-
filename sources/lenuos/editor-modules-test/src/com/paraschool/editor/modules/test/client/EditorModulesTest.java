package com.paraschool.editor.modules.test.client;

import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.api.client.ModulesStore;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditorModulesTest implements EntryPoint, ValueChangeHandler<String> {
	

	private Layout layout = new Layout();
	private ModulesStore modules = GWT.create(ModulesStore.class);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// set uncaught exception handler
	    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
	      public void onUncaughtException(Throwable throwable) {
	        String text = "Uncaught exception: ";
	        while (throwable != null) {
	          StackTraceElement[] stackTraceElements = throwable.getStackTrace();
	          text += throwable.toString() + "\n";
	          for (int i = 0; i < stackTraceElements.length; i++) {
	            text += "    at " + stackTraceElements[i] + "\n";
	          }
	          throwable = throwable.getCause();
	          if (throwable != null) {
	            text += "Caused by: ";
	          }
	        }
	        DialogBox dialogBox = new DialogBox(true, false);
	        DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor", "#ABCDEF");
	        System.err.print(text);
	        text = text.replaceAll(" ", "&nbsp;");
	        dialogBox.setHTML("<pre>" + text + "</pre>");
	        dialogBox.center();
	      }
	    });

	    // use a deferred command so that the handler catches onModuleLoad2() exceptions
	    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
	      public void execute() {
	        onModuleLoad2();
	      }
	    });
	}

	HTML test;
	HTML test2;
	
	/**
	 * This is the real entry point method.
	 */
	private void onModuleLoad2() {
		
		initModuleList();
		
		RootPanel.get().add(layout);
		
		History.addValueChangeHandler(this);
		if(History.getToken() != null && !History.getToken().isEmpty())
			showModule(History.getToken());

	}
	

	
	
	private void initModuleList() {
		
		layout.tree.setAnimationEnabled(true);
				
		HashMap<String, TreeItem> items = new HashMap<String, TreeItem>();
		
		for(final EditorModule module : modules.getModules()) {
			module.init();
			TreeItem item = null;
			if( (item =items.get(module.getDescriptor().getFamily())) == null){
				item = new TreeItem(module.getDescriptor().getFamily());
				items.put(module.getDescriptor().getFamily(), item);
				layout.tree.addItem(item);
			}
			Button b = new Button(module.getDescriptor().getName());
			b.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					History.newItem(module.getDescriptor().getId(), true);
					
				}
			});
			TreeItem mitem = new TreeItem(b);
			item.addItem(mitem);
		}

	}
	
	private void showModule(String id) {
		layout.content.clear();
		EditorModule module = null;
		for(EditorModule temp : modules.getModules()) {
			if(temp.getDescriptor().getId().equals(id)){
				module = temp;
				break;
			}
		}
		if(module != null)
			layout.content.add(new ModulePanel(module, null));
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		showModule(event.getValue());
	}

}
