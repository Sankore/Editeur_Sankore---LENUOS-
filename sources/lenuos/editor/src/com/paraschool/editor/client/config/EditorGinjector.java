package com.paraschool.editor.client.config;

import com.paraschool.commons.client.config.CommonsGinjector;
import com.paraschool.editor.client.AppController;

/*
 * Created at 5 nov. 2010
 * By bathily
 */ 
public interface EditorGinjector extends CommonsGinjector {
	AppController getAppController();	
}