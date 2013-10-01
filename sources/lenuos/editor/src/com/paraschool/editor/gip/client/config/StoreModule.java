package com.paraschool.editor.gip.client.config;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.paraschool.editor.client.ResourceStore;
import com.paraschool.editor.client.api.ExternalResourceStore;

/*
 * Created at 8 nov. 2010
 * By bathily
 */
public class StoreModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(ResourceStore.class).to(ExternalResourceStore.class).in(Singleton.class);
	}

}
