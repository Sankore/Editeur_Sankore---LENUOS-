package com.paraschool.editor.modules.commons.client;

import com.paraschool.editor.api.client.EditorModuleDescriptor;

/*
 * Created at 14 août 2010
 * By bathily
 */
public abstract class AbstractParaschoolEditorModuleDescriptor implements
		EditorModuleDescriptor {

	@Override
	public String getAutor() {
		return "Paraschool SA";
	}

	@Override
	public String getCompany() {
		return "Paraschool SA";
	}

}
