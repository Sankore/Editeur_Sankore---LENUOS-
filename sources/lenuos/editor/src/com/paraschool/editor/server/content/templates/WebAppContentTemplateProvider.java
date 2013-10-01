package com.paraschool.editor.server.content.templates;

import java.io.File;
import java.io.InputStream;

import javax.annotation.Nullable;
import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Template;
import com.paraschool.editor.server.content.WebAppContentProvider;

public class WebAppContentTemplateProvider extends WebAppContentProvider<Template> {

	@Inject
	public WebAppContentTemplateProvider(@Named("webapp.templates.directory") String path, @Nullable ServletContext servletContext) {
		super(path, servletContext);
	}

	@Override
	protected Template convert(InputStream source) {
		Template template = super.convert(source);
		template.setLocation(getPath()+File.separator+template.getLocation());
		return template;
	}
}
