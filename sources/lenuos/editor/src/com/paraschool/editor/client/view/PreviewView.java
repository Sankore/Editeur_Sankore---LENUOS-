package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.client.AppUtil;

/*
 * Created at 15 août 2010
 * By bathily
 */
public class PreviewView extends AppPopupPanel implements com.paraschool.editor.client.presenter.PreviewPresenter.Display {

	
	private Frame viewerFrame;
	private final PreviewPopupContent content;
	
	@Inject
	private PreviewView(PreviewPopupContent content) {
		super(true, false);
		this.content = content;
		window.add(content);
	}

	public HasClickHandlers getCloseButton() {
		return content.closeButton;
	}

	@Override
	public HasWidgets getViewerContainer() {
		return content.container;
	}
	
	@Override
	public void viewProject(Project project, int page) {
		
		int width = project.getDetails().getWidth();
		int height = project.getDetails().getHeight();
		
		Style style = getElement().getStyle();
		style.setWidth(width, Unit.PX);
		style.setLeft(50, Unit.PCT);
		style.setMarginLeft(-width/2 -30 , Unit.PX);

		String url = GWT.getHostPageBaseURL()+"index.html?locale="+LocaleInfo.getCurrentLocale().getLocaleName()+
			"&prefix=inproject/&file=preview.xml&suffix="+URL.encode("?project="+AppUtil.makeProjectRequestHashUrl(project.getDetails())+"#"+(page+1));
		
		viewerFrame = new Frame();
		FrameElement element = viewerFrame.getElement().<FrameElement>cast();
		viewerFrame.setWidth(width+"px");
		viewerFrame.setHeight(height+"px");
		element.setMarginHeight(0);
		element.setMarginWidth(0);
		element.setNoResize(true);
		
		viewerFrame.setUrl(url);
		getViewerContainer().add(viewerFrame);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void show() {
		if(viewerFrame != null) {
			viewerFrame.removeFromParent();
			viewerFrame = null;
		}
		super.show();
	}
	
	
}
