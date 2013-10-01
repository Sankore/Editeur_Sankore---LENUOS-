package com.paraschool.viewer.client;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.paraschool.commons.share.Project;

public abstract class XMLProjectDeserializer implements ProjectDeserializer {

	protected abstract Project parseDocument(Document root);
	
	@Override
	public Project create(String data) {
		Document root = XMLParser.parse(data);
		return parseDocument(root);
	}

	
}
