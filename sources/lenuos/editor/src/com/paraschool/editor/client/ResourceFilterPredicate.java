package com.paraschool.editor.client;

import com.paraschool.commons.client.Predicate;
import com.paraschool.commons.share.Resource;

public class ResourceFilterPredicate implements Predicate<Resource> {

	private final Class<? extends Resource> resourceClass;
	
	public ResourceFilterPredicate(Class<? extends Resource> resourceClass){
		this.resourceClass = resourceClass; 
	}
	
	@Override
	public boolean apply(Resource resource) {
		return resource.getClass().equals(resourceClass);
	}

}
