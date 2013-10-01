package com.paraschool.editor.server.config;

import java.lang.reflect.InvocationTargetException;

import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/*
 * Created at 25 sept. 2010
 * By bathily
 */

@Singleton
public class DataSourceProvider implements Provider<DataSource> {

	private static final Log logger = LogFactory.getLog(DataSourceProvider.class);

	private DataSource dataSource;
	
	@Inject
	private DataSourceProvider(@Named("datasource") String datasource,
			@Named("datasource.url") String url,
			@Named("datasource.user") String user,
			@Named("datasource.password") String password) {
		
		try {
			dataSource = (DataSource) Class.forName(datasource).newInstance();
			PropertyUtils.setProperty(dataSource, "URL", url);
			PropertyUtils.setProperty(dataSource, "user", user);
			PropertyUtils.setProperty(dataSource, "password", password);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error(datasource+" not found in classpath");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public DataSource get() {
		return dataSource;
	}
}
