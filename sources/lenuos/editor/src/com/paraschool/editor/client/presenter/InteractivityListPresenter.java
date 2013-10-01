package com.paraschool.editor.client.presenter;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.client.presenter.Presenter;
import com.paraschool.commons.share.Interactivity;
import com.paraschool.commons.share.Page;
import com.paraschool.editor.api.client.EditorModule;
import com.paraschool.editor.api.client.ModulesStore;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.event.interactivity.AddInteractivityEvent;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;
import com.paraschool.editor.shared.SampleDetails;

/*
 * Created at 25 juil. 2010
 * By Didier Bathily
 */
public class InteractivityListPresenter extends DefaultPresenter implements
		Presenter {

	private static final Logger logger = Logger.getLogger(InteractivityListPresenter.class.getName());
	
	public interface Display extends com.paraschool.commons.client.presenter.Display {
		HasClickHandlers getCloseButton();
		HasClickHandlers getChooseButton();
		HasClickHandlers getExamplesButton();
		
		void show();
		void hide();
		int getSelectedIndex();
		SampleDetails getSelectedSample();
		void setData(List<EditorModule> datas);
		
		void setExamples(List<SampleDetails> datas);
	}
	
	private final AppMessages messages;
	private final RpcActionFactory rpcActionFactory;
	private final Page page;
	private ModulesStore moduleStore;
	
	@Inject
	//TODO Use Gin assisted
	public InteractivityListPresenter(@Assisted Page page, ModulesStore moduleStore,
			EditorServiceAsync editorRpcService, ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, RpcActionFactory rpcActionFactory, Display display, AppMessages messages) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, display);
		this.rpcActionFactory = rpcActionFactory;
		this.page = page;
		this.moduleStore = moduleStore;
		this.messages = messages;
	}

	private void close() {
		((Display)display).hide();
		clear();
	}
	
	private void getSamples(final EditorModule module) {
		logger.fine("Get samples for module "+module.getDescriptor().getId());
		AsyncCallback<List<SampleDetails>> callback = new AsyncCallback<List<SampleDetails>>() {
			private void failed() {
				Window.alert(messages.noSampleAvailable());
			}
			@Override
			public void onFailure(Throwable caught) {
				logger.info(caught.getMessage());
				failed();
			}
			@Override
			public void onSuccess(List<SampleDetails> result) {
				if(result != null){
					logger.fine("Found "+result.size()+" samples.");
					((Display)display).setExamples(result);
				}else
					failed();
				
			}
		};
		rpcActionFactory.<List<SampleDetails>>create(callback, new RpcAttempt<List<SampleDetails>>() {
			@Override
			public void call(AsyncCallback<List<SampleDetails>> callback) {
				editorRpcService.getSamples(module.getDescriptor().getId(), LocaleInfo.getCurrentLocale().getLocaleName(), callback);
			}
		}).attempt();
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
			((Display)display).getChooseButton().addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					int index = ((Display)display).getSelectedIndex();
					if(index > -1){
						final EditorModule d = moduleStore.getModules().get(index);
						Interactivity i = new Interactivity(d.getDescriptor().getId());
						eventBus.fireEvent(new AddInteractivityEvent(i, page, ((Display)display).getSelectedSample()));
						close();
					}
				}
			})
		);
		
		registrations.add(
				((Display)display).getExamplesButton().addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						int index = ((Display)display).getSelectedIndex();
						if(index > -1){
							final EditorModule d = moduleStore.getModules().get(index);
							getSamples(d);
						}
					}
				})
			);
			
	}
	
	@Override
	public void go(final HasWidgets container) {
		bind();
		((Display)display).show();
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				((Display)display).setData(moduleStore.getModules());
			}
		});
		
	}

	
}
