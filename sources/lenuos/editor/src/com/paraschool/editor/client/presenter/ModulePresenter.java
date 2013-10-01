package com.paraschool.editor.client.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.Resource2ModuleObject;
import com.paraschool.commons.client.presenter.Presenter;
import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.Resource;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.api.client.ModulesStore;
import com.paraschool.editor.api.client.ObjectEditCallback;
import com.paraschool.editor.api.client.TextEditCallback;
import com.paraschool.editor.api.client.event.ModuleObjectChangeEvent;
import com.paraschool.editor.api.client.event.ModuleObjectDeleteEvent;
import com.paraschool.editor.api.client.event.ModuleObjectEvent;
import com.paraschool.editor.api.client.event.ModuleObjectEventHandler;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.AlohaTextEditor;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.event.interactivity.RemoveInteractivityEvent;
import com.paraschool.editor.client.event.project.EditObjectRequestEvent;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

public class ModulePresenter extends DefaultPresenter implements Presenter, EditModuleContext, ModuleObjectEventHandler {

	private static final Logger logger = Logger.getLogger(ModulePresenter.class.getName());
	
	public interface Display extends com.paraschool.commons.client.presenter.Display {
		void setInteractivityWidget(Widget widget, Widget optionsWidget);
		void setMissingModuleWidget();
		
		HasClickHandlers getDeleteButton();
		HasHTML getModuleTitle();
		
		void setCanDelete(boolean canDelete);
	}

	private final Project project;
	private final Page page;
	private Interactivity interactivity;
	private final ModulesStore modulesStore;
	private ModuleWidget moduleWidget;
	private Map<String, ModuleObjectEventHandler> objectHandlers = new HashMap<String, ModuleObjectEventHandler>();
	
	@Inject
	//TODO Use Gin assisted
	public ModulePresenter(@Assisted Page page,@Assisted Project project,
			ModulesStore modulesStore,	EditorServiceAsync editorRpcService,
			ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Display display) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, display);
		this.page = page;
		this.project = project;
		this.modulesStore = modulesStore;
		
	}

	@Override
	protected void bind() {
		registrations.add(
				((Display)display).getDeleteButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						eventBus.fireEvent(new RemoveInteractivityEvent(interactivity, page));
						display.asWidget().removeFromParent();
					}
				})
		);
		registrations.add(
				eventBus.addHandler(ModuleObjectEvent.TYPE, this)
		);
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.add(display.asWidget());
	}
	
	public void setInteractivity(Interactivity i) {
		this.interactivity = i;
		for(EditorModule module : modulesStore.getModules()) {
			if(module.getDescriptor().getId().equals(interactivity.getId())){
				((Display)display).getModuleTitle().setHTML(module.getDescriptor().getName());
				try{
					moduleWidget = module.newWidget();
					((Display)display).setCanDelete(getMode() == Mode.FULL);
					((Display)display).setInteractivityWidget(moduleWidget.editWidget(this), moduleWidget.optionsWidget(this));
				}catch (Throwable e) {
					((Display)display).setMissingModuleWidget();
				}
				break;
			}
		}
	}

	public void save() {
		if(moduleWidget != null)
			interactivity.setContent(moduleWidget.getEditData());
	}

	@Override
	public String getData() {
		return interactivity.getContent();
	}
	
	private void prepareObject(ModuleObject object) {
		object.setUrl(AppUtil.makeInProjectURL(project.getDetails(), object.getUrl()));
	}

	@Override
	public ModuleObject getObject(String id) {
		
		logger.fine("Module tried to access resource with id ["+id+"]");
		
		if(id == null) return null;
		
		Resource resource = project.getResources().get(id);
		if(resource == null) return null;
		
		logger.fine("Resource found with url ["+resource.getUrl()+"]");
		
		ModuleObject object = Resource2ModuleObject.toModuleObject(resource);
		prepareObject(object);
		return object;
	}

	@Override
	public void makeEditable(Widget hasHTML, TextEditCallback callback) {
		AlohaTextEditor.edit(hasHTML, callback);
	}

	@Override
	public void editObject(ObjectEditCallback callback, ModuleObject.Type ... type) {
		eventBus.fireEvent(new EditObjectRequestEvent(type, callback));
	}

	@Override
	public void addModuleObjectHandler(String id,
			ModuleObjectEventHandler handler) {
		objectHandlers.put(id, handler);
	}

	@Override
	public void removeModuleObjectHandler(String id) {
		objectHandlers.remove(id);
	}

	private void onChangeOrDelete(ModuleObjectEvent event) {
		ModuleObject object = event.getObject();
		
		prepareObject(object);
		
		ModuleObjectEventHandler handler = null;
		if(object != null && (handler = objectHandlers.get(object.getId())) != null){
			if(event instanceof ModuleObjectChangeEvent)
				handler.onChange((ModuleObjectChangeEvent)event);
			else if(event instanceof ModuleObjectDeleteEvent){
				handler.onDelete((ModuleObjectDeleteEvent)event);
				objectHandlers.remove(object.getId());
			}
		}
		
	}
	
	@Override
	public void onChange(ModuleObjectChangeEvent event) {
		onChangeOrDelete(event);		
	}

	@Override
	public void onDelete(ModuleObjectDeleteEvent event) {
		onChangeOrDelete(event);
	}

	@Override
	public Mode getMode() {
		return GWT.getModuleName().equals("editor") ? EditModuleContext.Mode.FULL : EditModuleContext.Mode.ONLY_MEDIA;
	}

}
