package com.paraschool.editor.client.config;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.AppController;
import com.paraschool.editor.client.MyAjaxHandler;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.presenter.factory.SecuredRpcActionFactoryImp;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public class EditorClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(Project.class).toProvider(ProjectProvider.class);
		bind(ParametersProvider.class).in(Singleton.class);
		bind(AppController.class).in(Singleton.class);
		bind(AjaxHandler.class).to(MyAjaxHandler.class).in(Singleton.class);
		bind(RpcActionFactory.class).to(SecuredRpcActionFactoryImp.class).in(Singleton.class);
	}

}
