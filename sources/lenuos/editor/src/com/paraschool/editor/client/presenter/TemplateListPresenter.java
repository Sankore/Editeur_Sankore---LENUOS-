package com.paraschool.editor.client.presenter;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.TemplateDetails;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.event.page.AddPageEvent;
import com.paraschool.editor.client.rpc.EditorServiceAsync;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
public class TemplateListPresenter extends DefaultPresenter {

	public interface Display extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getCloseButton();
		HasClickHandlers getList();
		
		void show();
		void hide();
		
		int getClicked(ClickEvent event);
		void setData(List<TemplateDetails> data);
	}
	
	private List<TemplateDetails> templates;
	
	@Inject
	private TemplateListPresenter(EditorServiceAsync editorRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Display display) {
		super(editorRpcService, null, eventBus, ajaxHandler, display);
	}
	
	@Override
	protected void bind() {
		registrations.add(
			((Display)display).getCloseButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					((Display)display).hide();
					clear();
				}
			})
		);
		
		registrations.add(
			((Display)display).getList().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					int selected = ((Display)display).getClicked(event);
					if(selected >= 0){
						display.asWidget().removeFromParent();
						Page page = new Page(templates.get(selected));
						eventBus.fireEvent(new AddPageEvent(page));
					}
				}
			})
		);
		
	}
	
	public void setDate(List<TemplateDetails> templates) {
		this.templates = templates;
		((Display)display).setData(templates);
	}
	
	public void go(final HasWidgets container) {
		super.go(container);
		((Display)display).show();
	}
	
}
