package com.paraschool.editor.client.presenter.factory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.i18n.LocalizableResource;
import com.paraschool.editor.client.presenter.PagePresenter;
import com.paraschool.editor.client.presenter.PagePresenter.Display;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public class PagePresenterFactoryImp extends PresenterFactory implements PagePresenterFactory {
	
	@Inject InteractivityListPresenterFactory interactivityListPresenterFactory;
	@Inject InteractivityPresenterFactory interactivityPresenterFactory;
	@Inject LocalizableResource resources;
	@Inject Provider<Display> displayProvider;
	@Inject RpcActionFactory rpcActionFactory;
	
	@Inject
	public PagePresenterFactoryImp(EditorServiceAsync editorRpcService, ProjectServiceAsync projectRpcService,
			EventBus eventBus, AjaxHandler ajaxHandler, Display display) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, null);
	}
	
	@Override
	public PagePresenter create(Page page, Project project) {
		return new PagePresenter(page, project, editorRpcService, projectRpcService, eventBus, ajaxHandler, rpcActionFactory,
				displayProvider.get(), resources, interactivityListPresenterFactory, interactivityPresenterFactory);
	}

}
