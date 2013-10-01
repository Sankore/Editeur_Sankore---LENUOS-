package com.paraschool.editor.server.mail;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.paraschool.editor.server.utils.LocaleUtil;

/**
 * Used for sending mail with Velocity.
 * @author blamouret
 *
 */
public abstract class VelocityMailContext {
	/**
	 * The Velocity content for generating mail.
	 */
	private final VelocityContext context;
	/**
	 * The language to use for generating mail.
	 */
	private final Locale lang;
	/**
	 * The genrated content of the mail. 
	 */
	private final String content;
	
	/**
	 * Default constructor
	 * @param context Velocity context
	 * @param lang mails's language
	 * @throws Exception
	 */
	protected VelocityMailContext(VelocityContext context, String lang) throws Exception {
		super();
		this.context = context;
		this.lang = LocaleUtil.getLocaleFromString(lang);
		this.content = this.buildContent();
	}
	
	/**
	 * Build mail content
	 * @return the mail content.
	 * @throws Exception
	 */
	private String buildContent() throws Exception {
		Template template = this.getTemplate();
		StringWriter writer = new StringWriter();
        template.merge(this.getContext(), writer);
        writer.close();
        return writer.toString();
	}

	/**
	 * Return Velocity context
	 * @return the Velocity context.
	 */
	protected VelocityContext getContext() {
		return this.context;
	}
	
	/**
	 * Return the mail'subject
	 * @return the mail'subject
	 */
	public abstract String getSubject();
	
	/**
	 * Return the mail's language
	 * @return the mail's language
	 */
	protected Locale getLang() {
		return this.lang;
	}
	
	/**
	 * Return the file name of the velocity template to use.
	 * @return the file name of the velocity template to use.
	 */
	protected abstract String getTemplateName();

	/**
	 * Return the velocity template to use.
	 * @return the velocity template to use.
	 * @throws Exception
	 */
	protected Template getTemplate() throws Exception {
		return Velocity.getTemplate(this.getTemplateName());
	}
	
	/**
	 * Return the mail's content.
	 * @return the mail's content.
	 */
	public String getContent() {
		return this.content;
	}

}
