package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.dao.EmailTemplateDao;
import org.iqvis.nvolv3.domain.EmailTemplate;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.email.EmailServiceImpl;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(Constants.SERVICE_EMAIL_TEMPLATE)
public class EmailTemplateServiceImpl implements EmailTemplateService {

	@Autowired
	EmailTemplateDao emailTemplateDao;

	@Autowired
	protected EmailServiceImpl emailService;

	public EmailTemplate get(String id) {

		return emailTemplateDao.get(id);
	}

	public EmailTemplate getByUniqueName(String name) {
		return emailTemplateDao.getByUniqueName(name);
	}

	public EmailTemplate get(String mailFor, String to) {

		return emailTemplateDao.get(mailFor, to);
	}

	public EmailTemplate getEmailByTo(String to) {

		return emailTemplateDao.getEmailByTo(to);
	}

	public void sendEmailToNvolvSales(User user, Event event, AppConfiguration appConfiguration) {

		EmailTemplate emailTempate = this.getByUniqueName(Constants.ATTACH_EVENT_EMAIL_TEMPLATE_FOR_SALES);

		// TODO FOR SUBJECT

		String subject = emailTempate.getSubject().replace("[ORGANIZER_NAME]", user.getFirstName() + " " + user.getLastName());

		subject = subject.replace("[ORGANIZER_EMAIL]", user.getEmail());

		subject = subject.replace("[ORGANIZER_FIRST_NAME]", user.getFirstName());

		subject = subject.replace("[ORGANIZER_LAST_NAME]", user.getLastName());

		subject = subject.replace("[ORGANIZER_ID]", user.getId());

		subject = subject.replace("[ORGANIZER_ACCOUNT_ID]", user.getAccountId());

		subject = subject.replace("[EVENT_NAME]", event.getName());

		subject = subject.replace("[EVENT_ORGANIZER_ID]", event.getOrganizerId());

		subject = subject.replace("[EVENT_ID]", event.getId());

		subject = subject.replace("[APP_ID]", appConfiguration.getId());

		subject = subject.replace("[APP_NAME]", appConfiguration.getName());

		subject = subject.replace("[APP_ORGANIZER]", appConfiguration.getOrganizerId());

		// TODO FOR TEMPLATE

		String template = emailTempate.getTemplate().replace("[ORGANIZER_NAME]", user.getFirstName() + " " + user.getLastName());

		template = template.replace("[ORGANIZER_EMAIL]", user.getEmail());

		template = subject.replace("[ORGANIZER_FIRST_NAME]", user.getFirstName());

		template = subject.replace("[ORGANIZER_LAST_NAME]", user.getLastName());

		template = template.replace("[ORGANIZER_ID]", user.getId());

		template = template.replace("[ORGANIZER_ACCOUNT_ID]", user.getAccountId());

		template = template.replace("[EVENT_NAME]", event.getName());

		template = template.replace("[EVENT_ORGANIZER_ID]", event.getOrganizerId());

		template = template.replace("[EVENT_ID]", event.getId());

		template = template.replace("[APP_ID]", appConfiguration.getId());

		template = template.replace("[APP_NAME]", appConfiguration.getName());

		template = template.replace("[APP_ORGANIZER]", appConfiguration.getOrganizerId());

		emailService.sendEmail(emailTempate.getTo(), subject, template);
	}

