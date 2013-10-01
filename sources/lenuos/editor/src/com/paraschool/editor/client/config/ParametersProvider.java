package com.paraschool.editor.client.config;

import com.google.inject.Inject;
import com.paraschool.editor.shared.Parameters;

public class ParametersProvider {

	private Parameters parameters;

	@Inject
	private ParametersProvider() {};
	
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public Parameters getParameters() {
		return parameters;
	}
	
}
