package com.paraschool.editor.server.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * Created at 1 août 2010
 * By bathily
 */
public class ProjectServiceInterceptor implements MethodInterceptor {
	
	private Log logger = LogFactory.getLog(ProjectServiceInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.debug(invocation.getThis().getClass() +"-"+invocation.getMethod().getName());
		return invocation.proceed();
	}

}
