package com.paraschool.editor.server.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;
import org.apache.shiro.web.servlet.IniShiroFilter;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletModule;
import com.paraschool.editor.server.SubjectUserProvider;
import com.paraschool.editor.server.UserProvider;
import com.paraschool.editor.server.rpc.AuthenticationServiceImpl;
import com.paraschool.editor.server.rpc.AuthenticationServlet;
import com.paraschool.editor.server.rpc.DisconnectServlet;
import com.paraschool.editor.server.security.AppCasAuthFilter;
import com.paraschool.editor.server.security.SecurityFilter;
import com.paraschool.editor.server.security.XWikiAuthFilter;

public class SecurityGuiceModule extends ServletModule {

	private final static Log logger = LogFactory.getLog(SecurityGuiceModule.class);
	
	private final Configuration configuration;
	
	public SecurityGuiceModule(Configuration configuration) {
		super();
		this.configuration = configuration;
	}

	@Override
	protected void configureServlets() {
		logger.debug("Bind security module");
		
		bind(UserProvider.class).to(SubjectUserProvider.class);

		AopAllianceAnnotationsAuthorizingMethodInterceptor interceptor = new AopAllianceAnnotationsAuthorizingMethodInterceptor();

		bindInterceptor(Matchers.any(), Matchers.annotatedWith(RequiresUser.class), interceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(RequiresAuthentication.class), interceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(RequiresGuest.class), interceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(RequiresPermissions.class), interceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(RequiresRoles.class), interceptor);

		logger.debug("Security interceptors binded");
		
		serve("/editor/authenticate","/gip/authenticate").with(AuthenticationServiceImpl.class);
		serve("/authenticates").with(AuthenticationServlet.class);
		serve("/disconnect").with(DisconnectServlet.class);
		
		String ini = configuration.getString("security.ini");
		logger.debug("Use["+ini+"] for shiro ini config file");
		Map<String, String> shiroParameters = new HashMap<String, String>();
		shiroParameters.put("configPath", ini);
		
		FilterKeyBindingBuilder filterBuilder = filter("index.html","/editor.html","/gip.html","/editor/*","/gip/*","/inproject/*","/exports/*","/publications/*","/rupload","/iupload","/mupload","/status","/authenticates","/disconnect");
		if(configuration.getBoolean("use.database")){
			bind(SecurityFilter.class).in(Singleton.class);
			filterBuilder.through(SecurityFilter.class, shiroParameters);
		}else {
			bind(IniShiroFilter.class).in(Singleton.class);
			filterBuilder.through(IniShiroFilter.class, shiroParameters);
		}
		
		if(configuration.getBoolean("cas.enable")) {
			initCasFilters(filterBuilder);
		}
		
		if(configuration.getBoolean("opensankore.sso.enable")) {
			bind(XWikiAuthFilter.class).in(Singleton.class);
			filterBuilder.through(XWikiAuthFilter.class);
		}
		
		
	}

	private void initCasFilters(FilterKeyBindingBuilder filterBuilder) {
		
		/* authentication filter */
		Map<String, String> casAuthenticationParam = new HashMap<String, String>();
		casAuthenticationParam.put("casServerLoginUrl", configuration.getString("cas.casServerLoginUrl"));
		casAuthenticationParam.put("serverName", configuration.getString("serverName"));
		bind(AuthenticationFilter.class).in(Singleton.class);
		filterBuilder.through(AuthenticationFilter.class, casAuthenticationParam);
		
		/* ticket validation filter */
		Map<String, String> casValidationAuthenticationParam = new HashMap<String, String>();
		casValidationAuthenticationParam.put("casServerUrlPrefix", configuration.getString("cas.casServerUrlPrefix"));
		casValidationAuthenticationParam.put("serverName", configuration.getString("serverName"));
		bind(Cas20ProxyReceivingTicketValidationFilter.class).in(Singleton.class);
		filterBuilder.through(Cas20ProxyReceivingTicketValidationFilter.class, casValidationAuthenticationParam);
	
		bind(HttpServletRequestWrapperFilter.class).in(Singleton.class);
		filterBuilder.through(HttpServletRequestWrapperFilter.class);

		bind(AppCasAuthFilter.class).in(Singleton.class);		
		filterBuilder.through(AppCasAuthFilter.class);
	}
}
