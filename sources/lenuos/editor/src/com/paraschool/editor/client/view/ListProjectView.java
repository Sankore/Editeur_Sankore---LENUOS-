package com.paraschool.editor.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.editor.client.rpc.ProjectService.Sort;
import com.paraschool.editor.shared.Page;
import com.paraschool.htmllist.client.HTMLList;

/*
 * Created at 7 juil. 2010
 * By Didier Bathily
 */
public class ListProjectView extends CompositeDisplayView implements com.paraschool.editor.client.presenter.ListProjectPresenter.Display {

	private static ListProjectViewUiBinder uiBinder = GWT.create(ListProjectViewUiBinder.class);
	interface ListProjectViewUiBinder extends UiBinder<HTMLPanel, ListProjectView> {}
	interface ListProjectCssResource extends CssResource, PagingList.CssResource {
		
		String arrowChoixProj();
		
		String root();
		String content();
		String title();
		String subtitle();
		String container();
		String menu();
		String list();
		
		String returnButtonWrapper();
		String returnButton();
		String arrowRetour();
		
		
		String wrapper();
		String projects();
		String boxRoot();
		String box();
		String boxWrapper();
		String left();
		String right();
		String name();
		String date();
		String objectif();
		String detail();
		String delete();
		String description();
		String details();
		String head();
		String bottom();
		String open();
		String opened();
		
		String create();
		String sorts();
		String selectedSort();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source(value={"css/ListProject.css","css/Constants.css"}) ListProjectCssResource css();
		
		@Source("images/arrow_choixProj.png") ImageResource arrowChoixProj();
		@Source("images/arrow_retour.png") ImageResource arrowRetour();
		@Source("images/bg_menuImg_btnListeProjet.png") ImageResource list();
		@Source("images/btn_retour.png") ImageResource returnButton();
		
		
		@Source("images/listeProj_bg_proj.png") ImageResource box();
		@Source("images/listeProj_bg_proj_hover.png") ImageResource opened();
		@Source("images/listeProj_bg_descLg_mil.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource head();
		@Source("images/listeProj_bg_descLg.png") ImageResource bottom();
		@Source("images/btn_dial.png") ImageResource open();
		@Source("images/poub.png") ImageResource delete();
		
	}
	
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	@UiField HTML advice;
	@UiField HTMLPanel sorts;
	@UiField HTML create;
	@UiField Button backButton;
	@UiField HTMLList projectsList;
	@UiField Button openButton;
	@UiField Button deleteButton;
	@UiField FlowPanel paging;
	
	@UiField Button sortDateButton;
	@UiField Button sortNameButton;
	
	private final Button fetch = new Button();
	
	Sort desiredSort = Sort.DATE;
	int desiredPage = 0;
	int selected = -1;
	
	public ListProjectView() {
		initWidget(uiBinder.createAndBindUi(this));
		
		create.setVisible(false);
		sorts.setVisible(false);
		fetch.setVisible(false);
		sorts.add(fetch);
		
		sortDateButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				desiredSort = Sort.DATE;
				fetch.click();
			}
		});
		sortNameButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				desiredSort = Sort.NAME;
				fetch.click();
			}
		});
	}
	
	public HasClickHandlers getBackButton() {
		return backButton;
	}

	public int getClicked(ClickEvent event) {
		return selected;
	}

	public HasClickHandlers getOpenButton() {
		return openButton;
	}
	
	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}

	public void setData(final Page<ProjectDetails> page) {
		projectsList.clear();
		
		final List<ProjectDetails> data = page.getData();
		if(data != null && data.size() > 0){
			advice.setVisible(true);
			sorts.setVisible(true);
			create.setVisible(false);
			Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
					private int i = 0;
					@Override
					public boolean execute() {
						addToList(data.get(i), i);
						return ++i < data.size();
					}
				}
			);
		}else{
			sorts.setVisible(false);
			advice.setVisible(false);
			create.setVisible(true);
		}
		
		paging.clear();
		
		paging.add(new PagingList(page.getCurrent(), page.getTotalPageCount(), Resources.INSTANCE.css(), new PagingList.Callback() {
			@Override
			public void onSelect(int index) {
				desiredPage = index;
				fetch.click();
			}
		}));
		
		updateSort();
		
	}
	
	private void addToList(final ProjectDetails detail, final int index) {
		final ProjectListItem item = GWT.create(ProjectListItem.class);
		item.setName(detail.getName());
		item.setDate(DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG).format(detail.getDate()));
		item.setObjectif(detail.getObjectif());
		item.setDescription(detail.getDescription());
		
		item.getOpen().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selected = projectsList.getIndex(item);
				openButton.click();
			}
		});
		
		item.getDelete().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				selected = projectsList.getIndex(item);
				deleteButton.click();
			}
		});
		
		projectsList.add(item);
	}

	private void updateSort() {
		if(desiredSort == Sort.DATE){
			sortDateButton.addStyleName(Resources.INSTANCE.css().selectedSort());
			sortNameButton.removeStyleName(Resources.INSTANCE.css().selectedSort());
		}
		if(desiredSort == Sort.NAME) {
			sortNameButton.addStyleName(Resources.INSTANCE.css().selectedSort());
			sortDateButton.removeStyleName(Resources.INSTANCE.css().selectedSort());
		}
			
	}
	
	@Override
	public void remove(int index) {
		projectsList.remove(index);
		if(projectsList.getWidgetCount() == 0){
			advice.setVisible(false);
			sorts.setVisible(false);
			fetch.setVisible(false);
			paging.clear();
			create.setVisible(true);
		}
	}

	@Override
	public HasClickHandlers getCreateButton() {
		return create;
	}

	@Override
	public Sort getDesiredSort() {
		return desiredSort;
	}

	@Override
	public int getDesiredPage() {
		return desiredPage;
	}

	@Override
	public HasClickHandlers getFetchButton() {
		return fetch;
	}
	
}
