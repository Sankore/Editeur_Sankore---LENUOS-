package com.paraschool.editor.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.i18n.AppConstants;
import com.paraschool.editor.client.presenter.HomePresenter;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;
/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class HomeView extends CompositeDisplayView implements HomePresenter.Display {

	private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

	interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
	}
	
	public interface HomeCssResource extends CssResource {
		String arrowPratique();
		String arrowEfficace();
		String menu();
		String container();
		String title();
		String subtitle();
		String create();
		String open();
		String recent();
		String content();
		String raccourci();
		String list();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/Constants.css","css/HomeView.css"}) HomeCssResource css();
		
		@Source("images/arrow_pratique.png") ImageResource arrowPratique();
		@Source("images/arrow_efficace.png") ImageResource arrowEfficace();
		
		@Source("images/bg_menuImg_btnCreer.png") ImageResource create();
		@Source("images/bg_menuImg_btnOuvrir.png") ImageResource open();
		@Source("images/bg_menuImg_btnProjetPrec.png") ImageResource recent();
		
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@UiField protected Panel root;
	@UiField HasClickHandlers createProjectButton;
	@UiField HasClickHandlers openProjectButton;
	@UiField HTMLList recentProjectsList;
	
	@UiField ParagraphElement  newProjectKeystroke;
	@UiField ParagraphElement  openProjectKeystroke;
	
	@Inject
	protected HomeView(AppConstants constants) {
		initWidget(uiBinder.createAndBindUi(this));
		
		String systemMeta = AppUtil.systemMetaKey();
		newProjectKeystroke.setInnerText(systemMeta +"+"+ String.valueOf((char)constants.newProjectKeyStroke()).toLowerCase());
		openProjectKeystroke.setInnerText(systemMeta +"+"+ String.valueOf((char)constants.openProjectKeyStroke()).toLowerCase());
	}

	public HasClickHandlers getCreateProjectButton() {
		return createProjectButton;
	}

	public HasClickHandlers getOpenProjectButton() {
		return openProjectButton;
	}

	public int getClicked(ClickEvent event) {
		return recentProjectsList.getIndexForEvent(event);
	}

	public HasClickHandlers getRecentList() {
		return recentProjectsList;
	}

	public void setData(List<ProjectDetails> data) {
		recentProjectsList.clear();
		for (int i = 0; i < data.size(); i++) {
			HTMLListItem item = new HTMLListItem();
			item.add(new InlineLabel((i+1)+"_ "+data.get(i).getName()));
			item.addStyleName(Resources.INSTANCE.appCss().gray());
			item.addStyleName(Resources.INSTANCE.appCss().clickable());
			recentProjectsList.add(item);
		}

	}
}
