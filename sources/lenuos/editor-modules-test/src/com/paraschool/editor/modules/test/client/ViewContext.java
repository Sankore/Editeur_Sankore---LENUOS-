package com.paraschool.editor.modules.test.client;

import java.util.List;

import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ViewModuleContext;

/*
 * Created at 8 sept. 2010
 * By bathily
 */
public class ViewContext extends AbstractContext implements ViewModuleContext {

	public ViewContext(String data) {
		super(data);
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
	public String getSavedData() {
		// TODO Auto-generated method stub
		return null;
	}

}
