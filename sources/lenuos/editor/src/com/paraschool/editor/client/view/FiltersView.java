package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;

/*
 * Created at 20 juil. 2010
 * By Didier Bathily
 */
public class FiltersView extends Composite {

	private static FiltersViewUiBinder uiBinder = GWT.create(FiltersViewUiBinder.class);
	interface FiltersViewUiBinder extends UiBinder<HTMLPanel, FiltersView> {}
	
	public interface FiltersCssResource extends CssResource {
		String filters();
		String even();
		String odd();
		String selected();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/Constants.css","css/FiltersView.css"}) FiltersCssResource css();
		
		@Source("images/separator.png")	ImageResource separator();
		@Source("images/interact_menu_hover_impair.png") ImageResource even();
		@Source("images/interact_menu_hover_pair.png")	ImageResource odd();
	}
	
	private class FilterItem extends HTMLListItem {
		public FilterItem(String label) {
			SpanElement span = DOM.createSpan().cast();
			span.setInnerHTML(label);
			getElement().appendChild(span);
			getElement().appendChild(DOM.createDiv());
		}
	}
	
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@UiField HTMLList list;
	
	public FiltersView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void select(int index) {
		Widget w = list.getWidget(index);
		w.addStyleName(Resources.INSTANCE.css().selected());
	}
	
	public void unselect(int index) {
		Widget selectedFilter = list.getWidget(index);
		selectedFilter.removeStyleName(Resources.INSTANCE.css().selected());
	}
	
	public void addFilter(String label) {
		FilterItem item = new FilterItem(label);
		item.setStyleName(list.getWidgetCount() % 2 == 0 ? Resources.INSTANCE.css().even() : Resources.INSTANCE.css().odd());
		list.add(item);
	}
}
