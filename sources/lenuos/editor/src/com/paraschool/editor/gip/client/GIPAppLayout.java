package com.paraschool.editor.gip.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.editor.client.i18n.AppConstants;
import com.paraschool.editor.client.view.AppLayout;
import com.paraschool.editor.client.view.AppResources;

/*
 * Created at 9 juil. 2010
 * By Didier Bathily
 */
public class GIPAppLayout extends FlowPanel implements AppLayout {

	interface LayoutCssResource extends CssResource {
		String content();
		String loggingPanel();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		@Source(value={"GIPAppLayout.css","../../client/view/css/Constants.css"}) LayoutCssResource css();
	}
	
	static {
		Resources.INSTANCE.css().ensureInjected();
	}
	
	FlowPanel logger = new FlowPanel();
	
	@Inject
	private GIPAppLayout(EventBus eventBus, AppConstants constants) {
		super();
		setStyleName(Resources.INSTANCE.css().content());
		
		PopupPanel loggingPanel = new PopupPanel();
		loggingPanel.setStyleName(Resources.INSTANCE.css().loggingPanel());
		loggingPanel.add(logger);
		try{
			if(Boolean.parseBoolean(Location.getParameter("log")))
				loggingPanel.show();
		}catch (NumberFormatException e) {}
	}

	@Override
	public FlowPanel getContent() {
		return this;
	}

	@Override
	public HasWidgets getLoggingPanel() {
		return logger;
	}


}
