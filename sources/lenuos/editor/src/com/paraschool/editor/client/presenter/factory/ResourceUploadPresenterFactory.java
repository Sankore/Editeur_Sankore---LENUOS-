package com.paraschool.editor.client.presenter.factory;

import com.google.inject.ImplementedBy;
import com.paraschool.editor.client.presenter.ResourceUploadPresenter;

/*
 * Created at 31 juil. 2010
 * By bathily
 */
@ImplementedBy(ResourceUploadPresenterFactoryImp.class)
public interface ResourceUploadPresenterFactory {
	ResourceUploadPresenter create(ResourceUploadPresenter.Display display);
}
