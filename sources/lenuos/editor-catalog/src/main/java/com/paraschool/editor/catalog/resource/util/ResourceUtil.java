package com.paraschool.editor.catalog.resource.util;

import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.paraschool.editor.catalog.models.Resource;

public class ResourceUtil {
	
	public static String getFilename(Resource resource, String uploadedFilename) {
		StringBuilder name = new StringBuilder(resource.getId().toString());
		name.append("_");
		name.append(UUID.randomUUID().toString());
		int pos = uploadedFilename.lastIndexOf(".");
		name.append(uploadedFilename.substring(pos));
		return name.toString();
	}
	
	public static String getPath(Calendar cal) {
		String fileSeparator = System.getProperty("file.separator");
		
		// year
		StringBuilder path = new StringBuilder(fileSeparator);
		path.append(new Integer(cal.get(Calendar.YEAR)).toString());

		// month
		path.append(fileSeparator);
		int month = cal.get(Calendar.MONTH)+1;
		if (month < 10) {
			path.append("0");
			path.append(month);
		} else {
			path.append(month);
		}
		
		return path.toString();
	}
	
	public static String getContextPath(HttpServletRequest request){
		if (request == null) {
			return null;
		}
		int index = request.getRequestURL().indexOf(request.getContextPath());
		String contextPath = request.getRequestURL().substring(0, index + request.getContextPath().length());
		return contextPath;
	}
	

}
