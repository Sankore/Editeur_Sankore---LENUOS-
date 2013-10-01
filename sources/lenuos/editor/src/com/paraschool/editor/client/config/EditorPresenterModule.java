package com.paraschool.editor.client.config;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.paraschool.editor.client.presenter.ConnectPresenter;
import com.paraschool.editor.client.presenter.PreviewPresenter;
import com.paraschool.editor.client.presenter.TemplateListPresenter;
import com.paraschool.editor.client.presenter.factory.InteractivityListPresenterFactory;
import com.paraschool.editor.client.presenter.factory.InteractivityPresenterFactory;
import com.paraschool.editor.client.presenter.factory.PagePresenterFactory;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public class EditorPresenterModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(PreviewPresenter.class).in(Singleton.class);
		bind(ConnectPresenter.class).in(Singleton.class);
		
		bind(PagePresenterFactory.class).in(Singleton.class);
		bind(InteractivityListPresenterFactory.class).in(Singleton.class);
		bind(InteractivityPresenterFactory.class).in(Singleton.class);
		
		bind(TemplateListPresenter.class);

	}

}
