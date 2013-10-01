package com.paraschool.editor.server.content.samples;

import java.io.File;
import java.io.InputStream;

import javax.servlet.ServletContext;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.editor.server.content.WebAppContentProvider;
import com.paraschool.editor.shared.Sample;

public class WebAppContentSampleProvider extends WebAppContentProvider<Sample> {

	@Inject
	public WebAppContentSampleProvider(@Named("webapp.samples.directory") String path, ServletContext servletContext) {
		super(path, servletContext);
	}

	@Override
	protected Sample convert(InputStream source) {
		Sample sample = super.convert(source);
		if(sample.getFiles() != null)
			sample.setFiles(getPath()+File.separator+sample.getFiles());
		return sample;
	}
}
