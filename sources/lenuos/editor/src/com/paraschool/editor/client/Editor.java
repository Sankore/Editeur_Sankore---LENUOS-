package com.paraschool.editor.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.paraschool.editor.client.config.InjectorProvider;

public class Editor implements EntryPoint {

	private static final Logger logger = Logger.getLogger(Editor.class.getName());
	
	public void onModuleLoad() {
		// set uncaught exception handler
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable throwable) {
				logger.log(Level.SEVERE, "",throwable);
			}
		});

		// use a deferred command so that the handler catches exceptions
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				GWT.<InjectorProvider>create(InjectorProvider.class).get().getAppController().go();
				//ExporterUtil.exportAll();
				//onLoadImpl();
			}
		});
		exportLocale();
	}
	
	private native void onLoadImpl() /*-{
		$wnd.Editor = new $wnd.com.paraschool.editor.api.EditorAPI();
		if ($wnd.applicationLoaded && typeof $wnd.applicationLoaded == 'function') $wnd.applicationLoaded();
	}-*/;

	public static native void exportLocale() /*-{
	    var l = @com.google.gwt.i18n.client.LocaleInfo::getCurrentLocale()().@com.google.gwt.i18n.client.LocaleInfo::getLocaleName()();
	    $wnd.Locale = l;
	 }-*/;
}
