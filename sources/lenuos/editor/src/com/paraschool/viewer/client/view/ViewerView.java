package com.paraschool.viewer.client.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.Resource2ModuleObject;
import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.Page;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.TemplateDetails;
import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.api.client.ModuleObject;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.editor.api.client.ModulesStore;
import com.paraschool.editor.api.client.ViewModuleContext;
import com.paraschool.htmllist.client.HTMLList;
import com.paraschool.htmllist.client.HTMLListItem;
import com.paraschool.viewer.client.LocaleUtil;
import com.paraschool.viewer.client.Viewer;
import com.paraschool.viewer.client.event.PageLoadedEvent;

/*
 * Created at 16 août 2010
 * By bathily
 */
public class ViewerView extends Composite implements com.paraschool.viewer.client.presenter.ViewerPresenter.Display {

	interface ViewerViewUiBinder extends UiBinder<Widget, ViewerView> {}
	private static ViewerViewUiBinder uiBinder = GWT.create(ViewerViewUiBinder.class);
	
	interface CssResource extends com.google.gwt.resources.client.CssResource {
		String root();
		String active();
		@ClassName("pagination-list") String paginationList();
		String current();
		@ClassName("navigation-container") String navigationContainer();
		@ClassName("navigation-previous")	String navigationPrevious();
		@ClassName("navigation-next") String navigationNext();
		@ClassName("pages-container") String pagesContainer();
		String nopage();
	}
	
	private final EventBus eventBus;
	private final ModulesStore modulesStore;
	private Project project;
	private List<List<ModuleWidget>> widgets = new ArrayList<List<ModuleWidget>>();
	
	private List<String> addedTemplate = new ArrayList<String>();
	private LinkElement selectedTemplate = null;
	private int selected = -1;
	
	@UiField Panel root;
	@UiField FlowPanel pages;
	@UiField HTMLList pagination;
	@UiField Panel navigationContainer;
	@UiField Button next;
	@UiField Button previous;
	@UiField Button save;
	@UiField Button finish;
	@UiField CssResource css;
	
	@Inject
	private ViewerView(EventBus eventBus, ModulesStore modulesStore) {
		initWidget(uiBinder.createAndBindUi(this));
		this.eventBus = eventBus;
		this.modulesStore = modulesStore;
	}
	
	@Override
	public HasClickHandlers getNextPageButton() {
		return next;
	}

	@Override
	public HasClickHandlers getPreviousPageButton() {
		return previous;
	}
	
	private void setTemplate(TemplateDetails template) {
		//Il ya toujours un template selectionner. Voir addTemplate()
		if(selectedTemplate.getId().equals("template-"+template.getId()))
			return;
		
		HeadElement head = (HeadElement)Document.get().getElementsByTagName("head").getItem(0);
		NodeList<Element> links = head.getElementsByTagName("link");
		LinkElement link = null;
		for(int i=0 ; i < links.getLength() ; i++) {
			LinkElement temp = (LinkElement) links.getItem(i);
			if(temp.getId().equals("template-"+template.getId())){
				link = temp;
				break;
			}
		}
		selectedTemplate.setDisabled(true);
		selectLink(link);
	}
	
	private void selectLink(LinkElement link) {
		link.setDisabled(false);
		selectedTemplate = link;
	}
	

