package com.paraschool.editor.server.mail;

import java.util.HashMap;

import org.apache.velocity.VelocityContext;

/**
 * Defines content generator for sending mail with login and password.
 * @author blamouret
 *
 */
public class LoginPasswordMailContext extends DefaultVelocityMailContext {
	
	private static final String TEMPLATE_NAME_PREFIX = "credentials";
	
	/**
	 * Define Velocity key for replacing login in the template.
	 */
	private static final String CONTEXT_LOGIN = "login";
	/**
	 * Define Velocity key for replacing password in the template.
	 */
	private static final String CONTEXT_PASSWORD = "password";

	/**
	 * Default constructor
	 * @param login the login to set in the mail
	 * @param password the login to set in the mail
	 * @param lang the mail's language
	 * @throws Exception
	 */
	public LoginPasswordMailContext(String login, String password, String lang) throws Exception {
		super(buildContext(login, password), lang);
	}

	/**
	 * Build the velocity context.
	 * @param login the login to set in the mail
	 * @param password the passowrd to set in the mail.
	 * @return the velocity context
	 */
	private static VelocityContext buildContext(String login, String password) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(CONTEXT_LOGIN, login);
		map.put(CONTEXT_PASSWORD, password);
		return new VelocityContext(map);
	}

	@Override
	protected String getTemplatePrefix() {
		return TEMPLATE_NAME_PREFIX;
	}
	
}
