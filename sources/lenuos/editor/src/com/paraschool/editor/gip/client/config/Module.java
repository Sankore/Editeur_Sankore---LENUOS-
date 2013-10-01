package com.paraschool.editor.gip.client.config;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.paraschool.editor.client.presenter.ConnectPresenter;
import com.paraschool.editor.client.presenter.CreateProjectPresenter;
import com.paraschool.editor.client.presenter.HomePresenter;
import com.paraschool.editor.client.presenter.InteractivityListPresenter;
import com.paraschool.editor.client.presenter.ListProjectPresenter;
import com.paraschool.editor.client.presenter.ModulePresenter;
import com.paraschool.editor.client.presenter.PagePresenter;
import com.paraschool.editor.client.presenter.PreviewPresenter;
import com.paraschool.editor.client.presenter.ProjectPresenter;
import com.paraschool.editor.client.presenter.ProjectPresenter.ExportResultDisplay;
import com.paraschool.editor.client.presenter.ProjectPresenter.ProjectFormDisplay;
import com.paraschool.editor.client.presenter.ResourceListPresenter;
import com.paraschool.editor.client.presenter.TemplateListPresenter;
import com.paraschool.editor.client.view.AppLayout;
import com.paraschool.editor.client.view.ConnectView;
import com.paraschool.editor.client.view.CreateProjectView;
import com.paraschool.editor.client.view.EditProjectFormView;
import com.paraschool.editor.client.view.ExportResultView;
import com.paraschool.editor.client.view.InteractivityListView;
import com.paraschool.editor.client.view.ListProjectView;
import com.paraschool.editor.client.view.ModuleView;
import com.paraschool.editor.client.view.PreviewView;
import com.paraschool.editor.client.view.ResourceListView;
import com.paraschool.editor.client.view.TemplateListView;
import com.paraschool.editor.gip.client.GIPAppLayout;
import com.paraschool.editor.gip.client.GIPHomeView;
import com.paraschool.editor.gip.client.GIPPageView;
import com.paraschool.editor.gip.client.GIPProjectView;

/*
 * Created at 5 nov. 2010
 * By bathily
 */
class Module extends AbstractGinModule {
	@Override
	protected void configure() {
		bind(AppLayout.class).to(GIPAppLayout.class).in(Singleton.class);
		bind(HomePresenter.Display.class).to(GIPHomeView.class);
		bind(CreateProjectPresenter.Display.class).to(CreateProjectView.class);
		bind(ListProjectPresenter.Display.class).to(ListProjectView.class);
		bind(ProjectPresenter.Display.class).to(GIPProjectView.class);
		bind(TemplateListPresenter.Display.class).to(TemplateListView.class);
		bind(ResourceListPresenter.Display.class).to(ResourceListView.class);
		bind(PagePresenter.Display.class).to(GIPPageView.class);
		bind(InteractivityListPresenter.Display.class).to(InteractivityListView.class);
		bind(ModulePresenter.Display.class).to(ModuleView.class);
		bind(PreviewPresenter.Display.class).to(PreviewView.class);
		bind(ConnectPresenter.Display.class).to(ConnectView.class);
		bind(ProjectFormDisplay.class).to(EditProjectFormView.class);
		bind(ExportResultDisplay.class).to(ExportResultView.class);
	}
}