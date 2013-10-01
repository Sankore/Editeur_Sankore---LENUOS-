package com.paraschool.editor.gip.client.config;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.paraschool.editor.client.config.EditorClientModule;
import com.paraschool.editor.client.config.EditorGinjector;
import com.paraschool.editor.client.config.EditorPresenterModule;
import com.paraschool.editor.client.config.InjectorProvider;

/*
 * Created at 5 nov. 2010
 * By bathily
 */
public class GipInjectorProvider implements InjectorProvider {

	@GinModules(value={EditorClientModule.class, Module.class, EditorPresenterModule.class, StoreModule.class}) 
	protected interface DefaultEditorGinjector extends EditorGinjector {}
	
	@Override
	public EditorGinjector get() {
		return GWT.create(DefaultEditorGinjector.class);
	}

}
