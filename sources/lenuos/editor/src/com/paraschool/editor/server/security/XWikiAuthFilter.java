package com.paraschool.editor.server.security;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.editor.server.ConnectionMonitorThread;
import com.paraschool.editor.server.Marshaller;
import com.paraschool.editor.server.security.XWikiAuthResponse.Status;

/*
 * Created at 28 févr. 2012
 * By bathily
 */
public class XWikiAuthFilter implements Filter {

	private static Log logger = LogFactory.getLog(XWikiAuthFilter.class);
	
	@Inject
	private Marshaller marshaller;
	
	private final String cookieDomain;
	private final String usernameCookieName;
	private final String cookieVerificationUrl;
	private final String loginUrl;
	private final String redirectUrl;
	private final int timeout;
	private final String proxyUrl;
	private final int proxyPort;
	
	@Inject
	public XWikiAuthFilter(
			@Named("opensankore.sso.cookie.domain") String cookieDomain,
			@Named("opensankore.sso.cookie.username") String usernameCookieName, 
			@Named("opensankore.sso.verificationurl") String cookieVerificationUrl, 
			@Named("opensankore.sso.loginurl") String loginUrl,
			@Named("opensankore.sso.redirecturl") String redirectUrl,
			@Named("opensankore.sso.timeout") int timeout,
			@Named("opensankore.sso.proxyurl") String proxyUrl,
			@Named("opensankore.sso.proxyport") int proxyPort) {
		
		this.cookieDomain = cookieDomain;
		this.usernameCookieName = usernameCookieName;
		this.cookieVerificationUrl = cookieVerificationUrl;
		this.loginUrl = loginUrl;
		this.redirectUrl = redirectUrl;
		this.timeout = timeout;
		this.proxyUrl = proxyUrl;
		this.proxyPort = proxyPort;
		
		logger.debug(String.format("Open Sankoré filter iniatilzed w/ : domain %s, usernameCookie %s, verification url %s, login url %s and redirect url %s",
				this.cookieDomain, this.usernameCookieName, this.cookieVerificationUrl, this.loginUrl, this.redirectUrl));
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		
		logger.debug("Filter request!");
		Subject currentUser = SecurityUtils.getSubject();
		//Si l'utilisateur est connecté, l'est il aussi sur open sankoré ie y a t'il toujours le cookie username avec la même valeur?
		if(currentUser.isAuthenticated()) {
			logger.debug("User is connected. Check if he is still connected to open sankoré");
			String username = getRemoteUsernameCookieValue(httpRequest);
			if(username != null) {
				if(isSameUser(httpRequest)) {
					logger.debug("Find a username. Username match, user is still connected to open sankoré");
				}else{
					logger.debug("Find a username. Username don't match, user change!!");
					currentUser.logout();
					if(!validateCookie(httpRequest, httpResponse)){
						logger.debug("Unable to validate cookie");
						redirectToLoginPage(httpRequest, httpResponse);
						return;
					}
				}
			}else{
				currentUser.logout();
				redirectToLoginPage(httpRequest, httpResponse);
				return;
			}
		}else {
			logger.debug("User is not connected. Check if he is connected to open sankoré.");
			String username = getRemoteUsernameCookieValue(httpRequest);
			if(username != null) {
				logger.debug("Find a username. Will validate open sankoré cookies");
				if(!validateCookie(httpRequest, httpResponse)){
					logger.debug("Unable to validate cookie");
					redirectToLoginPage(httpRequest, httpResponse);
					return;
				}
			}else{
				logger.debug("User is not connected to open sankoré.");
				redirectToLoginPage(httpRequest, httpResponse);
				return;
			}
		}
		
		chain.doFilter(request, response);
	}
	
	private String getRemoteUsernameCookieValue(HttpServletRequest request) {
		if(request.getCookies() != null)
			for(Cookie c : request.getCookies()) {
				if(c.getName().equals(this.usernameCookieName)) {
					return c.getValue();
				}
			}
		return null;
	}
	
	private String getStoredUsername() {
		return (String)SecurityUtils.getSubject().getSession().getAttribute("sso.username");
	}
	
	private void setStoredUsername(String username) {
		SecurityUtils.getSubject().getSession().setAttribute("sso.username", username);
	}
	
	private boolean isSameUser(HttpServletRequest request) {
		return getRemoteUsernameCookieValue(request).equals(getStoredUsername());
	}
	
	private boolean validateCookie(HttpServletRequest request, HttpServletResponse response) {
		DefaultHttpClient httpClient = new DefaultHttpClient( );
		
		if(proxyUrl != null && !proxyUrl.equals("")) {
			HttpHost proxy = new HttpHost(proxyUrl, proxyPort);
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		
		ConnectionMonitorThread monitorThread = new ConnectionMonitorThread(httpClient.getConnectionManager(), timeout);

		CookieStore cookieStore = new BasicCookieStore();
		for(Cookie c : request.getCookies()) {
			BasicClientCookie cookie = new BasicClientCookie(c.getName(), c.getValue());
			cookie.setDomain(this.cookieDomain);
			cookie.setPath(c.getPath());
			cookie.setVersion(c.getVersion());
			cookieStore.addCookie(cookie);
		}
		httpClient.setCookieStore(cookieStore);
		
		HttpGet validateMethod = new HttpGet(cookieVerificationUrl);
		try
		{
			if(logger.isDebugEnabled()) {
				logger.debug("Start connexion to ["+validateMethod.getURI()+"]");
			}
			
			monitorThread.start();
			HttpResponse httpResponse = httpClient.execute ( validateMethod );
			int httpStatus = httpResponse.getStatusLine().getStatusCode( );
			InputStream content = httpResponse.getEntity().getContent();
			if(logger.isDebugEnabled()){
				logger.debug("Verification done with status code ["+httpStatus+"] and response \n"/*+IOUtils.toString(content, "UTF-8")*/);
			}
			if(httpStatus == HttpStatus.SC_OK) {
				final XWikiAuthResponse user = (XWikiAuthResponse) marshaller.fromXML(content);
				logger.debug(user);
				if(user.getStatus().equals(Status.SUCCESS)) {
					Subject currentUser = SecurityUtils.getSubject();
					currentUser.logout();
					currentUser.login(new XWikiAuthenticationToken(user));
					setStoredUsername(user.getUsername());
					return true;
				}
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (IllegalStateException e) {
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			logger.debug("Close connexion");
			monitorThread.shutdown();
			httpClient.getConnectionManager().shutdown();
		}
		
		return false;
		
	}
	
	private void redirectToLoginPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = String.format(loginUrl, URLEncoder.encode(redirectUrl, "UTF-8"));
		logger.debug("User is not authenticated. Redirect to login page "+ url);
		response.sendRedirect(url);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
