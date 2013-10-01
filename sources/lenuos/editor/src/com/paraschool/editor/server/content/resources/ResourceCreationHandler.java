package com.paraschool.editor.server.content.resources;

import com.paraschool.commons.share.Resource;
import com.paraschool.editor.server.ProjectManager;

public interface ResourceCreationHandler<T extends Resource> {
	
	void onCreate(final T resource, final ProjectManager projectManager);
}
