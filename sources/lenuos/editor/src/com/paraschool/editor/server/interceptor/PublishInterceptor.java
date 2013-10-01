package com.paraschool.editor.server.interceptor;

import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.server.PublicationNotifier;
import com.paraschool.editor.server.UserProvider;
import com.paraschool.editor.shared.exception.MissingFeatureException;

/*
 * Created at 23 sept. 2010
 * By bathily
 */
public class PublishInterceptor implements MethodInterceptor {

	private Log logger = LogFactory.getLog(PublishInterceptor.class);
	
	@Inject @Named("publish.allowed") Boolean canPublish;
	@Inject Set<PublicationNotifier> notifiers;
	@Inject UserProvider userProvider;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if(!canPublish)
			throw new MissingFeatureException();
		
		String path = (String)invocation.proceed();
		Project project = (Project)invocation.getArguments()[0];
		
		logger.debug("Intercept publication of project ["+project.getDetails().getName()+"] at ["+path+"]");

		for(PublicationNotifier notifier : notifiers)
			notifier.notify(userProvider.getCurrentUser(), project, path);

		return path;
	}

}
