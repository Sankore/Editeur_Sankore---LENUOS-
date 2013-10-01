package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public abstract class PageView extends CompositeDisplayView {

	protected static PageViewUiBinder uiBinder = GWT.create(PageViewUiBinder.class);
	protected interface PageViewUiBinder extends UiBinder<Widget, PageView> {}

	@com.google.gwt.resources.client.CssResource.Shared
	public interface PageViewCssResource extends CssResource {

		String pageWidth();
		String pageHeight();
		
		String page();
		String area();

		String display();
		String displayHead();
		String displayBottom();

	}

	public interface Resources extends ClientBundle {
		Resources INSTANCE = GWT.create(Resources.class);

		@Source(value={"css/PageView.css","css/Constants.css"}) PageViewCssResource css();

		@Source("images/elementTwo_bg_haut.png") ImageResource displayHead();
		@Source("images/elementTwo_bg_mil.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource area();
		@Source("images/elementTwo_bg_bas.png") ImageResource displayBottom();
		
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}

	protected @UiField AbsolutePanel container;
	protected @UiField PageContentPanel area;
	protected @UiField FlowPanel subview;
	protected @UiField FlowPanel display;

	@Inject
	protected PageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HasWidgets getSubViewContainer() {
		return subview;
	}

	public PageContentPanel getInteractivitiesContainer() {
		return area;
	}

}