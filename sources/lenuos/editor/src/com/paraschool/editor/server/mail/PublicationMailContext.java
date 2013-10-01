package com.paraschool.editor.server.mail;

import java.util.HashMap;
import java.util.Locale;

import org.apache.velocity.VelocityContext;

import com.paraschool.commons.share.Project;

/*
 * Created at 27 sept. 2010
 * By bathily
 */
public class PublicationMailContext extends DefaultVelocityMailContext {
	
	private static final String TEMPLATE_NAME_PREFIX = "publication";
	
	public PublicationMailContext(Project project, String url) throws Exception {
		super(buildContext(project, url), Locale.getDefault().toString());
	}
	
	private static VelocityContext buildContext(Project project, String url) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("project", project);
		map.put("url", url);
		return new VelocityContext(map);
	}

	@Override
	protected String getTemplatePrefix() {
		return TEMPLATE_NAME_PREFIX;
	}

}