	@Override
	public void show(int selectedPage) {
		Page page = project.getPages().get(selectedPage);
		setTemplate(page.getTemplateDetails());
		if(selected >= 0){
			pages.getWidget(selected).removeStyleName(css.active());
			pages.getWidget(selected).setVisible(false);
			pagination.getWidget(selected).removeStyleName(css.current());
		}
		
		selected = selectedPage;
		
		pages.getWidget(selected).addStyleName(css.active());
		pages.getWidget(selected).setVisible(true);
		pagination.getWidget(selected).addStyleName(css.current());
		
		if(selected > 0) 
			previous.setVisible(true);
		else
			previous.setVisible(false);
		
		if(selected < project.getPages().size()-1)
			next.setVisible(true);
		else
			next.setVisible(false);
		
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void clear() {
		pagination.clear();
		pages.clear();
		this.project = null;
	}

	@Override
	public void setProject(Project project, ArrayList<ArrayList<String>> data) {
		clear();
		this.project = project;
		
		if(project != null)
			initPagesView(data);
		
	}

	private void initPagesView(final ArrayList<ArrayList<String>> data) {
		final List<Page> pages = project.getPages();
		final int length = pages.size();
		
		if(length > 0) {
			addPage(pages.get(0), 0, data != null ? data.get(0) : null);
			root.removeStyleName(css.nopage());
		
			if(length > 1)
				Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
					int i = 1;
					@Override
					public boolean execute() {
						addPage(pages.get(i), i, data != null ? data.get(i) : null);
						eventBus.fireEvent(new PageLoadedEvent(i));
						return ++i < length;
					}
				});
			eventBus.fireEvent(new PageLoadedEvent(0));
		}
	}

	private void addTemplate(Page page, int index) {
		TemplateDetails template = page.getTemplateDetails();
		
		if(addedTemplate.contains(template.getId()))
			return;
		
		String locale = "";
		Set<String> supportedLocales = template.getLocales();
		
		if(supportedLocales != null) {
			List<String> candidateLocales = LocaleUtil.getCandidateLocales(LocaleInfo.getCurrentLocale().getLocaleName());
			for(String l : candidateLocales) {
				if(supportedLocales.contains(l)){
					locale = l;
					break;
				}
			}
		}
		
		HeadElement head = (HeadElement)Document.get().getElementsByTagName("head").getItem(0);
		LinkElement link  = Document.get().createLinkElement();
		link.setId("template-"+template.getId());
		link.setDisabled(true);
		// Si c'est la 1ère page, il y a des chances que ce soit celle qui sera afficher. On preload le template
		if(index == 0)
			selectLink(link);
		link.setRel("stylesheet");
		link.setHref("templates/"+template.getId()+"/"+locale+"/main.css");
		head.appendChild(link);
		addedTemplate.add(template.getId());
	}
	
	private void addPage(Page page, int index, final ArrayList<String> datas) {
		
		PagePanel pagePanel = new PagePanel();
		addTemplate(page, index);
		//pagePanel.getEnonce().setText(page.getStatement() != null ? page.getStatement() : "");
		
		ArrayList<Interactivity> interactivities = page.getInteractivities();
		int i=0;
		for(final Interactivity interactivity : interactivities) {
			List<ModuleWidget> subWidgets = new ArrayList<ModuleWidget>();
			final String data = datas != null ? datas.get(i) : null;
			for(EditorModule module : modulesStore.getModules()) {
				if(module.getDescriptor().getId().equals(interactivity.getId())){
					ModuleWidget moduleWidget = module.newWidget();
					if(moduleWidget != null) {
						pagePanel.addInteractivity(module.getDescriptor().getId(), moduleWidget.viewWidget(new ViewModuleContext() {
							@Override
							public String getData() {
								return interactivity.getContent();
							}
							@Override
							public ModuleObject getObject(String id) {
								if(id == null) return null;
								Resource resource = project.getResources().get(id);
								ModuleObject object = Resource2ModuleObject.toModuleObject(resource);
								object.setUrl(URL.encode(Viewer.makeURL(object.getUrl())));
								return object;
							}
							@Override
							public String getSavedData() {
								return data;
							}
						}));
						subWidgets.add(moduleWidget);
					}
					break;
				}
			}
			i++;
			widgets.add(subWidgets);
		}
		
		addPagination();
		pagePanel.setVisible(false);
		pages.add(pagePanel);
	}
	
	private void addPagination() {
		HTMLListItem item = new HTMLListItem();
		InlineLabel label = new InlineLabel((pagination.getWidgetCount()+1)+"");
		item.add(label);
		pagination.add(item);
	}

	@Override
	public HasClickHandlers getPagination() {
		return pagination;
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return save;
	}
	
	@Override
	public HasClickHandlers getFinishButton() {
		return finish;
	}

	@Override
	public int getPaginationClickedIndex(ClickEvent event) {
		return pagination.getIndexForEvent(event);
	}

	@Override
	public List<List<ModuleWidget>> getWidgets() {
		return widgets;
	}


}
