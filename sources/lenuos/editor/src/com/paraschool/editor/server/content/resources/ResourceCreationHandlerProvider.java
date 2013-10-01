package com.paraschool.editor.server.content.resources;

import com.google.inject.ImplementedBy;
import com.paraschool.commons.share.Resource;

@ImplementedBy(ResourceCreationHandlerProviderImpl.class)
public interface ResourceCreationHandlerProvider {
	<T extends Resource> ResourceCreationHandler<T> get(T resource);
}
