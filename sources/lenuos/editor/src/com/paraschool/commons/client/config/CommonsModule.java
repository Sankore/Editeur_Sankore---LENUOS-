package com.paraschool.commons.client.config;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.paraschool.commons.client.EventBus;
import com.paraschool.editor.api.client.ModulesStore;

/*
 * Created at 17 août 2010
 * By bathily
 */
public class CommonsModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(ModulesStore.class).in(Singleton.class);
	}

	@Provides
	@Singleton
	EventBus provideEventBus() {
		return EventBus.getInstance();
	}
	
}
