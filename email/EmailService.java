package org.iqvis.nvolv3.email;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mail.MailException;

/**
 * Service for processing {@link EmailService} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface EmailService {

	public void sendEmail(String recipientAddress, String subject, String message) throws MailException;

	public void sendEmail(String[] recipientAddress, String subject, String message) throws MailException;

	public void sendEmailByNvolv(String[] recipientAddress, String subject, String message) throws MailException;

	public void sendEmailByNvolv(String recipientAddress, String subject, String message) throws MailException;

}
