package com.paraschool.editor.server;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/*
 * Created at 4 sept. 2010
 * By bathily
 */
public class LinkRequest {
	private static final Logger log = Logger.getLogger( LinkRequest.class );
	
	public interface Handler {
		void onSuccess(InputStream stream) throws Exception;
	}
	
	public LinkRequest(String url, Handler handler, int timeout) {
		HttpClient httpClient = new DefaultHttpClient( );
		ConnectionMonitorThread monitorThread = new ConnectionMonitorThread(httpClient.getConnectionManager(), timeout);
		HttpGet  getMethod  = new HttpGet( url );
		/*
		httpClient.getParams().setParameter(
		        ClientPNames.COOKIE_POLICY,CookiePolicy.);
		*/
		try
		{
			log.debug("Start connexion");
			monitorThread.start();
			HttpResponse httpResponse = httpClient.execute( getMethod );
			
			int httpStatus = httpResponse.getStatusLine().getStatusCode( );
			
			if ( httpStatus == HttpServletResponse.SC_OK 
					&& httpResponse.getEntity().getContentType().getValue().startsWith("text/html")
					&& handler != null )
			{
				handler.onSuccess(httpResponse.getEntity().getContent());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (IllegalStateException e) {
		} catch (Exception e) {
		} finally {
			log.debug("Close connexion");
			monitorThread.shutdown();
			httpClient.getConnectionManager().shutdown();
		}
	}

	
}
