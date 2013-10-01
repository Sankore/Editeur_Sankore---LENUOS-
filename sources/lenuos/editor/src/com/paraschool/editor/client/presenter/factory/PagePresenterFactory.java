package com.paraschool.editor.client.presenter.factory;

import com.google.inject.ImplementedBy;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.client.presenter.PagePresenter;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
@ImplementedBy(PagePresenterFactoryImp.class)
public interface PagePresenterFactory {
	PagePresenter create(Page page, Project project);
}
