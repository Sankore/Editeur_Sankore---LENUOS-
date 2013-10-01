package com.paraschool.editor.api.client;

/*
 * Created at 22 août 2010
 * By bathily
 */
public interface ModuleContext {
	public abstract ModuleObject getObject(String id);
	public abstract String getData();
}