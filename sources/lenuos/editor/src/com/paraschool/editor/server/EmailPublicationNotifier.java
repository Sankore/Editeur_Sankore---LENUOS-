package com.paraschool.editor.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.server.mail.PublicationMailContext;
import com.paraschool.editor.server.mail.VelocityMailContext;
import com.paraschool.editor.server.rpc.MailService;

/*
 * Created at 27 sept. 2010
 * By bathily
 */
public class EmailPublicationNotifier implements PublicationNotifier {

	private static Log logger = LogFactory.getLog(EmailPublicationNotifier.class);
	
	private MailService mailService;
	private String recipients;
	
	@Inject
	private EmailPublicationNotifier(MailService mailService, @Named("publication.notifiers.email.recipients") String recipients) {
		this.mailService = mailService;
		this.recipients = recipients;
	}
	
	@Override
	public void notify(User user, Project project, String url) {
		try {
			if(logger.isDebugEnabled())
				logger.debug("Notify publication of project ["+project.getDetails().getName()+"] to {"+recipients+"}");
			
			VelocityMailContext mailContext = new PublicationMailContext(project, url);
			mailService.sendMail(recipients.split(","), mailContext);
			
		} catch (Exception e) {
			logger.error("Unable to notify publication of project ["+project.getDetails().getName()+"]. "+e);
		}
	}

}
