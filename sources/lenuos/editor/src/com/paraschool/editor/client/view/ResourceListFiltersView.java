package com.paraschool.editor.client.view;

import com.google.inject.Inject;
import com.paraschool.editor.client.i18n.LocalizableResource;

public class ResourceListFiltersView extends FiltersView {

	@Inject
	private ResourceListFiltersView(LocalizableResource resources) {
		super();
		addFilter(resources.resourceFilterAll());
		addFilter(resources.resourceFilterImage());
		addFilter(resources.resourceFilterVideo());
		addFilter(resources.resourceFilterAnimation());
		addFilter(resources.resourceFilterSound());
		addFilter(resources.resourceFilterDocument());
		addFilter(resources.resourceFilterLink());
		addFilter(resources.resourceFilterOther());
	}
}
