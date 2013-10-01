package com.paraschool.viewer.client;

import com.paraschool.commons.share.Project;

public interface ProjectDeserializer {
	
	Project create(String data);
	
}
