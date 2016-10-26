package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventLink;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.service.ActivityService;
import org.iqvis.nvolv3.service.EmailTemplateService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("application/{appId}/events/{eventId}")
public class LinkToAppEventController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;
	
	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;
	
	@Resource(name = Constants.SERVICE_EMAIL_TEMPLATE)
	EmailTemplateService emailTemplateService;

	@RequestMapping(value = "/action", method = { RequestMethod.PUT, RequestMethod.DELETE })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage linkEvent(@PathVariable("appId") String appId, @PathVariable("eventId") String eventId,@RequestParam(value="not_send_mail",required=false) boolean not_send_mail, Model model, HttpServletRequest request) throws Exception {

		AppConfiguration appConfiguration = appConfiguration_Service.getAppObject(appId);

		if (appConfiguration == null) {

			throw new NotFoundException(appId, "AppConfiguration");
		}

		Event event = eventService.get(eventId);

		if (event == null) {

			throw new NotFoundException(appId, "Event");
		}
		
		
		
		if((event.getOrganizerId()+"").equals(appConfiguration.getOrganizerId())){
			
			if(RequestMethod.PUT.toString().equals(request.getMethod())){
				
				appConfiguration.linkEvent(eventId);
				
				event.linkApp(new EventLink(appId, "ORGANIZER"));
								
			}else if(RequestMethod.DELETE.toString().equals(request.getMethod())){
				
				appConfiguration.deLinkEvent(eventId);
				
				event.deLinkApp(new EventLink(appId, "ORGANIZER"));
			}
			
			appConfiguration_Service.edit(appConfiguration, appId);
			
			eventService.edit(event, eventId, event.getOrganizerId());
			
		}else{
			
			if(RequestMethod.PUT.toString().equals(request.getMethod())){
				
				event.linkApp(new EventLink(appId, "PARTNER"));
				
			}else if(RequestMethod.DELETE.toString().equals(request.getMethod())){
				
				event.deLinkApp(new EventLink(appId, "PARTNER"));
			}
			
			eventService.edit(event, eventId, event.getOrganizerId());
		}
		
		if(RequestMethod.PUT.toString().equals(request.getMethod()) && !not_send_mail){
			
			try{
				
				User user=userService.get(event.getOrganizerId());
				
				emailTemplateService.sendEmailTo(user, event, appConfiguration,Constants.NVOLV_APP_APPROVED_EMAIL_TEMPLATE);
			}
			catch(Exception e){
				
				e.printStackTrace();
			}
			
		}
		
		ResponseMessage message=new ResponseMessage();
		
		message.setHttpCode(Constants.SUCCESS_CODE);
		
		message.setMessage("Event {id:"+eventId+"} Has Been Linked Successfuly With Application {id:"+appId+"}");

		message.setRecord(event);
		
		message.setRecordId(eventId);
		
		message.setMessageCode(Constants.SUCCESS_CODE);
		
		return message;
	}

}
