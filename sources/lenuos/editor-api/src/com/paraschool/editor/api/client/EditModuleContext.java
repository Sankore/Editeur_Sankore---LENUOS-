package com.paraschool.editor.api.client;

import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.event.ModuleObjectEventHandler;



/*
 * Created at 14 août 2010
 * By bathily
 */
public interface EditModuleContext extends ModuleContext {

	enum Mode {
		FULL,
		LIGHT,
		ONLY_MEDIA,
		NONE
	}
	
	void makeEditable(Widget hasHTML, TextEditCallback callback);
	void editObject(ObjectEditCallback callback, ModuleObject.Type ... type );
	
	void addModuleObjectHandler(String id, ModuleObjectEventHandler handler);
	void removeModuleObjectHandler(String id);
	
	Mode getMode();
}
