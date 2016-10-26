package org.iqvis.nvolv3.mobile.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.domain.DeviceInfo;
import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.domain.EventLanguage;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.PushNotificationConfiguration;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.Activity;
import org.iqvis.nvolv3.mobile.bean.Event;
import org.iqvis.nvolv3.mobile.bean.EventModifire;
import org.iqvis.nvolv3.mobile.bean.EventSponsor;
import org.iqvis.nvolv3.mobile.bean.EventTrack;
import org.iqvis.nvolv3.mobile.bean.EventVendor;
import org.iqvis.nvolv3.mobile.bean.ListEvent;
import org.iqvis.nvolv3.mobile.bean.MobileAttendee;
import org.iqvis.nvolv3.mobile.bean.MobileEventAlert;
import org.iqvis.nvolv3.mobile.service.MobileEventService;
import org.iqvis.nvolv3.mobile.service.MobileEventServiceImpl;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.AttendeeService;
import org.iqvis.nvolv3.service.EventAlertService;
import org.iqvis.nvolv3.service.PersonnelService;
import org.iqvis.nvolv3.service.TrackService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping("")
public class MobileEventController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.MOBILE_SERVICE_EVENT)
	private MobileEventService eventService;

	@Resource(name = Constants.SERVICE_EVENT_ALERT)
	private EventAlertService eventAlertService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@Autowired
	private MobileEventServiceImpl mobileEventServiceImpl;

	@Resource(name = Constants.SERVICE_PERSONNEL)
	private PersonnelService personnelService;

	@Resource(name = Constants.SERVICE_TRACK)
	private TrackService trackService;

	@Resource(name = Constants.SERVICE_ATTENDEE)
	private AttendeeService attendeeService;

	/*
	 * Get All Events with the APPID
	 */
	// @RequestMapping(value = Urls.MOBILE_APP_EVENTS, method = {
	// RequestMethod.GET, RequestMethod.PUT })
	@RequestMapping(value = "mobile/{appid}/organizer/{organizerId}/events", method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<ListEvent> getAppEvents(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "appid") String appId, @PathVariable(value = "organizerId") String organizerId, @RequestParam(value = "langCode", required = false) String code, @RequestParam(value = "pageNumber", required = false) Integer pageNumber, @RequestParam(value = "pageSize", required = false) Integer pageSize, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to show all events");

		List<ListEvent> events = null;

		Pageable pageAble = new PageRequest(0, 20);

		if (pageNumber != null && pageSize != null) {

			pageAble = new PageRequest(pageNumber - 1, pageSize);

		}

		AppConfiguration existingAppConfig = null;

		if (null != appId && !appId.equalsIgnoreCase("")) {

			existingAppConfig = appConfiguration_Service.getAppObject(appId);
		}

		if (null != existingAppConfig) {

			if (existingAppConfig.getEvents() != null && existingAppConfig.getEvents().size() > 0) {

				List<Track> tracks = trackService.getOrganizerTracks(organizerId);

				if (tracks == null) {
					tracks = new ArrayList<Track>();
				}

				events = mobileEventServiceImpl.getAllAppEvents(existingAppConfig.getEvents(), tracks, code);
			}
			else {

				throw new NotFoundException(appId, "Events");
			}
		}
		else {

			throw new NotFoundException(appId, "AppConfiguration");
		}

		int total = events.size();

		if (pageNumber != null) {

			List<List<ListEvent>> pages = Utils.getPages(events, pageSize);

			if ((pageNumber - 1) < pages.size()) {

				events = pages.get((pageNumber - 1));
			}
			else {

				events = new ArrayList<ListEvent>();
			}

		}

		return new PageImpl<ListEvent>(events, pageAble, total);
	}

	/*
	 * Get All Events with the Organizer Id
	 */

	/*
	 * @RequestMapping(value = Urls.MOBILE_GET_EVENTS, method = {
	 * RequestMethod.GET, RequestMethod.PUT })
	 * 
	 * @ResponseStatus(HttpStatus.OK)
	 * 
	 * @ResponseBody public Page<Event> getEvents(@RequestBody(required = false)
	 * 
	 * @Valid Criteria search, @PathVariable(value = "organizerId") String
	 * organizerId, @RequestParam(value = "langCode", required = false) String
	 * code, Model model, HttpServletRequest request) throws NotFoundException {
	 * 
	 * logger.debug("Received request to show all events");
	 * 
	 * Pageable pageAble = new PageRequest(0, 20);
	 * 
	 * if (search != null) { if (search.getQuery() != null) { if
	 * (search.getQuery().getPageNumber() != null &&
	 * search.getQuery().getPageSize() != null) {
	 * 
	 * pageAble = new PageRequest(search.getQuery().getPageNumber() - 1,
	 * search.getQuery().getPageSize()); ; } } }
	 * 
	 * Page<Event> events = eventService.getAll(search, code, pageAble,
	 * organizerId);
	 * 
	 * return events; }
	 */

	/*
	 * Get Event By Id Including EventConfiguration Object
	 */

	@SuppressWarnings("deprecation")
	@RequestMapping(value = Urls.MOBILE_GET_EVENT, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getEvent(@PathVariable("appid") String appId, @PathVariable("id") String eventid, @PathVariable("organizerId") String organizerId, @RequestParam(value = "langCode", required = false) String code, @RequestParam(value = "deviceToken", required = false) String deviceToken, @RequestParam(value = "deviceType", required = false) String deviceType, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to fetch an event");

		if (deviceToken != null) {
			DeviceInfo deviceInfo = userDeviceInfoService.get(deviceToken);

			if (deviceInfo == null) {

				deviceInfo = new DeviceInfo();
			}

			AppConfiguration appConfiguration = appConfiguration_Service.get(appId, organizerId);

			if (appConfiguration != null) {

				deviceInfo.setPushNotificationConfiguration(appConfiguration.getPushNotificationConfiguration());
			}
			else {

				deviceInfo.setPushNotificationConfiguration(new PushNotificationConfiguration());
			}

			deviceInfo.setAppId(appId);

			deviceInfo.setDeviceToken(deviceToken);

			deviceInfo.setOrganizerId(organizerId);

			deviceInfo.setEventId(eventid);

			deviceInfo.setDeviceType(deviceType);

			if (code != null && code != "") {
				
				EventLanguage event_languages = new EventLanguage();

				event_languages.setEventId(eventid);

				event_languages.setSelected_language(code);

				deviceInfo.setEvent_languages(event_languages);
			}

			userDeviceInfoService.add(deviceInfo);

		}

		ObjectMapper objectMapper = new ObjectMapper();

		// EventModifire is a MixIn Class in order to Set JsonIgnor(false)
		objectMapper.getSerializationConfig().addMixInAnnotations(Event.class, EventModifire.class);

		Event existingEvent = null;

		if (null != eventid && !eventid.equalsIgnoreCase("")) {

			existingEvent = eventService.get(eventid, organizerId, code);

			List<Attendee> attendeeList = attendeeService.get("events", eventid);

			if (attendeeList != null && attendeeList.size() > 50) {

				attendeeList = attendeeList.subList(0, 49);
			}

			existingEvent.setEventAttendees(MobileAttendee.toMobileAttendee(attendeeList));

			List<EventAlert> tempFeedList = eventAlertService.getListEventAlertsByEventId(eventid, organizerId);

			List<MobileEventAlert> temp = new ArrayList<MobileEventAlert>();

			if (temp != null) {

				for (EventAlert eventAlert : tempFeedList) {

					MobileEventAlert t = new MobileEventAlert(eventAlert);

					t.setGmtTime(eventAlert.getAlertScheduledTime());

					// if (t.getGmtTime() != null) {

					if (DateTime.now(DateTimeZone.UTC).compareTo(t.getStartDate()) >= 0 && DateTime.now(DateTimeZone.UTC).compareTo(eventAlert.getExpiryTimeGMT()) < 0) {

						temp.add(t);

					}
					// }

				}
			}

			existingEvent.setAlerts(temp);

		}

		if (existingEvent == null) {

			throw new NotFoundException(eventid, "Event");
		}

		HttpHeaders headers = new HttpHeaders();

		objectMapper.configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);

		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();

		deserializationConfig.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		objectMapper.configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);

		objectMapper.setDeserializationConfig(deserializationConfig);

		String result = objectMapper.writeValueAsString(existingEvent);

		headers.set("Content-Type", "application/json; charset=UTF-8");

		return new ResponseEntity<Object>(objectMapper.readValue(result, Object.class), headers, HttpStatus.OK);
	}

	@RequestMapping(value = Urls.MOBILE_GET_EVENT_TRACKS, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<EventTrack> getEventTracks(@PathVariable("id") String eventid, @PathVariable("organizerId") String organizerId, @RequestParam(value = "langCode", required = false) String code, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an event tracks");

		List<EventTrack> existingEventTracks = null;

		if (null != eventid && !eventid.equalsIgnoreCase("")) {

			existingEventTracks = eventService.getEventTracks(eventid, organizerId, code);

		}

		if (existingEventTracks == null) {
			throw new NotFoundException(eventid, "EventTracks");
		}

		return existingEventTracks;
	}

	@RequestMapping(value = Urls.MOBILE_GET_EVENT_ALERT, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<EventAlert> getAllEventAlerts(@RequestBody(required = false) @Valid Criteria search, @PathVariable("organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all alerts");

		Page<EventAlert> advertList = null;

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {

			if (search.getQuery() != null) {

				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		advertList = eventAlertService.getEventAlertsByEventId(eventId, organizerId, pageAble);

		return advertList;
	}

	@RequestMapping(value = Urls.MOBILE_GET_EVENT_PERSONNELS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Personnel> getEventPersonnels(@RequestBody(required = false) @Valid Criteria search, @RequestParam(value = "langCode", required = false) String code, @PathVariable("organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all personnels");

		List<Personnel> personnels = null;

		if (null != eventId && !eventId.equalsIgnoreCase("")) {

			personnels = eventService.getEventPersonnels(eventId, organizerId, code);

		}
		if (personnels == null) {

			throw new NotFoundException(eventId, "EventPersonnels");

		}

		return personnels;
	}

	@RequestMapping(value = Urls.MOBILE_GET_EVENT_VENDORS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<EventVendor> getEventVendors(@RequestBody(required = false) @Valid Criteria search, @RequestParam(value = "langCode", required = false) String code, @PathVariable("organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all vendors");

		List<EventVendor> vendors = null;

		if (null != eventId && !eventId.equalsIgnoreCase("")) {

			vendors = eventService.getEventVendors(eventId, organizerId, code);

		}

		if (vendors == null) {

			throw new NotFoundException(eventId, "EventVendors");

		}

		return vendors;
	}

	@RequestMapping(value = Urls.MOBILE_GET_EVENT_SPONSORS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<EventSponsor> getEventSponsors(@RequestBody(required = false) @Valid Criteria search, @RequestParam(value = "langCode", required = false) String code, @PathVariable("organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all vendors");

		List<EventSponsor> sponsors = null;

		if (null != eventId && !eventId.equalsIgnoreCase("")) {

			sponsors = eventService.getEventSponsors(eventId, organizerId, code);

		}
		if (sponsors == null) {

			throw new NotFoundException(eventId, "EventSponsors");

		}

		return sponsors;
	}

	@RequestMapping(value = Urls.MOBILE_GET_EVENT_ACTIVITIES, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Activity> getEventActivities(@RequestBody(required = false) @Valid Criteria search, @RequestParam(value = "langCode", required = false) String code, @PathVariable("organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all activities");

		List<Activity> eventActivities = null;

		if (null != eventId && !eventId.equalsIgnoreCase("")) {

			eventActivities = eventService.getEventActivities(eventId, organizerId, code);

		}
		if (eventActivities == null) {

			throw new NotFoundException(eventId, "EventActivities");

		}

		return eventActivities;
	}

}
