package org.iqvis.nvolv3.service;

import java.util.List;

import org.iqvis.nvolv3.domain.EmailTemplate;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;

public interface EmailTemplateService {

	EmailTemplate get(String id);

	public EmailTemplate get(String mailFor, String to);

	public EmailTemplate getEmailByTo(String to);

	public void sendEmailToNvolvSales(User user, Event event, AppConfiguration appConfiguration);
	
	public EmailTemplate getByUniqueName(String name);
	
	public void sendEmailTo(User user, Event event, AppConfiguration appConfiguration,String templateUniqueName);
	
	public void sendEmailTo(String[] email ,User user, Event event, AppConfiguration appConfiguration, String templateUniqueName);
}
