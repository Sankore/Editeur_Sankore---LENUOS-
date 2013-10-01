package com.paraschool.editor.client.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.Collections;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.Predicate;
import com.paraschool.commons.client.Predicates;
import com.paraschool.commons.client.Resource2ModuleObject;
import com.paraschool.commons.share.AudioResource;
import com.paraschool.commons.share.DocumentResource;
import com.paraschool.commons.share.ImageResource;
import com.paraschool.commons.share.LinkResource;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.SWFResource;
import com.paraschool.commons.share.VideoResource;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ObjectEditCallback;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.AppUtil;
import com.paraschool.editor.client.ResourceFilterPredicate;
import com.paraschool.editor.client.ResourceStore;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.event.page.DeletedResourceEvent;
import com.paraschool.editor.client.event.page.UploadedResourceEvent;
import com.paraschool.editor.client.event.project.ChangeResourceEvent;
import com.paraschool.editor.client.event.project.ResourceEvent;
import com.paraschool.editor.client.event.project.ResourceEventHandler;
import com.paraschool.editor.client.presenter.factory.ResourceUploadPresenterFactory;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 13 juil. 2010
 * By Didier Bathily
 */
public class ResourceListPresenter extends DefaultPresenter implements ResourceStore {

	private static final Logger logger = Logger.getLogger(ResourceListPresenter.class.getName());
	
	public interface Display extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getCloseButton();
		HasClickHandlers getDeleteButton();
		
		Button getChooseButton();
		Button getCreateLinkButton();
		HasClickHandlers getList();
		HasClickHandlers getFilters();
		
		com.paraschool.editor.client.presenter.ResourceUploadPresenter.Display getUploadDisplay();
		
		void show();
		void hide();
		
		int getClicked(ClickEvent event);
		int getClickedFilter(ClickEvent event);
		
		int getSelectedIndex();
		
		void setCanShowChooseButton(boolean canShow);
		void setData(List<Resource> data);
		void addData(List<Resource> data);
		void addData(Resource data);
		void removeSelected();
		
