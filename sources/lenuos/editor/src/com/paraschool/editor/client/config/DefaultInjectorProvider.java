package com.paraschool.editor.client.config;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;

/*
 * Created at 5 nov. 2010
 * By bathily
 */
public class DefaultInjectorProvider implements InjectorProvider {

	@GinModules(value={EditorClientModule.class, EditorViewModule.class, EditorPresenterModule.class, StoreModule.class}) 
	protected interface DefaultEditorGinjector extends EditorGinjector {}
	
	@Override
	public EditorGinjector get() {
		return GWT.create(DefaultEditorGinjector.class);
	}

}
