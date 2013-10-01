package com.paraschool.editor.client.presenter.factory;

import com.google.inject.ImplementedBy;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.client.presenter.ModulePresenter;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
@ImplementedBy(InteractivityPresenterFactoryImpl.class)
public interface InteractivityPresenterFactory {
	ModulePresenter create(Page page, Project project);
}
