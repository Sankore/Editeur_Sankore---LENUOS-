package com.paraschool.editor.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Inject;
import com.paraschool.commons.share.TemplateDetails;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.i18n.LocalizableResource;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class TemplateListView extends Window implements com.paraschool.editor.client.presenter.TemplateListPresenter.Display {

	public interface Resources extends Window.Resources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("images/titleImg_lesTemplates.png") ImageResource titleIcon();
	}
	
	WindowListContent listWidget;
	
	@Inject
	private TemplateListView(WindowContent content, WindowListContent listWidget, LocalizableResource resources) {
		super(content);
		setWindowImageUrl(Resources.INSTANCE.titleIcon().getURL());
		setWindowTitle(resources.templateWindowLabel());
		setWindowDescription(resources.templateWindowDescription());
		
		this.listWidget = listWidget;
		this.listWidget.list.addStyleName(Resources.INSTANCE.appCss().clearfix());
		addToContent(listWidget);
		hideBar();
	}
	
	public int getClicked(ClickEvent event) {
		return listWidget.list.getIndexForEvent(event);
	}

	public HasClickHandlers getList() {
		return listWidget.list;
	}

	public void setData(List<TemplateDetails> data) {
		content.loadingIndicator.setVisible(false);
		listWidget.list.clear();
		
		for (int i = 0; i < data.size(); i++) {
			TemplateDetails details = data.get(i);
			ThumbnailListItem item = new ThumbnailListItem(AppUtil.makeInTemplateURL(details, details.getUrl()),details.getName(),details.getDescription());
			item.closeButton.setVisible(false);
			listWidget.list.add(item);
		}
	}

}
