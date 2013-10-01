package com.paraschool.editor.modules.commons.client.ui;

import com.google.gwt.user.client.ui.HTML;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.modules.commons.client.jso.BlocJSO;

public class BlocObject {
	public MediaContainer resource;
	public MediaContainer sound;
	public HTML text;
	
	public BlocObject(MediaContainer resource, MediaContainer sound,
			HTML text) {
		this.resource = resource;
		this.sound = sound;
		this.text = text;
	}
	
	public BlocJSO toJSO() {
		BlocJSO jso = BlocJSO.createObject().cast();
		ModuleObject temp;
		if((temp = resource.getObject()) != null)
			jso.setResource(temp.getId());
		if((temp = sound.getObject()) != null)
			jso.setSound(temp.getId());
		jso.setText(text.getHTML());
		
		return jso;
	}
}