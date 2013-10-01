package com.paraschool.editor.server.upload;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * Created at 27 août 2010
 * By bathily
 */
public class UploadListener implements ProgressListener {

	private static final String UPLOAD_LISTENER = "UPLOAD_LISTENER";
	private static final int SAVE_INTERVAL = 3000;
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private boolean isFinish = false;
	private boolean hasError = false;
	private int sleepMillisecond = 0;
	private long requestSize = 0L;
	private long byteRead = 0L;
	private Date lastSave = new Date();
	
	public UploadListener(int sleepMillisecond, long requestSize) {
		this.sleepMillisecond = sleepMillisecond;
		this.requestSize = requestSize;
		save();
	}
	
	public static UploadListener getForRequest(HttpServletRequest request) {
		return (UploadListener)request.getSession().getAttribute(UPLOAD_LISTENER);
	}
	
	public void save() {
		logger.debug("Save listener at "+byteRead+" bytes");
		UploadServlet.getThreadRequest().getSession().setAttribute(UPLOAD_LISTENER, this);
	}
	
	public void remove() {
		logger.debug("Remove listener");
		UploadServlet.getThreadRequest().getSession().removeAttribute(UPLOAD_LISTENER);
	}
	
	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
		boolean mustSave = byteRead == 0 && pBytesRead > 0 // Juste start 
						|| pBytesRead >= pContentLength // Finish
						|| new Date().getTime() - lastSave.getTime() > SAVE_INTERVAL ;
		
		byteRead = pBytesRead;
		logger.debug("Read "+pBytesRead+" from "+pContentLength);
		if(mustSave) save();
		
		if(sleepMillisecond > 0 && pBytesRead < pContentLength){
			try{
				Thread.sleep(sleepMillisecond);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public int getSleepMillisecond() {
		return sleepMillisecond;
	}

	public void setSleepMillisecond(int sleepMillisecond) {
		this.sleepMillisecond = sleepMillisecond;
	}

	public long getRequestSize() {
		return requestSize;
	}

	public void setRequestSize(long requestSize) {
		this.requestSize = requestSize;
	}

	public long getByteRead() {
		return byteRead;
	}

	public void setByteRead(long byteRead) {
		this.byteRead = byteRead;
	}

	public Date getLastSave() {
		return lastSave;
	}

	public void setLastSave(Date lastSave) {
		this.lastSave = lastSave;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public boolean hasError() {
		return hasError;
	}

}
