package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventCampaignService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.EVENT_CAMPAIGN_BASE_URL)
public class EventCampaignController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN)
	private EventCampaignService eventService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Autowired
	private EventCampaignService eventCampaignService;

	/*
	 * Get All Events with the optional criteria
	 */

	@RequestMapping(value = Urls.GET_EVENT_CAMPAIGNS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<EventCampaign> getEventCampaigns(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all events");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
					;
				}
			}
		}

		Page<EventCampaign> events = eventService.getAll(search, request, pageAble, organizerId);

		return events;
	}

	/*
	 * Create Event
	 */

	@RequestMapping(value = Urls.ADD_EVENT_CAMPAIGN, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addEvent(@RequestBody @Valid EventCampaign eventCampaign, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request, BindingResult result) throws Exception {

		logger.debug("Received request to add eventcampaign");

		ResponseMessage response = new ResponseMessage();

		if (result.hasErrors()) {

			response.setMessage(result.getFieldError().getDefaultMessage());
			response.setMessageCode(Constants.ERROR_CODE);
			// do something
		}
		else {

			try {
				eventCampaign.setOrganizerId(organizerId);

				EventCampaign addedEvent = eventService.add(eventCampaign);

				addedEvent = eventCampaignService.get(addedEvent.getId(), organizerId);

				response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "EventCampaign"));

				response.setRecord(addedEvent);

				response.setRecordId(addedEvent.getId().toString());

				response.setDetails_url(eventService.getEvenCampaignDetailUrl(addedEvent, request));

				logger.debug("EventCampaign has been added successfully");

			}
			catch (Exception e) {

				logger.debug("Exception while adding EventCampaign", e);

				throw new Exception(e);
			}

			return response;
		}

		return response;
	}

	/*
	 * Update Event
	 */

	@RequestMapping(value = Urls.UPDATE_EVENT_CAMPAIGN, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editEvent(@RequestBody @Valid EventCampaign eventCampaign, @PathVariable("id") String campaignId, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an event");

		logger.debug(" Campaign ID ----- " + campaignId);

		logger.debug(" Event ID ----- " + eventCampaign.getEventId());

		ResponseMessage response = new ResponseMessage();

		String eventId = eventCampaign.getEventId();

		try {

			eventCampaign.setOrganizerId(organizerId);

			EventCampaign editedEventCampaign = eventService.edit(eventCampaign, campaignId, organizerId);

			editedEventCampaign = eventCampaignService.get(editedEventCampaign.getId(), organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "EventCampaign"));

			response.setRecord(editedEventCampaign);

			response.setRecordId(editedEventCampaign.getId().toString());

			response.setDetails_url(eventService.getEvenCampaignDetailUrl(editedEventCampaign, request));

			EventCampaign ec = eventCampaignService.get(editedEventCampaign.getId(), organizerId);

			Event event = eService.get(eventId, organizerId);

			List<DateTime> eventDates = new ArrayList<DateTime>();

			logger.debug(" Event ----- " + event.getId());

			logger.debug(" Event Campaign Schedule ----- " + ec.getId());

			eventDates = event.getEventDates();

			DateTime event_start_date = eventDates.get(0);

			DateTime event_end_date = eventDates.get(eventDates.size() - 1);

			DateTime campaign_start_date = null;

			DateTime campaign_end_date = null;

			Boolean flag1 = false;

			if (ec.getCompleteEvent() == false) {

				campaign_start_date = ec.getStartDate();

				campaign_end_date = ec.getEndDate();

			}

			if (ec.getCompleteEvent() == true) {

				flag1 = true;

			}
			else if ((campaign_start_date.compareTo(event_start_date) > 0 || campaign_start_date.compareTo(event_start_date) == 0) && (campaign_start_date.compareTo(event_end_date) < 0 || campaign_start_date.compareTo(event_end_date) == 0) && (campaign_end_date.compareTo(event_end_date) < 0 || campaign_end_date.compareTo(event_end_date) == 0) && (campaign_end_date.compareTo(event_start_date) > 0 || campaign_end_date.compareTo(event_start_date) == 0)) {

				flag1 = true;
			}
			else {

				flag1 = false;
			}

			List<String> l = new ArrayList<String>();

			l.add(eventId);

			if (flag1 == true) {

				dataChangeLogService.add(l, "EVENT", Constants.EVENT_CAMPAIGN_PARTICIPANT_LOG_KEY, editedEventCampaign.getId(), Constants.LOG_ACTION_UPDATE, editedEventCampaign.getClass().toString());
			}
			else {

				dataChangeLogService.add(l, "EVENT", Constants.EVENT_CAMPAIGN_PARTICIPANT_LOG_KEY, editedEventCampaign.getId(), Constants.LOG_ACTION_DELETE, editedEventCampaign.getClass().toString());
			}

		}
		catch (Exception e) {

			logger.debug("Exception while updating event", e);

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(campaignId, "Event");

			}
			else {
				e.printStackTrace();
				
				throw new Exception(e);
			}
		}

		return response;
	}

	/*
	 * Delete Event
	 */

	@RequestMapping(value = Urls.UPDATE_EVENT_CAMPAIGN, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage deleteEvent(@PathVariable("id") String eventCampaignId, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an EventCampaign");

		ResponseMessage response = new ResponseMessage();

		try {

			EventCampaign eventCampaign = eventService.get(eventCampaignId, organizerId);

			if (eventCampaign != null) {

				eventCampaign.setIsDeleted(true);

				eventService.edit(eventCampaign, eventCampaignId, organizerId);
				
				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "EventCampaign"));

				response.setRecordId(eventCampaignId);

				response.setDetails_url("");

				EventCampaign ec = eventCampaignService.get(eventCampaign.getId(), eventId);

				List<String> l = new ArrayList<String>();

				l.add(eventId);

				dataChangeLogService.add(l, "EVENT", Constants.EVENT_CAMPAIGN_PARTICIPANT_LOG_KEY, eventCampaign.getId(), Constants.LOG_ACTION_DELETE, eventCampaign.getClass().toString());

			}
			else {

				throw new NotFoundException(eventCampaignId, "EventCampaign");
			}

		}
		catch (Exception e) {

			logger.debug("Exception while deleting eventCampaign", e);

			if (e.getClass().equals(NotFoundException.class)) {
				
				throw new NotFoundException(eventCampaignId, "EventCampaign");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_EVENT_CAMPAIGN, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public EventCampaign getEventCampaign(@PathVariable("id") String eventCampaignid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an Campaign");

		EventCampaign existingEventCampaign = null;

		if (null != eventCampaignid && !eventCampaignid.equalsIgnoreCase("")) {

			existingEventCampaign = eventService.get(eventCampaignid, organizerId);
		}

		if (existingEventCampaign == null) {
			throw new NotFoundException(eventCampaignid, "EventCampaign");
		}

		return existingEventCampaign;
	}
}
