package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.EventSpeakers;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventObjectStatus;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.EventSponsor;
import org.iqvis.nvolv3.mobile.bean.EventTrack;
import org.iqvis.nvolv3.mobile.service.MobileEventService;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventAlertService;
import org.iqvis.nvolv3.service.EventCampaignService;
import org.iqvis.nvolv3.service.EventResourceService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.service.VendorService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.EVENT_BASE_URL)
public class EventController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.MOBILE_SERVICE_EVENT)
	private MobileEventService mobileEventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_VENDOR)
	private VendorService vendorService;

	@Resource(name = Constants.SERVICE_EVENT_ALERT)
	private EventAlertService eventAdvertService;

	@Resource(name = Constants.SERVICE_EVENT_RESOURCE)
	private EventResourceService eventResourceService;

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN)
	private EventCampaignService eventCampaignService;
	
	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;

	/*
	 * Get All Events with the optional criteria
	 */

	@RequestMapping(value = Urls.GET_EVENTS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Event> getEvents(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all events");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {

			if (search.getQuery() != null) {

				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<Event> events = eventService.getAll(search, request, pageAble, organizerId);

		return events;
	}

	@RequestMapping(value = "/test/test/test", method = { RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String test(@PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		eventService.get("547c0250e4b0041c97bec846").getBannerO();

		return "";
	}

	/*
	 * Create Event
	 */

	@RequestMapping(value = Urls.ADD_EVENT, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addEvent(@RequestBody @Valid Event event, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request, BindingResult result) throws Exception {

		logger.debug("Received request to add event");

		ResponseMessage response = new ResponseMessage();

		if (result.hasErrors()) {

			response.setMessage(result.getFieldError().getDefaultMessage());
			response.setMessageCode(Constants.ERROR_CODE);
			// do something
		}
		else {

			try {
				
				User user=userService.get(organizerId);
				
				event.setPartnerId(user.getPartnerId());
				
				event.setOrganizerId(organizerId);

				Event addedEvent = eventService.add(event);

				addedEvent = eventService.get(addedEvent.getId(), organizerId);

				response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Event"));

				response.setRecordId(addedEvent.getId().toString());

				response.setDetails_url(eventService.getEventDetailUrl(addedEvent, request));

				response.setRecord(addedEvent);

				List<String> l = new ArrayList<String>();

				l.add(addedEvent.getId());

				dataChangeLogService.add(l, "EVENT", "", "", Constants.LOG_ACTION_ADD, addedEvent.getClass().toString());

				logger.debug("Event has been added successfully");

			}
			catch (Exception e) {

				logger.debug("Exception while adding event", e);

				throw new Exception(e);
			}

			return response;
		}

		return response;
	}

	/*
	 * Update Event
	 */

	@RequestMapping(value = Urls.UPDATE_EVENT, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editEvent(@RequestBody Event event, @PathVariable("id") String eventid, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an event");

		ResponseMessage response = new ResponseMessage();

		// try {

		event.setOrganizerId(organizerId);

		Event editedEvent = eventService.edit(event, eventid, organizerId);

		response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Event"));

		response.setRecordId(editedEvent.getId().toString());

		editedEvent = eventService.get(editedEvent.getId(), organizerId);

		response.setRecord(editedEvent);

		response.setDetails_url(eventService.getEventDetailUrl(editedEvent, request));

		List<String> l = new ArrayList<String>();

		l.add(editedEvent.getId());

		dataChangeLogService.add(l, "EVENT", "", "", Constants.LOG_ACTION_UPDATE, editedEvent.getClass().toString());

		// }
		// catch (Exception e) {
		//
		// logger.debug("Exception while updating event", e);
		//
		// if (e.getClass().equals(NotFoundException.class)) {
		//
		// throw new NotFoundException(eventid, "Event");
		//
		// }
		// else {
		//
		// throw new Exception(e);
		// }
		// }

		return response;
	}

	/*
	 * Delete Event
	 */

	@RequestMapping(value = Urls.UPDATE_EVENT, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage deleteEvent(@PathVariable("id") String eventid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an event");

		ResponseMessage response = new ResponseMessage();

		try {

			Event event = eventService.get(eventid, organizerId);

			if (event != null) {

				event.setIsDeleted(true);

				eventService.edit(event, eventid, organizerId);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Event"));

				response.setRecordId(eventid);

				response.setDetails_url("");

				List<String> l = new ArrayList<String>();
				l.add(event.getId());
				dataChangeLogService.add(l, "EVENT", "", "", Constants.LOG_ACTION_DELETE, event.getClass().toString());

			}
			else {

				throw new NotFoundException(eventid, "Event");
			}

		}
		catch (Exception e) {

			logger.debug("Exception while deleting event", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(eventid, "Event");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	/*
	 * Get Event By Id
	 */

	@RequestMapping(value = Urls.GET_EVENT, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Event getEvent(@PathVariable("id") String eventid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an event");

		Event existingEvent = null;

		if (null != eventid && !eventid.equalsIgnoreCase("")) {

			existingEvent = eventService.get(eventid, organizerId);
		}

		if (existingEvent == null) {

			throw new NotFoundException(eventid, "Event");
		}

		// if (null != existingEvent.getEventConfiguration()) {
		//
		// existingEvent.getEventConfigurationEx().setSupported_languages(existingEvent.getSupported_languages());
		//
		// }

		return existingEvent;
	}

	/*
	 * Event Speakers
	 */

	/*
	 * @RequestMapping(value = Urls.GET_EVENTS_PERSONNELS, method =
	 * RequestMethod.GET)
	 * 
	 * @ResponseStatus(HttpStatus.OK)
	 * 
	 * @ResponseBody public Page<EventSpeakers>
	 * getEventsSpeakers(@RequestParam(value = "pageNumber", required = false)
	 * Integer pageNumber, @RequestParam(value = "pageSize", required = false)
	 * Integer pageSize, @PathVariable(value = "organizerId") String
	 * organizerId, @RequestParam(value = "langCode", required = false) String
	 * code, Model model, HttpServletRequest request) {
	 * 
	 * logger.debug("Received request to show all ");
	 * 
	 * Pageable pageAble = null;
	 * 
	 * if (pageNumber == null && pageSize == null) {
	 * 
	 * pageNumber = 0; pageSize = 20;
	 * 
	 * pageAble = new PageRequest(pageNumber, pageSize); } else { pageAble = new
	 * PageRequest(pageNumber - 1, pageSize); }
	 * 
	 * Page<EventSpeakers> events = eventService.getAllEventsSpeakers(request,
	 * pageAble, organizerId, code);
	 * 
	 * return events; }
	 */

	@RequestMapping(value = Urls.GET_EVENT_PERSONNELS, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public EventSpeakers getEventSpeakers(@PathVariable(value = "organizerId") String organizerId, @PathVariable("id") String eventid, @RequestParam(value = "langCode", required = false) String code, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all ");

		EventSpeakers eventSpeakers = eventService.getEventSpeakers(request, eventid, organizerId, code);

		List<Personnel> personnels = mobileEventService.getEventPersonnelList(eventid, organizerId, code, false);

		List<Personnel> keynotePersonnels = mobileEventService.getEventPersonnelList(eventid, organizerId, code, true);

		eventSpeakers.setKeyNotePersonnels(keynotePersonnels);

		eventSpeakers.setPersonnels(personnels);

		return eventSpeakers;
	}

	@RequestMapping(value = Urls.GET_EVENT_SPONSORS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<EventSponsor> getEventSponsors(@RequestBody(required = false) @Valid Criteria search, @PathVariable("organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all vendors");

		List<EventSponsor> sponsors = null;

		if (null != eventId && !eventId.equalsIgnoreCase("")) {

			sponsors = eventService.getEventSponsors(eventId, organizerId);

		}
		if (sponsors == null) {

			throw new NotFoundException(eventId, "EventSponsors");
		}

		return sponsors;
	}

	@RequestMapping(value = Urls.GET_EVENT_TRACKS, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<EventTrack> getEventTracks(@PathVariable("id") String eventid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException, Exception {

		logger.debug("Received request to fetch an event tracks");

		List<EventTrack> existingEventTracks = null;

		if (null != eventid && !eventid.equalsIgnoreCase("")) {

			existingEventTracks = eventService.getEventTracks(eventid, organizerId);

		}

		if (existingEventTracks == null) {

			throw new NotFoundException(eventid, "EventTracks");
		}

		return existingEventTracks;
	}

	// ///////////////////////////////////////Event Personnel
	// API////////////////////////////////////////////////////////

	@RequestMapping(value = "/{eventId}/objectstatus", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public EventObjectStatus OrganizerPortalEventStatus(@PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "eventId") String eventId, HttpServletRequest request) throws Exception {

		Event event = eventService.get(eventId, organizerId);

		boolean resources = false, personnel = false, alerts = false, sponsors = false, campaigns = false, tracks = false, activities = false, vendors = false, venue = false;

		if (event.getEventPersonnels() != null && event.getEventPersonnels().size() > 0) {
			personnel = true;
		}

		if (event.getSponsors() != null && event.getSponsors().size() > 0) {
			sponsors = true;
		}

		if (event.getTracks() != null && event.getTracks().size() > 0) {
			tracks = true;
		}

		if (event.getActivities() != null && event.getActivities().size() > 0) {
			activities = true;
		}

		if (event.getVenueId() != null && !event.getVenueId().equals("")) {
			venue = true;
		}

		long vendorCount = vendorService.count(eventId, organizerId);

		if (vendorCount > 0) {

			vendors = true;
		}

		long campaignCount = eventCampaignService.count(eventId, organizerId);

		if (campaignCount > 0) {

			campaigns = true;
		}

		long eventAlertCount = eventAdvertService.count(eventId, organizerId);

		if (eventAlertCount > 0) {

			alerts = true;
		}

		long resourceCount = eventResourceService.count(organizerId, eventId);

		if (resourceCount > 0) {

			resources = true;
		}

		return new EventObjectStatus(resources, personnel, alerts, sponsors, campaigns, tracks, activities, vendors, venue);
	}

}
