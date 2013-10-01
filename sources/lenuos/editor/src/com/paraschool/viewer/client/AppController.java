package com.paraschool.viewer.client;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Project;
import com.paraschool.viewer.client.event.ChangePageEvent;
import com.paraschool.viewer.client.event.ChangePageEventHandler;
import com.paraschool.viewer.client.event.ChangePageToIndexEvent;
import com.paraschool.viewer.client.event.FinishViewerEvent;
import com.paraschool.viewer.client.event.PageLoadedEvent;
import com.paraschool.viewer.client.event.PagesLoadedEventHandler;
import com.paraschool.viewer.client.event.SaveViewerEvent;
import com.paraschool.viewer.client.event.ViewerEvent;
import com.paraschool.viewer.client.event.ViewerEventHandler;
import com.paraschool.viewer.client.presenter.ViewerPresenter;

/*
 * Created at 17 août 2010
 * By bathily
 */
public class AppController implements ValueChangeHandler<String> {
	
	private static final Logger logger = Logger.getLogger(Viewer.class.getName());
	private final HostAPI api = GWT.create(HostAPI.class);
	private final EventBus eventBus;
	private final ViewerPresenter presenter;
	private Project project;
	private int selectedPage = 0;
	
	@Inject
	private AppController(EventBus eventBus, ViewerPresenter presenter) {
		this.eventBus = eventBus;
		this.presenter = presenter;
	}
	
	protected void bind() {
		eventBus.addHandler(ChangePageEvent.TYPE, new ChangePageEventHandler() {
			
			@Override
			public void previous(ChangePageEvent event) {
				if(selectedPage > 0)
					History.newItem((selectedPage)+"");
			}
			
			@Override
			public void next(ChangePageEvent event) {
				if(selectedPage < project.getPages().size() - 1)
					History.newItem((selectedPage+2)+"");
			}

			@Override
			public void to(ChangePageToIndexEvent event) {
				int index = event.getDestination();
				if(index >= 0 && index < project.getPages().size())
					History.newItem((index+1)+"");
			}
		});
		
		
		eventBus.addHandler(ViewerEvent.TYPE, new ViewerEventHandler() {
			
			@Override
			public void onSave(SaveViewerEvent event) {
				ArrayList<ArrayList<String>> datas = presenter.getAllPageResultDatas();
				api.save(null, null, datas);
			}
			
			@Override
			public void onFinish(FinishViewerEvent event) {
				ArrayList<ArrayList<String>> datas = presenter.getAllPageResultDatas();
				api.finish(null, null, datas);
			}
		});
		
		History.addValueChangeHandler(this);
	}
	
	public void view(Project project) {
		logger.fine("App controller loaded");
		
		this.project = project;
		bind();
		final int start = getIndexFromHistoryToken(History.getToken());
		
		eventBus.addHandler(PageLoadedEvent.TYPE, new PagesLoadedEventHandler() {
			@Override
			public void onPageLoaded(PageLoadedEvent event) {		
				logger.fine("Page "+event.getIndex()+" loaded");
				if(event.getIndex() == start){
					logger.fine("Start it.");
					History.newItem((start+1)+"",false); // Pour éviter le rechargement de la page lorsqu'on passe de "" à "#1"
					History.fireCurrentHistoryState();
				}
					
			}
		});
		
		presenter.setProject(project, api.getSaveData());
		presenter.go(RootPanel.get());
		
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue();
		int page = getIndexFromHistoryToken(token);
		if(page >= 0 && page < project.getPages().size()) {
			showPage(page);
		}else{
			History.newItem("");
		}
	}
	
	private int getIndexFromHistoryToken(String token) {
		int page = 0;
		try{
			page = Integer.parseInt(token) - 1;
		}catch (NumberFormatException e) {
			if("last".equalsIgnoreCase(History.getToken()))
				page = project.getPages().size() - 1;
		}
		return page;
	}
	
	private void showPage(int page) {
		selectedPage = page;
		presenter.showPage(page);
	}
}
