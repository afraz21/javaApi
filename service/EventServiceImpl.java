package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.ActivityPersonnels;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.bean.EventSpeakers;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.EventDao;
import org.iqvis.nvolv3.dao.LocationDao;
import org.iqvis.nvolv3.dao.PersonnelDao;
import org.iqvis.nvolv3.dao.ReferenceDataDao;
import org.iqvis.nvolv3.dao.SponsorDao;
import org.iqvis.nvolv3.dao.TracksDao;
import org.iqvis.nvolv3.dao.UserDao;
import org.iqvis.nvolv3.dao.VenueDao;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventConfiguration;
import org.iqvis.nvolv3.domain.EventFeedConfiguration;
import org.iqvis.nvolv3.domain.EventSelective;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.domain.UpdateEventPersonnelList;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.domain.UserFeedbackQuestion;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.AlreadyExistException;
import org.iqvis.nvolv3.exceptionHandler.AppTypeNotAllowedException;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.EventTrack;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.Query;
import org.iqvis.nvolv3.search.Where;
import org.iqvis.nvolv3.utils.AppType;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ch.lambdaj.Lambda;

import com.google.gson.Gson;

//------------------------------------//
@SuppressWarnings("restriction")
@Service(Constants.SERVICE_EVENT)
@Transactional
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;

	@Autowired
	private PersonnelDao personnelDao;

	@Autowired
	SponsorDao sponsorDao;

	@Autowired
	ReferenceDataDao referenceDataDao;

	@Autowired
	private TracksDao trackDao;

	@Autowired
	private LocationDao locationDao;

	@Autowired
	VenueDao venueDao;

	@Autowired
	UserDao userDao;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Autowired
	EventConfigurationService eventConfigurationService;

	public Page<Event> getAll(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String organizerId) {
		Page<Event> eventsPage = eventDao.getAll(Utils.parseCriteria(eventSearch, ""), pageAble, organizerId);

		List<Event> list = eventsPage.getContent();

		User u = userDao.get(organizerId);

		List<Venue> venList = u.getVenues();

		List<Event> listTemp = new ArrayList<Event>();

		for (Event event : list) {

			if (event.getVenueId() != null && !event.getVenueId().equalsIgnoreCase("")) {

				try {
					
				
				
				Venue venue = Lambda.select(venList, Lambda.having(Lambda.on(Venue.class).getId(), Matchers.equalToIgnoringCase(event.getVenueId() + ""))).get(0);

				event.setVenue(venue);
			
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				}

			// try {
			// Boolean b =
			// eventConfigurationService.getEventConfiguration().getFeed().isEvent_feed_moderation();
			if (Utils.toEventConfiguration(event.getEventConfiguration()) != null && Utils.toEventConfiguration(event.getEventConfiguration()).getFeed() != null) {

				event.setIsfeedModerated(Utils.toEventConfiguration(event.getEventConfiguration()).getFeed().isEvent_feed_moderation());
			}
			// }
			// catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			listTemp.add(event);

		}

		return new PageImpl<Event>(listTemp);
	}

	@Resource(name = Constants.SERVICE_REFERENCE_DATA)
	private ReferenceDataService reference_Data_Service;

	public Event get(String id, String organizerId) {

		Event ev = eventDao.get(id, organizerId);

		try {

			if (ev != null && ev.getVenueId() != null && !ev.getVenueId().equals("")) {
				Venue ven = null;
				try {
					ven = venueDao.get(ev.getVenueId(), organizerId);
				}
				catch (Exception e) {

				}
				ev.setVenue(ven);

				ReferenceData data = reference_Data_Service.get(Constants.EVENT_TYPE);

				if (data != null && data.getData() != null) {

					for (Data innerData : data.getData()) {

						if (innerData.getId().equals(ev.getEventTypeId())) {

							ev.setEventTypeDetail(innerData);

							break;

						}
					}
				}

				// List<Activity> ev_activity = ev.getActivities();
				//
				// for (Activity act : ev_activity) {
				//
				//
				//
				// act.setLocation(null);
				// }
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// EventConfiguration
		// eventConfiguration=Utils.toEventConfiguration(ev.getEventConfiguration());
		//
		// if (ev != null && eventConfiguration != null &&
		// eventConfiguration.getFeed() != null) {
		//
		// ev.setIsfeedModerated(eventConfiguration.getFeed().isEvent_feed_moderation());
		//
		// // eventConfigurationService.updateEventConfiguration(organizerId,
		// ev.getId(), "eventConfiguration.feed.Activity_feed_moderation",
		// ev.getIsfeedModerated());
		//
		// }

		return ev;
	}

	public Event add(Event event) throws Exception {

		if (null == event.getLanguages()) {

			List<String> languages = new ArrayList<String>();
			languages.add("EN");
			event.setLanguages(languages);
		}

		event.setEventConfiguration(eventConfigurationService.getEventConfiguration());

		if (event.getHasExpo() == null) {

			event.setHasExpo(false);
		}

		if (event.getIsActive() == null) {

			event.setIsActive(true);
		}

		if (event.getIsDeleted() == null) {

			event.setIsDeleted(false);
		}

		if (event.getIsDownloadable() == null) {

			event.setIsDownloadable(false);
		}

		if (event.getIsfeedModerated() == null) {

			event.setIsfeedModerated(false);

		}

		if (event.getIsLive() == null) {

			event.setIsLive(false);
		}

		if (event.getIsOverlayCampaignActive() == null) {

			event.setIsOverlayCampaignActive(false);
		}

		if (event.getIsMultiDate() == null) {

			event.setIsMultiDate(false);
		}

		if (event.getShowFeed() == null) {

			event.setShowFeed(false);
		}

		if (event.getIsLive()) {

			event.setIsLive(false);
		}

		if (event.getEventDates().size() > 0) {

			List<DateTime> eventDates = event.getEventDates();

			DateTime esd = eventDates.get(0);

			DateTime eed = eventDates.get(eventDates.size() - 1);

			DateTime esdGMT = esd;

			DateTime eedGMT = eed;

			if (event.getTimeZone() != null && !event.getTimeZone().equals("")) {

				String time_diff = event.getTimeZone();

				// System.out.println("Event Time Zone Difference : " +
				// time_diff);

				String str_time_diff[] = time_diff.split(":");

				int len = str_time_diff[0].length();

				// System.out.println("Length : " + len);

				if (len > 2) {

					char plus_minus = str_time_diff[0].charAt(0);

					int h = Integer.parseInt(str_time_diff[0].substring(1, 3));

					int m = Integer.parseInt(str_time_diff[1]);

					// System.out.println("Sign : " + plus_minus + " | Hour : "
					// + h + " | Min : " + m);

					if (plus_minus == '+') {

						esdGMT = esdGMT.minusMinutes(m);

						esdGMT = esdGMT.minusHours(h);

						eedGMT = eedGMT.minusMinutes(m);

						eedGMT = eedGMT.minusHours(h);

					}
					else if (plus_minus == '-') {

						esdGMT = esdGMT.plusMinutes(m);

						esdGMT = esdGMT.plusHours(h);

						eedGMT = eedGMT.plusMinutes(m);

						eedGMT = eedGMT.plusHours(h);

					}
				}
			}

			event.setEventStartDateGMT(esdGMT);

			event.setEventEndDateGMT(eedGMT);
		}

		event = eventDao.add(event);

		if (event.getIsfeedModerated() != null) {

			eventConfigurationService.updateEventConfiguration(event.getOrganizerId(), event.getId(), "eventConfiguration.feed.activity_feed_moderation", event.getIsfeedModerated());

			eventConfigurationService.updateEventConfiguration(event.getOrganizerId(), event.getId(), "eventConfiguration.feed.event_feed_moderation", event.getIsfeedModerated());
		}

		return event;
	}

	public Boolean delete(String id) {

		return eventDao.delete(id);
	}

	public Event edit(Event event, String eventid, String organizerId) throws Exception, NotFoundException {

		boolean flagForVenueChange = false;

		Event existingEvent = null;

		if (null != eventid && !StringUtils.isEmpty(eventid)) {

			existingEvent = eventDao.get(eventid, organizerId);

			if (existingEvent.getEventConfiguration() != null) {

				// EventConfiguration temp =
				// Utils.toEventConfiguration(existingEvent.getEventConfiguration());

				if (event.getIsfeedModerated() != null) {

					eventConfigurationService.updateEventConfiguration(organizerId, eventid, "eventConfiguration.feed.event_feed_moderation", event.getIsfeedModerated());

					eventConfigurationService.updateEventConfiguration(organizerId, eventid, "eventConfiguration.feed.activity_feed_moderation", event.getIsfeedModerated());

					existingEvent = eventDao.get(eventid, organizerId);
				}

			}

		}

		if (null == existingEvent) {

			throw new NotFoundException(eventid, "Event");
		}
		//
		// if (event.isEventLevelChange() != null) {
		//
		// existingEvent.setEventLevelChange(event.isEventLevelChange());
		// }
		// else {
		// existingEvent.setEventLevelChange(true);
		// }

		if (null != event.getName() && !StringUtils.isEmpty(event.getName())) {

			// if (!event.getName().equals(existingEvent.getName() + "")) {
			// event.setAppLevelChange(true);
			// }

			existingEvent.setName(event.getName());

		}

		if (event.getFeedback_configuration() != null) {

			if (!event.getFeedback_configuration().equals(existingEvent.getFeedback_configuration())) {

				List<String> l = new ArrayList<String>();

				l.add(existingEvent.getId());

				l.remove("");

				if (l.size() > 0) {
					if (event.getFeedback_configuration().getActivity_feedback() != null) {

						dataChangeLogService.add(l, "EVENT", Constants.LOG_USER_FEEDBACK_QUESTION, event.getFeedback_configuration().getActivity_feedback(), Constants.LOG_ACTION_UPDATE, UserFeedbackQuestion.class.toString());
					}

					if (event.getFeedback_configuration().getEvent_feedback() != null) {

						dataChangeLogService.add(l, "EVENT", Constants.LOG_USER_FEEDBACK_QUESTION, event.getFeedback_configuration().getEvent_feedback(), Constants.LOG_ACTION_UPDATE, UserFeedbackQuestion.class.toString());
					}

					if (event.getFeedback_configuration().getPersonnel_feedback() != null) {

						dataChangeLogService.add(l, "EVENT", Constants.LOG_USER_FEEDBACK_QUESTION, event.getFeedback_configuration().getPersonnel_feedback(), Constants.LOG_ACTION_UPDATE, UserFeedbackQuestion.class.toString());

					}

					if (event.getFeedback_configuration().getTrack_feedback() != null) {

						dataChangeLogService.add(l, "EVENT", Constants.LOG_USER_FEEDBACK_QUESTION, event.getFeedback_configuration().getTrack_feedback(), Constants.LOG_ACTION_UPDATE, UserFeedbackQuestion.class.toString());
					}

				}

				// System.out.println("-------------------------------------------");

			}

			existingEvent.setFeedback_configuration(event.getFeedback_configuration());
		}

		if (event.isDayLightSaving() != null) {

			existingEvent.setDayLightSaving(event.isDayLightSaving());
		}

		if (null != event.getSocialMediaLinks()) {
			existingEvent.setSocialMediaLinks(event.getSocialMediaLinks());
		}

		if (event.getLive() != null) {

			existingEvent.setLive(event.getLive());
		}

		if (event.getGoLiveModeration() != null) {

			existingEvent.setGoLiveModeration(event.getGoLiveModeration());
		}

		if (event.getEventTypeId() != null) {

			existingEvent.setEventTypeId((event.getEventTypeId()));
		}

		if (event.getEventHashTag() != null) {

			existingEvent.setEventHashTag(event.getEventHashTag());

			existingEvent.setSocialMediaHashTags(event.getEventHashTag());

			/// eventConfigurationService.updateEventConfiguration(organizerId,
			/// eventid, "eventConfiguration.feed.twitter_feed_pull_enabled",
			/// true);

		}

		if (null != event.isMultiDate()) {

			existingEvent.setMultiDate(event.isMultiDate());
		}

		if (event.getLinkedApp() != null) {

			existingEvent.setLinkedApp(event.getLinkedApp());
		}

		if (null != event.getLastModifiedBy() && !StringUtils.isEmpty(event.getLastModifiedBy())) {
			existingEvent.setLastModifiedBy(event.getLastModifiedBy());
		}

		if (event.getTimeZone() != null) {

			existingEvent.setTimeZone(event.getTimeZone());
		}

		if (null != event.getMultiLingual() && event.getMultiLingual().size() > 0) {
			List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

			if (null != existingEvent.getMultiLingual()) {
				finalLanguages = Utils.updateMultiLingual(existingEvent.getMultiLingual(), event.getMultiLingual());
			}
			else {

				finalLanguages = event.getMultiLingual();
			}

			if (isChange(event.getMultiLingual(), existingEvent.getMultiLingual())) {
				// event.setAppLevelChange(true);
			}

			existingEvent.setMultiLingual(finalLanguages);
		}

		if (null != event.getEventDates() && event.getEventDates().size() > 0) {
			if (isChange(event.getEventDates(), existingEvent.getEventDates())) {
				// event.setAppLevelChange(true);
			}

			existingEvent.setEventDates(event.getEventDates());
		}

		if (existingEvent.getEventDates().size() > 0) {

			List<DateTime> eventDates = existingEvent.getEventDates();

			DateTime esd = eventDates.get(0);

			DateTime eed = eventDates.get(eventDates.size() - 1);

			DateTime esdGMT = esd;

			DateTime eedGMT = eed;

			if (existingEvent.getTimeZone() != null && !existingEvent.getTimeZone().equals("")) {

				String time_diff = existingEvent.getTimeZone();

				// System.out.println("Event Time Zone Difference : " +
				// time_diff);

				String str_time_diff[] = time_diff.split(":");

				int len = str_time_diff[0].length();

				// System.out.println("Length : " + len);

				if (len > 2) {

					char plus_minus = str_time_diff[0].charAt(0);

					int h = Integer.parseInt(str_time_diff[0].substring(1, 3));

					int m = Integer.parseInt(str_time_diff[1]);

					// System.out.println("Sign : " + plus_minus + " | Hour : "
					// + h + " | Min : " + m);

					if (plus_minus == '+') {

						esdGMT = esdGMT.minusMinutes(m);

						esdGMT = esdGMT.minusHours(h);

						eedGMT = eedGMT.minusMinutes(m);

						eedGMT = eedGMT.minusHours(h);

					}
					else if (plus_minus == '-') {

						esdGMT = esdGMT.plusMinutes(m);

						esdGMT = esdGMT.plusHours(h);

						eedGMT = eedGMT.plusMinutes(m);

						eedGMT = eedGMT.plusHours(h);

					}
				}
			}

			existingEvent.setEventStartDateGMT(esdGMT);

			existingEvent.setEventEndDateGMT(eedGMT);
		}

		if (null != event.getLanguages() && event.getLanguages().size() > 0) {

			existingEvent.setLanguages(event.getLanguages());
		}

		if (event.getTracks() != null) {

			// if (isChange(event.getTracks(), existingEvent.getTracks())) {
			// // event.setAppLevelChange(true);
			// }

			existingEvent.setTracks(event.getTracks());
		}

		if (event.getSponsors() != null) {

			existingEvent.setSponsors(event.getSponsors());
		}

		if (event.getEventPersonnels() != null) {

			// if (isChange(event.getEventPersonnels(),
			// existingEvent.getEventPersonnels())) {
			// // event.setAppLevelChange(true);
			// }

			existingEvent.setEventPersonnels(event.getEventPersonnels());
		}

		if (event.getPictureO() != null && !StringUtils.isEmpty(event.getPictureO())) {

			existingEvent.setPicture(event.getPictureO());
		}

		if (event.getBannerO() != null && !StringUtils.isEmpty(event.getBannerO())) {

			existingEvent.setBanner(event.getBannerO());
		}

		if (event.getVenueId() != null) {

			if ("".equals(("" + event.getVenueId()).trim())) {

				event.setVenueId(null);
			}
			else {

				try {
					Venue venue = venueDao.get(event.getVenueId(), organizerId);

					existingEvent.setVenue(venue);
				}
				catch (Exception e) {

				}
			}

			flagForVenueChange = (existingEvent.getVenueId() + "").equals(event.getVenueId() + "") ? false : true;

			existingEvent.setVenueId(event.getVenueId());
		}

		if (event.getContact_Email() != null && !StringUtils.isEmpty(event.getContact_Email())) {

			existingEvent.setContact_Email(event.getContact_Email());
		}

		if (event.getContact_Phone_Primary() != null && !StringUtils.isEmpty(event.getContact_Phone_Primary())) {

			existingEvent.setContact_Phone_Primary(event.getContact_Phone_Primary());
		}

		if (event.getContact_Phone_Secondary() != null && !StringUtils.isEmpty(event.getContact_Phone_Secondary())) {

			existingEvent.setContact_Phone_Secondary(event.getContact_Phone_Secondary());
		}

		if (event.getContact_Fax_Primary() != null && !StringUtils.isEmpty(event.getContact_Fax_Primary())) {

			existingEvent.setContact_Fax_Primary(event.getContact_Fax_Primary());
		}

		if (event.getContact_Fax_Secondary() != null && !StringUtils.isEmpty(event.getContact_Fax_Secondary())) {

			existingEvent.setContact_Fax_Secondary(event.getContact_Fax_Secondary());
		}

		if (event.getEvent_Website() != null) {
			existingEvent.setEvent_Website(event.getEvent_Website());
		}

		if (event.isWebsite_isExternal() != null) {

			existingEvent.setWebsite_isExternal(event.isWebsite_isExternal());
		}

		if (event.getTimeZone() != null && !StringUtils.isEmpty(event.getTimeZone())) {

			existingEvent.setTimeZone(event.getTimeZone());
		}

		if (event.getIsDownloadable() != null && !StringUtils.isEmpty(event.getIsDownloadable())) {

			existingEvent.setIsDownloadable(event.getIsDownloadable());
		}

		if (event.getIsLive() != null && !StringUtils.isEmpty(event.getIsLive())) {

			existingEvent.setIsLive(event.getIsLive());
		}

		if (event.getIsDeleted() != null && !StringUtils.isEmpty(event.getIsDeleted())) {

			existingEvent.setIsDeleted(event.getIsDeleted());
		}

		existingEvent.setHasExpo(event.getHasExpo() != existingEvent.getHasExpo() ? event.getHasExpo() : existingEvent.getHasExpo());

		if (event.getExpoLocationId() != null) {

			existingEvent.setExpoLocationId(event.getExpoLocationId());
		}

		if (event.getAppType() != null) {

			if (AppType.valueOf(AppType.class, event.getAppType()) != null) {

				existingEvent.setAppType(event.getAppType());
			}
			else {
				throw new AppTypeNotAllowedException(event.getAppType());
			}
		}

		if (event.getTestOrganizersEmails() != null) {

			existingEvent.setTestOrganizersEmails(event.getTestOrganizersEmails());
		}

		if (event.getShowFeed() != null) {

			existingEvent.setShowFeed(event.getShowFeed());
		}

		if (event.getIsOverlayCampaignActive() != null) {

			existingEvent.setIsOverlayCampaignActive(event.getIsOverlayCampaignActive());
		}

		if (event.getIsfeedModerated() != null) {

			existingEvent.setIsfeedModerated(event.getIsfeedModerated());

		}

		// if (event.isAppLevelChange() != null) {
		//
		// existingEvent.setAppLevelChange(event.isAppLevelChange());
		//
		// List<String> ids = appConfiguration_Service.getIdsByEvent(eventid,
		// organizerId);
		//
		// for (String appId : ids) {
		//
		// AppConfiguration t = appConfiguration_Service.get(appId,
		// organizerId);
		//
		// t.setAppLevelChange(event.isAppLevelChange());
		//
		// appConfiguration_Service.edit(t, appId);
		// }
		//
		// }

		Event editedEvent = eventDao.edit(existingEvent);

		if (existingEvent.getEventHashTag() != null && existingEvent.getEventHashTag().size() > 0) {

			eventConfigurationService.updateEventConfiguration(organizerId, eventid, "eventConfiguration.feed.twitter_feed_pull_enabled", true);

		}
		else {

			eventConfigurationService.updateEventConfiguration(organizerId, eventid, "eventConfiguration.feed.twitter_feed_pull_enabled", false);
		}

		if (flagForVenueChange) {

			this.onVenueChangePostProcess(eventid);

			if (editedEvent.getVenueId() != null && editedEvent.getVenueId() != "") {

				List<String> l = new ArrayList<String>();

				l.add(editedEvent.getId());

				dataChangeLogService.add(l, "EVENT", Constants.VENUE_LOG_KEY, editedEvent.getVenueId(), Constants.LOG_ACTION_UPDATE, editedEvent.getClass().toString());

			}
		}

		existingEvent.setLastModifiedBy(event.getCreatedBy());

		return editedEvent;

	}

	public String getEventDetailUrl(Event event, HttpServletRequest request) {

		return eventDao.getEventDetailUrl(event, request);
	}

	public boolean validateEventAssociationWithOrganizer(String organizerId, String eventId) {

		return eventDao.validateEventAssociationWithOrganizer(organizerId, eventId);
	}

	public Page<Event> getAllEvents(HttpServletRequest request, Pageable pageAble, String organizerId) {

		return eventDao.getAllEvents(pageAble, organizerId);
	}

	public Page<EventSpeakers> getAllEventsSpeakers(HttpServletRequest request, Pageable pageAble, String organizerId, String code) {

		if (code == null || com.amazonaws.util.StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		List<EventSpeakers> eventSpekersList = new ArrayList<EventSpeakers>();

		Page<org.iqvis.nvolv3.domain.Event> eventsPage = eventDao.getAllEvents(pageAble, organizerId);

		List<org.iqvis.nvolv3.domain.Event> events = eventsPage.getContent();

		for (org.iqvis.nvolv3.domain.Event event : events) {

			/*
			 * Event Population
			 */

			EventSpeakers eventWithSpeakers = new EventSpeakers();

			eventWithSpeakers.setId(event.getId());

			eventWithSpeakers.setName(event.getName());

			/*
			 * ActivityPersonnels Population
			 */
			if (null != event.getActivities()) {

				List<Activity> keyNoteActivities = Lambda.select(event.getActivities(), Lambda.having(Lambda.on(Activity.class).getType(), Matchers.equalToIgnoringCase(Constants.ACTIVITY_TYPE_KEY_NOTE)));

				if (null != keyNoteActivities) {

					List<Personnel> personnels = new ArrayList<Personnel>();

					List<String> personnelIds = new ArrayList<String>();

					for (Activity activity : keyNoteActivities) {

						if (null != activity.getPersonnels()) {

							for (ActivityPersonnels personnel : activity.getPersonnels()) {

								personnelIds.addAll(personnel.getPersonnels());
							}

							try {

								personnels = personnelDao.getPersonnelsByIds(personnelIds, activity.getOrganizerId());

								if (null != personnels) {

									for (Personnel p : personnels) {

										try {
											if (null != p.getMultiLingual()) {
												p.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(p.getMultiLingual(), code));
											}
										}
										catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

							}
							catch (NotFoundException e) {

								e.printStackTrace();
							}

						}
					}

					eventWithSpeakers.setKeyNotePersonnels(personnels);

					personnelIds = new ArrayList<String>();

					personnels = new ArrayList<Personnel>();

					for (Activity activity : event.getActivities()) {

						if (null != activity.getPersonnels()) {

							for (ActivityPersonnels personnel : activity.getPersonnels()) {

								personnelIds.addAll(personnel.getPersonnels());
							}

							try {

								personnels = personnelDao.getPersonnelsByIds(personnelIds, activity.getOrganizerId());

								if (null != personnels) {

									for (Personnel p : personnels) {

										try {
											if (null != p.getMultiLingual()) {
												p.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(p.getMultiLingual(), code));
											}
										}
										catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

							}
							catch (NotFoundException e) {

								e.printStackTrace();
							}

						}
					}

					eventWithSpeakers.setPersonnels(personnels);

				}

			}

			eventSpekersList.add(eventWithSpeakers);
		}

		Page<EventSpeakers> eventPage = new PageImpl<EventSpeakers>(eventSpekersList, pageAble, eventSpekersList.size());

		return eventPage;
	}

	public EventSpeakers getEventSpeakers(HttpServletRequest request, String eventId, String organizerId, String code) {
		// TODO FIX EVENT KEY-NOTE-PERSONNEL
		if (code == null || com.amazonaws.util.StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		Event event = eventDao.get(eventId, organizerId);

		/*
		 * Event Population
		 */

		EventSpeakers eventWithSpeakers = new EventSpeakers();

		if (event != null) {

			eventWithSpeakers.setId(event.getId());

			eventWithSpeakers.setName(event.getName());

			/*
			 * ActivityPersonnels Population
			 */
			if (null != event.getActivities()) {

				List<Activity> keyNoteActivities = Lambda.select(event.getActivities(), Lambda.having(Lambda.on(Activity.class).getType(), Matchers.equalTo(Constants.ACTIVITY_TYPE_KEY_NOTE)));

				if (null != keyNoteActivities) {

					List<Personnel> personnels = new ArrayList<Personnel>();

					List<String> personnelIds = new ArrayList<String>();

					for (Activity activity : keyNoteActivities) {

						if (null != activity.getPersonnels()) {

							for (ActivityPersonnels personnel : activity.getPersonnels()) {

								personnelIds.addAll(personnel.getPersonnels());
							}

							try {

								personnels = personnelDao.getPersonnelsByIds(personnelIds, activity.getOrganizerId());

								if (null != personnels) {

									for (Personnel p : personnels) {

										try {
											if (null != p.getMultiLingual()) {
												p.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(p.getMultiLingual(), code));
											}
										}
										catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

							}
							catch (NotFoundException e) {

								e.printStackTrace();
							}

						}
					}

					eventWithSpeakers.setKeyNotePersonnels(personnels);

					personnelIds = new ArrayList<String>();

					personnels = new ArrayList<Personnel>();

					for (Activity activity : event.getActivities()) {

						if (null != activity.getPersonnels()) {

							for (ActivityPersonnels personnel : activity.getPersonnels()) {

								personnelIds.addAll(personnel.getPersonnels());
							}

							try {

								if (personnelIds != null && personnelIds.size() > 0) {

									personnels = personnelDao.getPersonnelsByIds(personnelIds, activity.getOrganizerId());

									if (null != personnels) {

										for (Personnel p : personnels) {

											try {
												if (null != p.getMultiLingual()) {
													p.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(p.getMultiLingual(), code));
												}
											}
											catch (Exception e) {
												e.printStackTrace();
											}
										}
									}

								}
							}
							catch (NotFoundException e) {

								e.printStackTrace();
							}

						}
					}

					eventWithSpeakers.setPersonnels(personnels);

				}

			}

		}

		return eventWithSpeakers;

	}

	public List<org.iqvis.nvolv3.mobile.bean.EventSponsor> getEventSponsors(String eventId, String organizerId) {

		org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId, organizerId);

		List<org.iqvis.nvolv3.mobile.bean.EventSponsor> eventSponsors = new ArrayList<org.iqvis.nvolv3.mobile.bean.EventSponsor>();

		if (event != null && event.getSponsors() != null) {

			for (org.iqvis.nvolv3.bean.EventSponsor eventSponsor : event.getSponsors()) {

				Sponsor sponsor = sponsorDao.get(eventSponsor.getSponsorId());

				if (sponsor != null) {

					org.iqvis.nvolv3.mobile.bean.EventSponsor newEventSponsor = new org.iqvis.nvolv3.mobile.bean.EventSponsor();

					newEventSponsor.setId(sponsor.getId());

					newEventSponsor.setName(sponsor.getName());

					// newEventSponsor.setPicture(sponsor.getPicture());

					newEventSponsor.setPicture(sponsor.getPictureO());

					newEventSponsor.setType(sponsor.getType());

					newEventSponsor.setIsActive(sponsor.getIsActive());

					newEventSponsor.setIsDeleted(sponsor.getIsDeleted());

					newEventSponsor.setMultiLingual(sponsor.getMultiLingual());

					newEventSponsor.setOrganizerId(sponsor.getOrganizerId());

					newEventSponsor.setFirstName(sponsor.getFirstName());

					newEventSponsor.setLastName(sponsor.getLastName());

					newEventSponsor.setCreatedBy(sponsor.getCreatedBy());

					newEventSponsor.setEmail(sponsor.getEmail());

					newEventSponsor.setUser(sponsor.getUser());

					newEventSponsor.setInvite(sponsor.getInvite());

					newEventSponsor.setPhone(sponsor.getPhone());

					newEventSponsor.setBusinessCategory(sponsor.getBusinessCategory());

					newEventSponsor.setCreatedDate(sponsor.getCreatedDate());

					newEventSponsor.setLastModifiedBy(sponsor.getLastModifiedBy());

					newEventSponsor.setLastModifiedDate(sponsor.getLastModifiedDate());

					newEventSponsor.setVersion(sponsor.getVersion());

					Data sponsorCategory = null;
					try {
						sponsorCategory = referenceDataDao.getById(eventSponsor.getSponsorCategoryId(), sponsor.getOrganizerId());
					}
					catch (NotFoundException e) {

						e.printStackTrace();
					}

					newEventSponsor.setSponsorCategory(sponsorCategory);

					newEventSponsor.setVersion(sponsor.getVersion());

					eventSponsors.add(newEventSponsor);
				}

			}

		}

		return eventSponsors;
	}

	public List<EventTrack> getEventTracks(String eventId, String organizerId) throws NotFoundException {

		org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId, organizerId);

		Venue venue = null;
		if (event == null) {
			throw new NotFoundException(eventId, "Event");
		}

		if (event.getVenueId() != null && !new String(event.getVenueId() + "").equals("")) {

			try {

				venue = venueDao.get(event.getVenueId(), organizerId);

			}
			catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/*
		 * Tracks Population
		 */
		List<EventTrack> mobileTracks = new ArrayList<EventTrack>();

		if (event.getTracks() != null) {

			// Getting event Tracks id

			List<String> eventTracksList = new ArrayList<String>();

			for (org.iqvis.nvolv3.bean.EventTrack track : event.getTracks()) {

				eventTracksList.add(track.getTrackId());
			}

			/*
			 * 
			 * Query here to get all tracks in go
			 */

			List<Track> existingTracks = trackDao.getTracks(eventTracksList, organizerId);

			/*
			 * 
			 * We will loop here all tracks
			 */

			for (Track existingTrack : existingTracks) {

				EventTrack mobileTrack = new EventTrack();

				try {

					if (null != existingTrack) {

						mobileTrack.setId(existingTrack.getId());

						List<org.iqvis.nvolv3.bean.EventTrack> eventtrack = Lambda.select(event.getTracks(), Lambda.having(Lambda.on(org.iqvis.nvolv3.bean.EventTrack.class).getTrackId(), Matchers.equalTo(existingTrack.getId())));

						if (eventtrack.size() > 0) {

							mobileTrack.setColorCode(eventtrack.get(0).getColorCode());

						}

						if (eventtrack.size() > 0) {

							mobileTrack.setSortOrder(eventtrack.get(0).getSortOrder() + "");

						}
						mobileTrack.setIsDeleted(existingTrack.getIsDeleted());

						try {

							mobileTrack.setMultiLingual(existingTrack.getMultiLingual());

						}
						catch (Exception e) {
							e.printStackTrace();
						}

						mobileTrack.setName(existingTrack.getName());

						mobileTrack.setPicture(existingTrack.getPictureO());

						mobileTrack.setOrganizerId(event.getOrganizerId());

						mobileTrack.setVersion(existingTrack.getVersion());

						mobileTrack.setCreatedBy(existingTrack.getCreatedBy());

						mobileTrack.setLastModifiedBy(existingTrack.getLastModifiedBy());

						mobileTrack.setLastModifiedDate(existingTrack.getLastModifiedDate());

						// Session of tracks

						if (event.getActivities() != null) {

							List<org.iqvis.nvolv3.mobile.bean.Activity> trackSessions = new ArrayList<org.iqvis.nvolv3.mobile.bean.Activity>();

							for (Activity activity : event.getActivities()) {

								if (activity.getTracks() != null && activity.getTracks().contains(existingTrack.getId())) {

									org.iqvis.nvolv3.mobile.bean.Activity trackActivity = new org.iqvis.nvolv3.mobile.bean.Activity();

									trackActivity.setId(activity.getId());

									trackActivity.setEventId(activity.getEventId());

									trackActivity.setOrganizerId(activity.getOrganizerId());

									trackActivity.setStartTime(activity.getStartTime());

									trackActivity.setEndTime(activity.getEndTime());

									trackActivity.setEventDate(activity.getEventDate());

									trackActivity.setIsActive(activity.getIsActive());

									trackActivity.setIsDeleted(activity.getIsDeleted());

									// Activity Location//

									Location location = null;
									try {
										if (activity.getLocation() != null && !activity.getLocation().equals("")) {

											List<Location> activityLocation = Lambda.select(venue.getLocations(), Lambda.having(Lambda.on(Location.class).getId(), Matchers.equalTo(activity.getLocation())));

											// location =
											// locationDao.get(activity.getLocation(),
											// activity.getOrganizerId());
											if (activityLocation.size() > 0) {

												location = activityLocation.get(0);
											}

											trackActivity.setLocation(location);
										}
									}
									catch (Exception e) {

										e.printStackTrace();
									}
									// /

									trackActivity.setType(activity.getType());

									trackActivity.setVersion(activity.getVersion());

									trackActivity.setCreatedBy(activity.getCreatedBy());

									trackActivity.setCreatedDate(activity.getCreatedDate());

									trackActivity.setLastModifiedBy(activity.getLastModifiedBy());

									trackActivity.setLastModifiedDate(activity.getLastModifiedDate());

									trackActivity.setName(activity.getName());

									trackActivity.setCreatedDate(activity.getCreatedDate());

									trackActivity.setMultiLingual(activity.getMultiLingual());

									List<org.iqvis.nvolv3.mobile.bean.ActivityPersonnels> tracksActivityPersonnels = new ArrayList<org.iqvis.nvolv3.mobile.bean.ActivityPersonnels>();

									List<Personnel> personnels = new ArrayList<Personnel>();

									for (ActivityPersonnels aPersonnels : activity.getPersonnels()) {

										org.iqvis.nvolv3.mobile.bean.ActivityPersonnels trackPersonnel = new org.iqvis.nvolv3.mobile.bean.ActivityPersonnels();

										// trackPersonnel.setId(aPersonnels.getId());

										Data data = referenceDataDao.getById(aPersonnels.getTypeId(), activity.getOrganizerId());

										trackPersonnel.setType(data);

										personnels = personnelDao.getPersonnelsByIds(aPersonnels.getPersonnels(), activity.getOrganizerId());

										if (null != personnels) {

											for (Personnel p : personnels) {

												try {

													p.setMultiLingual(p.getMultiLingual());

												}
												catch (Exception e) {
													e.printStackTrace();
												}
											}
										}

										trackPersonnel.setPersonnels(personnels);

										tracksActivityPersonnels.add(trackPersonnel);
									}

									trackActivity.setPersonnels(personnels);

									trackSessions.add(trackActivity);

								}
							}
							// Comment mobileTrack.setSessions to remove
							// sessions from MobileEvent
							mobileTrack.setSessions(trackSessions);

						}

						mobileTracks.add(mobileTrack);

					}
				}
				catch (NotFoundException e) {

					e.printStackTrace();
				}

			}

		}

		return mobileTracks;
	}

	public Event get(String id) {

		return eventDao.get(id);
	}

	public Page<org.iqvis.nvolv3.bean.Personnel> getEventPersonnels(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String organizerId, String eventId) {

		List<org.iqvis.nvolv3.bean.Personnel> personnels = new ArrayList<org.iqvis.nvolv3.bean.Personnel>();

		List<String> personnelIds = new ArrayList<String>();

		// Getting event for event personnel
		// Event event = eventDao.get(eventId);
		int count =0;
		
		try {
			count = getEventPersonnels("", eventId, organizerId).size();
		}
		catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Where> whereNewList = new ArrayList<Where>();

		List<OrderBy> orderByNewList = new ArrayList<OrderBy>();

		if (eventSearch != null && eventSearch.getQuery() != null) {

			if (eventSearch.getQuery().getOrderBy() != null) {

				List<OrderBy> orderBy = new ArrayList<OrderBy>(eventSearch.getQuery().getOrderBy());

				if (orderBy != null) {
					int i = 0;

					for (OrderBy orderBy2 : orderBy) {

						if (orderBy2.getField().equals("sortOrder") || orderBy2.getField().equals("isFeatured")) {

							eventSearch.getQuery().getOrderBy().remove(i);

							orderByNewList.add(orderBy2);
						}

						i = i + 1;
					}
				}

			}

			if (eventSearch.getQuery().getWhere() != null) {

				List<Where> where = new ArrayList<Where>(eventSearch.getQuery().getWhere());

				if (where != null) {

					int i = 0;

					for (Where where2 : where) {

						if (where2.getFieldName().equals("sortOrder") || where2.getFieldName().equals("isFeatured")) {

							eventSearch.getQuery().getWhere().remove(i);

							whereNewList.add(where2);

						}

						i = i + 1;
					}
				}

			}

		}

		Query query = new Query();

		query.setOrderBy(orderByNewList);

		query.setWhere(whereNewList);

		Criteria eventPersonnelSearch = new Criteria();

		eventPersonnelSearch.setQuery(query);

		List<EventPersonnel> eventPersonnelList = eventDao.getAllEventPersonnels(Utils.parseQuery(eventPersonnelSearch, "eventPersonnels."), eventPersonnelSearch, eventId, pageAble);

		if (eventPersonnelList != null) {

			for (EventPersonnel ep : eventPersonnelList) {

				if (ep != null) {

					personnelIds.add(ep.getPersonnelId());
				}
			}
		}

		org.springframework.data.mongodb.core.query.Criteria criteria = Utils.parseQuery(eventSearch, "personnels.");

		criteria.and("personnels._id").in(personnelIds);

		Page<Personnel> personnelsPage = personnelDao.getAll(criteria, eventSearch, null, organizerId);

		List<String> mergeList = new ArrayList<String>();

		if (eventPersonnelList != null) {

			for (EventPersonnel eventPersonnel : eventPersonnelList) {

				for (Personnel personnel : personnelsPage) {

					if (eventPersonnel.getPersonnelId().equals(personnel.getId())) {

						org.iqvis.nvolv3.bean.Personnel p = new org.iqvis.nvolv3.bean.Personnel();

						p.setId(personnel.getId());

						p.setVersion(personnel.getVersion());

						p.setName(personnel.getName());

						p.setPicture(personnel.getPictureO());

						p.setEmail(personnel.getEmail());

						p.setPhone(personnel.getPhone());

						p.setOrganizerId(personnel.getOrganizerId());

						p.setMultiLingual(personnel.getMultiLingual());

						p.setType(personnel.getType());

						p.setIsDeleted(personnel.getIsDeleted());

						p.setIsActive((boolean) personnel.getIsActive());

						p.setActivities(personnel.getActivities());

						p.setSortOrder(eventPersonnel.getSortOrder());

						p.setFeatured(eventPersonnel.isFeatured());

						if (mergeList.contains(personnel.getId())) {
							continue;

						}
						else {

							mergeList.add(personnel.getId());
						}

						personnels.add(p);

					}

				}
			}

		}

	//	Page<org.iqvis.nvolv3.bean.Personnel> eventPage = new PageImpl<org.iqvis.nvolv3.bean.Personnel>(personnels, pageAble, personnels.size());
		
		Page<org.iqvis.nvolv3.bean.Personnel> eventPage = new PageImpl<org.iqvis.nvolv3.bean.Personnel>(personnels, pageAble, count);
		
		
		return eventPage;
	}

	public EventPersonnel add(EventPersonnel eventPersonnel, String eventId, String organizerId) throws Exception {

		EventPersonnel ep = eventDao.get(eventPersonnel.getPersonnelId(), eventId, organizerId);

		if (ep != null) {

			throw new AlreadyExistException(eventPersonnel.getPersonnelId(), "Event Personnel");
		}

		return eventDao.add(eventPersonnel, eventId, organizerId);
	}

	public List<EventPersonnel> updateBulk(UpdateEventPersonnelList eventPersonnelPostList, String eventId, String organizerId) throws Exception {

		List<EventPersonnel> dbPersonnels = eventDao.getEventPersonnels("", eventId, organizerId);
		
		for (EventPersonnel eventPersonnel : eventPersonnelPostList.getData()) {

			for (EventPersonnel dbPersonnel : dbPersonnels) {

				if (dbPersonnel.getPersonnelId().equals(eventPersonnel.getPersonnelId())) {

					if (eventPersonnel.getSortOrder() != null) {

						dbPersonnel.setSortOrder(eventPersonnel.getSortOrder());
					}

					if (null != eventPersonnel.isFeatured()) {

						dbPersonnel.setFeatured(eventPersonnel.isFeatured());
					}

					if (eventPersonnel.getQuestionnaireType() != null) {
						
						dbPersonnel.setQuestionnaireType(eventPersonnel.getQuestionnaireType());
					}

					if (eventPersonnel.getQuestionnaireId() != null) {

						dbPersonnel.setQuestionnaireId(eventPersonnel.getQuestionnaireId());
					}

					dbPersonnel.setVersion(dbPersonnel.getVersion() + 1);

					List<String> l = new ArrayList<String>();

					l.add(eventId);

					dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, dbPersonnel.getPersonnelId(), Constants.LOG_ACTION_UPDATE, EventPersonnel.class.toString());

				}

			}
		}

		if (dbPersonnels.size() > 0) {

			eventDao.updateEvent(organizerId, eventId, "eventPersonnels", dbPersonnels);
		}

		return dbPersonnels;
	}

	public EventPersonnel edit(EventPersonnel eventPersonnel, String eventId, String organizerId) throws Exception {

		EventPersonnel ep = eventDao.get(eventPersonnel.getPersonnelId(), eventId, organizerId);

		if (ep != null) {

			if (eventPersonnel.getSortOrder() != null) {

				ep.setSortOrder(eventPersonnel.getSortOrder());
			}

			if (null != eventPersonnel.isFeatured()) {

				ep.setFeatured(eventPersonnel.isFeatured());
			}

			if (eventPersonnel.getQuestionnaireType() != null) {
				ep.setQuestionnaireType(eventPersonnel.getQuestionnaireType());
			}

			if (eventPersonnel.getQuestionnaireId() != null) {
				ep.setQuestionnaireId(eventPersonnel.getQuestionnaireId());
			}

			ep.setVersion(ep.getVersion() + 1);

			return eventDao.edit(ep, eventId, organizerId);

		}
		else {

			throw new NotFoundException(eventPersonnel.getPersonnelId(), "EventPersonnel");

		}

	}

	public String delete(String eventPersonnelId, String eventId, String organizerId) throws Exception {

		return eventDao.delete(eventPersonnelId, eventId, organizerId);
	}

	public org.iqvis.nvolv3.bean.Personnel get(String eventPersonnelId, String eventId, String organizerId) throws Exception {

		EventPersonnel ep = eventDao.get(eventPersonnelId, eventId, organizerId);

		if (ep == null) {

			throw new NotFoundException(eventPersonnelId, "EventPersonnel");
		}

		Personnel personnel = personnelDao.get(eventPersonnelId, organizerId);

		org.iqvis.nvolv3.bean.Personnel p = new org.iqvis.nvolv3.bean.Personnel();

		p.setId(personnel.getId());

		p.setVersion(personnel.getVersion());

		p.setName(personnel.getName());

		p.setPicture(personnel.getPictureO());

		p.setEmail(personnel.getEmail());

		p.setPhone(personnel.getPhone());

		p.setOrganizerId(personnel.getOrganizerId());

		p.setMultiLingual(personnel.getMultiLingual());

		p.setType(personnel.getType());

		p.setIsDeleted(personnel.getIsDeleted());

		p.setIsActive(personnel.getIsActive());

		p.setActivities(personnel.getActivities());

		p.setSortOrder(ep.getSortOrder());

		p.setFeatured((boolean) ep.isFeatured());

		return p;
	}


	public org.iqvis.nvolv3.bean.Personnel get(EventPersonnel ep,Personnel personnel ,String eventId, String organizerId) throws Exception {

		org.iqvis.nvolv3.bean.Personnel p = new org.iqvis.nvolv3.bean.Personnel();

		p.setId(personnel.getId());

		p.setVersion(personnel.getVersion());

		p.setName(personnel.getName());

		p.setPicture(personnel.getPictureO());

		p.setEmail(personnel.getEmail());

		p.setPhone(personnel.getPhone());

		p.setOrganizerId(personnel.getOrganizerId());

		p.setMultiLingual(personnel.getMultiLingual());

		p.setType(personnel.getType());

		p.setIsDeleted(personnel.getIsDeleted());

		p.setIsActive(personnel.getIsActive());

		p.setActivities(personnel.getActivities());

		p.setSortOrder(ep.getSortOrder());

		p.setFeatured((boolean) ep.isFeatured());

		return p;
	}

	
	
	public List<String> getEventIds(String trackId) {

		List<Event> listTemp = eventDao.getEventsContainsTrack(trackId, "tracks.trackId");

		List<String> listIds = new ArrayList<String>();

		for (Event event : listTemp) {

			listIds.add(event.getId());

		}

		return listIds;
	}

	public List<String> getEventIdsBySelector(String trackId, String selector) {

		List<Event> listTemp = eventDao.getEventsContainsTrack(trackId, selector);

		List<String> listIds = new ArrayList<String>();

		for (Event event : listTemp) {

			listIds.add(event.getId());

		}

		return listIds;
	}

	public List<String> getEventIdsByQuestionnair(String questionairId) {

		List<String> listIds = new ArrayList<String>();

		List<String> trackListTemp = this.getEventIdsBySelector(questionairId, "tracks.questionnaireId");

		for (String string : trackListTemp) {
			if (!listIds.contains(string)) {
				listIds.add(string);
			}
		}

		List<String> personnelListTemp = this.getEventIdsBySelector(questionairId, "eventPersonnels.questionnaireId");

		for (String string : personnelListTemp) {
			if (!listIds.contains(string)) {
				listIds.add(string);
			}
		}

		List<String> activitiesListTemp = this.getEventIdsBySelector(questionairId, "activities.questionnaireId");

		for (String string : activitiesListTemp) {
			if (!listIds.contains(string)) {
				listIds.add(string);
			}
		}

		List<String> eventTrackListTemp = this.getEventIdsBySelector(questionairId, "feedback_configuration.track_feedback");

		for (String string : eventTrackListTemp) {
			if (!listIds.contains(string)) {
				listIds.add(string);
			}
		}

		List<String> eventPersonnelListTemp = this.getEventIdsBySelector(questionairId, "feedback_configuration.personnel_feedback");

		for (String string : eventPersonnelListTemp) {
			if (!listIds.contains(string)) {
				listIds.add(string);
			}
		}

		List<String> eventActivitiesListTemp = this.getEventIdsBySelector(questionairId, "feedback_configuration.activity_feedback");

		for (String string : eventActivitiesListTemp) {
			if (!listIds.contains(string)) {
				listIds.add(string);
			}
		}

		List<String> eventListTemp = this.getEventIdsBySelector(questionairId, "feedback_configuration.event_feedback");

		for (String string : eventListTemp) {
			if (!listIds.contains(string)) {
				listIds.add(string);
			}
		}

		return listIds;
	}

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;

	private void onVenueChangePostProcess(String eventId) {

		org.iqvis.nvolv3.domain.Event event = eventService.get(eventId);

		if (event != null && event.getActivities() != null) {

			for (Activity activity : event.getActivities()) {

				try {

					if (!activity.getIsDeleted()) {

						activity.setLocation(null);

						activity.setLocationDetails(null);

						activityService.edit(activity, eventId);

					}
				}
				catch (NotFoundException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();

				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}

		}
	}

	public List<Activity> getPersonnelsActvities(String pId, String eventId) throws Exception {
		// TODO Auto-generated method stub

		List<Activity> activityList = new ArrayList<Activity>();

		Activity activity = new Activity();

		List<String> strActivity = eventDao.getPersonnelActivities(pId, eventId);

		for (String str : strActivity) {

			activity = activityService.get(str, eventId);

			List<ActivityPersonnels> per = activity.getPersonnels() == null ? new ArrayList<ActivityPersonnels>() : activity.getPersonnels();

			for (ActivityPersonnels activityPersonnels : per) {

				ReferenceData referenceData = reference_Data_Service.get(Constants.PERSONNEL_TYPE, activity.getOrganizerId());

				if (referenceData != null && referenceData.getData() != null) {

					for (Data data : referenceData.getData()) {

						if (data.getId().equals(activityPersonnels.getTypeId())) {

							activityPersonnels.setPersonnelType(data.getName());

							break;
						}
					}

				}

			}

			activityList.add(activity);

		}

		return activityList;
	}

	private boolean isChange(Object f, Object s) {
		Gson gson = new Gson();

		String jsonF = gson.toJson(f);

		String jsonS = gson.toJson(s);

		return !jsonS.equals(jsonF);
	}

	public void test() {
		// TODO Auto-generated method stub
		eventDao.test();
	}

	public List<String> get(org.springframework.data.mongodb.core.query.Query query, boolean isIds) {
		// TODO Auto-generated method stub
		return eventDao.get(query, isIds);
	}

	public Page<Event> geAppEvents(String appId, Criteria eventSearch, Pageable pageAble) {

		Page<Event> AppEvents = eventDao.getAll(appId, Utils.parseCriteria(eventSearch, ""), pageAble);

		return AppEvents;
		// Page<Event> eventPage = new PageImpl<Event>(AppEvents, pageAble,
		// AppEvents.size());

		// List<Event> listTemp = new ArrayList<Event>();
		//
		// Map<String, User> orgList = new HashMap<String, User>();
		//
		// for (Event event : eventPage.getContent()) {
		//
		// User u = null;
		//
		// if (!orgList.containsKey(event.getOrganizerId())) {
		//
		// u = userDao.get(event.getOrganizerId());
		//
		// orgList.put(event.getOrganizerId(), u);
		//
		// }
		// else {
		//
		// u = orgList.get(event.getOrganizerId());
		//
		// }
		//
		// List<Venue> venList = u==null?new ArrayList<Venue>():u.getVenues();
		//
		// if (event.getVenueId() != null &&
		// !event.getVenueId().equalsIgnoreCase("") && venList.size() > 0) {
		//
		// List<Venue> venue = Lambda.select(venList,
		// Lambda.having(Lambda.on(Venue.class).getId(),
		// Matchers.equalToIgnoringCase(event.getVenueId() + "")));
		//
		// if (venue.size() > 0) {
		// event.setVenue(venue.get(0));
		// }
		// }
		//
		// // try {
		// // Boolean b =
		// //
		// eventConfigurationService.getEventConfiguration().getFeed().isEvent_feed_moderation();
		// if (Utils.toEventConfiguration(event.getEventConfiguration()) != null
		// &&
		// Utils.toEventConfiguration(event.getEventConfiguration()).getFeed()
		// != null) {
		//
		// event.setIsfeedModerated(Utils.toEventConfiguration(event.getEventConfiguration()).getFeed().isEvent_feed_moderation());
		// }
		// // }
		// // catch (Exception e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// //
		// listTemp.add(event);
		//
		// }

		// return new PageImpl<Event>(listTemp);

	}

	public List<Event> getEvents(List<String> ids) {

		return eventDao.getEvents(ids);
	}

	public List<EventSelective> getActiveEvents() {

		return eventDao.getActiveEvents();
	}

	public List<EventPersonnel> getEventPersonnels(String id, String eventId, String organizerId) throws NotFoundException {

		return eventDao.getEventPersonnels(id, eventId, organizerId);
	}

}
