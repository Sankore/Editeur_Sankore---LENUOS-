package com.paraschool.editor.client.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.Resource;
import com.paraschool.editor.api.client.TextEditCallback;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.AlohaTextEditor;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.event.interactivity.AddInteractivityEvent;
import com.paraschool.editor.client.event.interactivity.InteractivityEvent;
import com.paraschool.editor.client.event.interactivity.InteractivityEventHandler;
import com.paraschool.editor.client.event.interactivity.RemoveInteractivityEvent;
import com.paraschool.editor.client.event.page.ExportPageEvent;
import com.paraschool.editor.client.event.page.RemovePageEvent;
import com.paraschool.editor.client.event.page.UploadedResourceEvent;
import com.paraschool.editor.client.i18n.LocalizableResource;
import com.paraschool.editor.client.presenter.factory.InteractivityListPresenterFactory;
import com.paraschool.editor.client.presenter.factory.InteractivityPresenterFactory;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;
import com.paraschool.editor.client.view.EditorPageContentPanel;
import com.paraschool.editor.client.view.PageContentPanel;
import com.paraschool.editor.client.view.PageMenu;
import com.paraschool.editor.shared.SampleDetails;
/*
 * Created at 8 juil. 2010
 * By Didier Bathily
 */
public class PagePresenter extends DefaultPresenter implements MenuHandler, DragHandler, TextEditCallback {

	private static final Logger logger = Logger.getLogger(PagePresenter.class.getName());
	
	public interface Display extends DisplayWithMenu {
		HasWidgets getSubViewContainer();
		PageContentPanel getInteractivitiesContainer();
	}
	
	private final Page page;
	private final Project project;
	private final HashMap<Interactivity, ModulePresenter> presenters;
	
	private final InteractivityListPresenterFactory interactivityListPresenterFactory;
	private final InteractivityPresenterFactory interactivityPresenterFactory;
	private final RpcActionFactory rpcActionFactory;
	private final LocalizableResource resources;
	
	
	private int dragSource, dragDestination;
	
