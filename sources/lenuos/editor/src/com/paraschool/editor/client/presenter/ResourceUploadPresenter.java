package com.paraschool.editor.client.presenter;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.inject.Inject;
import com.paraschool.commons.client.EventBus;
import com.paraschool.commons.share.Project;
import com.paraschool.commons.share.ProjectDetails;
import com.paraschool.commons.share.Resource;
import com.paraschool.commons.share.ResourceUtils;
import com.paraschool.commons.share.StringUtils;
import com.paraschool.editor.client.AjaxHandler;
import com.paraschool.editor.client.RpcAttempt;
import com.paraschool.editor.client.event.page.UploadedResourceEvent;
import com.paraschool.editor.client.i18n.AppMessages;
import com.paraschool.editor.client.presenter.factory.RpcActionFactory;
import com.paraschool.editor.client.rpc.EditorServiceAsync;
import com.paraschool.editor.client.rpc.ProjectServiceAsync;

/*
 * Created at 19 juil. 2010
 * By Didier Bathily
 */
public class ResourceUploadPresenter extends DefaultPresenter {
	
	public interface Display extends com.paraschool.commons.client.presenter.Display {
		Hidden getId();
		Hidden getPath();
		Hidden getLocale();
		FileUpload getFile();
		FormPanel getForm();
		void setProgress(int progress);
		void reset();
		void setStart();
		void setFinish();
		void setError();
	}
	
	class UploaderStatusTimer extends Timer {

		boolean firstRun = true;
		
		private int attempt = 0;
		private final int maxAttemptOnError;
		private final int interval;
		
		public UploaderStatusTimer(int maxAttemptOnError, int delay, int interval) {
			this.maxAttemptOnError = maxAttemptOnError;
			this.interval = interval;
			if(delay <= 0)
				run();
			else
				schedule(delay);
		}
		
		private void relaunch() {
			if(interval <= 0)
				run();
			else
				schedule(interval);
		}
		
		@Override
		public void run() {
			RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "../rupload");
			requestBuilder.setCallback(new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request,	Response response) {
					try{
						Document root = XMLParser.parse(response.getText());
						
						String status = root.getElementsByTagName("status").item(0).getFirstChild().getNodeValue();
						((Display)display).setProgress(Integer.parseInt(root.getElementsByTagName("percent").item(0).getFirstChild().getNodeValue()));
						Long.parseLong(root.getElementsByTagName("total").item(0).getFirstChild().getNodeValue());
						Long.parseLong(root.getElementsByTagName("current").item(0).getFirstChild().getNodeValue());
						
						attempt = 0;
						
						if("INPROGRESS".equals(status) || firstRun) 
							relaunch();
						
						else if("FINISH".equals(status))
							((Display)display).setFinish();
						
						else if("ERROR".equals(status))
							((Display)display).setError();
						
						firstRun = false;
						
					}catch (Exception e) {
						if(attempt < maxAttemptOnError) {
							attempt++;
							relaunch();
						}else
							((Display)display).setError();
					}
				}
				@Override
				public void onError(Request request, Throwable exception) {}
			});
			try {
				requestBuilder.send();
			} catch (RequestException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private final Project project;
	private RpcActionFactory rpcActionFactory;
	private AppMessages messages;
	
	@Inject
	public ResourceUploadPresenter(Project project, EditorServiceAsync editorRpcService,
			ProjectServiceAsync projectRpcService, EventBus eventBus,
			AjaxHandler ajaxHandler, AppMessages messages, RpcActionFactory rpcActionFactory,Display display) {
		super(editorRpcService, projectRpcService, eventBus, ajaxHandler, display);
		
		this.project = project;
		
		this.messages = messages;
		this.rpcActionFactory = rpcActionFactory;
	}

	
	@Override
	protected void bind() {
		((Display)display).reset();
		
		registrations.add(
				((Display)display).getFile().addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						((Display)display).getForm().submit();
					}
				})
		);
		
		registrations.add(
				((Display)display).getForm().addSubmitHandler(new SubmitHandler() {
					@Override
					public void onSubmit(SubmitEvent event) {
						
						String filename = ((Display)display).getFile().getFilename(); 
						String type = ResourceUtils.getMimeType(filename);
						
						if(type == null || !ResourceUtils.getValidExtensions().contains(type)){
							event.cancel();
							Window.alert(type == null ? messages.uploadFileUnknow() : messages.uploadFileInvalid(type));
							return;
						}
						
						/*
						 * IE n'initialisait pas les hiddens au moment de leur création
						 * c'est pourquoi on le fait à l'envoie
						 */
						ProjectDetails details = project.getDetails();
						((Display)display).getId().setValue(details.getId());
						if(details.getPath() != null)
							((Display)display).getPath().setValue(details.getPath());
						if(details.getLocale() != null)
							((Display)display).getLocale().setValue(details.getLocale());
						((Display)display).setStart();
						
						//FIXME Sous ie çà pose problème
						//new UploaderStatusTimer(3,100,0);
					}
				})
		);
		
		registrations.add(
				((Display)display).getForm().addSubmitCompleteHandler(new SubmitCompleteHandler() {
					
					@Override
					public void onSubmitComplete(SubmitCompleteEvent event) {
						try{
							String result = StringUtils.unescapeHTML(event.getResults(), 0);

							Document root = XMLParser.parse(result);
							
							String status = ((Element)root.getFirstChild()).getAttribute("status");
							if("ok".equals(status)){
								NodeList list = root.getChildNodes();
								for(int i=0 ; i < list.getLength() ; i++) {
									Node node = list.item(i);
									addResource(node.getFirstChild().toString());
								}
							}else{
								int errorCode = -1;
								try{
									errorCode = Integer.parseInt(((Element)root.getElementsByTagName("code").item(0)).getFirstChild().getNodeValue());
								}catch (NumberFormatException e) {}
								String values = ((Element)root.getElementsByTagName("values").item(0)).getFirstChild().getNodeValue();
								((Display)display).setError();
								displayError(errorCode, values);
							}
							
							((Display)display).setFinish();
							
						}catch (Throwable e) {
							e.printStackTrace();
							((Display)display).setError();
						}
					}
				})
			);
	}
	
	private void displayError(int code, String values) {
		String message = messages.unknowUploadError();
		switch (code) {
		case 0:
			long size = 0;
			try{
				size = Long.parseLong(values);
			}catch (NumberFormatException e) {}
			message = messages.uploadSizeExceededError(size/1024000);
			break;
		default:
			break;
		}
		Window.alert(message);
	}
	
	private void addResource(final String xml) {
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
				projectRpcService.addResource(project.getDetails(), xml, callback);
			}
			
		}).attempt();
	}
	
}
