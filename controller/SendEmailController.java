package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.service.EmailTemplateService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
@RequestMapping("organizer/{organizerId}/app/{appId}/event/{eventId}/mailtosales")
public class SendEmailController {


	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;
	
	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;
	
	@Resource(name = Constants.SERVICE_EMAIL_TEMPLATE)
	EmailTemplateService emailTemplateService;
	
	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage addActivity(@PathVariable("organizerId") String organizerId,@PathVariable("eventId") String eventId,@PathVariable("appId") String appId, Model model, HttpServletRequest request) throws Exception {
		
		ResponseMessage response=new ResponseMessage();
		
		AppConfiguration appConfiguration=appConfiguration_Service.getAppObject(appId);
		
		Event event=eventService.get(eventId, organizerId);
		
		User user=userService.get(organizerId);
		
		User partner=userService.get(appConfiguration.getPartnerId());;
		
		emailTemplateService.sendEmailTo(partner.getContact().getSalesEmailList(),user, event, appConfiguration,Constants.NVOLV_APP_SALES_EMAIL_TEMPLATE);
		
		response.setMessage("Email Has Been Sent");
		
		response.setHttpCode(Constants.SUCCESS_CODE);
		
		response.setMessageCode(Constants.SUCCESS_CODE);
		
		return response;
	}
	
}