	@Inject
	//TODO Use Gin assisted (and remove injector in constructor)
	public PagePresenter(@Assisted Page page, @Assisted Project project, EditorServiceAsync editorRpcService, ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, RpcActionFactory rpcActionFactory, Display display, LocalizableResource resources,
			InteractivityListPresenterFactory interactivityListPresenterFactory,InteractivityPresenterFactory interactivityPresenterFactory) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, display);
		
		this.page = page;
		this.project = project;
		this.rpcActionFactory = rpcActionFactory;
		this.resources = resources;
		this.interactivityListPresenterFactory = interactivityListPresenterFactory;
		this.interactivityPresenterFactory = interactivityPresenterFactory;
		
		presenters = new HashMap<Interactivity, ModulePresenter>();
	}
	
	@Override
	protected void bind() {
		bindMenu(((Display)display).getMenu());
		AlohaTextEditor.edit(((Display)display).getInteractivitiesContainer().getEnonce(), this);
	}
	
	public void bindMenu(Menu menu) {
		final PageMenu pageMenu = (PageMenu) menu;
		
		registrations.add(
			pageMenu.getAddInteractivityButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					interactivityListPresenterFactory.create(page).go(((Display)display).getSubViewContainer());
				}
			})
		);
		
		registrations.add(
				pageMenu.getExportButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						eventBus.fireEvent(new ExportPageEvent(page));
					}
				})
			);
		
		registrations.add(
			pageMenu.getDeleteButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					display.asWidget().removeFromParent();
					eventBus.fireEvent(new RemovePageEvent(page));
				}
			})
		);
		
		registrations.add(
			eventBus.addHandler(InteractivityEvent.TYPE, new InteractivityEventHandler() {
				public void onRemoveinteractivity(RemoveInteractivityEvent event) {
					Page p = event.getPage();
					if(page == p) {
						removeInteractivity(event.getInteractivity());
					}
				}
				public void onAddinteractivity(AddInteractivityEvent event) {
					Page p = event.getPage();
					if(page == p) {
						addInteractivity(event.getInteractivity(), event.getSample());
					}
				}
			})	
		);
	}
	
	protected void addInteractivity(Interactivity i, SampleDetails sample) {
		if(sample == null){
			showInteractivity(i);
			page.addInteractivity(i);
		}else{
			ModulePresenter presenter = prepareModulePresenter();
			presenters.put(i, presenter);
			installSample(i, sample, presenter);
		}
			
	}
	
	protected void removeInteractivity(Interactivity i) {
		page.removeInteractivity(i);
		ModulePresenter presenter = presenters.get(i);
		presenter.clear();
		presenters.remove(i);
		if(page.getInteractivities().size() == 0) {
			((EditorPageContentPanel)((Display)display).getInteractivitiesContainer()).showEnonce(false);
		}
	}
	
	protected ModulePresenter prepareModulePresenter() {
		if(page.getInteractivities().size() > 0) {
			((EditorPageContentPanel)((Display)display).getInteractivitiesContainer()).showEnonce(true);
		}
		ModulePresenter presenter = interactivityPresenterFactory.create(page, project);
		presenter.go(((Display)display).getInteractivitiesContainer().getInteractivityPanel());
		return presenter;
	}
	
	protected void showInteractivity(Interactivity i) {
		ModulePresenter presenter = prepareModulePresenter();
		presenters.put(i, presenter);
		presenter.setInteractivity(i);
	}
	
	public void save() {
		for(ModulePresenter presenter : presenters.values()) {
			presenter.save();
		}
	}
	
	@Override
	public void clear() {
		super.clear();
		for(ModulePresenter presenter : presenters.values())
			presenter.clear();
	}

	@Override
	public void go(final HasWidgets container) {
		bind();
		container.add(display.asWidget());
		
		String statement = page.getStatement();
		if(statement == null) statement = resources.emptyStatement();
		((Display)display).getInteractivitiesContainer().getEnonce().setHTML(statement);
		
		final List<Interactivity> interactivities = page.getInteractivities();
		if(interactivities.size() > 0)
			Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
					private int i = 0;
					@Override
					public boolean execute() {
						Interactivity interactivity = interactivities.get(i);
						showInteractivity(interactivity);
						return ++i < interactivities.size();
					}
				}
			);
		
		if(((Display)display).getInteractivitiesContainer().getInteractivityPanel() instanceof com.paraschool.editor.client.view.EditorInteractivityPanel){
			((com.paraschool.editor.client.view.EditorInteractivityPanel)((Display)display).getInteractivitiesContainer().getInteractivityPanel()).getDragController().addDragHandler(this);
		}
		
	}


	@Override
	public void onDragEnd(DragEndEvent event) {
		DragContext context = event.getContext(); 
		dragDestination = DOM.getChildIndex(context.draggable.getParent().getElement(), context.draggable.getElement());
		if(dragDestination != dragSource)
			moveInteractivity(dragSource, dragDestination);
	}

	@Override
	public void onDragStart(DragStartEvent event) {
		DragContext context = event.getContext(); 
		dragSource = DOM.getChildIndex(context.draggable.getParent().getElement(), context.draggable.getElement());
	}

	private void moveInteractivity(int from, int to) {
		Interactivity fromInteractivity = page.getInteractivities().get(from);
		Interactivity toInteractivity = page.getInteractivities().get(to);
		page.getInteractivities().set(from, toInteractivity);
		page.getInteractivities().set(to, fromInteractivity);
	}
	
	@Override
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {}
	@Override
	public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {}

	@Override
	public void onEdit() {
		String result = ((Display)display).getInteractivitiesContainer().getEnonce().getHTML();
		
		if(result == null || (result = result.trim()).length() == 0){
			((Display)display).getInteractivitiesContainer().getEnonce().setHTML(resources.emptyStatement());
		}
		
	}
	
	private void installSample(final Interactivity i, final SampleDetails sampleDetails, final ModulePresenter presenter) {
		logger.fine("Install sample "+sampleDetails.getId()+" for module "+sampleDetails.getModule());
		AsyncCallback<Map<String, Resource>> callback = new AsyncCallback<Map<String, Resource>>() {
			private void failed() {
				getSampleContent(i, sampleDetails, presenter);
			}
			@Override
			public void onFailure(Throwable caught) {
				logger.info(caught.getMessage());
				failed();
			}
			@Override
			public void onSuccess(Map<String, Resource> result) {
				logger.fine("Sample files installation succeed.");
				for(Resource r : result.values()) {
					eventBus.fireEvent(new UploadedResourceEvent(r));
				}
				getSampleContent(i, sampleDetails, presenter);
			}
		};
		rpcActionFactory.<Map<String, Resource>>create(callback, new RpcAttempt<Map<String, Resource>>() {
			@Override
			public void call(AsyncCallback<Map<String, Resource>> callback) {
				editorRpcService.installSampleInProject(project.getDetails(), sampleDetails, callback);
			}
		}).attempt();
	}

	private void getSampleContent(final Interactivity i, final SampleDetails sampleDetails, final ModulePresenter presenter) {
		logger.fine("Get content for sample "+sampleDetails.getId());
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			private void failed() {
				page.addInteractivity(i);
				presenter.setInteractivity(i);
			}
			@Override
			public void onFailure(Throwable caught) {
				logger.info(caught.getMessage());
				failed();
			}
			@Override
			public void onSuccess(String result) {
				logger.fine("Get content succeed.");
				i.setContent(result);
				page.addInteractivity(i);
				presenter.setInteractivity(i);
			}
		};
		rpcActionFactory.<String>create(callback, new RpcAttempt<String>() {
			@Override
			public void call(AsyncCallback<String> callback) {
				editorRpcService.getSampleContent(sampleDetails, callback);
			}
		}).attempt();
	}
}
