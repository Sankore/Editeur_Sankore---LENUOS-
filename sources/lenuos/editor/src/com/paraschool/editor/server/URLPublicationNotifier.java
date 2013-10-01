package com.paraschool.editor.server;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.commons.share.Project;
import com.paraschool.editor.server.utils.UrlUtil;

/*
 * Created at 31 oct. 2010
 * By bathily
 */
public class URLPublicationNotifier implements PublicationNotifier {

	private final static Log logger = LogFactory.getLog(URLPublicationNotifier.class);
	private final JSONMarshaller marshaller;
	private final String host;
	private final int timeout;
	private final String publishDirectory;
	private final boolean embed;
	private final boolean encrypt;
	private final String key;
	private final String iv;
	
	@Inject
	public URLPublicationNotifier(@Named("publication.notifiers.http.url") String host,
			JSONMarshaller marshaller, @Named("publication.notifiers.http.timeout") int timeout,
			@Named("publish.directory") String publishDirectory,
			@Named("publication.notifiers.http.embed-file") boolean embed,
			@Named("publication.notifiers.http.encrypt") boolean encrypt,
			@Named("publication.notifiers.http.key") String key,
			@Named("publication.notifiers.http.iv") String iv) {
		
		this.host = host;
		this.marshaller = marshaller;
		this.timeout = timeout;
		this.publishDirectory = publishDirectory;
		this.embed = embed;
		this.encrypt = encrypt;
		this.key = key;
		this.iv = iv;
	}
	
	private File getZipFile(String url) throws Exception {
		int i = url.lastIndexOf("publications/");
		if(i == -1)
			throw new Exception("Malformed publication URL : \"publications/\" not found in [+ur+]");
		String publicationFileName = url.substring(i+"publications/".length());
		return new File(publishDirectory+File.separator+publicationFileName);
	}
	
	@Override
	public void notify(User user, Project project, String url) {
		PublicationTicket ticket = new PublicationTicket(new Date().getTime(), user.getUsername());
		String json = marshaller.toXML(ticket);
		if(encrypt) json = encrypt(json);
		
		VelocityContext context = new VelocityContext();
		context.put("project", project);
		context.put("url", url);
		try{
			Template lomTemplate = Velocity.getTemplate("lomfr.vm");
			StringWriter st = new StringWriter();
			lomTemplate.merge(context, st);
			
			HttpClient httpClient = new DefaultHttpClient( );
			ConnectionMonitorThread monitorThread = new ConnectionMonitorThread(httpClient.getConnectionManager(), timeout);
	
			StringBuilder builder = new StringBuilder(host);
			UrlUtil.addParameter(builder, "ticket", URLEncoder.encode(json, "UTF-8"));
			
			MultipartEntity multipart = new MultipartEntity();
			
			StringBody lomValue = new StringBody(st.toString());
			multipart.addPart("lom", lomValue);
			
			if(embed) {
				File zip = getZipFile(url);
				FileBody file = new FileBody(zip,"application/zip");
				multipart.addPart("file", file);
			}
			
			HttpPost postMethod = new HttpPost(builder.toString());
			postMethod.setEntity(multipart);
			
			try
			{
				if(logger.isDebugEnabled()) {
					logger.debug("Notify ["+host+"] with \n"+json+"\n and lom \n"+st.toString());
					logger.debug("Start connexion to ["+postMethod.getURI()+"]");
				}
				
				monitorThread.start();
				HttpResponse httpResponse = httpClient.execute ( postMethod );
				
				if(logger.isDebugEnabled()){
					int httpStatus = httpResponse.getStatusLine().getStatusCode( );
					logger.debug("Notification done with status code ["+httpStatus+"] and response \n"+IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"));
				}
				
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			} catch (IllegalStateException e) {
			} catch (Exception e) {
			} finally {
				logger.debug("Close connexion");
				monitorThread.shutdown();
				httpClient.getConnectionManager().shutdown();
			}
			
			
		}catch (Exception e) {
			logger.warn(e);
			e.printStackTrace();
		}
		
	}

	private String encrypt(String json) {
		byte[] sessionKey = key.getBytes();
		byte[] ivBytes = iv.getBytes() ;
		
		byte[] plaintext = json.getBytes();
		try{
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE,
					new SecretKeySpec(sessionKey, "AES"), 
					new IvParameterSpec(ivBytes));
			byte[] ciphertext = cipher.doFinal(plaintext);
			return new String(ciphertext);
		}catch (Exception e) {
			e.printStackTrace();
			return json;
		}
			
	}
}
