package com.paraschool.viewer.client.config;

import com.google.gwt.inject.client.GinModules;
import com.paraschool.commons.client.config.CommonsGinjector;
import com.paraschool.viewer.client.AppController;
import com.paraschool.viewer.client.XMLProjectDeserializer;

/*
 * Created at 17 août 2010
 * By bathily
 */
@GinModules({ViewerModule.class})
public interface ViewerGinJector extends CommonsGinjector {
	
	AppController getAppController();
	XMLProjectDeserializer getXMLProjectDeserializer();
	
}
