package com.spm.common.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.util.Log;

public class MailServiceImpl implements MailService {
	
	private static final String TAG = MailServiceImpl.class.getSimpleName();
	
	private static final String MAIL_USER = "mural86@gmail.com"; // PropertiesUtils.getStringProperty("mail.user");
	private static final String MAIL_PASSWORD = "bocajtkm1"; // PropertiesUtils.getStringProperty("mail.password");
	private static final String MAIL_HOST = "smtp.gmail.com"; // PropertiesUtils.getStringProperty("mail.host");
	private static final String MAIL_PORT = "465"; // PropertiesUtils.getStringProperty("mail.port");
	private static final String MAIL_DEFAULT_FROM = "mural86@gmail.com"; // PropertiesUtils.getStringProperty("mail.from");
	private static final String MAIL_DEFAULT_TO = "mural86@gmail.com"; // PropertiesUtils.getStringProperty("mail.to");
	private static final String SMTP = "smtp";
	
	private Session session = Session.getDefaultInstance(new Properties(), null);
	
	/**
	 * @see com.spm.common.mail.MailService#sendMail(java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void sendMail(String subject, String body, String sender, String recipient) throws MailException {
		
		Transport transport = null;
		try {
			Message message = this.makeMessage(subject, body, sender, recipient);
			transport = this.makeTransport();
			transport.sendMessage(message, message.getAllRecipients());
		} catch (MessagingException e) {
			throw new MailException(e);
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					Log.e(TAG, "", e);
				}
			}
		}
	}
	
	/**
	 * @see com.spm.common.mail.MailService#sendMail(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMail(String subject, String body) throws MailException {
		sendMail(subject, body, MAIL_DEFAULT_FROM, MAIL_DEFAULT_TO);
	}
	
	private Message makeMessage(String subject, String body, String sender, String recipient) throws AddressException,
			MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sender));
		message.setSubject(subject);
		message.setText(body);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		message.saveChanges();
		
		return message;
	}
	
	private Transport makeTransport() throws MessagingException {
		Properties props = (Properties)System.getProperties().clone();
		props.put("mail." + SMTP + ".auth", "true");
		props.put("mail.smtp.port", MAIL_PORT);
		props.put("mail.smtp.socketFactory.port", MAIL_PORT);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		Session session = Session.getInstance(props, null);
		
		Transport transport = session.getTransport(SMTP);
		transport.connect(MAIL_HOST, /* Integer.valueOf(MAIL_PORT), */MAIL_USER, MAIL_PASSWORD);
		return transport;
	}
}
