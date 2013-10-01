package com.paraschool.editor.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.HasHeight;
import com.paraschool.commons.share.HasInfo;
import com.paraschool.commons.share.HasWidth;
import com.paraschool.commons.share.Info;
import com.paraschool.commons.share.LinkResource;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.ResourceUtils;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.event.project.ChangeResourceEvent;
import com.paraschool.editor.client.i18n.LocalizableResource;
import com.paraschool.editor.client.presenter.ResourceListPresenter.Display;

/*
 * Created at 13 juil. 2010
 * By Didier Bathily
 */
public class ResourceListView extends Window implements Display, ValueChangeHandler<String> {

	interface ResourceListViewCssResource extends CssResource {

		String resource();

		String uploadButton();
		String importButton();
		String catalogButton();
		String createLinkButton();
		
		String detail();
		String opened();
		String closed();
		String chooseButton();
		String head();
		String middle();
		String middleWrapper();
		String bottom();
		String viewContainer();
		String textContainer();
		String name();
		String technical();
		String more();
		String type();
		String description();
	}

	public interface Resources extends Window.Resources {
		Resources INSTANCE = GWT.create(Resources.class);

		@Source(value = { "css/Constants.css", "css/ResourceListView.css" })
		ResourceListViewCssResource css();
		
		@Source("images/btn_choixRess.png")
		ImageResource uploadButton();

		@Source("images/icn_navFive_importer.png")
		ImageResource importButton();

		@Source("images/icn_navFive_catalogue.png")
		ImageResource catalogButton();

		@Source("images/titleImg_mesRessources.png")
		ImageResource titleIcon();

		@Source("images/icn_btn_focus_ress.png")
		ImageResource chooseIcon();

		@Source("images/barre_btn_focus_ress.png")
		ImageResource chooseButton();

		@Source("images/bg_haut_focus_ress.png")
		ImageResource head();

		@Source("images/bg_mil_focus_ress.png")
		@ImageOptions(repeatStyle = RepeatStyle.Vertical)
		ImageResource middle();

		@Source("images/bg_bas_focus_ress.png")
		@ImageOptions(repeatStyle = RepeatStyle.Vertical)
		ImageResource bottom();

		@Source("images/icon-picture.png")
		ImageResource defaultImageThumbnail();

		@Source("images/icon-video.png")
		ImageResource defaultVideoThumbnail();

		@Source("images/icon-swf.png")
		ImageResource defaultAnimationThumbnail();
		
		@Source("images/icon-audio.png")
		ImageResource defaultAudioThumbnail();

		@Source("images/icon-document.png")
		ImageResource defaultPDFThumbnail();

		@Source("images/icon-document.png")
		ImageResource defaultDocumentThumbnail();
		
		@Source("images/icon-link.png")
		ImageResource defaultLinkThumbnail();

		@Source("images/icon.png")
		ImageResource otherResourceThumbnail();

	}

	{
		Resources.INSTANCE.css().ensureInjected();
	}

	final Project project;
	final EventBus eventBus;
	final WindowListContent listResources;
	final FiltersView filters;
	final ResourceDetailView details;
	final ResourceWindowBar buttonBar;
	final Button deleteButton;

	int selectedIndex = -1;
	int selectedFilterIndex = -1;
	ThumbnailListItem selected = null;
	List<Resource> data;