		void setSelectedFilter(int index);
	}

	private static final Predicate<Resource> imagePredicate = Predicates.not(new ResourceFilterPredicate(ImageResource.class)); 
	private static final Predicate<Resource> videoPredicate = Predicates.not(new ResourceFilterPredicate(VideoResource.class));
	private static final Predicate<Resource> audioPredicate = Predicates.not(new ResourceFilterPredicate(AudioResource.class));
	private static final Predicate<Resource> swfPredicate = Predicates.not(new ResourceFilterPredicate(SWFResource.class));
	private static final Predicate<Resource> docPredicate = Predicates.not(new ResourceFilterPredicate(DocumentResource.class));
	private static final Predicate<Resource> linkPredicate = Predicates.not(new ResourceFilterPredicate(LinkResource.class));
	
	@SuppressWarnings("unchecked")
	private static final Predicate<Resource> otherPredicate = Predicates.not(Predicates.and(Arrays.asList(
		imagePredicate, videoPredicate, audioPredicate, swfPredicate, docPredicate, linkPredicate
	)));
	
	
	private final Project project;
	
	private ResourceUploadPresenter uploadPresenter;
	private List<Resource> resources;
	private List<Resource> unfilteredResources;
	private ArrayList<Predicate<Resource>> predicates;
	
	@Inject private RpcActionFactory rpcActionFactory;
	@Inject ResourceUploadPresenterFactory uploadPresenterFactory;
	
	@Inject
	public ResourceListPresenter(Project project, ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, Display display) {
		super(null, projectRpcService, eventBus, ajaxHandler, display);
		
		logger.fine("Init resource store");
		
		this.project = project;
		predicates = new ArrayList<Predicate<Resource>>();
		unfilteredResources = Ordering.natural().onResultOf(new Function<Resource, String>() {
			@Override
			public String apply(Resource r) {
				return r.getName().toLowerCase();
			}
		}).sortedCopy(project.getResources().values());
		
		bindResourceChangeEvent();
		
		((Display)display).setCanShowChooseButton(false);
	}

	private void close() {
		((Display)display).hide();
		super.clear();
	}
	
	protected void bindResourceChangeEvent() {
		logger.fine("Bind resource change event");
		
		eventBus.addHandler(ResourceEvent.TYPE, new ResourceEventHandler() {

			public void onUploaded(UploadedResourceEvent event) {
				Resource resource = event.getResource();
				if(logger.isLoggable(Level.FINE))
					logger.fine("Receive new UploadResourceEvent. Add the resource ["+resource.getUrl()+"] with id ["+resource.getId()+"] to the resources");
				
				unfilteredResources.add(resource);
				if(!isFiltered(resource) && resources != null) {
					resources.add(resource);
					((Display)display).addData(resource);
				}
			}

			public void onDeleted(DeletedResourceEvent event) {
				Resource resource = event.getResource();
				unfilteredResources.remove(resource);
				resources.remove(resource);
				((Display)display).removeSelected();
		
			}

			@Override
			public void onChange(ChangeResourceEvent event) {
				Resource resource = event.getResource();
				for(int i=0 ; i<unfilteredResources.size(); i++) {
					if(unfilteredResources.get(i).getId().equals(resource.getId())){
						unfilteredResources.set(i, resource);
						break;
					}
				}
				
			}
		});
			
	}
	
	@Override
	protected void bind() {
		
		registrations.add(
			((Display)display).getCloseButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					close();
				}
			})
		);

		registrations.add(
			((Display)display).getDeleteButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					final int index = ((Display)display).getSelectedIndex();
					if(index > -1){
						deleteResource(index);
					}
				}
			})
		);
		
		registrations.add(
			((Display)display).getFilters().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					int index = ((Display)display).getClickedFilter(event);
					if(index != -1){
						predicates.clear();
						switch (index) {
						case 1:
							addFilter(imagePredicate);
							break;
						case 2:
							addFilter(videoPredicate);
							break;
						case 3:
							addFilter(swfPredicate);
							break;
						case 4:
							addFilter(audioPredicate);
							break;
						case 5:
							addFilter(docPredicate);
							break;
						case 6:
							addFilter(linkPredicate);
							break;
						case 7:
							addFilter(otherPredicate);
							break;
						default:
							break;
						}
						filterResource();
						((Display)display).setSelectedFilter(index);
					}
				}
			})
		);

		registrations.add(
			Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

				public void onPreviewNativeEvent(NativePreviewEvent preview) {
					NativeEvent event = preview.getNativeEvent();
					int keycode = event.getKeyCode();
					if(keycode == KeyCodes.KEY_DELETE) {
						final int index = ((Display)display).getSelectedIndex();
						if(index > -1){
							preview.consume();
							deleteResource(index);
						}

					}
				}
			})
		);
		
		registrations.add(
				((Display)display).getCreateLinkButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						String url = Window.prompt("L'url", "http://");
						if(url != null)
							addResource(url);
					}
				})
			);
	}
	
	private void addResource(final String url) {
		AsyncCallback<Resource> callback = new AsyncCallback<Resource>() {
			@Override
			public void onFailure(Throwable caught) {}

			@Override
			public void onSuccess(Resource result) {
				eventBus.fireEvent(new UploadedResourceEvent(result));
			}
		};

		rpcActionFactory.<Resource>create(callback, new RpcAttempt<Resource>() {
			@Override
			public void call(AsyncCallback<Resource> callback) {
				ProjectDetails details = project.getDetails();
				projectRpcService.addResource(details, new LinkResource(url, url, url, null, null, details.getLocale()), callback);
			}
		}).attempt();
	}
	
	private void deleteResource(final int index) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Boolean result) {
				if(result) {
					eventBus.fireEvent(new DeletedResourceEvent(resources.get(index)));
				}
			}
		};
		
		rpcActionFactory.<Boolean>create(callback, new RpcAttempt<Boolean>() {
			@Override
			public void call(AsyncCallback<Boolean> callback) {
				projectRpcService.removeResource(project.getDetails(),
						resources.get(index).details(), callback);
			}
			
		}).attempt();
	}

	private void filterResource() {
		resources = Collections.filter(unfilteredResources, Predicates.or(predicates)) ;
		((Display)display).setData(resources);
	}
	
	private boolean isFiltered(Resource resource) {
		for(Predicate<Resource> predicate : predicates) {
			if(predicate.apply(resource))
				return true;
		}
		return false;
	}
	
	private void addFilter(Predicate<Resource> predicate) {
		if(!predicates.contains(predicate)){
			predicates.add(predicate);
		}
	}
	
	public void removeFilter(Predicate<Resource> predicate) {
		predicates.remove(predicate);
	}

	/* (non-Javadoc)
	 * @see com.paraschool.editor.client.presenter.ResourceStore#getAnObject(com.paraschool.editor.api.client.ObjectEditCallback, com.paraschool.editor.api.client.ModuleObject.Type)
	 */
	@Override
	public void get(final ObjectEditCallback callback, final ModuleObject.Type ...types) {	
		if(callback != null) {
			((Display)display).setCanShowChooseButton(true);
			registrations.add(
					((Display)display).getChooseButton().addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							int selected = ((Display)display).getSelectedIndex();
							if(selected >= 0){
								Resource resource = resources.get(selected);
								ModuleObject object = Resource2ModuleObject.toModuleObject(resource);
								object.setUrl(AppUtil.makeInProjectURL(project.getDetails(), object.getUrl()));
								callback.onEdit(object);
								close();
							}
						}
					})
			);
		}else
			((Display)display).setCanShowChooseButton(false);
		
		go(null);
	}
	
	@Override
	public void go(final HasWidgets container) {
		
		if(uploadPresenter == null) {
			uploadPresenter = uploadPresenterFactory.create(((Display)display).getUploadDisplay());
			uploadPresenter.go(null);
			
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					filterResource();
				}
			});
		}
		
		super.go(null);
		
		((Display)display).show();
	}
	
	@Override
	public void clear() {
		super.clear();
		if(uploadPresenter != null){
			uploadPresenter.clear();
			uploadPresenter = null;
		}
	}
}
