package com.paraschool.editor.server.content.resources;

import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.paraschool.commons.share.Resource;

public class ResourceCreationHandlerProviderImpl implements
		ResourceCreationHandlerProvider {

	@Inject Injector injector;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Resource> ResourceCreationHandler<T> get(T resource) {
		try{
			return injector.getInstance(Key.get(ResourceCreationHandler.class, Names.named(resource.getClass().toString())));
		}catch (ConfigurationException e) {
			return null;
		}
		
	}

}
