package com.paraschool.editor.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.paraschool.commons.client.Collections;
import com.paraschool.commons.client.Predicate;
import com.paraschool.commons.client.Predicates;
import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.api.client.EditorModuleDescriptor;
import com.paraschool.editor.client.i18n.LocalizableResource;
import com.paraschool.editor.shared.SampleDetails;
import com.paraschool.htmllist.client.HTMLList;


/*
 * Created at 25 juil. 2010
 * By Didier Bathily
 */
public class InteractivityListView extends Window implements com.paraschool.editor.client.presenter.InteractivityListPresenter.Display {

	interface InteractivityListViewCssResource extends CssResource {
		String items();
		String interactivities();
		String examples();
		String backButton();
		String even();
		String odd();
		String button();
		String actions();
		
		String create();
		String view();
		String hide();
	}
	
	public interface Resources extends AppResources {
		Resources INSTANCE = GWT.create(Resources.class);
		
		@Source("images/titleImg_lesInteract.png") ImageResource titleIcon();
		@Source(value={"css/Constants.css","css/InteractivityListView.css"}) InteractivityListViewCssResource css();
		
		@Source("images/interact_bg_bloc_ac_bordure.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource odd();
		@Source("images/interact_bg_bloc_ss_bordure.png") @ImageOptions(repeatStyle=RepeatStyle.Vertical) ImageResource even();
		
		@Source("images/icon-create.png") ImageResource createButton();
		@Source("images/icon-view.png") ImageResource viewButton();
		@Source("images/icon-back.png") ImageResource backButton();
		@Source("images/arrow-down.png") ImageResource arrow();
	}
	{
		Resources.INSTANCE.css().ensureInjected();
	}
	
	private class FamilyPredicate implements Predicate<EditorModule> {
		private String family;
		private FamilyPredicate(String family) {
			this.family = family;
		}
		
		@Override
		public boolean apply(EditorModule input) {
			return family.equals(input.getDescriptor().getFamily());
		}
	}
	
	private final LocalizableResource resources;
	
	private final FiltersView filters;
	private final FlowPanel examplesPanel;
	private final HTMLList list;
	private final HTMLList examplesList;
	private final Button chooseButton;
	private final Button exampleButton;
	private final Button backToListButton;
	
	private final ArrayList<String> meetedFamily = new ArrayList<String>();
	private int selected = -1;
	private int selectedExample = -1;
	private int selectedFilter = -1;
	
	private List<EditorModule> datas = null;
	private List<EditorModule> showedDatas = null;
	private List<SampleDetails> samples = null;
	private Predicate<EditorModule> predicate = Predicates.not(Predicates.<EditorModule>accept());
	
