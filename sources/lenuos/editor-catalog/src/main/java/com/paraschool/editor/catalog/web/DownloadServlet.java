package com.paraschool.editor.catalog.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;

import com.paraschool.editor.catalog.resource.util.ResourceUtils;

/**
 * Servlet implementation class DownloadServlet
 */
public class DownloadServlet extends HttpServlet {
	
	static final Log logger = LogFactory.getLog(DownloadServlet.class);
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.download(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.download(request, response);
	}

		
	public void download(HttpServletRequest request, HttpServletResponse response){
		
		ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
		String dirName = resourceBundle.getString("dir_upload");
		
		String fileUrl = request.getParameter("fileUrl");
		
		File file = new File(dirName, fileUrl);
		
		try {
			String fileName = file.getName();
			response.setContentType(ResourceUtils.getMimeType(fileName));
			response.setContentLength(new Long(file.length()).intValue());
			response.setHeader("Content-Disposition","attachment; filename=\"" + file.getName() +"\"");
			 
			FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
			
		} catch (IOException e) {
			logger.error(e);
		}
		
		
	}
	

}
