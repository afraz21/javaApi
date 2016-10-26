package org.iqvis.nvolv3.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.iqvis.nvolv3.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(Constants.SERVICE_EMAIL)
@Transactional
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private JavaMailSender mailSenderNvolvSupport;

	public void sendEmail(String recipientAddress, String subject, String message) throws MailException {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(message, "text/html");
			mimeMessage.setHeader("MIME-Version", "1.0");
			// mimeMessage.setHeader("Content-Type" ,
			// mimeMessage.getContentType() );
			helper.setTo(recipientAddress);
			helper.setFrom("nvolv");
			helper.setSubject(subject);
			// helper.setFrom("");
			mailSender.send(mimeMessage);

		}
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void sendEmail(String[] recipientAddress, String subject, String message) throws MailException {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(message, "text/html");
			mimeMessage.setHeader("MIME-Version", "1.0");
			// mimeMessage.setHeader("Content-Type" ,
			// mimeMessage.getContentType() );
			helper.setTo(recipientAddress);
			helper.setFrom("nvolv");
			helper.setSubject(subject);
			// helper.setFrom("");
			mailSender.send(mimeMessage);

		}
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void sendEmailByNvolv(String recipientAddress, String subject, String message) throws MailException {

		MimeMessage mimeMessage = mailSenderNvolvSupport.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(message, "text/html");
			mimeMessage.setHeader("MIME-Version", "1.0");
			// mimeMessage.setHeader("Content-Type" ,
			// mimeMessage.getContentType() );
			helper.setTo(recipientAddress);
			helper.setFrom("nvolv");
			helper.setSubject(subject);
			// helper.setFrom("");
			mailSenderNvolvSupport.send(mimeMessage);

		}
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void sendEmailByNvolv(String[] recipientAddress, String subject, String message) throws MailException {

		MimeMessage mimeMessage = mailSenderNvolvSupport.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(message, "text/html");
			mimeMessage.setHeader("MIME-Version", "1.0");
			// mimeMessage.setHeader("Content-Type" ,
			// mimeMessage.getContentType() );
			helper.setTo(recipientAddress);
			helper.setFrom("nvolv");
			helper.setSubject(subject);
			// helper.setFrom("");
			mailSenderNvolvSupport.send(mimeMessage);

		}
		catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
