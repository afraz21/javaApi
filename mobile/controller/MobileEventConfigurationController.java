package org.iqvis.nvolv3.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.EventConfiguration;
import org.iqvis.nvolv3.domain.EventConfigurationUpdate;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.service.EventConfigurationService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
public class MobileEventConfigurationController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT_CONFIGURATION)
	private EventConfigurationService eventConfigurationService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@RequestMapping(value = Urls.MOBILE_EVENT_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseMessage addEventConfiguration(@RequestBody EventConfiguration config, @PathVariable("id") String eventId, @PathVariable("organizerId") String organizerId, HttpServletRequest request) {
		ResponseMessage message = new ResponseMessage();

		message.setRecord(eventConfigurationService.add(eventId, organizerId, config));

		message.setDetails_url(request.getRequestURL().toString());

		List<String> l = new ArrayList<String>();

		l.add(eventId);

		dataChangeLogService.add(l, "", Constants.EVENT_CONFIGURATION_LOG_KEY, "", Constants.LOG_ACTION_ADD, config.getClass().toString());

		return message;
	}

	@RequestMapping(value = Urls.MOBILE_EVENT_CONFIGURATION, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage addEventConfigurationUpdate(@RequestBody Object config, @PathVariable("id") String eventId, @PathVariable("organizerId") String organizerId, HttpServletRequest request) {
		ResponseMessage message = new ResponseMessage();

		System.out.println(config);

		message.setRecord(eventConfigurationService.add(eventId, organizerId, config));

		message.setDetails_url(request.getRequestURL().toString());

		List<String> l = new ArrayList<String>();

		l.add(eventId);

		dataChangeLogService.add(l, "", Constants.EVENT_CONFIGURATION_LOG_KEY, "", Constants.LOG_ACTION_UPDATE, config.getClass().toString());

		return message;
	}
	
	@RequestMapping(value = Urls.MOBILE_EVENT_CONFIGURATION+"/selected", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseMessage addEventConfigurationSelectedUpdate(@RequestBody @Valid EventConfigurationUpdate config, @PathVariable("id") String eventId, @PathVariable("organizerId") String organizerId, HttpServletRequest request) {
		ResponseMessage message = new ResponseMessage();

		System.out.println(config);

		message.setRecord(eventConfigurationService.add(eventId, organizerId, config));

		message.setDetails_url(request.getRequestURL().toString());

		List<String> l = new ArrayList<String>();

		l.add(eventId);

		dataChangeLogService.add(l, "", Constants.EVENT_CONFIGURATION_LOG_KEY, "", Constants.LOG_ACTION_UPDATE, config.getClass().toString());

		return message;
	}

	@RequestMapping(value = Urls.MOBILE_EVENT_CONFIGURATION, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Object getEventConfiguration(@PathVariable("id") String eventId, @PathVariable("organizerId") String organizerId, HttpServletRequest request) throws NotFoundException, Exception {

		// eventConfigurationService.updateEventConfiguration(organizerId,
		// eventId, "eventConfiguration.activity_feedback_enabled", true);

		return eventConfigurationService.get(eventId, organizerId);
	}

}
