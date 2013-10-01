package com.paraschool.editor.client.presenter.factory;

import com.google.inject.ImplementedBy;
import com.paraschool.commons.share.Page;
import com.paraschool.editor.client.presenter.InteractivityListPresenter;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
@ImplementedBy(InteractivityListPresenterFactoryImp.class)
public interface InteractivityListPresenterFactory {
	InteractivityListPresenter create(Page page);
}
