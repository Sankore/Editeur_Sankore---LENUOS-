package com.paraschool.editor.server.rpc;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.paraschool.editor.server.mail.VelocityMailContext;
import com.paraschool.editor.shared.exception.MailException;

/**
 * Used for sending mail.
 * @author blamouret
 *
 */
public class MailService {

	private static final Log log = LogFactory.getLog(MailService.class);
	
	private static final String encoding = "UTF-8";
	private static final String content = "text/html";
	private static final String contentType = content + "; charset=" + encoding;
	
	private final boolean auth;
	private final String host;
	private final String port;
	private final String user;
	private final String password;
	private final String from;
	private final boolean ttls;
	private final boolean ssl;
	private final boolean debug;
	
	/**
	 * Default constructor
	 */
	@Inject
	public MailService(@Named("mail.smtp.auth") boolean auth,
			@Named("mail.smtp.host") String host,
			@Named("mail.smtp.port") String port,
			@Named("mail.smtp.user") String user,
			@Named("mail.smtp.password") String password,
			@Named("mail.from") String from,
			@Named("mail.smtp.starttls.enable") boolean ttls,
			@Named("mail.smtp.ssl.enable") boolean ssl,
			@Named("mail.smtp.debug") boolean debug) {
		super();
		this.auth = auth;
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.from = from;
		this.ttls = ttls;
		this.ssl = ssl;
		this.debug = debug;
	}
	
	/**
	 * Create a Mime message.
	 * @param session the mail session
	 * @param to for whose sending mail 
	 * @return the Mime message.
	 * @throws AddressException
	 * @throws MessagingException
	 */
	private MimeMessage createMessage(Session session, String to[]) throws AddressException, MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(this.from));
		message.setRecipients(Message.RecipientType.TO, this.toInternetAddress(to));
		return message;
		
	}

	/**
	 * Send mail.
	 * @param to for whose sending mail 
	 * @param subject the mail'subject
	 * @param text the text'subject.
	 */
	public void sendMail(String to, String subject, String text) {
		this.sendMail(new String[]{to}, subject, text);
	}
	
	/**
	 * Send mail.
	 * @param to for whose sending mail 
	 * @param subject the mail'subject
	 * @param text the text'subject.
	 */
	public void sendMail(String to[], String subject, String text) {
		Session session = this.getSession();
		
		try {
			MimeMessage message = this.createMessage(session, to);

			message.setSubject(subject, encoding);
			message.setText(text, encoding);
			message.setHeader("Content-Type", contentType);
			
			Transport transport = session.getTransport("smtp");
			transport.connect(user, password);
			transport.sendMessage(message, message.getAllRecipients());
			
			log.debug("Message send ...");

		} catch (MessagingException e) {
			log.error("Cannot build message", e);
			throw new MailException(e);
		}
	}
	
	/**
	 * Build InternetAddress according to String email. 
	 * @param to the string email
	 * @return the InternetAddress mail.
	 * @throws AddressException
	 */
	private InternetAddress[] toInternetAddress(String[] to) throws AddressException {
		InternetAddress[] addressTo = new InternetAddress[to.length];
		for (int i = 0; i < to.length; i++) {
			addressTo[i] = new InternetAddress(to[i]);
		}
		return addressTo;
	}
	
	/**
	 * Return the mail session.
	 */
	private Session getSession() {
		Properties properties = System.getProperties();
		
		properties.setProperty("mail.smtp.auth", this.auth+"");
		properties.setProperty("mail.smtp.host", this.host);
		if(port != null && port.trim().length() != 0)
			properties.setProperty("mail.smtp.port", this.port);
		properties.setProperty("mail.smtp.starttls.enable", this.ttls+"");
		properties.setProperty("mail.smtp.ssl.enable", this.ssl+"");
		
		Session session = Session.getInstance(properties);
		session.setDebug(debug);
		return session;
	}

	/**
	 * Send email with Velocity.
	 * @param to the email address to send mail.
	 * @param context the mail context.
	 */
	public void sendMail(String to, VelocityMailContext context) {
		this.sendMail(new String[]{to}, context);
	}	
	
	/**
	 * Send email with Velocity.
	 * @param to the email addresses to send mail.
	 * @param context the mail context.
	 */
	public void sendMail(String to[], VelocityMailContext context) {

		Session session = this.getSession();
		
		try {
			MimeMessage message = this.createMessage(session, to);

			message.setSubject(context.getSubject(), encoding);
			Multipart multipart = new MimeMultipart();
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(context.getContent(), content);
            multipart.addBodyPart(bodyPart);
            
            message.setContent(multipart);
			message.setHeader("Content-Type", multipart.getContentType());

			Transport.send(message);
			log.debug("Message send ...");

		} catch (Exception e) {
			log.error("Cannot build message", e);
			throw new MailException(e);
		}

	}
	
}
