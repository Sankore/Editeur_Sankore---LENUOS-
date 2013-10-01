package com.paraschool.viewer.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.paraschool.commons.share.Project;
import com.paraschool.viewer.client.config.ViewerGinJector;

public class Viewer implements EntryPoint {

	private static final Logger logger = Logger.getLogger(Viewer.class.getName());
	private final ViewerGinJector injector = GWT.create(ViewerGinJector.class);

	private static String base = "";
	private static String suffix = "";
	
	public static String makeURL(String relative) {
		return base+relative+suffix;
	}
	protected void getProjectFromXML(String file) {
		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, makeURL(file));
		try {
			request.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if(response.getStatusCode() == Response.SC_OK || response.getStatusCode() == 0) {
						init(injector.getXMLProjectDeserializer().create(response.getText()));
					}else{
						error(null);
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert(exception.toString());
					error(exception);
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
			error(e);
		}
	}

	protected void init(Project project) {
		AppController controller = injector.getAppController();
		DOM.getElementById("loading").removeFromParent();
		controller.view(project);
	}

	protected void error(Throwable t) {
		if(t!=null)
			Window.alert(t.getCause()+":"+t.getMessage());
	}
	
	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable throwable) {
				logger.log(Level.SEVERE, "",throwable);
			}
		});

	    // use a deferred command so that the handler catches onModuleLoad2() exceptions
	    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
	      public void execute() {
	    	  GWT.create(ViewerAPI.class);
	    	  onModuleLoad2();
	    	  onLoadImpl();
	      }
	    });
	}
	
	private void onModuleLoad2() {
		String file = Window.Location.getParameter("file");
		if(file == null){
			file = "project.xml";
			String locale = null;
			if((locale = Window.Location.getParameter("plocale")) != null) 
				file = "project_"+locale+".xml";
		}

		String prefix = Window.Location.getParameter("prefix");
		if(prefix != null) base = prefix;

		String asuffix = Window.Location.getParameter("suffix");
		if(asuffix != null) suffix = asuffix;
		
		getProjectFromXML(file);
	}


	private native void onLoadImpl() /*-{
		if ($wnd.applicationLoaded && typeof $wnd.applicationLoaded == 'function') $wnd.applicationLoaded();
	}-*/;
}
