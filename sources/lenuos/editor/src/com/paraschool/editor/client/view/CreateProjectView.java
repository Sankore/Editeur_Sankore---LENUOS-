package com.paraschool.editor.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.CreateProjectPresenter;
import com.paraschool.editor.shared.ProjectModel.Owner;
import com.paraschool.editor.shared.ProjectModelRequest;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;
/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class CreateProjectView extends ProjectFormView implements CreateProjectPresenter.Display{

	private static CreateProjectViewUiBinder uiBinder = GWT.create(CreateProjectViewUiBinder.class);
	interface CreateProjectViewUiBinder extends UiBinder<Widget, CreateProjectView> {}
	
	interface CreateProjectCssResource extends CssResource {
		String arrowRemplir();
		String form();
		String root();
		String content();
		String title();
		String subtitle();
		String container();
		String menu();
		String create();
		
		String returnButtonWrapper();
		String returnButton();
		String arrowRetour();
		String submit();
		
		String modelsPart();
		String selected();
		
		String objectif();
		String description();
		String message();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/CreateProject.css","css/Constants.css"}) CreateProjectCssResource css();
		
		@Source("images/arrow_remplir.gif") ImageResource arrowRemplir();
		@Source("images/arrow_retour.png") ImageResource arrowRetour();
		@Source("images/bg_menuImg_btnNew.png") ImageResource create();
		@Source("images/btn_retour.png") ImageResource returnButton();
	}
	
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@UiField Panel modelsWrapper;
	@UiField Panel formWrapper;
	@UiField FormPanel form;
	@UiField FileUpload file;
	@UiField HTMLList models;
	@UiField Button delete;
	@UiField HTML message;
	
	private int selected = -1;
	private ArrayList<ProjectModelRequest> modelRequests;
	@Inject private AppMessages messages;
	
	@Inject
	private CreateProjectView() {
		initWidget(uiBinder.createAndBindUi(this));
		AppUtil.bindEnterKeyInTextbox(nameBox, submitButton);
		AppUtil.bindEnterKeyInTextbox(objectifBox, submitButton);
		modelRequests = new ArrayList<ProjectModelRequest>();
		
		models.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				int index = -1;
				if ((index = models.getIndexForEvent(event)) > -1) {
					unselect(selected);
					select(index);
				}else{
					unselect(selected);
				}
			}
		});
	}

	@Override
	public FormPanel getUploadModelForm() {
		return form;
	}

	@Override
	public FileUpload getModelFileUpload() {
		return file;
	}

	@Override
	public void setModels(HashMap<Owner, ArrayList<ProjectDetails>> modelMap) {
		
		models.clear();
		if(modelMap == null)
			return;
		
		for(Entry<Owner, ArrayList<ProjectDetails>> entry : modelMap.entrySet()) {
			for(ProjectDetails details : entry.getValue()) {
				Owner owner = entry.getKey();
				modelRequests.add(new ProjectModelRequest(details.getId(), owner));
				models.add(createModelWidget(details, owner));
			}
		}
		
	}
	
	private Widget createModelWidget(ProjectDetails details, Owner owner) {
		final HTMLListItem item = new HTMLListItem();
		item.addStyleName(owner.name());
		Label name = new Label(details.getName());
		Label date = new Label(DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG).format(details.getDate()));
		item.add(name);
		item.add(date);
		Button idelete = new Button("Supprimer");
		idelete.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				select(((HTMLList) item.getParent()).getWidgetIndex(item));
				delete.click();
			}
		});
		item.add(idelete);
		return item;
	}

	private void select(int index) {
		selected = index;
		if(index >= 0 && index < models.getWidgetCount()) {
			Widget w = models.getWidget(index);
			w.addStyleName(Resources.INSTANCE.css().selected());
		}
	}
	
	private void unselect(int index) {
		if(index >= 0 && index < models.getWidgetCount()) {
			Widget w = models.getWidget(index);
			w.removeStyleName(Resources.INSTANCE.css().selected());
		}
		selected = -1;
	}
	
	@Override
	public ProjectModelRequest getModelRequest() {
		if(selected > -1)
			return modelRequests.get(selected);
		return null;
	}

	@Override
	public Button getDeleteModelButton() {
		return delete;
	}

	@Override
	public void setCanUploadModel(boolean canUpload) {
		formWrapper.setVisible(canUpload);
	}
	
	@UiHandler("modelsSwitcher")
	protected void toogleModels(ClickEvent event) {
		((Button)event.getSource()).setVisible(false);
		modelsWrapper.setVisible(!modelsWrapper.isVisible());
	}

	@Override
	public void setDetails(ProjectDetails details) {
		super.setDetails(details);
		message.setText(messages.createProjectMessage(details.getAuthor().getName()));
	}
	
	
}
