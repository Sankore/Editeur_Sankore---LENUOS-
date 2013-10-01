package com.paraschool.editor.client.event.project;

import com.paraschool.editor.api.client.ObjectEditCallback;
import com.paraschool.editor.api.client.ModuleObject;

public class EditObjectRequestEvent extends EditEvent {

	private final ModuleObject.Type[] type;
	
	public EditObjectRequestEvent(ModuleObject.Type[] type, ObjectEditCallback callback) {
		super(callback);
		this.type = type;
	}
	
	public ModuleObject.Type[] getType() {
		return this.type;
	}

	@Override
	protected void dispatch(EditEventHandler handler) {
		handler.onObjectEdited(this);
	}

}
