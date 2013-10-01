package com.paraschool.editor.server.content.templates;

import java.io.File;
import java.io.InputStream;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Template;
import com.paraschool.editor.server.content.FileContentProvider;

public class FileTemplateProvider extends FileContentProvider<Template> {

	@Inject
	private FileTemplateProvider(@Named("templates.directory") String path) {
		super(path);
	}

	@Override
	protected Template convert(InputStream source) {
		Template template = super.convert(source);
		template.setLocation(getPath()+File.separator+template.getLocation());
		return template;
	}
}
