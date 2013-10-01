package com.paraschool.editor.modules.relier.client.ui;

import com.google.gwt.user.client.ui.HTML;

class Pin extends HTML {
	/**
	 * 
	 */
	Integer id;
	public Pin(Integer id) {
		super(id == null ? "" : (id+1)+"");
	}
	public Pin() {
		this(null);
	}
}