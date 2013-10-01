package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.commons.client.Resource2ModuleObject;
import com.paraschool.commons.share.ImageResource;
import com.paraschool.commons.share.LinkResource;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.VideoResource;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ui.UIUtils;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.i18n.LocalizableResource;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 20 juil. 2010
 * By Didier Bathily
 */
public class ResourceDetailView extends HTMLListItem {

	private static ResourceDetailViewUiBinder uiBinder = GWT
			.create(ResourceDetailViewUiBinder.class);

	interface ResourceDetailViewUiBinder extends
			UiBinder<FlowPanel, ResourceDetailView> {
	}

	@UiField Button chooseButton;
	@UiField FlowPanel media;
	@UiField Anchor name;
	@UiField ParagraphElement type;
	@UiField ParagraphElement description;
	@UiField TextBox width;
	@UiField TextBox height;
	
	final ProjectDetails projectDetails;
	final LocalizableResource messages;
	
	public ResourceDetailView(ProjectDetails projectDetails) {
		add(uiBinder.createAndBindUi(this));
		setResource(null);
		this.projectDetails = projectDetails;
		messages = GWT.create(LocalizableResource.class);
	}

	private void setElementsText(String name, String url,
			int width, int height,
			String typeDescription, String description) {
		this.name.setHref(url);
		this.name.setHTML(name);
		
		this.width.setValue(width+"");
		this.height.setValue(height+"");
		
		this.type.setInnerText(typeDescription);
		this.description.setInnerText(description);
	}
	
	public void setResource(final Resource resource) {
		media.clear();
		setElementsText(null, null, 0, 0, null, null);
		
		if(resource != null) {
			ModuleObject object = Resource2ModuleObject.toModuleObject(resource);
			String url = AppUtil.makeInProjectURL(projectDetails , object.getUrl());
			
			setElementsText(resource.getName(), url, object.getWidth(), object.getHeight(),
					getTypeDescription(resource), resource.getDescription());
			
			if(!(resource instanceof LinkResource))
				object.setUrl(url);
			Widget mediaWidget = UIUtils.getWidgetForObject(object);
			if(mediaWidget != null)
				media.add(mediaWidget);
			
		}
	}
	
	public String getTypeDescription(Resource resource) {
		if(resource instanceof ImageResource && ((ImageResource)resource).getInfo() != null)
			return messages.resourceImageTypeDescription(((ImageResource)resource).getInfo().getWidth(), ((ImageResource)resource).getInfo().getHeight());
		if(resource instanceof VideoResource && ((VideoResource)resource).getInfo() != null)
			return messages.resourceVideoTypeDescription(((VideoResource)resource).getInfo().getWidth(), ((VideoResource)resource).getInfo().getHeight(), ((VideoResource)resource).getInfo().getDuration());
		return resource.getMimetype();
	}
}