	@Inject
	private InteractivityListView(WindowContent content, FiltersView filters, LocalizableResource resources) {
		super(content);
		
		this.resources = resources;
		
		setWindowImageUrl(Resources.INSTANCE.titleIcon().getURL());
		setWindowTitle(resources.interactivitiesWindowLabel());
		setWindowDescription(resources.interactivitiesWindowDescription());
		
		backToListButton = new Button(resources.backToListButtonLabel());
		backToListButton.addStyleName(Resources.INSTANCE.css().button());
		backToListButton.addStyleName(Resources.INSTANCE.css().backButton());
		backToListButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hideExamples();
			}
		});
		
		content.headPanel.insert(backToListButton, 0);
		
		examplesPanel = new FlowPanel();
		examplesPanel.setStyleName(Resources.INSTANCE.css().examples());
		examplesPanel.addStyleName(Window.Resources.INSTANCE.windowCss().windowContentRightBottom());
		examplesList = new HTMLList();
		examplesList.setStyleName(Resources.INSTANCE.css().items());
		examplesPanel.add(examplesList);
		
		content.rightContent.add(examplesPanel);
		content.content.addStyleName(Resources.INSTANCE.css().interactivities());
		
		this.filters = filters;
		addToLeftContent(filters);
		
		list = new HTMLList();
		list.setStyleName(Resources.INSTANCE.css().items());
		list.addStyleName(Resources.INSTANCE.appCss().clearfix());
		
		addToContent(list);
		chooseButton = new Button();
		exampleButton = new Button();
		addToLeftBar(chooseButton);
		addToLeftBar(exampleButton);
		hideBar();
		
		filters.list.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int index = InteractivityListView.this.filters.list.getIndexForEvent(event);
				if(index > -1 && index <= meetedFamily.size()) {
					if(index == 0)
						predicate = Predicates.accept();
					else
						predicate = Predicates.not(new FamilyPredicate(meetedFamily.get(index-1)));
					if(selectedFilter != -1)
						InteractivityListView.this.filters.unselect(selectedFilter);
					InteractivityListView.this.filters.select(index);
					selectedFilter = index;
					refresh();
				}
			}
		});
		
		hideExamples();
	}

	private void showData(List<EditorModule> subDatas) {
		hideExamples();
		content.loadingIndicator.setVisible(false);
		list.clear();
		for(int i=0 ; i<subDatas.size() ; i++) {
			EditorModule module = subDatas.get(i);
			EditorModuleDescriptor descriptor = module.getDescriptor();
			
			InteractivityListItem item = new InteractivityListItem(descriptor.getThumbnail().getURL(),
					descriptor.getName(), descriptor.getDescription());
			item.addStyleName(i%2 == 0 ? Resources.INSTANCE.css().even() : Resources.INSTANCE.css().odd());
			
			final int index = i;
			item.chooseButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selected = index;
					chooseButton.click();
				}
			});
			
			item.examplesButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selected = index;
					exampleButton.click();
				}
			});
			
			list.add(item);
			addFamily(descriptor.getFamily());
		}
		showedDatas = subDatas;
	}

	private void refresh() {
		showData(Collections.filter(datas, predicate));
	}
	
	private void addFamily(String family) {
		if(!meetedFamily.contains(family)){
			filters.addFilter(family);
			meetedFamily.add(family);
		}
	}
	
	@Override
	public void setData(List<EditorModule> datas) {
		java.util.Collections.sort(datas,
				Ordering.natural().onResultOf(new Function<EditorModule, String>() {
					@Override
					public String apply(EditorModule module) {
						return module.getDescriptor().getName().toLowerCase();
					}
				}));
		
		filters.list.clear();
		filters.addFilter(resources.interactivitiesFilterAll());
		for(int i=0 ; i<datas.size() ; i++) {
			EditorModule module = datas.get(i);
			EditorModuleDescriptor descriptor = module.getDescriptor();
			addFamily(descriptor.getFamily());
		}
		this.datas = datas;
		selectedFilter = -1;
		refresh();
	}


	@Override
	public HasClickHandlers getChooseButton() {
		return chooseButton;
	}


	@Override
	public int getSelectedIndex() {
		return datas.indexOf(showedDatas.get(selected));
	}
	
	@Override
	public SampleDetails getSelectedSample() {
		return samples != null && selectedExample >-1 ? samples.get(selectedExample) : null;
	}

	@Override
	public HasClickHandlers getExamplesButton() {
		return exampleButton;
	}
	
	

	@Override
	public void setExamples(List<SampleDetails> datas) {
		java.util.Collections.sort(datas,
				Ordering.natural().onResultOf(new Function<SampleDetails, String>() {
					@Override
					public String apply(SampleDetails sample) {
						return sample.getName().toLowerCase();
					}
				}));
		this.samples = datas;
		showExamples(datas);
	}
	
	private void showExamples(List<SampleDetails> samples) {
		examplesList.clear();
		hideContent();
		examplesPanel.removeStyleName(Resources.INSTANCE.css().hide());
		backToListButton.setVisible(true);
		EditorModule module = datas.get(getSelectedIndex());
		for(int i=0 ; i<samples.size() ; i++) {
			SampleDetails detail = samples.get(i);
			InteractivityExampleListItem item = new InteractivityExampleListItem(detail, module.getDescriptor().getName());
			item.addStyleName(i%2 == 0 ? Resources.INSTANCE.css().even() : Resources.INSTANCE.css().odd());
			final int index = i;
			item.chooseButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					selectedExample = index;
					chooseButton.click();
				}
			});
			examplesList.add(item);
		}
		com.google.gwt.user.client.Window.scrollTo(com.google.gwt.user.client.Window.getScrollLeft(), 0);
	}
	
	private void hideExamples() {
		examplesList.clear();
		examplesPanel.addStyleName(Resources.INSTANCE.css().hide());
		showContent();
		backToListButton.setVisible(false);
		selectedExample = -1;
	}

	
}
