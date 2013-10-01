package com.paraschool.editor.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.client.AppUtil;

/*
 * Created at 15 août 2010
 * By bathily
 */
public class ExportResultView extends AppPopupPanel implements com.paraschool.editor.client.presenter.ProjectPresenter.ExportResultDisplay {

	
	private final ExportResultPopupContent content;
	
	@Inject
	private ExportResultView(ExportResultPopupContent content) {
		super(true, false);
		this.content = content;
		window.add(content);
	}

	public HasClickHandlers getCloseButton() {
		return content.closeButton;
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void show() {
		content.clear();
		super.show();
	}

	@Override
	public void setExportResult(ProjectDetails details, List<String> urls) {
		for(String url : urls) {
			content.addUrl(AppUtil.makeExportURL(details, url));
		}
	}
	
}
