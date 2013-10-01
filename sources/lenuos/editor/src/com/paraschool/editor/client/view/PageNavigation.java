package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.editor.client.ContentMoverPopUpPanel;
import com.paraschool.editor.client.ContentMoverPopUpPanel.MoverCallback;
import com.paraschool.editor.client.event.page.HidePageEvent;
import com.paraschool.editor.client.event.page.MovePageEvent;
import com.paraschool.editor.client.event.page.ShowPageEvent;
import com.paraschool.editor.client.view.ProjectView.Resources;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;

public class PageNavigation extends Composite {

	private static PageNavigationUiBinder uiBinder = GWT.create(PageNavigationUiBinder.class);
	interface PageNavigationUiBinder extends UiBinder<HTMLList, PageNavigation> {}
	
	
	public final static int PAGESBUTTONS_INITIAL_WIDGET_COUNT = 2; // Dans l'ul il y a 2 li supplémentaires, 1 pour le slider et 1 pour l'ajout de page
	private final EventBus eventBus;
	private int selectedIndex = -1;
	
	private final HTMLList list;
	protected @UiField PagesButton addPageButton;
	protected @UiField HTMLListItem slider;
	
	@Inject
	private PageNavigation(final EventBus eventBus) {
		super();
		
		this.eventBus = eventBus;
		
		list = uiBinder.createAndBindUi(this);
		initWidget(list);
		
		list.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final int clickedIndex = list.getIndexForEvent(event) - PAGESBUTTONS_INITIAL_WIDGET_COUNT;
				if(clickedIndex >= 0){
					if(clickedIndex != selectedIndex){
						switchToPage(clickedIndex);
					}else
						new ContentMoverPopUpPanel(getPageButton(clickedIndex).button, clickedIndex, new MoverCallback() {
							@Override
							public void onMove(int destination) {
								eventBus.fireEvent(new MovePageEvent(clickedIndex, destination));
							}
						});
				}
					
					
			}
		});
	}
	
	public Button getAddPageButton() {
		return addPageButton.button;
	}
	
	public PagesButton getPageButton(int index) {
		return (PagesButton)list.getWidget(index + PAGESBUTTONS_INITIAL_WIDGET_COUNT);
	}
	
	public void add(PagesButton pb) {
		list.add(pb);
		slider.removeStyleName(Resources.INSTANCE.css().nopage());
		slider.addStyleName(Resources.INSTANCE.css().forpage());
	}
	
	public int getWidgetCount() {
		return list.getWidgetCount() - PAGESBUTTONS_INITIAL_WIDGET_COUNT;
	}
	
	public void insert(PagesButton w, int index) {
		list.insert(w, index + PAGESBUTTONS_INITIAL_WIDGET_COUNT);
	}
	
	public boolean remove(int index) {
		boolean removed = list.remove(index+PAGESBUTTONS_INITIAL_WIDGET_COUNT);
		
		if(removed && getWidgetCount() == 0) {
			slider.removeStyleName(Resources.INSTANCE.css().forpage());
			slider.addStyleName(Resources.INSTANCE.css().nopage());
			moveSlider(-1);
		}
		
		return removed;
	}
	
	public int getSelectedPage() {
		return selectedIndex;
	}
	
	public void showNextPage() {
		int index = selectedIndex + 1;
		if(index < getWidgetCount()){
			switchToPage(index);
		}
	}

	public void showPreviousPage() {
		int index = selectedIndex - 1;
		if(index >= 0 && index < getWidgetCount() - 2){
			switchToPage(index);
		}
	}
	
	public void unselectPage(int index) {
		selectedIndex = -1;
		eventBus.fireEvent(new HidePageEvent(index));
		moveSlider(-1);
	}
	
	public void selectPage(int index) {
		selectedIndex = index;
		eventBus.fireEvent(new ShowPageEvent(index));
		moveSlider(index);
	}
	
	private void switchToPage(int index) {
		unselectPage(selectedIndex);
		selectPage(index);
	}
	
	private void moveSlider(int index) {
		Style style = slider.getElement().getStyle();
		style.setTop((Resources.INSTANCE.css().liHeight() + Resources.INSTANCE.css().liPaddingTop()) * (index-1 + PageNavigation.PAGESBUTTONS_INITIAL_WIDGET_COUNT) ,Unit.PX);
	}
}
