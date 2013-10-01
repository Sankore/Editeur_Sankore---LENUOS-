package com.paraschool.editor.server.rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.paraschool.commons.share.ResourceUtils;

@Singleton
public class MimetypesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
    private MimetypesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> mimetypes = ResourceUtils.getAllMimeTypes();
		StringBuilder responseBuilder = new StringBuilder();
		for(Entry<String, String> mimetype : mimetypes.entrySet()) {
			responseBuilder.append(mimetype.getKey()).append("=").append(mimetype.getValue()).append("\n");
		}
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println(responseBuilder.toString());
		out.close();
	}
}
