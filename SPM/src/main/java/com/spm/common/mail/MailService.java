package com.spm.common.mail;

/**
 * 
 * @author Maxi Rosson
 */
public interface MailService {
	
	/**
	 * @param subject
	 * @param body
	 * @param sender
	 * @param recipient
	 * @throws MailException
	 */
	public void sendMail(String subject, String body, String sender, String recipient) throws MailException;
	
	/**
	 * @param subject
	 * @param body
	 * @throws MailException
	 */
	public void sendMail(String subject, String body) throws MailException;
	
}
