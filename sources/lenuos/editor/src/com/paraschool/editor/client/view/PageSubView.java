package com.paraschool.editor.client.view;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.paraschool.commons.client.presenter.Display;

/*
 * Created at 25 juil. 2010
 * By Didier Bathily
 */
public class PageSubView extends Composite implements HasWidgets, Display {

	private static PageSubViewUiBinder uiBinder = GWT
			.create(PageSubViewUiBinder.class);

	interface PageSubViewUiBinder extends UiBinder<Widget, PageSubView> {
	}
	
	interface PageSubViewCssResource extends CssResource {
		String subView();
		String subViewHead();
		String subViewContent();
		String subViewBottom();

		String viewLoading();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		@Source(value={"css/PageSubView.css","css/Constants.css"}) PageSubViewCssResource css();
		
		@Source("images/bg_contenuEdit_haut.png") ImageResource subViewHead();
		@Source("images/bg_contenuEdit_bas.png") ImageResource subViewBottom();
		@Source("images/bg_contenuEdit_mil.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource subViewContent();
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	Button closeButton;
	@UiField FlowPanel content;
	@UiField Widget loadingIndicator;
	
	public PageSubView() {
		initWidget(uiBinder.createAndBindUi(this));
		closeButton = new Button();
	}

	public HasClickHandlers getCloseButton() {
		return closeButton;
	}
	
	public void add(Widget w) {
		content.add(w);
	}

	public void clear() {
		content.clear();
	}

	public Iterator<Widget> iterator() {
		return content.iterator();
	}

	public boolean remove(Widget w) {
		return content.remove(w);
	}

	public Widget asWidget() {
		return this;
	}

}
