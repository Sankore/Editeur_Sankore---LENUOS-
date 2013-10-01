package com.paraschool.editor.api.client;

import java.util.ArrayList;
import java.util.List;



public interface ModulesStore {
	static List<Class<? extends EditorModule>> ONLY = new ArrayList<Class<? extends EditorModule>>();
	public List<EditorModule> getModules();
}
