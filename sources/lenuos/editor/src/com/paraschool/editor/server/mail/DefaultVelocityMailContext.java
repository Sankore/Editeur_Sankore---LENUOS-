package com.paraschool.editor.server.mail;

/*
 * Created at 27 sept. 2010
 * By bathily
 */
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.paraschool.editor.server.utils.LocaleUtil;

public abstract class DefaultVelocityMailContext extends VelocityMailContext {

	private static Log logger = LogFactory.getLog(DefaultVelocityMailContext.class);
	
	/**
	 * Define velocity template prefix
	 */
	private static final String TEMPLATE_NAME_PREFIX = "mails/";
	
	/**
	 * Define velocity template suffix
	 */
	private static final String TEMPLATE_NAME_SUFFIX = ".vm";

	/**
	 * Define Velocity key for getting subject from the template.
	 */
	protected static final String CONTEXT_SUBJECT = "subject";
	
	public DefaultVelocityMailContext(VelocityContext context, String lang)
			throws Exception {
		super(context, lang);
	}

	@Override
	public final String getSubject() {
		return (String) this.getContext().get(CONTEXT_SUBJECT);
	}

	protected abstract String getTemplatePrefix();
	
	@Override
	protected String getTemplateName() {
		logger.debug("Get template name for locale ["+getLang()+"]");
		String template = findTemplateForLocales(LocaleUtil.getCandidateLocales(getLang()));
		logger.debug("Find this template ["+template+"]");
		return template;
	}

	private String findTemplateForLocales(List<Locale> candidates) {
		for(Locale l : candidates) {
			String lString = l.toString();
			String template = String.format(TEMPLATE_NAME_PREFIX + getTemplatePrefix() + "%s" + TEMPLATE_NAME_SUFFIX,
					lString.trim().length() == 0 ? "" : "_" + l.toString());

			logger.debug("Search this mail template ["+template+"]");
			if(Velocity.resourceExists(template))
				return template;
		}
		return null;
	}
}