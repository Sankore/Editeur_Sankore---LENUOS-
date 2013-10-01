package com.paraschool.editor.modules.test.client;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.paraschool.editor.api.client.EditModuleContext;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ModuleObject.Type;
import com.paraschool.editor.api.client.ObjectEditCallback;
import com.paraschool.editor.api.client.TextEditCallback;
import com.paraschool.editor.api.client.event.ModuleObjectEventHandler;

/*
 * Created at 7 sept. 2010
 * By bathily
 */
public class EditContext extends AbstractContext implements EditModuleContext {

	private Mode mode;
	
	public EditContext(String data) {
		this(data, EditModuleContext.Mode.FULL);
	}
	
	public EditContext(String data, Mode mode) {
		super(data);
		this.mode = mode;
	}
	
	@Override
	public ModuleObject getObject(String id) {
		List<ModuleObject> objects = Resources.objects;
		for(ModuleObject object : objects)
			if(object.getId().equals(id))
				return object;
		return null;
	}

	@Override
	public void makeEditable(Widget hasHTML, TextEditCallback callback) {
		AlohaTextEditor.edit(hasHTML, callback);
		
	}

	@Override
	public void editObject(ObjectEditCallback callback, Type ... type) {
		new Resources(callback);
	}

	@Override
	public void addModuleObjectHandler(String id, ModuleObjectEventHandler handler) {}

	@Override
	public void removeModuleObjectHandler(String id) {}

	@Override
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
}
