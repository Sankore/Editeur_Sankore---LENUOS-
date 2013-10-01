package com.paraschool.editor.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.paraschool.commons.client.presenter.Display;
import com.paraschool.commons.share.ProjectDetails;

public interface ProjectFormDisplay extends Display {

	HasClickHandlers getSubmitButton();
	HasClickHandlers getCancelButton();
	
	void setDetails(ProjectDetails details);
	ProjectDetails getDetails();
	
	HasValue<String> getName();
	HasValue<String> getDescription();
	HasValue<String> getObjectif();
	
	HasValue<String> getVersion();
	
	HasValue<String> getAutorName();
	HasValue<String> getAutorEmail();
	HasValue<String> getAutorUrl();
	HasValue<String> getAutorCompany();
	
}
