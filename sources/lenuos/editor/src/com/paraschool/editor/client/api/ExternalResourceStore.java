package com.paraschool.editor.client.api;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.Resource2ModuleObject;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.ResourceUtils;
import com.paraschool.editor.api.client.ModuleObject.Type;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ObjectEditCallback;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.ResourceStore;
import com.paraschool.editor.client.event.app.ErrorMessageEvent;
import com.paraschool.editor.client.event.page.UploadedResourceEvent;
import com.paraschool.editor.client.i18n.AppMessages;

/*
 * Created at 7 nov. 2010
 * By bathily
 */
public class ExternalResourceStore implements ResourceStore {

	private static final Logger logger = Logger.getLogger(ExternalResourceStore.class.getName());
	
	private final Project project;
	private final EventBus eventBus;
	private final AppMessages messages;
	private final Map<String, ObjectEditCallback> registry;
	
	private ResourceStoreCallback store;

	@Inject
	private ExternalResourceStore(Project project, EventBus eventBus, AppMessages messages) {
		super();
		this.project = project;
		this.eventBus = eventBus;
		this.messages = messages;
		this.registry = new HashMap<String, ObjectEditCallback>();
		bind();
	}
	
	private void bind() {
		eventBus.addHandler(StoreEvent.TYPE, new StoreEventHandler() {
			
			@Override
			public void onSetResource(SetResourceEvent event) {
				onReceive(event.getRequestId(), event.getJso());
			}
			
			@Override
			public void onChangeStore(ChangeStoreEvent event) {
				store = event.getStore();
			}
		});
		
	}

	private boolean isValid(ResourceJSO jso) {
		return jso != null && jso.getUrl().trim().length() != 0 && jso.getName().trim().length() != 0
				&& ResourceUtils.getValidExtensions().contains(jso.getMimetype());
	}
	
	private Resource toResource(ResourceJSO jso) {
		return ResourceUtils.build(jso.getId(), jso.getUrl(), jso.getName(), 0, jso.getMimetype(), jso.getThumbnail(), jso.getDescription(), project.getDetails().getLocale());
	}
	
	private void onReceive(String requestId, ResourceJSO jso) {
		ObjectEditCallback callback = registry.get(requestId);
		if(callback != null && isValid(jso)) {
			registry.remove(requestId);
			if(jso.getId() == null) jso.setId(requestId);
			logger.fine("Receive this jso "+new JSONObject(jso).toString());
			
			Resource resource = toResource(jso);
			logger.fine("Will fire UploadedResourceEvent ["+resource.getUrl()+"]");
			eventBus.fireEvent(new UploadedResourceEvent(resource));
			
			ModuleObject object = Resource2ModuleObject.toModuleObject(resource);
			object.setUrl(AppUtil.makeInProjectURL(project.getDetails(), object.getUrl()));
			logger.fine("Launch module callback with this new url ["+object.getUrl()+"]");
			callback.onEdit(object);
		}
	}
	
	@Override
	public void get(ObjectEditCallback callback, Type... types) {
		if(store != null) {
			String requestId = DOM.createUniqueId();
			registry.put(requestId, callback);
			store.get(requestId);
		}
		else
			eventBus.fireEvent(new ErrorMessageEvent(messages.unavailableStore()));
	}
}
