package com.paraschool.editor.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.InlineLabel;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;

public class PagingList extends HTMLList {

	public interface Callback {
		void onSelect(int index);
	}
	
	public interface CssResource extends com.google.gwt.resources.client.CssResource {
		String paging();
		String selectedPage();
	}
	
	public PagingList(final int index, final int count, final CssResource css, final Callback selectCallback) {
		addStyleName(css.paging());
		for(int i = 0 ; i < count ; i++) {
			HTMLListItem item = new HTMLListItem();
			item.add(new InlineLabel((i+1)+""));
			if(i==index) {
				item.addStyleName(css.selectedPage());
			}
			
			final int j = i;
			item.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selectCallback.onSelect(j);
				}
			});
			add(item);
		}
	}
}
