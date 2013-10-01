package com.paraschool.editor.client.api;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.Exportable;

import com.paraschool.commons.client.EventBus;
import com.paraschool.editor.client.event.appmenu.ExportAppMenuEvent;
import com.paraschool.editor.client.event.appmenu.PreviewAppMenuEvent;
import com.paraschool.editor.client.event.appmenu.PublishAppMenuEvent;
import com.paraschool.editor.client.event.appmenu.QuitAppMenuEvent;
import com.paraschool.editor.client.event.appmenu.SaveAppMenuEvent;
import com.paraschool.editor.client.event.project.ClosedProjectEvent;
import com.paraschool.editor.client.event.project.OpenedProjectEvent;
import com.paraschool.editor.client.event.project.StateProjectEvent;
import com.paraschool.editor.client.event.project.StateProjectEventHandler;

/*
 * Created at 30 oct. 2010
 * By bathily
 */
@Export
public class EditorAPI implements Exportable {

	private EventBus eventBus;
	private SimpleCallback projectOpenedCallback;
	private SimpleCallback projectClosedCallback;
	
	public EditorAPI() {
		eventBus = EventBus.getInstance();
		eventBus.addHandler(StateProjectEvent.TYPE, new StateProjectEventHandler() {

			@Override
			public void onOpened(OpenedProjectEvent event) {
				if(projectOpenedCallback != null) projectOpenedCallback.call();
			}

			@Override
			public void onClosed(ClosedProjectEvent event) {
				if(projectClosedCallback != null) projectClosedCallback.call();
			}
			
		});
	}
	
	public void save() {
		eventBus.fireEvent(new SaveAppMenuEvent());
	}
	
	public void preview() {
		eventBus.fireEvent(new PreviewAppMenuEvent());
	}
	
	@Export("get")
	public void export() {
		eventBus.fireEvent(new ExportAppMenuEvent());
	}
	
	public void publish() {
		eventBus.fireEvent(new PublishAppMenuEvent());
	}
	
	public void close(boolean confirm) {
		eventBus.fireEvent(new QuitAppMenuEvent(confirm));
	}
	
	public void setProjectOpenedCallback(SimpleCallback projectOpenedCallback) {
		this.projectOpenedCallback = projectOpenedCallback;
	}

	public void setProjectClosedCallback(SimpleCallback projectClosedCallback) {
		this.projectClosedCallback = projectClosedCallback;
	}

	public void setExternalResourceStore(ResourceStoreCallback store) {
		eventBus.fireEvent(new ChangeStoreEvent(store));
	}
	
	public void setResource(String requestId, ResourceJSO jso) {
		eventBus.fireEvent(new SetResourceEvent(requestId, jso));
	}
}
