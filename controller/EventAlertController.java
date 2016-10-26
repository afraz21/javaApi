package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.push.PushData;
import org.iqvis.nvolv3.push.Query;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventAlertService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.PushLoggingService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.EVENT_ADVERT_BASE_URL)
public class EventAlertController {

	@Resource(name = Constants.PUSH_LOGGING_SERVICE)
	private PushLoggingService pushLoggingService;

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT_ALERT)
	private EventAlertService eventAdvertService;

	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;
	
	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@RequestMapping(value = Urls.ADD_EVENT_ADVERT, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid EventAlert eventAdvert, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add Event Alert");

		ResponseMessage response = new ResponseMessage();
		
		Event event = eventService.get(eventAdvert.getEventId(), eventAdvert.getOrganizerId());

		eventAdvert.setOrganizerId(organizerId);

		HashMap<String, List<AppConfiguration>> map = new HashMap<String, List<AppConfiguration>>();

		try {

			eventAdvert.setIsSent(eventAdvert.getSentNow() != null && eventAdvert.getSentNow() == true ? true : false);
			
			if(eventAdvert.getIsSent()==true){
				
				DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z");
				
				String eventTime=Utils.toEventTimeZoon(DateTime.now(DateTimeZone.UTC), event.getTimeZone());
				
				eventAdvert.setSentTime(formater.parseDateTime(eventTime + " Z"));
			}

			EventAlert addedVendor = eventAdvertService.add(eventAdvert);

			if (((eventAdvert.getSendAsPush() != null && eventAdvert.getSentNow() != null) && eventAdvert.getSendAsPush() && eventAdvert.getSentNow())) {

				final Query query = userDeviceInfoService.getQuery("eventId", eventAdvert.getEventId(), null,false);

				PushData pushData = eventAdvertService.getAlertPushDate(eventAdvert);
				
				query.setData(pushData);

				List<AppConfiguration> appList = new ArrayList<AppConfiguration>();

				if (map.containsKey(eventAdvert.getEventId())) {
					appList = new ArrayList<AppConfiguration>(map.get(eventAdvert.getEventId()));
				}
				else {

					List<String> keys = new ArrayList<String>();

					List<Object> values = new ArrayList<Object>();

					appList = new ArrayList<AppConfiguration>(appConfiguration_Service.get(keys, values));
				}

				List<String> checkList=new ArrayList<String>();
				
				for (AppConfiguration appConfiguration : appList) {
					
					if ((appConfiguration.getX_PARSE_APPLICATION_ID() == null || appConfiguration.getX_PARSE_REST_API_KEY() == null) || (appConfiguration.getX_PARSE_APPLICATION_ID() == "" || appConfiguration.getX_PARSE_REST_API_KEY() == "")) {

						continue;
					}
					

					if (checkList.contains(appConfiguration.getX_PARSE_APPLICATION_ID())) {
						continue;
					}
					else {
						checkList.add(appConfiguration.getX_PARSE_APPLICATION_ID());
					}

					Utils.sendAlert(query, appConfiguration.getX_PARSE_APPLICATION_ID(), appConfiguration.getX_PARSE_REST_API_KEY());

				}

				eventAdvert.setIsSent(true);

				pushLoggingService.add(query);
			}

			addedVendor = eventAdvertService.get(addedVendor.getId(), organizerId);
			// Response Message
			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Event Alert"));

			response.setRecordId(addedVendor.getId().toString());

			response.setRecord(addedVendor);

			response.setDetails_url(eventAdvertService.getEventAdvertDetailUrl(addedVendor, request));

			logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "EventAdvert"));

		}
		catch (Exception e) {

			logger.debug("Exception while adding EventAdvert", e);

			throw new Exception(e);
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT_ADVERT, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody EventAlert eventAdvert, @PathVariable("id") String vendorId, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a EventAdvert");

		ResponseMessage response = new ResponseMessage();

		eventAdvert.setId(vendorId);

		eventAdvert.setOrganizerId(organizerId);

		try {

			EventAlert editedVendor = eventAdvertService.edit(eventAdvert);

			editedVendor = eventAdvertService.get(editedVendor.getId(), organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Event Alert"));

			response.setRecordId(editedVendor.getId().toString());

			response.setRecord(editedVendor);

			response.setDetails_url(eventAdvertService.getEventAdvertDetailUrl(editedVendor, request));

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(vendorId, "EventAdvert");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT_ADVERT, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String eventAdvertId, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a EventAdvert");

		ResponseMessage response = new ResponseMessage();

		try {

			EventAlert vendor = eventAdvertService.get(eventAdvertId, organizerId);

			if (vendor != null) {

				if (!vendor.getOrganizerId().equals(organizerId)) {

					throw new NotFoundException(eventAdvertId, "Event Alert");

				}

				vendor.setIsDeleted(true);

				eventAdvertService.edit(vendor);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Event Alert"));

				response.setRecordId(eventAdvertId);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(eventAdvertId, "Event Alert");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(eventAdvertId, "Event Alert");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_EVENT_ADVERT, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public EventAlert get(@PathVariable("id") String Id, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a Event Alert");

		EventAlert existingEventAdvert = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingEventAdvert = eventAdvertService.get(Id, organizerId);

			if (existingEventAdvert == null || !existingEventAdvert.getOrganizerId().equals(organizerId)) {

				throw new NotFoundException(Id, "Event Alert");

			}

		}

		if (existingEventAdvert == null) {

			throw new NotFoundException(Id, "Event Alert");
		}

		return existingEventAdvert;
	}

	@RequestMapping(value = Urls.GET_EVENT_ADVERTS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<EventAlert> getAll(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all Event Alert");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<EventAlert> vendors = eventAdvertService.getAll(search, request, pageAble, organizerId);

		return vendors;
	}

}
