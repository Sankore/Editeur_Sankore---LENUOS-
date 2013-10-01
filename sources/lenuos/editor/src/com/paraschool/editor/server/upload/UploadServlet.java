package com.paraschool.editor.server.upload;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

	private static ThreadLocal<HttpServletRequest> perThreadRequet = new ThreadLocal<HttpServletRequest>();
	
	private static String STATUS_TAGNAME = "status";
	private static String PERCENT_TAGNAME = "percent";
	private static String CURRENT_TAGNAME = "current";
	private static String TOTAL_TAGNAME = "total";

	private enum STATUS {
		INPROGRESS,
		FINISH,
		ERROR
	}
	
	private enum ERROR_CODE {
		SIZE_LIMIT
	}

	protected final Log logger = LogFactory.getLog(getClass());
	
	private long sizeLimit;
	private int sleep;
	
	public UploadServlet(long sizeLimit, int sleep) {
		this.sizeLimit = sizeLimit;
		this.sleep = sleep;
		logger.info("New upload servlet with size limit at ["+sizeLimit+"] and sleep all ["+sleep+"]");
	}

	private void renderResponse(HttpServletResponse response, String text) throws IOException {
		response.setContentType("text/html");
		ServletOutputStream out = response.getOutputStream(); 
		out.println(StringEscapeUtils.escapeHtml(text));
		out.close();
	}
	
	private String formatError(ERROR_CODE code, String values) {
		return formatError(code.ordinal(),values);
	}
	
	private String formatError(int code, String values) {
		return "<response><code>"+code+"</code><values>"+values+"</values></response>";
	}

	protected String getFormParameterInItems(List<FileItem> items, String name) {
		for(FileItem item : items){
			if(item.isFormField() && item.getFieldName().equals(name))
				return item.getString();
		}
		return null;
	}

	protected boolean filter(HttpServletRequest request , HttpServletResponse response) throws Exception{
		if(sizeLimit >= 0 && request.getContentLength() > sizeLimit){
			renderResponse(response, formatError(ERROR_CODE.SIZE_LIMIT,sizeLimit+""));
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		perThreadRequet.set(request);
		try{
			if(!ServletFileUpload.isMultipartContent(request))
				return;
			
			UploadListener listener = new UploadListener(sleep, request.getContentLength());
			
			try {
				if(!filter(request, response)){
					listener.setHasError(false);
					return;
				}
					
			} catch (Exception e1) {
				return;
			}
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(sizeLimit);
			upload.setProgressListener(listener);

			try {

				logger.debug("upload start for project []");
				List<FileItem> items = (List<FileItem>)upload.parseRequest(request);
				
				String xmlResponse = executeAction(request, items);
				renderResponse(response, xmlResponse);

				logger.debug("upload finish : "+xmlResponse);
				
				listener.setFinish(true);

			} catch (Exception e) {
				e.printStackTrace();
				logger.warn(e);
				listener.setHasError(true);
				renderResponse(response, formatError(-1,e.getMessage()));
			}
		}finally{
			perThreadRequet.set(null);
			
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		perThreadRequet.set(request);

		logger.debug("Try to access upload status");

		UploadListener listener = UploadListener.getForRequest(request);
		if(listener == null) return;

		logger.debug("Found a listener, get upload status");

		long currentBytes = 0;
		long totalBytes = 0;
		long percent = 0;

		currentBytes = listener.getByteRead();
		totalBytes = listener.getRequestSize();
		percent = (totalBytes != 0 ? currentBytes * 100 / totalBytes : 0);
		String xmlResponse = 
			"<response>"+
			"<"+STATUS_TAGNAME+">"+(listener.hasError() ? STATUS.ERROR : (listener.isFinish() ? STATUS.FINISH : STATUS.INPROGRESS))+"</"+STATUS_TAGNAME+">"+
			"<"+CURRENT_TAGNAME+">"+currentBytes+"</"+CURRENT_TAGNAME+">"+
			"<"+TOTAL_TAGNAME+">"+totalBytes+"</"+TOTAL_TAGNAME+">"+
			"<"+PERCENT_TAGNAME+">"+percent+"</"+PERCENT_TAGNAME+">"+
			"</response>";

		logger.debug(xmlResponse);

		response.setContentType("application/xml");
		ServletOutputStream out = response.getOutputStream(); 
		out.println(xmlResponse);
		out.close();

		if(listener.isFinish() || listener.hasError())
			listener.remove();

		perThreadRequet.set(null);
	}

	protected String executeAction(HttpServletRequest request, List<FileItem> items) {
		return "<response status='ok'></response>";
	}

	public static HttpServletRequest getThreadRequest() {
		return perThreadRequet.get();
	}

}
