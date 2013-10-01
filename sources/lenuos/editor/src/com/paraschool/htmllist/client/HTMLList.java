package com.paraschool.htmllist.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public class HTMLList extends ComplexPanel implements InsertPanel, HasClickHandlers{
	public static enum ListType {
		UNORDERED {
			public Element createElement() {
				return Document.get().createULElement();
			}
		},
		ORDERED {
			public Element createElement() {
				return Document.get().createOLElement();
			}
		};

		public abstract Element createElement();
	}

	public HTMLList() {
		setElement(ListType.UNORDERED.createElement());
	}
	
	public HTMLList(ListType listType) {
		setElement(listType.createElement());
	}
	
	@Override
	public void add(Widget w) {
		super.add(w, getElement());
	}
	
	@Override
	public void insert(Widget w, int beforeIndex) {
		super.insert(w, getElement(), beforeIndex, true);
	}
	
	/*
	public void addItem(String html) {
		add(new HTMLListItem(html));
	}
	*/

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}
	
	protected com.google.gwt.user.client.Element getElementForEvent(Event event) {
		com.google.gwt.user.client.Element target = DOM.eventGetTarget(event); 
		for(; target != null; target = DOM.getParent(target)) {
			if(DOM.getElementProperty(target, "tagName").equalsIgnoreCase("li")) {
				com.google.gwt.user.client.Element list = DOM.getParent(target);
				if(list == getElement())
					return target;
			}
			if(target == getElement())
				return null;
		}
		return null;
	}
	
	public int getIndexForEvent(ClickEvent event) {
		com.google.gwt.user.client.Element li = getElementForEvent(Event.as(event.getNativeEvent()));
		int index = -1;
		if(li != null)
			index = DOM.getChildIndex(getElement(), li);
		return index;
	}
	
	public int getIndex(HTMLListItem item) {
		return DOM.getChildIndex(getElement(), item.getElement());
	}

	@Override
	public boolean remove(int index) {
		return remove( getWidget(index) );
	}

	@Override
	public boolean remove(Widget w) {
		return super.remove(w);
	}
	
	
}
