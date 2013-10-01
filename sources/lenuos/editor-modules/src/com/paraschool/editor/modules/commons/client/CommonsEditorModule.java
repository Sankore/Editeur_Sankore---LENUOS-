package com.paraschool.editor.modules.commons.client;

import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.api.client.EditorModuleDescriptor;
import com.paraschool.editor.modules.commons.client.i18n.CommonsModuleMessages;

/*
 * Created at 1 sept. 2010
 * By bathily
 */
public abstract class CommonsEditorModule implements EditorModule {

	protected CommonsModuleMessages messages;
	protected CommonsModuleResources resources;
	protected EditorModuleDescriptor descriptor = null;
	
	public CommonsModuleMessages getMessages() {
		return messages;
	}
	
	public CommonsModuleResources getResources() {
		return resources;
	}
	
	public abstract String getID();
	
	public abstract String getVersion();
	
	@Override
	public EditorModuleDescriptor getDescriptor() {
		if(descriptor == null)
			descriptor = new CommonsEditorModuleDescriptor(this);
		return descriptor;
	}

}
