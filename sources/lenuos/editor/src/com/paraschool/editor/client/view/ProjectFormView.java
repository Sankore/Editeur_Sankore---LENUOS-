package com.paraschool.editor.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.paraschool.commons.share.Author;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.client.presenter.ProjectFormDisplay;
/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class ProjectFormView extends CompositeDisplayView implements ProjectFormDisplay{

	@UiField Button submitButton;
	@UiField Button backButton;

	@UiField TextBox nameBox; 
	@UiField TextArea descriptionBox;
	@UiField TextBox objectifBox;
	@UiField TextBox versionBox;
	
	@UiField TextBox authorNameBox; 
	@UiField TextBox authorEmailBox; 
	@UiField TextBox authorUrlBox;
	@UiField TextBox authorCompanyBox;	
	
	public HasClickHandlers getCancelButton() {
		return backButton;
	}
	
	public HasClickHandlers getSubmitButton() {
		return submitButton;
	}

	public HasValue<String> getName() {
		return nameBox;
	}
	
	public HasValue<String> getDescription() {
		return descriptionBox;
	}
	
	public HasValue<String> getObjectif() {
		return objectifBox;
	}

	@Override
	public HasValue<String> getAutorName() {
		return authorNameBox;
	}

	@Override
	public HasValue<String> getAutorEmail() {
		return authorEmailBox;
	}

	@Override
	public HasValue<String> getAutorUrl() {
		return authorUrlBox;
	}

	@Override
	public HasValue<String> getAutorCompany() {
		return authorCompanyBox;
	}

	private void setAutor(Author author) {
		getAutorName().setValue(author.getName());
		getAutorEmail().setValue(author.getEmail());
		getAutorCompany().setValue(author.getCompany());
		getAutorUrl().setValue(author.getHref());
	}

	@Override
	public void setDetails(ProjectDetails details) {
		getName().setValue(details.getName());
		getDescription().setValue(details.getDescription(), true);
		getObjectif().setValue(details.getObjectif(), true);
		getVersion().setValue(details.getVersion());
		setAutor(details.getAuthor());
	}

	@Override
	public ProjectDetails getDetails() {
		return new ProjectDetails(getName().getValue(),
					getDescription().getValue(), getObjectif().getValue(),
					getVersion().getValue(),"HTMLViewer",
					new Author(getAutorName().getValue(), getAutorEmail().getValue(),
								getAutorUrl().getValue(), getAutorCompany().getValue()));
	}

	@Override
	public HasValue<String> getVersion() {
		return versionBox;
	}

}
