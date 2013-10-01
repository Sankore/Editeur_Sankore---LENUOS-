package com.paraschool.editor.modules.commons.client;

import com.google.gwt.resources.client.ImageResource;
import com.paraschool.editor.api.client.EditorModuleDescriptor;

/*
 * Created at 1 sept. 2010
 * By bathily
 */
public class CommonsEditorModuleDescriptor implements EditorModuleDescriptor {

	private final CommonsEditorModule module;
	
	public CommonsEditorModuleDescriptor(CommonsEditorModule module) {
		this.module = module;
	}
	
	@Override
	public String getId() {
		return module.getID();
	}

	@Override
	public String getVersion() {
		return module.getVersion();
	}

	@Override
	public String getFamily() {
		return module.messages.family();
	}

	@Override
	public String getName() {
		return module.messages.name();
	}

	@Override
	public String getDescription() {
		return module.messages.description();
	}

	@Override
	public String getAutor() {
		return "Paraschool SA";
	}

	@Override
	public String getCompany() {
		return "Paraschool SA";
	}

	@Override
	public ImageResource getThumbnail() {
		return module.resources.thumbnail();
	}

}
