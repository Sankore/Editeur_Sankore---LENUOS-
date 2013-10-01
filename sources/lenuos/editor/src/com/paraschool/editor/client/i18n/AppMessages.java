package com.paraschool.editor.client.i18n;

import com.google.gwt.i18n.client.Messages;

/*
 * Created at 21 juil. 2010
 * By Didier Bathily
 */
public interface AppMessages extends Messages {

	String appTitle();
	
	String unableToLoadApplication();
	
	String projectCreationFailed(String id);
	String projectCreationFailedCauseFolderExist(String id);
	String projectCreationFailedCauseFileError(String id);
	String projectNotFound(String id);
	
	String projectOnSave(String id);
	String projectSaveSuccess(String id);
	String projectSaveError(String id);
	
	String projectDeletionSuccess(String id);
	String projectDeletionError(String id);
	
	String projectExportSuccess(String id);
	String projectExportError(String id);
	
	String projectPublishSuccess(String id);
	String projectPublishError(String id);
	
	String modelCreationSucceed(String id);
	String modelCreationError(String id);
	
	String featureDisable();
	
	String uploadInProgress();
	String uploadFinished();
	String uploadHasError();
	String uploadFileUnknow();
	String uploadFileInvalid(String type);
	String unknowUploadError();
	String uploadSizeExceededError(long value);
	
	String rpcRetryMessage();
	
	@Key("store.unavailable")
	String unavailableStore();
	
	String missingModuleWidget();
	
	String confirmDeletion();
	String someProjectDetailsAreMissing();
	
	String willDisconnect();
	String willDisconnectToSSO();
	
	String cleanThanks();
	String noSampleAvailable();
	String createProjectMessage(String name);
}
