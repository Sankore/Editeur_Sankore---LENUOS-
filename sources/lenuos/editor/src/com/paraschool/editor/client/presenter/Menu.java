package com.paraschool.editor.client.presenter;

import com.google.gwt.event.dom.client.HasClickHandlers;

/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
public interface Menu {
	HasClickHandlers getDeleteButton();
	HasClickHandlers getQuickEditButton();
	HasClickHandlers getToolsButton();
	HasClickHandlers getPreferenceButton();
}
