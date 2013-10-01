package com.paraschool.editor.client;

import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ObjectEditCallback;

/*
 * Created at 7 nov. 2010
 * By bathily
 */
public interface ResourceStore {

	public abstract void get(final ObjectEditCallback callback,
			final ModuleObject.Type... types);

}