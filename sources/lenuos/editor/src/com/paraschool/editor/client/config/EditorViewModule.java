package com.paraschool.editor.client.config;

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
import com.paraschool.editor.client.view.DefaultAppLayout;
import com.paraschool.editor.client.view.EditProjectFormView;
import com.paraschool.editor.client.view.EditorPageView;
import com.paraschool.editor.client.view.ExportResultView;
import com.paraschool.editor.client.view.HomeView;
import com.paraschool.editor.client.view.InteractivityListView;
import com.paraschool.editor.client.view.ListProjectView;
import com.paraschool.editor.client.view.ModuleView;
import com.paraschool.editor.client.view.PreviewView;
import com.paraschool.editor.client.view.ProjectView;
import com.paraschool.editor.client.view.ResourceListView;
import com.paraschool.editor.client.view.TemplateListView;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
public class EditorViewModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(AppLayout.class).to(DefaultAppLayout.class).in(Singleton.class);
		bind(HomePresenter.Display.class).to(HomeView.class);
		bind(CreateProjectPresenter.Display.class).to(CreateProjectView.class);
		bind(ListProjectPresenter.Display.class).to(ListProjectView.class);
		bind(ProjectPresenter.Display.class).to(ProjectView.class);
		bind(TemplateListPresenter.Display.class).to(TemplateListView.class);
		bind(ResourceListPresenter.Display.class).to(ResourceListView.class);
		bind(PagePresenter.Display.class).to(EditorPageView.class);
		bind(InteractivityListPresenter.Display.class).to(InteractivityListView.class);
		bind(ModulePresenter.Display.class).to(ModuleView.class);
		bind(PreviewPresenter.Display.class).to(PreviewView.class);
		bind(ConnectPresenter.Display.class).to(ConnectView.class);
		bind(ProjectFormDisplay.class).to(EditProjectFormView.class);
		bind(ExportResultDisplay.class).to(ExportResultView.class);
	}

}
