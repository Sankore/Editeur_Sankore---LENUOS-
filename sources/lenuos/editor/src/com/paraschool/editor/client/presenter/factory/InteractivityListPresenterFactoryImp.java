package com.paraschool.editor.client.presenter.factory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Page;
import com.paraschool.editor.api.client.ModulesStore;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.InteractivityListPresenter;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public class InteractivityListPresenterFactoryImp extends PresenterFactory
		implements InteractivityListPresenterFactory {

	private final ModulesStore modulesStore;
	private final RpcActionFactory rpcActionFactory;
	private final Provider<InteractivityListPresenter.Display> displayProvider;
	private final AppMessages messages;
	
	@Inject
	public InteractivityListPresenterFactoryImp(
			ModulesStore modulesStore,
			EditorServiceAsync editorRpcService,
			ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, RpcActionFactory rpcActionFactory,
			Provider<InteractivityListPresenter.Display> displayProvider,
			AppMessages messages) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, null);
		this.modulesStore = modulesStore;
		this.rpcActionFactory = rpcActionFactory;
		this.displayProvider = displayProvider;
		this.messages = messages;
	}

	@Override
	public InteractivityListPresenter create(Page page) {
		return new InteractivityListPresenter(page, modulesStore, editorRpcService, 
				projectRpcService, eventBus, ajaxHandler, rpcActionFactory, displayProvider.get(), messages);
	}

}
