package com.paraschool.editor.client.presenter.factory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.api.client.ModulesStore;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.presenter.ModulePresenter;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public class InteractivityPresenterFactoryImpl extends PresenterFactory
		implements InteractivityPresenterFactory {

	ModulesStore modulesStore;
	Provider<ModulePresenter.Display>  displayProvider;
	
	@Inject
	public InteractivityPresenterFactoryImpl(
			ModulesStore modulesStore,
			EditorServiceAsync editorRpcService,
			ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Provider<ModulePresenter.Display>  displayProvider) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, null);
		this.modulesStore = modulesStore;
		this.displayProvider = displayProvider;
	}

	@Override
	public ModulePresenter create(Page page, Project project) {
		return new ModulePresenter(page, project, modulesStore, editorRpcService, projectRpcService, eventBus, ajaxHandler,
				displayProvider.get());
	}

}
