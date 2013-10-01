package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.htmllist.client.HTMLList;

public class WindowListContent extends Composite {

	interface WindowListContentUiBinder extends UiBinder<Widget, WindowListContent> {}
	private static WindowListContentUiBinder uiBinder = GWT.create(WindowListContentUiBinder.class);
	
	interface WindowListContentCssResource extends CssResource {
		String listContainer();
		String templates();		
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/WindowListContent.css","css/Constants.css"}) WindowListContentCssResource css();
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@UiField HTMLList list;
	
	@Inject
	private WindowListContent() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
