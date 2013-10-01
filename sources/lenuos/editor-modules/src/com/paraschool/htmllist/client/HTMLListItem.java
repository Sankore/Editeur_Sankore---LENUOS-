package com.paraschool.htmllist.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created at 10 juil. 2010
 * By Didier Bathily
 */
public class HTMLListItem extends ComplexPanel implements HasWidgets, HasClickHandlers {

	public HTMLListItem() { 
		setElement(DOM.createElement("li")); 
	} 
	
	/*
	public HTMLListItem(String html) {
		this();
		setHTML(html);
	}
	*/
	
	@Override
	public void add(Widget w) { 
		super.add(w, getElement()); 
	} 
	public void insert(Widget w, int beforeIndex) { 
		super.insert(w, getElement(), beforeIndex, true); 
	}
	/*
	public String getText() { 
		return DOM.getInnerText(getElement()); 
	} 
	public void setText(String text) { 
		DOM.setInnerText(getElement(), (text == null) ? "" : text); 
	}

	public String getHTML() {
		return DOM.getInnerHTML(getElement());
	}

	public void setHTML(String html) {
		DOM.setInnerHTML(getElement(), (html == null) ? "" : html);
	}
	*/

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	} 
	
	public int getIndex() {
		return DOM.getChildIndex(getParent().getElement(), getElement());
	}
}
