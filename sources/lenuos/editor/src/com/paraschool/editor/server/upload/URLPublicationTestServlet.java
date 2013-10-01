package com.paraschool.editor.server.upload;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Singleton;

@Singleton
public class URLPublicationTestServlet extends UploadServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public URLPublicationTestServlet() {
		super(-1, -1);
	}

	@Override
	protected String executeAction(HttpServletRequest request,
			List<FileItem> items) {
		
		logger.info("Receive a ticket\n"+request.getParameter("ticket"));
		
		String parts = "";
		for(FileItem item : items) {
			parts += String.format("<part><name>%s</name><field>%s</field><formfield>%s</formfield><value>%s</value></part>", item.getName(), item.getFieldName(),item.isFormField(), item.isFormField() ? item.getString() : item.getName());
		}
		return "<response>"+parts+"</response>";
	}

	
}
