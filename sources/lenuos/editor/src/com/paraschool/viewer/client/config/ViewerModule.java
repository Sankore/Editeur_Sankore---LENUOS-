package com.paraschool.viewer.client.config;

import com.google.gwt.inject.client.AbstractGinModule;
import com.paraschool.viewer.client.DefaultXMLProjectSerializer;
import com.paraschool.viewer.client.XMLProjectDeserializer;
import com.paraschool.viewer.client.presenter.ViewerPresenter;
import com.paraschool.viewer.client.view.ViewerView;

/*
 * Created at 17 août 2010
 * By bathily
 */
public class ViewerModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(ViewerPresenter.Display.class).to(ViewerView.class);
		
		bind(XMLProjectDeserializer.class).to(DefaultXMLProjectSerializer.class);
	}

}