	@Inject
	private ResourceListView(Project project, EventBus eventBus, 
			WindowContent content, WindowListContent listResources, 
			ResourceWindowBar buttonBar, ResourceListFiltersView filters, LocalizableResource resources) {
		super(content);
		
		this.project = project;
		this.eventBus = eventBus;
		
		deleteButton = new Button();
		deleteButton.setVisible(false);
		addToContent(deleteButton);

		content.leftContent.addStyleName(Resources.INSTANCE.css().resource());
		setWindowImageUrl(Resources.INSTANCE.titleIcon().getURL());
		setWindowTitle(resources.resourcesWindowLabel());
		setWindowDescription(resources.resourcesWindowDescription());

		this.filters = filters;
		addToLeftContent(filters);
		
		this.listResources = listResources;
		listResources.list.addStyleName(Resources.INSTANCE.appCss().clearfix());
		addToContent(listResources);
		details = new ResourceDetailView(project.getDetails());

		this.buttonBar = buttonBar;
		addToRightBar(buttonBar);
		buttonBar.createLinkButton.setVisible(false);

		details.chooseButton.setVisible(false);

		listResources.list.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {				
				int index = -1;
				if ((index = getClicked(event)) > -1) {
					Widget w = ResourceListView.this.listResources.list.getWidget(index);
					if(w instanceof ThumbnailListItem){
						unselect();
						setSelectedIndex(index);
						select((ThumbnailListItem)w);
					}
				}else
					unselect();
			}
		});

		details.width.addValueChangeHandler(this);
		details.height.addValueChangeHandler(this);
	}
	
	private void setSelectedIndex(int index) {
		int detailIndex = ResourceListView.this.listResources.list.getIndex(details);
		selectedIndex = index - (detailIndex > 0 && detailIndex < index ? 1 : 0);
	}

	private void unselect() {
		details.removeFromParent();
		details.removeStyleName(Resources.INSTANCE.css().opened());
		details.addStyleName(Resources.INSTANCE.css().closed());
		details.setResource(null);
		if (selected != null)
			selected.removeStyleName(ThumbnailListItem.Resources.INSTANCE.css()
					.selected());
		selectedIndex = -1;
		selected = null;
	}

	private void select(ThumbnailListItem item) {
		selected = item;
		selected.addStyleName(ThumbnailListItem.Resources.INSTANCE.css()
				.selected());
		
		listResources.list.insert(details, getDetailViewInsertIndex());
		details.removeStyleName(Resources.INSTANCE.css().closed());
		details.addStyleName(Resources.INSTANCE.css().opened());
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				if(selectedIndex>=0 && selectedIndex < data.size())
					details.setResource((Resource) data.toArray()[selectedIndex]);
			}
		});
	}
	
	private int getDetailViewInsertIndex() {
		
		if(selectedIndex == listResources.list.getWidgetCount() - 1)
			return selectedIndex+1;
		else{
			int row = (selectedIndex / 5);
			return ((5 * (row + 1)) < listResources.list.getWidgetCount() ?  5 * (row + 1) : listResources.list.getWidgetCount());
		}
			
	}

	private String getDefaultThumbnail(Resource resource) {
		String thumbnail = Resources.INSTANCE.otherResourceThumbnail().getURL();
		String type = resource.getMimetype();
		if (ResourceUtils.isImage(type))
			thumbnail = Resources.INSTANCE.defaultImageThumbnail().getURL();
		else if (ResourceUtils.isVideo(type))
			thumbnail = Resources.INSTANCE.defaultVideoThumbnail().getURL();
		else if (ResourceUtils.isSWF(type))
			thumbnail = Resources.INSTANCE.defaultAnimationThumbnail().getURL();
		else if (ResourceUtils.isAudio(type))
			thumbnail = Resources.INSTANCE.defaultAudioThumbnail().getURL();
		else if (ResourceUtils.isDocument(type))
			thumbnail = Resources.INSTANCE.defaultDocumentThumbnail().getURL();
		else if (ResourceUtils.isPDF(type))
			thumbnail = Resources.INSTANCE.defaultPDFThumbnail().getURL();
		else if(resource instanceof LinkResource)
			thumbnail = Resources.INSTANCE.defaultLinkThumbnail().getURL();
		return thumbnail;
	}

	@Override
	public HasClickHandlers getCloseButton() {
		return content.closeButton;
	}

	@Override
	public int getClicked(ClickEvent event) {
		return listResources.list.getIndexForEvent(event);
	}

	@Override
	public HasClickHandlers getList() {
		return listResources.list;
	}

	@Override
	public void setData(List<Resource> data) {
		content.loadingIndicator.setVisible(false);
		listResources.list.clear();
		addData(data);
	}

	@Override
	public void addData(List<Resource> data) {
		for (Resource res : data) {
			addData(res);
		}
		this.data = data;
	}

	@Override
	public void addData(Resource res) {
		String thumbnail = AppUtil.makeInProjectURL(project.getDetails(), res.getThumbnail());
		if (thumbnail == null)
			thumbnail = getDefaultThumbnail(res);

		ThumbnailListItem item = new ThumbnailListItem(thumbnail,
				res.getName());
		item.closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				unselect();
				setSelectedIndex(listResources.list.getIndexForEvent(event));
				deleteButton.click();
			}
		});
		
		int count = listResources.list.getWidgetCount();
		int detailIndex = listResources.list.getIndex(details);
		if(detailIndex >0 && detailIndex == count -1 && detailIndex%5 != 0)
			listResources.list.insert(item, count-1);
		else
			listResources.list.add(item);
	}

	@Override
	public void removeSelected() {
		listResources.list.remove(selectedIndex);
		unselect();
	}

	@Override
	public com.paraschool.editor.client.presenter.ResourceUploadPresenter.Display getUploadDisplay() {
		return buttonBar.uploader;
	}

	@Override
	public Button getChooseButton() {
		return details.chooseButton;
	}

	@Override
	public int getSelectedIndex() {
		GWT.log(selectedIndex+"");
		return selectedIndex;
	}

	@Override
	public void setCanShowChooseButton(boolean canShow) {
		details.chooseButton.setVisible(canShow);
	}

	@Override
	public HasClickHandlers getDeleteButton() {
		return deleteButton;
	}

	@Override
	public HasClickHandlers getFilters() {
		return filters.list;
	}

	@Override
	public int getClickedFilter(ClickEvent event) {
		int index = filters.list.getIndexForEvent(event); 
		return index == selectedFilterIndex ? -1 : index;
	}

	@Override
	public void setSelectedFilter(int index) {
		
		if (index > -1) {
			filters.select(index);
			if(selectedFilterIndex != -1){
				filters.unselect(selectedFilterIndex);
			}
			selectedFilterIndex = index;
			if(selectedFilterIndex == 6) {
				showLinkButton(true);
			}else
				showLinkButton(false);
		}
	}
	
	private void showLinkButton(boolean show) {
		buttonBar.uploader.setVisible(!show);
		buttonBar.createLinkButton.setVisible(show);
	}

	@Override
	public Button getCreateLinkButton() {
		return buttonBar.createLinkButton;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		int newvalue = -1;
		try{
			newvalue = Integer.parseInt(event.getValue());
		}catch (NumberFormatException e) {}
		
		if(newvalue >-1 && selectedIndex >= 0 && selectedIndex < data.size()){
			final Resource r = data.get(selectedIndex);
			boolean hasChange = false;
			
			if(r instanceof HasInfo){
				Info info = ((HasInfo<?>)r).getInfo();
				
				if(info == null){
					info = ResourceUtils.build((HasInfo<?>)r);
					((HasInfo) r).setInfo(info);
				}
				
				if(event.getSource().equals(details.width) && info instanceof HasWidth){
					HasWidth hasWidth = ((HasWidth)info);
					hasChange = hasWidth.getWidth() != newvalue;
					hasWidth.setWidth(newvalue);
					
				}else if(info instanceof HasHeight){
					HasHeight hasHeight = ((HasHeight)info);
					hasChange = hasHeight.getHeight() != newvalue;
					hasHeight.setHeight(newvalue);
				}
					
			}
			
			if(hasChange){
				details.setResource(r);
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						eventBus.fireEvent(new ChangeResourceEvent(r));
					}
				});
			}
				
		}
	}
}
