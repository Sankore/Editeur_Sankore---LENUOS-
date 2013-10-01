package com.paraschool.editor.server.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.h2.tools.Server;

import com.google.inject.Provider;

/*
 * Created at 24 sept. 2010
 * By bathily
 */
public class H2DataSourceProvider implements Provider<DataSource> {

	private static final Log logger = LogFactory.getLog(H2DataSourceProvider.class);
	
	private Server server;
	private JdbcDataSource datasource;
	
	final String url;
	final String user;
	final String password;
	final String tcpOptions;
	
	public H2DataSourceProvider(String url, String user, String password, String tcpOptions) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.tcpOptions = tcpOptions;
		
		if(logger.isDebugEnabled()) {
			logger.debug("Initialize database starter with:");
			logger.debug("url - "+url);
			logger.debug("user - "+user);
			logger.debug("password - "+password);
			logger.debug("options - "+tcpOptions);
		}
		
		datasource = new JdbcDataSource();
		datasource.setURL(url);
		datasource.setUser(user);
		datasource.setPassword(password);
	}
	
	
	public void start(ServletContext context) throws SQLException, FileNotFoundException {

		if (tcpOptions != null) {
			String[] params = StringUtils.split(tcpOptions, ' ');
			
			logger.debug("Start the server");
			
			server = Server.createTcpServer(params);
			server.start();
		}
		
		String sql = context.getRealPath("/WEB-INF/db/create.sql");
		Connection conn = datasource.getConnection();
		RunScript.execute(conn, new FileReader(sql));
		conn.close();
	}
	
	public void stop() {
		
		Connection conn;
		try {
			conn = datasource.getConnection();
			try {
				conn.createStatement().execute("SHUTDOWN");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	        	conn.close();
	        }
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally{
			if (server != null) {
	            server.stop();
	            server = null;
	        }
		}
		        
	}

	@Override
	public DataSource get() {
		return datasource;
	}
}
