package com.paraschool.editor.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.editor.client.event.app.ErrorMessageEvent;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.shared.exception.MissingFeatureException;
import com.paraschool.editor.shared.exception.ProjectException;
import com.paraschool.editor.shared.exception.ProjectFileException;
import com.paraschool.editor.shared.exception.ProjectFolderExistException;
import com.paraschool.editor.shared.exception.ProjectNotFoundException;

/*
 * Created at 24 juil. 2010
 * By Didier Bathily
 */
public class MyAjaxHandler implements AjaxHandler {

	private final EventBus eventBus;
	private final Element loading;
	@Inject AppMessages messages;
	
	@Inject
	private MyAjaxHandler(final EventBus eventBus) {
		this.eventBus = eventBus;
		loading = DOM.getElementById("loading");
	}
	
	public void onComplete() {
		if(loading != null)
			loading.setAttribute("style", "display:none;");
	}
	
	public void onError(Throwable caught) {
		String message = caught.getLocalizedMessage();
		
		if(caught instanceof ProjectException){
			String name = ((ProjectException) caught).getDetails().getName();
			if(caught.getClass().equals(ProjectFolderExistException.class)) message = messages.projectCreationFailedCauseFolderExist(name);
			else if(caught.getClass().equals(ProjectFileException.class)) message = messages.projectCreationFailedCauseFileError(name);
			else if(caught.getClass().equals(ProjectNotFoundException.class)) message = messages.projectNotFound(name);
			else message = messages.projectCreationFailed( name );
			
		}else if(caught.getClass().equals(MissingFeatureException.class)) message = messages.featureDisable();
		
		eventBus.fireEvent(new ErrorMessageEvent(message));
	}

	public void onStart() {
		if(loading != null)
			loading.setAttribute("style", "");
	}

	public void onSuccess() {
		onComplete();
	}

}
