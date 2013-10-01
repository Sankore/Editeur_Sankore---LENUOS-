package com.paraschool.editor.modules.commons.client.ui.edit;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.ListStyleType;
import com.google.gwt.dom.client.Style.Unit;
import com.paraschool.htmllist.client.HTMLList;

public class CommonsOptionsWidget extends HTMLList {


	public CommonsOptionsWidget() {
		super();
		Style style = getElement().getStyle();
		style.setListStyleType(ListStyleType.NONE);
		style.setMargin(0, Unit.PX);
		style.setPadding(0, Unit.PX);
	}

}
