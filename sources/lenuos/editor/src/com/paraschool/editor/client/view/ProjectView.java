package com.paraschool.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.editor.client.presenter.ProjectPresenter;
/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class ProjectView extends CompositeDisplayView implements ProjectPresenter.Display{

	private static ProjectViewUiBinder uiBinder = GWT.create(ProjectViewUiBinder.class);

	interface ProjectViewUiBinder extends UiBinder<Widget, ProjectView> {
	}

	interface ProjectViewCssResource extends CssResource {
		String arrowAjouterPage();
		String container();
		
		String projectNameWrapper();
		String projectName();
		
		String nav();
		String navLeft();
		String navRight();
		String navContent();
		String buttons();
		String addResourceButton();
		String selected();
		
		String addResourceButtonWrapper();
		String display();
		String displayNav();
		String slide();
		String addPage();
		String emptyPage();
		String emptyPageHead();
		String emptyPageContent();
		String emptyPageBottom();
		String pageButton();
		
		String nopage();
		String forpage();
		
		String pages();
		
		int liHeight();
		int liPaddingTop();
		
		String form();
		String formOpened();
		String formClosed();
	}

	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);

		@Source(value={"css/ProjectView.css","css/Constants.css"}) ProjectViewCssResource css();

		@Source("images/arrow_ajouterPage.png") ImageResource arrowAjouterPage();
		@Source("images/gch_barNavNoir.gif") ImageResource navLeft();
		@Source("images/dt_barNavNoir.gif") ImageResource navRight();
		@Source("images/mil_barNavNoir.gif") @ImageOptions(repeatStyle=RepeatStyle.Horizontal) ImageResource navContentBg();
		
		@Source("images/icn_page_gris.png") ImageResource pageButton();
		@Source("images/icn_page_bleu.png") ImageResource pageButtonSelected();

		@Source("images/puce_menu2H_nomProj.png") ImageResource projectNameBg();
		@Source("images/icn_page_plus.png") ImageResource addPage();
		@Source("images/onglet_menu_V_gch.jpg") ImageResource nopage();
		@Source("images/onglet_menu_V_gch_pages.png") ImageResource forpage();
		@Source("images/puce_menu2H_ressource.png") ImageResource exploreResource();
		
		@Source("images/elementOne_bg_haut.png") ImageResource emptyPageHead();
		@Source("images/elementOne_bg_mil.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource emptyPageContent();
		@Source("images/elementOne_bg_bas.png") ImageResource emptyPageBottom();
		
		@Source("images/separator_link_ress.png") ImageResource separator();
	}

	{
		Resources.INSTANCE.css().ensureInjected();
	}

	protected @UiField Panel head;
	protected @UiField Panel nav;
	
	protected @UiField Button addResourceButton;
	protected @UiField Label projectNameLabel;
	protected @UiField InlineLabel hideForm;
	
	protected @UiField FlowPanel formContainer;
	
	protected @UiField FlowPanel windowContainer;
	protected @UiField FlowPanel pages;
	protected @UiField(provided=true) PageNavigation pagesButtons;
	
	protected @UiField Widget arrow;
	
	private final PopupPanel popUp;
	
	@Inject
	protected ProjectView(PageNavigation pagesButtons) {
		this.pagesButtons = pagesButtons;
		initWidget(uiBinder.createAndBindUi(this));
		popUp = new PopupPanel(false, true);
	}

	private Widget getPageWidget(int index) {
		return pages.getWidget(index);
	}

	private void addButton(int index) {
		PagesButton pb = new PagesButton(index);
		pb.setStyleName(Resources.INSTANCE.appCss().flotL());
		
		if(index < pagesButtons.getWidgetCount())
			pagesButtons.insert(pb, index);
		else
			pagesButtons.add(pb);

	}
	
	private void removeButton(int index) {
		pagesButtons.remove(index);
	}

	public HasText getProjectName() {
		return projectNameLabel;
	}

	public Button getAddPageButton() {
		return pagesButtons.getAddPageButton();
	}

	public Button getShowResourcesButton() {
		return addResourceButton;
	}

	public HasWidgets getPagesContainer() {
		return pages;
	}

	public PopupPanel getWindowContainer() {
		return  popUp;
	}

	public void pageAdded(int index) {
		for(int i = index; i < pagesButtons.getWidgetCount(); i++) {
			pagesButtons.getPageButton(i).setIndex(i+1);
		}
		addButton(index);
	}

	public void pageMoved(int source, int destination) {
		for(int i = Math.min(source, destination) ; i <= Math.max(source, destination) ; i++) {
			pagesButtons.getPageButton(i).setIndex(i);
		}
		
		Widget w = pages.getWidget(source);
		pages.insert(w, destination + (destination > source ? 1 : 0));
	}

	public void pageRemoved(int index) {
		if(index > -1){
			for(int i = index; i < pagesButtons.getWidgetCount(); i++) {
				pagesButtons.getPageButton(i).setIndex(i-1);
			}
			removeButton(index);
		}
	}

	
	@Override
	public int currentPageIndex() {
		return pagesButtons.getSelectedPage();
	}

	@Override
	public PageNavigation getNavigation() {
		return pagesButtons;
	}

	@Override
	public void showPage(int index) {
		if(index > -1) {
			getPageWidget(index).setVisible(true);
		}
	}

	@Override
	public void hidePage(int index) {
		if(index > -1) {
			getPageWidget(index).setVisible(false);
		}
	}

	@Override
	public HasClickHandlers getShowProjectFormButton() {
		return projectNameLabel;
	}

	@Override
	public HasWidgets getFormContainer() {
		return formContainer;
	}

	@Override
	public void showForm() {
		formContainer.removeStyleName(Resources.INSTANCE.css().formClosed());
		formContainer.addStyleName(Resources.INSTANCE.css().formOpened());
		projectNameLabel.setVisible(false);
		hideForm.setVisible(true);
	}

	@Override
	public void hideForm() {
		formContainer.removeStyleName(Resources.INSTANCE.css().formOpened());
		formContainer.addStyleName(Resources.INSTANCE.css().formClosed());
		projectNameLabel.setVisible(true);
		hideForm.setVisible(false);
	}

	@Override
	public HasClickHandlers getHideProjectFormButton() {
		return hideForm;
	}
	

}
