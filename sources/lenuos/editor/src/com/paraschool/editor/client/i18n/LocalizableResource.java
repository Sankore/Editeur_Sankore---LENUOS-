package com.paraschool.editor.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface LocalizableResource extends Messages {
	String templateWindowLabel();
	String templateWindowDescription();
	
	String resourcesWindowLabel();
	String resourcesWindowDescription();
	String resourcesWindowSelectionLabel();
	String resourcesChooseButtonLabel();
	String resourcesImportButtonLabel();
	String resourcesCatalogButtonLabel();
	
	String resourceFilterAll();
	String resourceFilterImage();
	String resourceFilterVideo();
	String resourceFilterAnimation();
	String resourceFilterSound();
	String resourceFilterDocument();
	String resourceFilterLink();
	String resourceFilterOther();
	
	String resourceImageTypeDescription(int width, int height);
	String resourceVideoTypeDescription(int width, int height, long duration);
	
	String textEditorWindowLabel();
	String textEditorWindowDescription();
	
	String projectFormError();
	String performSaveMessage();
	
	String performSaveForCreateLocaleDefaultMessage(String newLocale);
	String performSaveForCreateLocaleMessage(String newLocale, String currentLocale);
	String performSaveForChangeLocaleDefaultMessage();
	String performSaveForChangeLocaleMessage(String locale);
	
	String confirmLocaleDeletion(String locale);
	
	String failedToCreateLocale(String locale);
	String failedToGetLocale(String locale);
	String failedToDeleteLocale(String locale);
	
	String interactivitiesWindowLabel();
	String interactivitiesWindowDescription();
	String interactivitiesFilterAll();
	String backToListButtonLabel();
	
	String emptyStatement();
	
	String noLocaleInProject();
	String projectHasLocale ();
}
