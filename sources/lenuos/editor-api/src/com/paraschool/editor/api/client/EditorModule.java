package com.paraschool.editor.api.client;


/*
 * Created at 14 août 2010
 * By bathily
 */
public interface EditorModule {
	void init();
	EditorModuleDescriptor getDescriptor();
	ModuleWidget newWidget();
}
