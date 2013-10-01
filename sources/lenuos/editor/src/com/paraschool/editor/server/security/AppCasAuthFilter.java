package com.paraschool.editor.server.security;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import com.google.inject.Inject;

/**
 * Servlet Filter implementation class AppCasAuthFilter
 * Used for shiro authentication according to Cas Server authentication.
 */
public class AppCasAuthFilter implements Filter {
	
	private static Log logger = LogFactory.getLog(AppCasAuthFilter.class);
	
	/**
	 * True if used a Cas server for authentication. 
	 */
	private Boolean casServer = null;
	
	/**
	 * Default Constructor
	 * @param authService the authentication service to use.
	 */
	@Inject
	public AppCasAuthFilter() {
		super();
		logger.debug("CAS filter initialized");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		if (this.isCasServer()) {
			logger.debug("A CAS server was parameted. Filter the request");
			Principal principal = WebUtils.toHttp(request).getUserPrincipal();
			if (principal != null) {
				this.login(principal);
			}
		}
		chain.doFilter(request, response);
	}
	
	/**
	 * Login user in shiro.
	 * @param principal
	 */
	private void login(Principal principal) {
		
		logger.debug("User [" + principal.getName() + "] request authentication");
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			logger.debug("User is not authenticated. Try to authenticate");
			currentUser.logout();
			CasAuthenticationToken token = new CasAuthenticationToken(principal);
			currentUser.login(token);
		}
		logger.debug("Is user authenticated ?" + currentUser.isAuthenticated());
	}
	
	@Override
	public void destroy() {
		// Nothing to do
	}

	/**
	 * Return true is there is a Cas server for authentication.
	 * @return true is there is a Cas server for authentication.
	 */
	public boolean isCasServer() {
		if (this.casServer == null) {
			this.casServer = Boolean.FALSE;
			RealmSecurityManager sm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
			for (Realm realm : sm.getRealms()) {
				if (realm instanceof AppCasRealm) {
					this.casServer = Boolean.TRUE;
					break;
				}
			}
		}
		return this.casServer;
	}
	
	@Override
	public void init(FilterConfig filter) throws ServletException {
		// Nothing to do
	}

}