	public void sendEmailTo(User user, Event event, AppConfiguration appConfiguration, String templateUniqueName) {

		EmailTemplate emailTempate = this.getByUniqueName(templateUniqueName);

		// TODO FOR SUBJECT

		String subject = emailTempate.getSubject().replace("[ORGANIZER_NAME]", user.getFirstName() + " " + user.getLastName());

		subject = subject.replace("[ORGANIZER_EMAIL]", user.getEmail());

		subject = subject.replace("[ORGANIZER_FIRST_NAME]", user.getFirstName());

		subject = subject.replace("[ORGANIZER_LAST_NAME]", user.getLastName());

		subject = subject.replace("[ORGANIZER_ID]", user.getId());

		subject = subject.replace("[ORGANIZER_ACCOUNT_ID]", user.getAccountId());

		subject = subject.replace("[EVENT_NAME]", event.getName());

		subject = subject.replace("[EVENT_ORGANIZER_ID]", event.getOrganizerId());

		subject = subject.replace("[EVENT_ID]", event.getId());

		subject = subject.replace("[APP_ID]", appConfiguration.getId());

		subject = subject.replace("[APP_NAME]", appConfiguration.getName());

		subject = subject.replace("[APP_ORGANIZER]", appConfiguration.getOrganizerId());

		// TODO FOR TEMPLATE

		String template = emailTempate.getTemplate().replace("[ORGANIZER_NAME]", user.getFirstName() + " " + user.getLastName());

		template = template.replace("[ORGANIZER_EMAIL]", user.getEmail());

		template = template.replace("[ORGANIZER_FIRST_NAME]", user.getFirstName());

		template = template.replace("[ORGANIZER_LAST_NAME]", user.getLastName());

		template = template.replace("[ORGANIZER_ID]", user.getId());

		template = template.replace("[ORGANIZER_ACCOUNT_ID]", user.getAccountId());

		template = template.replace("[EVENT_NAME]", event.getName());

		template = template.replace("[EVENT_ORGANIZER_ID]", event.getOrganizerId());

		template = template.replace("[EVENT_ID]", event.getId());

		template = template.replace("[APP_ID]", appConfiguration.getId());

		template = template.replace("[APP_NAME]", appConfiguration.getName());

		template = template.replace("[APP_ORGANIZER]", appConfiguration.getOrganizerId());

		emailService.sendEmailByNvolv(user.getEmail(), subject, template);
	}
	
	public void sendEmailTo(String[] email ,User user, Event event, AppConfiguration appConfiguration, String templateUniqueName) {

		EmailTemplate emailTempate = this.getByUniqueName(templateUniqueName);

		// TODO FOR SUBJECT

		String subject = emailTempate.getSubject().replace("[ORGANIZER_NAME]", user.getFirstName() + " " + user.getLastName());

		subject = subject.replace("[ORGANIZER_EMAIL]", user.getEmail());

		subject = subject.replace("[ORGANIZER_FIRST_NAME]", user.getFirstName());

		subject = subject.replace("[ORGANIZER_LAST_NAME]", user.getLastName());

		subject = subject.replace("[ORGANIZER_ID]", user.getId());

		subject = subject.replace("[ORGANIZER_ACCOUNT_ID]", user.getAccountId());

		subject = subject.replace("[EVENT_NAME]", event.getName());

		subject = subject.replace("[EVENT_ORGANIZER_ID]", event.getOrganizerId());

		subject = subject.replace("[EVENT_ID]", event.getId());

		subject = subject.replace("[APP_ID]", appConfiguration.getId());

		subject = subject.replace("[APP_NAME]", appConfiguration.getName());

		subject = subject.replace("[APP_ORGANIZER]", appConfiguration.getOrganizerId());

		// TODO FOR TEMPLATE

		String template = emailTempate.getTemplate().replace("[ORGANIZER_NAME]", user.getFirstName() + " " + user.getLastName());

		template = template.replace("[ORGANIZER_EMAIL]", user.getEmail());

		template = template.replace("[ORGANIZER_FIRST_NAME]", user.getFirstName());

		template = template.replace("[ORGANIZER_LAST_NAME]", user.getLastName());

		template = template.replace("[ORGANIZER_ID]", user.getId());

		template = template.replace("[ORGANIZER_ACCOUNT_ID]", user.getAccountId());

		template = template.replace("[EVENT_NAME]", event.getName());

		template = template.replace("[EVENT_ORGANIZER_ID]", event.getOrganizerId());

		template = template.replace("[EVENT_ID]", event.getId());

		template = template.replace("[APP_ID]", appConfiguration.getId());

		template = template.replace("[APP_NAME]", appConfiguration.getName());

		template = template.replace("[APP_ORGANIZER]", appConfiguration.getOrganizerId());

		emailService.sendEmailByNvolv(email, subject, template);
	}

}
