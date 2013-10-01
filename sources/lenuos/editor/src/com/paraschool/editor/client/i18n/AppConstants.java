package com.paraschool.editor.client.i18n;

import com.google.gwt.i18n.client.Constants;

/*
 * Created at 21 juil. 2010
 * By Didier Bathily
 */
public interface AppConstants extends Constants {

	String[] advices();
	
	// Delay in ms
	int menuAnimationDelay();
	int notificationAnimationDelay();
	int notificationHideDelay();
	int adviceSwitchDelay();
	
	int newProjectKeyStroke();
	int openProjectKeyStroke();
	int saveProjectKeyStroke();
	int createModelKeyStroke();
	int previewProjectKeyStroke();
	int exportProjectKeyStroke();
	int publishProjectKeyStroke();
	int quitProjectKeyStroke();
	
	int newPageKeyStroke();
	int resourceKeyStroke();
	int newInteractivityKeyStroke();
	
}
