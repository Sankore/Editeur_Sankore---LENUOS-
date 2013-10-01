package com.paraschool.viewer.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.presenter.AbstractPresenter;
import com.paraschool.commons.client.presenter.Presenter;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.api.client.ModuleWidget;
import com.paraschool.viewer.client.event.ChangePageToIndexEvent;
import com.paraschool.viewer.client.event.ChangePageToNextEvent;
import com.paraschool.viewer.client.event.ChangePageToPreviousEvent;
import com.paraschool.viewer.client.event.FinishViewerEvent;
import com.paraschool.viewer.client.event.SaveViewerEvent;

/*
 * Created at 16 août 2010
 * By bathily
 */
public class ViewerPresenter extends AbstractPresenter implements Presenter {

	public interface Display extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getPagination();
		HasClickHandlers getNextPageButton();
		HasClickHandlers getPreviousPageButton();
		HasClickHandlers getSaveButton();
		HasClickHandlers getFinishButton();
		List<List<ModuleWidget>> getWidgets();
		int getPaginationClickedIndex(ClickEvent event);
		void setProject(Project project, ArrayList<ArrayList<String>> data);
		void show(int page);
	}
	
	private Project project;
	private ArrayList<ArrayList<String>> saves;
	
	@Inject
	private ViewerPresenter(EventBus eventBus,Display display) {
		super(eventBus, display);
	}
	
	@Override
	protected void bind() {
		registrations.add(
				((Display)display).getNextPageButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						eventBus.fireEvent(new ChangePageToNextEvent());
					}
				})
		);
		
		registrations.add(
				((Display)display).getPreviousPageButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						eventBus.fireEvent(new ChangePageToPreviousEvent());
					}
				})
		);
		
		registrations.add(
				((Display)display).getPagination().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						int index = ((Display)display).getPaginationClickedIndex(event);
						if(index >= 0)
							eventBus.fireEvent(new ChangePageToIndexEvent(index));
					}
				})
		);
		
		registrations.add(
				((Display)display).getSaveButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						eventBus.fireEvent(new SaveViewerEvent());
					}
				})
		);
		
		registrations.add(
				((Display)display).getFinishButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						eventBus.fireEvent(new FinishViewerEvent());
					}
				})
		);
	}
	
	public void showPage(int pageIndex) {
		if( pageIndex >= 0 && pageIndex < project.getPages().size() ){
			((Display)display).show(pageIndex);
		}
	}
	
	public void setProject(Project project, ArrayList<ArrayList<String>> saves) {
		this.project = project;
		this.saves = saves;
	}

	@Override
	public void go(HasWidgets container) {
		if(project != null){
			Window.setTitle(project.getDetails().getName());
			((Display)display).setProject(project, saves);
		}
		super.go(container);
	}
	
	
	public ArrayList<ArrayList<String>> getAllPageResultDatas() {
		List<List<ModuleWidget>> widgets = ((Display)display).getWidgets();
		ArrayList<ArrayList<String>> datas = new ArrayList<ArrayList<String>>(widgets.size());
		for(int i=0 ; i<widgets.size() ; i++) {
			datas.add(i, getModulesResultData(widgets.get(i)));
		}
		return datas;
	}
	
	public ArrayList<String> getPageResultDatas(int pageIndex) {
		if( pageIndex >= 0 && pageIndex < project.getPages().size() ){
			List<ModuleWidget> modules = ((Display)display).getWidgets().get(pageIndex);
			return getModulesResultData(modules);
		}
		return null;
	}
	
	public ArrayList<String> getModulesResultData(List<ModuleWidget> modules) {
		ArrayList<String> datas = new ArrayList<String>(modules.size());
		for(int i=0 ; i<modules.size();i++){
			datas.add(i, modules.get(i).getResultData());
		}
		return datas;
	}
}
