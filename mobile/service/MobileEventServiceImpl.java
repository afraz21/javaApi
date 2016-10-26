package org.iqvis.nvolv3.mobile.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.ActivityPersonnels;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.bean.EventSponsor;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.EventAlertDao;
import org.iqvis.nvolv3.dao.EventCampaignDao;
import org.iqvis.nvolv3.dao.EventDao;
import org.iqvis.nvolv3.dao.FeedDao;
import org.iqvis.nvolv3.dao.LocationDao;
import org.iqvis.nvolv3.dao.PersonnelDao;
import org.iqvis.nvolv3.dao.ReferenceDataDao;
import org.iqvis.nvolv3.dao.SponsorDao;
import org.iqvis.nvolv3.dao.ThemeDao;
import org.iqvis.nvolv3.dao.TracksDao;
import org.iqvis.nvolv3.dao.VendorDao;
import org.iqvis.nvolv3.dao.VenueDao;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Comment;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventConfiguration;
import org.iqvis.nvolv3.domain.EventResource;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.domain.UserFeedbackQuestion;
import org.iqvis.nvolv3.domain.Vendor;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.bean.DataCMS;
import org.iqvis.nvolv3.mobile.bean.DataCMSMenu;
import org.iqvis.nvolv3.mobile.bean.Event;
import org.iqvis.nvolv3.mobile.bean.EventTrack;
import org.iqvis.nvolv3.mobile.bean.EventVendor;
import org.iqvis.nvolv3.mobile.bean.EventVenue;
import org.iqvis.nvolv3.mobile.bean.KeynotePersonnel;
import org.iqvis.nvolv3.mobile.bean.LabelEntities;
import org.iqvis.nvolv3.mobile.bean.LabelEntitiesMenu;
import org.iqvis.nvolv3.mobile.bean.LisTrack;
import org.iqvis.nvolv3.mobile.bean.ListEvent;
import org.iqvis.nvolv3.mobile.bean.MobileEventResource;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.iqvis.nvolv3.mobile.bean.Texts;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventConfigurationService;
import org.iqvis.nvolv3.service.EventResourceService;
import org.iqvis.nvolv3.service.UserFeedbackQuestionService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.StringUtils;

import ch.lambdaj.Lambda;

@SuppressWarnings("restriction")
@Service(Constants.MOBILE_SERVICE_EVENT)
@Transactional
public class MobileEventServiceImpl implements MobileEventService {

	@Autowired
	private EventDao eventDao;

	@Autowired
	private PersonnelDao personnelDao;

	@Autowired
	private VenueDao venueDao;

	@Autowired
	private TracksDao trackDao;

	@Autowired
	private EventAlertDao eventAdvertDao;

	@Autowired
	private VendorDao vendorDao;

	@Autowired
	private SponsorDao sponsorDao;

	@Autowired
	EventAlertDao eventAlertDao;

	@Autowired
	private ReferenceDataDao reference_Data_Dao;

	@Autowired
	EventCampaignDao eventCampaignDao;

	@Autowired
	LocationDao locationDao;

	@Autowired
	FeedDao feedDao;

	@Autowired
	EventConfigurationService eventConfigurationService;

	@Autowired
	EventResourceService eventResourcesService;

	@Autowired
	private ThemeDao themeDao;

	@Resource(name = Constants.USER_FEEDBACK_QUESTION_RESOURCE)
	private UserFeedbackQuestionService userFeedbackQuestionService;

	protected static Logger logger = Logger.getLogger("MobileEventServiceImpl");

	public Page<Event> getAll(Criteria eventSearch, String code, Pageable pageAble, String organizerId) throws Exception {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		List<String> testOrganizer = new ArrayList<String>();

		testOrganizer.add("dev@test.com");

		List<Event> mobileEventView = new ArrayList<Event>();

		Page<org.iqvis.nvolv3.domain.Event> eventsPage = eventDao.getAll(Utils.parseCriteria(eventSearch, ""), pageAble, organizerId);

		List<org.iqvis.nvolv3.domain.Event> events = eventsPage.getContent();

		for (org.iqvis.nvolv3.domain.Event event : events) {

			Event mobileEvent = getAllEventData(event.getId(), event, code);

			mobileEvent.setTestOrganizers(testOrganizer);

			mobileEventView.add(mobileEvent);
		}

		Page<Event> eventPage = new PageImpl<Event>(mobileEventView, pageAble, mobileEventView.size());

		return eventPage;
	}

	public Event get(String id, String organizerId, String code) throws Exception, NotFoundException {

		org.iqvis.nvolv3.domain.Event event = eventDao.get(id, organizerId);

		return getAllEventData(id, event, code);
	}

	public Event getAllEventData(String id, org.iqvis.nvolv3.domain.Event event, String code) throws Exception {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;

			EventConfiguration newEventConfiguration = Utils.toEventConfiguration(event.getEventConfiguration());

			if (event != null && newEventConfiguration != null && newEventConfiguration.getDefault_lang() != null) {

				code = newEventConfiguration.getDefault_lang().getKey();

			}

		}

		List<UserFeedbackQuestion> questionniars = new ArrayList<UserFeedbackQuestion>();

		List<org.iqvis.nvolv3.mobile.bean.Activity> orphanActivities = new ArrayList<org.iqvis.nvolv3.mobile.bean.Activity>();

		if (event != null) {

			if (event.getFeedback_configuration() != null) {

				UserFeedbackQuestion questionniar = userFeedbackQuestionService.get(event.getFeedback_configuration().getEvent_feedback());

				if (questionniar != null) {

					if (!questionniars.contains(questionniar)) {

						questionniars.add(questionniar);
					}
				}

				questionniar = userFeedbackQuestionService.get(event.getFeedback_configuration().getActivity_feedback());

				if (questionniar != null) {

					if (!questionniars.contains(questionniar)) {

						questionniars.add(questionniar);
					}
				}

				questionniar = userFeedbackQuestionService.get(event.getFeedback_configuration().getPersonnel_feedback());

				if (questionniar != null) {

					if (!questionniars.contains(questionniar)) {

						questionniars.add(questionniar);
					}
				}

				questionniar = userFeedbackQuestionService.get(event.getFeedback_configuration().getTrack_feedback());

				if (questionniar != null) {

					if (!questionniars.contains(questionniar)) {

						questionniars.add(questionniar);
					}
				}

			}

			String current_lang = code;

			String event_default_lang = Constants.APPLICATION_DEFAULT_LANGUAGE;

			List<MultiLingual> mLing = Utils.getMultiLingualByLangCode(event.getMultiLingual(), code);

			EventConfiguration tempEventConfiguration = Utils.toEventConfiguration(event.getEventConfiguration());

			if (event != null && tempEventConfiguration != null && tempEventConfiguration.getDefault_lang() != null) {

				event_default_lang = tempEventConfiguration.getDefault_lang().getKey();

			}

			if (mLing.size() <= 0) {

				code = event_default_lang;
			}
			/*
			 * Event Population
			 */

			Event mobileEvent = new Event();

			mobileEvent.setFeedback_configuration(event.getFeedback_configuration());

			mobileEvent.setEventTimeZone(event.getTimeZone());

			mobileEvent.setSelected_language(code);

			List<EventResource> listResources = eventResourcesService.getAllResources(event.getOrganizerId(), event.getId());

			List<MobileEventResource> listMaps = new ArrayList<MobileEventResource>();

			List<MobileEventResource> listResourcesOthers = new ArrayList<MobileEventResource>();

			Object theme = null;

			String themeId = Utils.getThemeId(event.getEventConfiguration());

			if (themeId != null && themeId != "") {

				theme = themeDao.get(themeId);

			}

			mobileEvent.setEventConfiguration(Utils.filterForEventConfiguration(event.getEventConfiguration(), code, theme));

			// TODO fetch theme for event configuration

			for (EventResource eventResource : listResources) {

				if (eventResource.getType().toLowerCase().trim().equals("map")) {

					MobileEventResource eventResourceObject = new MobileEventResource(eventResource);
					try {
						eventResourceObject.setMultiLingual(Utils.getMultiLingualByLangCode(eventResourceObject.getMultiLingual(), current_lang, event_default_lang));
					}
					catch (Exception e) {

					}
					listMaps.add(eventResourceObject);

				}
				else {

					MobileEventResource eventResourceObject = new MobileEventResource(eventResource);
					try {
						eventResourceObject.setMultiLingual(Utils.getMultiLingualByLangCode(eventResourceObject.getMultiLingual(), current_lang, event_default_lang));
					}
					catch (Exception e) {

					}

					listResourcesOthers.add(eventResourceObject);

				}
			}

			if (event.getEventPersonnels() != null) {

				for (EventPersonnel mobileEventPersonnel : event.getEventPersonnels()) {

					if (Constants.CUSTOM.equals(mobileEventPersonnel.getQuestionnaireType())) {

						UserFeedbackQuestion questionniar = userFeedbackQuestionService.get(mobileEventPersonnel.getQuestionnaireId());

						if (questionniar != null) {

							if (!questionniars.contains(questionniar)) {

								questionniars.add(questionniar);
							}
						}
					}
				}
			}

			// Setting event personnels list
			mobileEvent.setEventPersonnelList(event.getEventPersonnels());

			mobileEvent.setMaps(listMaps);

			mobileEvent.setResources(listResourcesOthers);

			mobileEvent.setId(event.getId());

			mobileEvent.setName(event.getName());

			mobileEvent.setEventDates(event.getEventDates());

			if (event.getEventDates() != null && event.getEventDates().size() > 0) {

				if (event.getEventDates().size() == 1) {

					mobileEvent.setStartDate(event.getEventDates().get(0));

					mobileEvent.setEndDate(event.getEventDates().get(0));

				}
				else {

					List<DateTime> evenDates = event.getEventDates();

					Collections.sort(evenDates);

					mobileEvent.setStartDate(evenDates.get(0));

					mobileEvent.setEndDate(evenDates.get(evenDates.size() - 1));
				}
			}

			// Event Configuration Object Setting into MobileEvent object
			// if (event.getEventConfiguration() != null) {
			//
			// eventConfigurationService.updateEventConfiguration(event.getOrganizerId(),
			// event.getId(), "eventConfiguration.supported_languages",
			// event.getSupported_languages());
			//
			// eventConfigurationService.updateEventConfiguration(event.getOrganizerId(),
			// event.getId(), "eventConfiguration.default_lang",
			// event.getDefault_lang());
			//
			// mobileEvent.setEventConfiguration(event.getEventConfiguration());
			// }

			mobileEvent.setCreatedBy(event.getCreatedBy());

			mobileEvent.setCreatedDate(event.getCreatedDate());

			mobileEvent.setIsActive(event.getIsActive());

			mobileEvent.setIsDeleted(event.getIsDeleted());

			mobileEvent.setIsDownloadable(event.getIsDownloadable());

			mobileEvent.setIsLive(event.getIsLive());

			mobileEvent.setLastModifiedBy(event.getLastModifiedBy());

			mobileEvent.setLastModifiedDate(event.getLastModifiedDate());

			mobileEvent.setHasExpo(event.getHasExpo());

			mobileEvent.setIsfeedModerated(event.getIsfeedModerated());

			mobileEvent.setShowFeed(event.getShowFeed());

			mobileEvent.setIsOverlayCampaignActive(event.getIsfeedModerated());

			mobileEvent.setVersion(event.getVersion());

			mobileEvent.setEventHashTag(event.getEventHashTag());

			// / setting all personnels types for personnels from refernce data

			ReferenceData personnelType = reference_Data_Dao.get(Constants.PERSONNEL_TYPE, event.getOrganizerId());

			if (personnelType != null) {

				if (personnelType.getData() != null) {

					for (Data tempData : personnelType.getData()) {
						try {
							tempData.setMultiLingual(Utils.getMultiLingualByLangCode(tempData.getMultiLingual(), current_lang, event_default_lang));
						}
						catch (Exception e) {

						}
					}

				}

				mobileEvent.setPersonnelTypes(personnelType);
			}

			if (null != event.getMultiLingual()) {

				try {
					mobileEvent.setMultiLingual(Utils.getMultiLingualByLangCode(event.getMultiLingual(), current_lang, event_default_lang));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			mobileEvent.setOrganizerId(event.getOrganizerId());

			if (null != event.getPictureO()) {

				mobileEvent.setPicture(event.getPictureO());
			}

			if (null != event.getBannerO()) {

				mobileEvent.setBanner(event.getBannerO());
			}

			/*
			 * ActivityPersonnels Population
			 */
			List<Personnel> personalList = getEventPersonnelList(event.getId(), event.getOrganizerId(), event, code, false);

			mobileEvent.setPersonnels(personalList);

			/*
			 * Venue Population
			 */

			Venue venue = null;
			if (event.getVenueId() != null & !StringUtils.isNullOrEmpty(event.getVenueId())) {
				try {
					venue = venueDao.get(event.getVenueId(), event.getOrganizerId());

					try {
						if (null != venue.getMultiLingual()) {
							venue.setMultiLingual(Utils.getMultiLingualAddressesByLangCode(venue.getMultiLingual(), current_lang, event_default_lang));
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}

					/*
					 * Location Population
					 */

					if (venue.getLocations() != null) {

						for (Location l : venue.getLocations()) {

							if (null != l.getMultiLingual()) {

								try {
									l.setMultiLingual(Utils.getMultiLingualByLangCode(l.getMultiLingual(), current_lang, event_default_lang));

								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}

						}
					}

					mobileEvent.setVenue(venue);

				}
				catch (NotFoundException e) {

					e.printStackTrace();
				}

			}

			/*
			 * 
			 * We will loop here all activities
			 */

			if (event.getActivities() != null) {

				for (Activity activity : event.getActivities()) {

					// TODO/**From Here Tooooooo**/

					org.iqvis.nvolv3.mobile.bean.Activity trackActivity = new org.iqvis.nvolv3.mobile.bean.Activity();

					trackActivity.setId(activity.getId());

					trackActivity.setEventId(activity.getEventId());

					trackActivity.setOrganizerId(activity.getOrganizerId());

					trackActivity.setStartTime(activity.getStartTime());

					trackActivity.setEndTime(activity.getEndTime());

					trackActivity.setEventDate(activity.getEventDate());

					trackActivity.setIsActive(activity.getIsActive());

					trackActivity.setIsDeleted(activity.getIsDeleted());

					trackActivity.setTracks(activity.getTracks());

					trackActivity.setQuestionnaireId(activity.getQuestionnaireId());

					trackActivity.setQuestionnaireType(activity.getQuestionnaireType());
					
					trackActivity.setTimeZoneOffSet(activity.getTimeZoneOffSet());

					if (Constants.CUSTOM.equals(activity.getQuestionnaireType())) {

						UserFeedbackQuestion questionniar = userFeedbackQuestionService.get(activity.getQuestionnaireId());

						if (questionniar != null) {
							if (!questionniars.contains(questionniar)) {
								questionniars.add(questionniar);
							}
						}
					}

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
						logger.debug("VenueId is empty of event : " + event.getId());
					}
					// /
					trackActivity.setInner(activity);

					trackActivity.setType(activity.getType());

					trackActivity.setVersion(activity.getVersion());

					trackActivity.setCreatedBy(activity.getCreatedBy());

					trackActivity.setCreatedDate(activity.getCreatedDate());

					trackActivity.setLastModifiedBy(activity.getLastModifiedBy());

					trackActivity.setLastModifiedDate(activity.getLastModifiedDate());

					trackActivity.setName(activity.getName());

					trackActivity.setCreatedDate(activity.getCreatedDate());

					try {

						trackActivity.setMultiLingual(Utils.getMultiLingualByLangCode(activity.getMultiLingual(), current_lang, event_default_lang));

					}
					catch (Exception e) {

					}

					// TODO undo comment
					if (event.getEventConfiguration() != null) {

						EventConfiguration newEventConfiguration = Utils.toEventConfiguration(event.getEventConfiguration());

						if (newEventConfiguration.getFeed() != null) {

							trackActivity.setShowFeed(newEventConfiguration.getFeed().isActivity_feed_enabled());

							trackActivity.setActivity_feed_moderation(newEventConfiguration.getFeed().isActivity_feed_moderation());
						}
					}
					else {
						trackActivity.setShowFeed(false);

						trackActivity.setActivity_feed_moderation(false);
					}

					if (!trackActivity.getIsDeleted()) {

						orphanActivities.add(trackActivity);
					}
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

				List<Track> existingTracks = trackDao.getTracks(eventTracksList, event.getOrganizerId());

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
								if (existingTrack.getMultiLingual() != null) {

									mobileTrack.setMultiLingual(Utils.getMultiLingualByLangCode(existingTrack.getMultiLingual(), current_lang, event_default_lang));

								}
							}
							catch (Exception e) {
								e.printStackTrace();
							}

							mobileTrack.setQuestionnaireId(eventtrack.get(0).getQuestionnaireId());

							mobileTrack.setQuestionnaireType(eventtrack.get(0).getQuestionnaireType());

							if (Constants.CUSTOM.equals(eventtrack.get(0).getQuestionnaireType())) {

								UserFeedbackQuestion questionniar = userFeedbackQuestionService.get(eventtrack.get(0).getQuestionnaireId());

								if (questionniar != null) {

									if (!questionniars.contains(questionniar)) {

										questionniars.add(questionniar);
									}
								}
							}

							mobileTrack.setName(existingTrack.getName());

							mobileTrack.setPicture(existingTrack.getPictureO());

							mobileTrack.setOrganizerId(event.getOrganizerId());

							mobileTrack.setVersion(existingTrack.getVersion());

							mobileTrack.setCreatedBy(existingTrack.getCreatedBy());

							mobileTrack.setLastModifiedBy(existingTrack.getLastModifiedBy());

							mobileTrack.setLastModifiedDate(existingTrack.getLastModifiedDate());

							mobileTracks.add(mobileTrack);

						}
					}
					catch (Exception e) {

						e.printStackTrace();
					}

				}

			}

			mobileEvent.setQuestionniars(questionniars);

			mobileEvent.setTracks(mobileTracks);

			mobileEvent.setOrpActivities(orphanActivities);

			/*
			 * Sponsors
			 */

			List<org.iqvis.nvolv3.mobile.bean.EventSponsor> eventSponsors = new ArrayList<org.iqvis.nvolv3.mobile.bean.EventSponsor>();

			if (event.getSponsors() != null) {

				ReferenceData ref = reference_Data_Dao.get(Constants.LOG_UTLITY_KEY_SPONSOR_TYPE, event.getOrganizerId());

				for (EventSponsor eventSponsor : event.getSponsors()) {

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

						

						newEventSponsor.setSponsorCategoryId(eventSponsor.getSponsorCategoryId());

						try {

							newEventSponsor.setMultiLingual(Utils.getMultiLingualByLangCode(sponsor.getMultiLingual(), current_lang, event_default_lang));

						}
						catch (Exception e1) {

							e1.printStackTrace();

						}

						newEventSponsor.setOrganizerId(sponsor.getOrganizerId());

						newEventSponsor.setFirstName(sponsor.getFirstName());

						newEventSponsor.setLastName(sponsor.getLastName());

						newEventSponsor.setCreatedBy(sponsor.getCreatedBy());

						newEventSponsor.setEmail(sponsor.getEmail());

						newEventSponsor.setUser(sponsor.getUser());

						newEventSponsor.setInvite(sponsor.getInvite());

						newEventSponsor.setPhone(sponsor.getPhone());

						newEventSponsor.setBusinessCategory(sponsor.getBusinessCategory());

						List<Data> dataTemp = new ArrayList<Data>(ref == null || ref.getData() == null ? new ArrayList<Data>() : ref.getData());

						Data sponsorCategory = null;
						try {

							List<Data> dataList = Lambda.select(dataTemp, Lambda.having(Lambda.on(Data.class).getId(), Matchers.equalTo(eventSponsor.getSponsorCategoryId())));

							if (dataList != null && dataList.size() > 0) {
								sponsorCategory = dataList.get(0);
							}

							// sponsorCategory =
							// reference_Data_Dao.getById(eventSponsor.getSponsorCategoryId(),
							// sponsor.getOrganizerId());

							if (sponsorCategory != null && sponsorCategory.getMultiLingual() != null) {

								sponsorCategory.setMultiLingual(Utils.getMultiLingualByLangCode(sponsorCategory.getMultiLingual(), current_lang, event_default_lang));

							}

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

			mobileEvent.setSponsors(eventSponsors);

			/*
			 * 
			 * Vendors
			 */

			List<EventVendor> eventVendors = new ArrayList<EventVendor>();

			List<Vendor> exsitingEventVendors = vendorDao.getEventVendors(event.getOrganizerId(), event.getId());

			ReferenceData ref = reference_Data_Dao.get(Constants.LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY, event.getOrganizerId());

			for (Vendor vendor : exsitingEventVendors) {

				Sponsor sponsor = sponsorDao.get(vendor.getSponsorId());

				EventVendor eventVendor = new EventVendor();

				if (sponsor != null) {

					eventVendor.setId(vendor.getId());

					eventVendor.setName(sponsor.getName());

					// eventVendor.setPicture(sponsor.getPicture());

					eventVendor.setPicture(sponsor.getPictureO());

					eventVendor.setIsDeleted(vendor.getIsDeleted());

					eventVendor.setIsActive(vendor.getIsActive());

					eventVendor.setVendorCategoryId(vendor.getVendorCategoryId());

					eventVendor.setSortOrder(vendor.getSortOrder());

					try {
						// if (sponsor.getMultiLingual() != null) {
						//
						// eventVendor.setMultiLingual(Utils.getMultiLingualByLangCode(sponsor.getMultiLingual(),
						// code));
						//
						// }
						if (null != vendor.getMultiLingual()) {
							eventVendor.setMultiLingual(Utils.getMultiLingualByLangCode(vendor.getMultiLingual(), current_lang, event_default_lang));
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}

					// eventVendor.setMultiLingual(sponsor.getMultiLingual());

					eventVendor.setEventId(vendor.getEventId());

					eventVendor.setBoothNumber(vendor.getBoothNumber());

					eventVendor.setCreatedDate(vendor.getCreatedDate());

					eventVendor.setLongitude(vendor.getLongitude());

					eventVendor.setLatitude(vendor.getLatitude());

					eventVendor.setSponsorId(vendor.getSponsorId());

					eventVendor.setVersion(vendor.getVersion());

					List<Data> dataTemp = new ArrayList<Data>(ref == null || ref.getData() == null ? new ArrayList<Data>() : ref.getData());

					Data data = null;
					try {

						// data =
						// reference_Data_Dao.getById(vendor.getVendorCategoryId(),
						// vendor.getOrganizerId());

						List<Data> dataList = Lambda.select(dataTemp, Lambda.having(Lambda.on(Data.class).getId(), Matchers.equalTo(vendor.getVendorCategoryId())));

						if (dataList != null && dataList.size() > 0) {
							data = dataList.get(0);
						}

						try {

							if (data != null) {

								data.setMultiLingual(Utils.getMultiLingualByLangCode(data.getMultiLingual(), current_lang, event_default_lang));
							}
						}
						catch (Exception e) {

							e.printStackTrace();
						}

					}
					catch (Exception e) {
						e.printStackTrace();
					}

					eventVendor.setVendorCategory(data);

					eventVendors.add(eventVendor);
				}
			}

			mobileEvent.setVendors(eventVendors);

			/*
			 * 
			 * Event Alerts
			 */

			mobileEvent.setEventAlerts(eventAlertDao.getListEventAlertsByEventId(event.getId(), event.getOrganizerId()));

			/**
			 * 
			 * 
			 */

			mobileEvent.setEventCampaigns(eventCampaignDao.getAll(event.getOrganizerId(), event.getId(), Constants.PHOTO_OVERLAY));

			/**
			 * 
			 */

			List<Feed> feedList = feedDao.getAllFeeds(event.getId());

			if (feedList != null) {
				for (Feed feed : feedList) {

					int commentCount = 0;

					if (feed.getComments() != null) {
						
						commentCount = feed.getComments().size();
						
						Collections.sort(feed.getComments(), new MobileCommentObjectSort());

					}
					
					feed.setComments((feed.getComments() != null && feed.getComments().size() > 10 ? feed.getComments().subList(0, 10) : feed.getComments()));
					
					feed.setCommentCount(commentCount);
				}
			}

			mobileEvent.setEventFeeds(feedList != null && feedList.size() > 10 ? feedList.subList(0, 10) : feedList);

			return mobileEvent;

		}
		else {
			throw new NotFoundException(id, "Event");
		}

	}

	// TODO public List<String> getCampaignsOnly(String organizerId,String
	// eventId)
	public List<String> getCampaignsOnly(String organizerId, String eventId) {
		List<EventCampaign> list = eventCampaignDao.getAll(organizerId, eventId, Constants.PHOTO_OVERLAY);

		List<String> campaigns = new ArrayList<String>();

		for (EventCampaign eventCampaign : list) {
			if (!campaigns.contains(eventCampaign.getCampaignType()))
				campaigns.add(eventCampaign.getCampaignType());
		}

		return campaigns;
	}

	public List<EventTrack> getEventTracks(String eventId, String organizerId, org.iqvis.nvolv3.domain.Event event, List<Track> trackList, String code) {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		// try {
		// List<String> personnelIds = new ArrayList<String>();
		// if (event.getEventPersonnels() != null) {
		// for (EventPersonnel personnel : event.getEventPersonnels()) {
		// personnelIds.add(personnel.getPersonnelId());
		// }
		// }
		//
		// organizerPersonnels = new
		// ArrayList<Personnel>(personnelDao.getPersonnelsByOrganizer(organizerId,
		// personnelIds));
		// }
		// catch (NotFoundException e2) {
		//
		// e2.printStackTrace();
		// }

		// org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId,
		// organizerId);

		EventConfiguration evConf = Utils.toEventConfiguration(event.getEventConfiguration());

		/*
		 * Tracks Population
		 */
		List<EventTrack> mobileTracks = new ArrayList<EventTrack>();

		if (event.getTracks() != null) {

			List<String> ids = new ArrayList<String>();

			for (org.iqvis.nvolv3.bean.EventTrack eventTrack : event.getTracks()) {
				ids.add(eventTrack.getTrackId());
			}

			// List<Track> trackList = new
			// ArrayList<Track>(trackDao.getTracks(ids,
			// event.getOrganizerId()));

			for (org.iqvis.nvolv3.bean.EventTrack track : event.getTracks()) {

				EventTrack mobileTrack = new EventTrack();

				try {

					List<Track> tracks = Lambda.select(trackList, Lambda.having(Lambda.on(Track.class).getId(), Matchers.equalTo(track.getTrackId())));

					Track existingTrack = null;

					if (tracks != null && tracks.size() > 0) {

						existingTrack = tracks.get(0);
					}

					if (null != existingTrack) {

						mobileTrack.setId(track.getTrackId());

						mobileTrack.setColorCode(track.getColorCode());

						mobileTrack.setIsDeleted(existingTrack.getIsDeleted());

						try {

							if (null != existingTrack.getMultiLingual()) {
								mobileTrack.setMultiLingual(Utils.getMultiLingualByLangCode(existingTrack.getMultiLingual(), code));
							}

							if (mobileTrack.getMultiLingual() == null || mobileTrack.getMultiLingual().size() < 1) {

								mobileTrack.setMultiLingual(Utils.getMultiLingualByLangCode(existingTrack.getMultiLingual(), evConf.getDefault_lang().getKey()));
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}

						mobileTrack.setName(existingTrack.getName());

						mobileTrack.setPicture(existingTrack.getPictureO());

						mobileTrack.setSortOrder(track.getSortOrder() + "");

						mobileTrack.setOrganizerId(event.getOrganizerId());

						mobileTrack.setVersion(existingTrack.getVersion());

						mobileTrack.setCreatedBy(existingTrack.getCreatedBy());

						mobileTrack.setLastModifiedBy(existingTrack.getLastModifiedBy());

						mobileTrack.setLastModifiedDate(existingTrack.getLastModifiedDate());

						mobileTracks.add(mobileTrack);

					}
				}
				catch (Exception e) {

					e.printStackTrace();
				}

			}
		}

		return mobileTracks;
	}

	public List<EventTrack> getEventTracks(String eventId, String organizerId, String code) {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId, organizerId);

		EventConfiguration evConf = Utils.toEventConfiguration(event.getEventConfiguration());

		/*
		 * Tracks Population
		 */
		List<EventTrack> mobileTracks = new ArrayList<EventTrack>();

		if (event.getTracks() != null) {

			List<String> ids = new ArrayList<String>();

			for (org.iqvis.nvolv3.bean.EventTrack eventTrack : event.getTracks()) {
				ids.add(eventTrack.getTrackId());
			}

			List<Track> trackList = new ArrayList<Track>(trackDao.getTracks(ids, event.getOrganizerId()));

			ReferenceData ref = reference_Data_Dao.get(Constants.PERSONNEL_TYPE, event.getOrganizerId());

			for (org.iqvis.nvolv3.bean.EventTrack track : event.getTracks()) {

				EventTrack mobileTrack = new EventTrack();

				try {

					List<Track> tracks = Lambda.select(trackList, Lambda.having(Lambda.on(Track.class).getId(), Matchers.equalTo(track.getTrackId())));

					Track existingTrack = null;

					if (tracks != null && tracks.size() > 0) {

						existingTrack = tracks.get(0);
					}

					if (null != existingTrack) {

						mobileTrack.setId(track.getTrackId());

						mobileTrack.setColorCode(track.getColorCode());

						mobileTrack.setIsDeleted(existingTrack.getIsDeleted());

						try {

							if (null != existingTrack.getMultiLingual()) {
								mobileTrack.setMultiLingual(Utils.getMultiLingualByLangCode(existingTrack.getMultiLingual(), code));
							}

							if (mobileTrack.getMultiLingual().size() < 1) {

								mobileTrack.setMultiLingual(Utils.getMultiLingualByLangCode(existingTrack.getMultiLingual(), evConf.getDefault_lang().getKey()));
							}
						}
						catch (Exception e) {
							e.printStackTrace();
						}

						mobileTrack.setName(existingTrack.getName());

						mobileTrack.setPicture(existingTrack.getPictureO());

						mobileTrack.setSortOrder(track.getSortOrder() + "");

						mobileTrack.setOrganizerId(event.getOrganizerId());

						mobileTrack.setVersion(existingTrack.getVersion());

						mobileTrack.setCreatedBy(existingTrack.getCreatedBy());

						mobileTrack.setLastModifiedBy(existingTrack.getLastModifiedBy());

						mobileTrack.setLastModifiedDate(existingTrack.getLastModifiedDate());

						// Session of tracks

						if (event.getActivities() != null) {

							List<org.iqvis.nvolv3.mobile.bean.Activity> trackSessions = new ArrayList<org.iqvis.nvolv3.mobile.bean.Activity>();

							for (Activity activity : event.getActivities()) {

								if (activity.getTracks() != null && activity.getTracks().contains(track.getTrackId())) {

									org.iqvis.nvolv3.mobile.bean.Activity trackActivity = new org.iqvis.nvolv3.mobile.bean.Activity();

									trackActivity.setId(activity.getId());

									trackActivity.setEventId(activity.getEventId());

									trackActivity.setOrganizerId(activity.getOrganizerId());

									trackActivity.setStartTime(activity.getStartTime());

									trackActivity.setEndTime(activity.getEndTime());

									trackActivity.setEventDate(activity.getEventDate());

									trackActivity.setIsActive(activity.getIsActive());

									trackActivity.setIsDeleted(activity.getIsDeleted());

									Location location = null;

									if (activity.getLocation() != null && !activity.getLocation().equals("")) {

										// Activity Location//
										location = locationDao.get(activity.getLocation(), activity.getOrganizerId());

										if (location != null && null != location.getMultiLingual()) {
											try {

												location.setMultiLingual(Utils.getMultiLingualByLangCode(location.getMultiLingual(), code));

											}
											catch (Exception e) {
												e.printStackTrace();
											}
										}
									}

									trackActivity.setLocation(location);

									// /
									trackActivity.setType(activity.getType());

									trackActivity.setVersion(activity.getVersion());

									trackActivity.setCreatedBy(activity.getCreatedBy());

									trackActivity.setCreatedDate(activity.getCreatedDate());

									trackActivity.setLastModifiedBy(activity.getLastModifiedBy());

									trackActivity.setLastModifiedDate(activity.getLastModifiedDate());

									trackActivity.setName(activity.getName());

									trackActivity.setCreatedDate(activity.getCreatedDate());

									trackActivity.setVersion(activity.getVersion());

									if (null != activity.getMultiLingual()) {
										try {
											trackActivity.setMultiLingual(Utils.getMultiLingualByLangCode(activity.getMultiLingual(), code));
										}
										catch (Exception e) {
											e.printStackTrace();
										}
									}

									List<org.iqvis.nvolv3.mobile.bean.ActivityPersonnels> tracksActivityPersonnels = new ArrayList<org.iqvis.nvolv3.mobile.bean.ActivityPersonnels>();

									List<Personnel> personnels = new ArrayList<Personnel>();

									if (activity.getPersonnels() != null && activity.getPersonnels().size() > 0) {

										List<Data> dataTemp = new ArrayList<Data>(ref == null || ref.getData() == null ? new ArrayList<Data>() : ref.getData());

										for (ActivityPersonnels aPersonnels : activity.getPersonnels()) {

											org.iqvis.nvolv3.mobile.bean.ActivityPersonnels trackPersonnel = new org.iqvis.nvolv3.mobile.bean.ActivityPersonnels();

											// trackPersonnel.setId(aPersonnels.getId());

											Data data = null;

											// reference_Data_Dao.getById(aPersonnels.getTypeId(),
											// activity.getOrganizerId());

											List<Data> dataList = Lambda.select(dataTemp, Lambda.having(Lambda.on(Data.class).getId(), Matchers.equalTo(aPersonnels.getTypeId())));

											if (dataList != null && dataList.size() > 0) {
												data = dataList.get(0);
											}

											if (data != null) {
												try {
													data.setMultiLingual(Utils.getMultiLingualByLangCode(data.getMultiLingual(), code));
												}
												catch (Exception e1) {
													e1.printStackTrace();
												}
											}

											trackPersonnel.setType(data);

											personnels = personnelDao.getPersonnelsByIds(aPersonnels.getPersonnels(), activity.getOrganizerId());

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

											trackPersonnel.setPersonnels(personnels);

											tracksActivityPersonnels.add(trackPersonnel);
										}

										trackActivity.setPersonnels(personnels);
									}
									trackSessions.add(trackActivity);

								}
							}

							// Comment mobileTrack.setSessions to remove
							// sessions from MobileEvent

							mobileTrack.setSessions(trackSessions);

						}

						// System.out.println("Added Track Id : " +
						// mobileTrack.getId());

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

	public boolean validateEventAssociationWithOrganizer(String organizerId, String eventId) {

		return eventDao.validateEventAssociationWithOrganizer(organizerId, eventId);
	}

	public List<Personnel> getEventPersonnelList(String eventId, String organizerId, org.iqvis.nvolv3.domain.Event event, String code, boolean keynote) throws NotFoundException {

		// org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId,
		// organizerId);

		List<EventPersonnel> list = event.getEventPersonnels();

		List<String> personnelIds = new ArrayList<String>();

		if (list != null) {

			for (EventPersonnel eventPersonnel : list) {

				if (keynote) {
					if (eventPersonnel.isFeatured()) {

						personnelIds.add(eventPersonnel.getPersonnelId());

					}
				}
				else {
					personnelIds.add(eventPersonnel.getPersonnelId());
				}

			}

			if (personnelIds.size() > 0) {

				List<Personnel> personnels1 = personnelDao.getPersonnelsByIds(personnelIds, organizerId);

				if (personnels1 != null) {

					try {
						for (Personnel personnels : personnels1) {

							personnels.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(personnels.getMultiLingual(), code, Constants.APPLICATION_DEFAULT_LANGUAGE));

						}
					}
					catch (Exception ex) {

					}

				}

				return personnels1;

			}

		}

		return null;
	}

	public List<Personnel> getEventPersonnelList(String eventId, String organizerId, String code, boolean keynote) throws NotFoundException {

		org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId, organizerId);

		List<EventPersonnel> list = event.getEventPersonnels();

		List<String> personnelIds = new ArrayList<String>();

		if (list != null) {

			for (EventPersonnel eventPersonnel : list) {

				if (keynote) {
					if (eventPersonnel.isFeatured()) {

						personnelIds.add(eventPersonnel.getPersonnelId());

					}
				}
				else {
					personnelIds.add(eventPersonnel.getPersonnelId());
				}

			}

			if (personnelIds.size() > 0) {

				List<Personnel> personnels1 = personnelDao.getPersonnelsByIds(personnelIds, organizerId);

				if (personnels1 != null) {

					try {
						for (Personnel personnels : personnels1) {

							personnels.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(personnels.getMultiLingual(), code, Constants.APPLICATION_DEFAULT_LANGUAGE));

						}
					}
					catch (Exception ex) {

					}

				}

				return personnels1;

			}

		}

		return null;
	}

	public List<Personnel> getEventPersonnels(String eventId, String organizerId, org.iqvis.nvolv3.domain.Event event, String code) {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		// org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId,
		// organizerId);

		List<Personnel> personnels = new ArrayList<Personnel>();

		if (null != event.getActivities()) {

			ReferenceData ref = reference_Data_Dao.get(Constants.PERSONNEL_TYPE, event.getOrganizerId());

			List<Data> dataTemp = new ArrayList<Data>(ref.getData());

			for (Activity activity : event.getActivities()) {

				if (null != activity.getPersonnels()) {

					for (ActivityPersonnels personnel : activity.getPersonnels()) {

						List<String> personnelIds = new ArrayList<String>();

						Data data = null;
						try {

							// data =
							// reference_Data_Dao.getById(personnel.getTypeId(),
							// activity.getOrganizerId());

							List<Data> dataList = Lambda.select(dataTemp, Lambda.having(Lambda.on(Data.class).getId(), Matchers.equalTo(personnel.getTypeId())));

							if (dataList != null && dataList.size() > 0) {
								data = dataList.get(0);
							}

						}
						catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						if (data != null) {
							try {

								data.setMultiLingual(Utils.getMultiLingualByLangCode(data.getMultiLingual(), code));

							}
							catch (Exception e1) {

								e1.printStackTrace();
							}
						}

						personnelIds.addAll(personnel.getPersonnels());

						try {

							List<Personnel> personnels1 = null;

							if (personnelIds.size() > 0) {

								personnels1 = personnelDao.getPersonnelsByIds(personnelIds, activity.getOrganizerId());

								if (null != personnels1) {

									for (Personnel p : personnels1) {

										try {

											if (null != p.getMultiLingual()) {
												p.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(p.getMultiLingual(), code));

												if (data != null) {

													p.setType(data.getName());
												}
											}
										}
										catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

							}
							// MobilePersonnels activityPersonnels = new
							// MobilePersonnels();

							// activityPersonnels.setPersonnels(personnels1);

							personnels.addAll(personnels1);

						}
						catch (NotFoundException e) {

							e.printStackTrace();
						}

					}

				}
			}

		}
		return personnels;
	}

	public List<Personnel> getEventPersonnels(String eventId, String organizerId, String code) {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId, organizerId);

		List<Personnel> personnels = new ArrayList<Personnel>();

		if (null != event.getActivities()) {

			ReferenceData ref = reference_Data_Dao.get(Constants.PERSONNEL_TYPE, event.getOrganizerId());

			List<Data> dataTemp = new ArrayList<Data>(ref.getData());

			for (Activity activity : event.getActivities()) {

				if (null != activity.getPersonnels()) {

					for (ActivityPersonnels personnel : activity.getPersonnels()) {

						List<String> personnelIds = new ArrayList<String>();

						Data data = null;
						try {

							// data =
							// reference_Data_Dao.getById(personnel.getTypeId(),
							// activity.getOrganizerId());

							List<Data> dataList = Lambda.select(dataTemp, Lambda.having(Lambda.on(Data.class).getId(), Matchers.equalTo(personnel.getTypeId())));

							if (dataList != null && dataList.size() > 0) {
								data = dataList.get(0);
							}

						}
						catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						if (data != null) {
							try {

								data.setMultiLingual(Utils.getMultiLingualByLangCode(data.getMultiLingual(), code));

							}
							catch (Exception e1) {

								e1.printStackTrace();
							}
						}

						personnelIds.addAll(personnel.getPersonnels());

						try {

							List<Personnel> personnels1 = null;

							if (personnelIds.size() > 0) {

								personnels1 = personnelDao.getPersonnelsByIds(personnelIds, activity.getOrganizerId());

								if (null != personnels1) {

									for (Personnel p : personnels1) {

										try {

											if (null != p.getMultiLingual()) {
												p.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(p.getMultiLingual(), code));

												if (data != null) {

													p.setType(data.getName());
												}
											}
										}
										catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

							}
							// MobilePersonnels activityPersonnels = new
							// MobilePersonnels();

							// activityPersonnels.setPersonnels(personnels1);

							personnels.addAll(personnels1);

						}
						catch (NotFoundException e) {

							e.printStackTrace();
						}

					}

				}
			}

		}
		return personnels;
	}

	public List<EventVendor> getEventVendors(String eventId, String organizerId, String code) {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		List<EventVendor> eventVendors = new ArrayList<EventVendor>();

		List<Vendor> exsitingEventVendors = vendorDao.getEventVendors(organizerId, eventId);

		ReferenceData ref = reference_Data_Dao.get(Constants.LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY, organizerId);

		List<Data> dataTemp = new ArrayList<Data>(ref.getData());

		for (Vendor vendor : exsitingEventVendors) {

			Sponsor sponsor = sponsorDao.get(vendor.getSponsorId());

			EventVendor eventVendor = new EventVendor();

			eventVendor.setId(vendor.getId());

			eventVendor.setName(sponsor.getName());

			// eventVendor.setPicture(vendor.getPicture());

			eventVendor.setPicture(vendor.getPictureO());

			eventVendor.setIsDeleted(vendor.getIsDeleted());

			eventVendor.setIsActive(vendor.getIsActive());

			try {
				if (null != vendor.getMultiLingual()) {
					eventVendor.setMultiLingual(Utils.getMultiLingualByLangCode(vendor.getMultiLingual(), code));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			eventVendor.setEventId(vendor.getEventId());

			eventVendor.setBoothNumber(vendor.getBoothNumber());

			eventVendor.setCreatedDate(vendor.getCreatedDate());

			eventVendor.setLongitude(vendor.getLongitude());

			eventVendor.setLatitude(vendor.getLatitude());

			eventVendor.setSponsorId(vendor.getSponsorId());

			eventVendor.setVersion(vendor.getVersion());

			Data data = null;
			try {
				// data =
				// reference_Data_Dao.getById(vendor.getVendorCategoryId(),
				// vendor.getOrganizerId());

				List<Data> dataList = Lambda.select(dataTemp, Lambda.having(Lambda.on(Data.class).getId(), Matchers.equalTo(vendor.getVendorCategoryId())));

				if (dataList != null && dataList.size() > 0) {
					data = dataList.get(0);
				}

				try {
					if (data.getMultiLingual() != null) {
						data.setMultiLingual(Utils.getMultiLingualByLangCode(data.getMultiLingual(), code));
					}
				}
				catch (Exception e) {

					e.printStackTrace();
				}

			}
			catch (Exception e) {
				e.printStackTrace();
			}
			eventVendor.setVendorCategory(data);

			eventVendors.add(eventVendor);
		}

		return eventVendors;
	}

	public List<org.iqvis.nvolv3.mobile.bean.EventSponsor> getEventSponsors(String eventId, String organizerId, String code) {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId, organizerId);

		List<org.iqvis.nvolv3.mobile.bean.EventSponsor> eventSponsors = new ArrayList<org.iqvis.nvolv3.mobile.bean.EventSponsor>();

		if (event != null && event.getSponsors() != null) {

			ReferenceData ref = reference_Data_Dao.get(Constants.LOG_UTLITY_KEY_SPONSOR_TYPE, organizerId);

			List<Data> dataTemp = new ArrayList<Data>(ref.getData());

			for (EventSponsor eventSponsor : event.getSponsors()) {

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

					try {
						if (sponsor.getMultiLingual() != null) {
							newEventSponsor.setMultiLingual(Utils.getMultiLingualByLangCode(sponsor.getMultiLingual(), code));
						}
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}

					newEventSponsor.setOrganizerId(sponsor.getOrganizerId());

					newEventSponsor.setFirstName(sponsor.getFirstName());

					newEventSponsor.setLastName(sponsor.getLastName());

					newEventSponsor.setCreatedBy(sponsor.getCreatedBy());

					newEventSponsor.setEmail(sponsor.getEmail());

					newEventSponsor.setUser(sponsor.getUser());

					newEventSponsor.setInvite(sponsor.getInvite());

					newEventSponsor.setPhone(sponsor.getPhone());

					newEventSponsor.setBusinessCategory(sponsor.getBusinessCategory());

					newEventSponsor.setVersion(sponsor.getVersion());

					Data sponsorCategory = null;
					try {
						// sponsorCategory =
						// reference_Data_Dao.getById(eventSponsor.getSponsorCategoryId(),
						// sponsor.getOrganizerId());

						List<Data> dataList = Lambda.select(dataTemp, Lambda.having(Lambda.on(Data.class).getId(), Matchers.equalTo(sponsor.getSponsorCategoryId())));

						if (dataList != null && dataList.size() > 0) {
							sponsorCategory = dataList.get(0);
						}

					}
					catch (Exception e) {

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

	public Page<Event> getAllAppEvents(Pageable pageAble, List<String> eventIds, String code) throws Exception {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		List<String> testOrganizer = new ArrayList<String>();

		testOrganizer.add("dev@test.com");

		List<Event> mobileEventView = new ArrayList<Event>();

		Page<org.iqvis.nvolv3.domain.Event> eventsPage = eventDao.getAllAppEvents(pageAble, eventIds);

		List<org.iqvis.nvolv3.domain.Event> events = eventsPage.getContent();

		for (org.iqvis.nvolv3.domain.Event event : events) {

			Event mobileEvent = getAllEventData(event.getId(), event, code);

			mobileEvent.setTestOrganizers(testOrganizer);

			mobileEventView.add(mobileEvent);
		}

		Page<Event> eventPage = new PageImpl<Event>(mobileEventView, pageAble, mobileEventView.size());

		return eventPage;
	}

	public List<org.iqvis.nvolv3.mobile.bean.Activity> getEventActivities(String eventId, String organizerId, String code) {

		if (code == null || StringUtils.isNullOrEmpty(code)) {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;
		}

		org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId, organizerId);

		List<org.iqvis.nvolv3.mobile.bean.Activity> eventActivities = new ArrayList<org.iqvis.nvolv3.mobile.bean.Activity>();

		if (event.getActivities() != null) {

			for (Activity activity : event.getActivities()) {

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
						// Activity Location//
						location = locationDao.get(activity.getLocation(), activity.getOrganizerId());

						if (null != location.getMultiLingual()) {
							try {

								location.setMultiLingual(Utils.getMultiLingualByLangCode(location.getMultiLingual(), code));

							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}

					}

				}
				catch (NotFoundException e1) {
					e1.printStackTrace();
				}

				trackActivity.setLocation(location);
				// /

				trackActivity.setType(activity.getType());

				trackActivity.setCreatedDate(activity.getCreatedDate());

				trackActivity.setVersion(activity.getVersion());

				trackActivity.setCreatedBy(activity.getCreatedBy());

				trackActivity.setCreatedDate(activity.getCreatedDate());

				trackActivity.setLastModifiedBy(activity.getLastModifiedBy());

				trackActivity.setLastModifiedDate(activity.getLastModifiedDate());

				trackActivity.setName(activity.getName());

				if (null != activity.getMultiLingual()) {
					try {
						trackActivity.setMultiLingual(Utils.getMultiLingualByLangCode(activity.getMultiLingual(), code));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}

				eventActivities.add(trackActivity);
			}
		}
		return eventActivities;
	}

	// TODO come back here
	public List<ListEvent> getAllAppEvents(List<String> eventIds, List<Track> tracks, String code) throws Exception {
		logger.debug(String.format("Event Ids : [%s], Language Code : $s", eventIds, code));
		if (null == code || code == "") {

			code = Constants.APPLICATION_DEFAULT_LANGUAGE;

		}

		String oldCode = code;

		List<org.iqvis.nvolv3.domain.Event> listE = eventDao.getAppEventsApp(eventIds);

		List<ListEvent> listEvent = new ArrayList<ListEvent>();

		for (org.iqvis.nvolv3.domain.Event e : listE) {

			ListEvent mye = new ListEvent();

			mye.setId(e.getId());

			mye.setEventConfiguration(e.getEventConfiguration());

			mye.setDownloadable(e.getIsDownloadable());

			mye.setActive(e.getIsActive());

			mye.setEventHashTag(e.getEventHashTag());
			
			if (e.getMultiLingual() != null) {

				List<MultiLingual> mLing = Utils.getMultiLingualByLangCode(e.getMultiLingual(), code);

				if (mLing.size() > 0) {

					mye.setDescriptionLong(mLing.get(0).getDescriptionLong());

				}
				else {

					EventConfiguration evConf = Utils.toEventConfiguration(e.getEventConfiguration());

					mLing = Utils.getMultiLingualByLangCode(e.getMultiLingual(), evConf.getDefault_lang().getKey());

					mye.setDescriptionLong(mLing.get(0).getDescriptionLong());

				}

			}

			mye.setBanner(new Picture(e.getBannerO()));

			mye.setEventDates(e.getEventDates());

			mye.setEventTimeZone(e.getTimeZone());

			mye.setLive(e.getIsLive());

			mye.setIsProfileMust(e.getIsProfileMust());
			
			if (e.getMultiLingual() != null) {

				List<MultiLingual> mling = Utils.getMultiLingualByLangCode(e.getMultiLingual(), code);

				if (mling.size() > 0) {

					mye.setSelected_language(code);
				}
				else {

					EventConfiguration evConf = Utils.toEventConfiguration(e.getEventConfiguration());

					mling = Utils.getMultiLingualByLangCode(e.getMultiLingual(), evConf.getDefault_lang().getKey());

					mye.setSelected_language(evConf.getDefault_lang().getKey());
				}

				mye.setMultiLingual(mling);

			}
			mye.setOrganizerId(e.getOrganizerId());

			try {
				if (e.getVenueId() != null && e.getVenueId() != "")

					mye.setVenue(new EventVenue(venueDao.get(e.getVenueId(), e.getOrganizerId()), oldCode, code));
			}
			catch (Exception e2) {
				// TODO: handle exception
			}

			// List<KeynotePersonnel> tempPerList = new
			// ArrayList<KeynotePersonnel>();

			// List<Personnel> personalList = getEventPersonnels(e.getId(),
			// e.getOrganizerId(), code);

			List<KeynotePersonnel> personalList = getAllEventPersonnelList(e.getId(), e.getOrganizerId(), e, code, true);

			if (personalList != null) {

				mye.setKeynote_personnels(personalList);
			}
			List<LisTrack> tempList = new ArrayList<LisTrack>();

			List<EventTrack> listEventTrack = this.getJustTracks(e, tracks, code);

			for (EventTrack trk : listEventTrack) {

				LisTrack t = new LisTrack();

				t.setPicture(new Picture(trk.getPictureMedia()));

				t.setId(trk.getId());

				List<MultiLingual> mLing = Utils.getMultiLingualByLangCode(trk.getMultiLingual(), code);

				if (mLing.size() > 0) {

				}
				else {

					EventConfiguration evConf = Utils.toEventConfiguration(e.getEventConfiguration());

					mLing = Utils.getMultiLingualByLangCode(trk.getMultiLingual(), evConf.getDefault_lang().getKey());

				}

				t.setMultiLingual(mLing);

				t.setColorCode(trk.getColorCode());

				t.setSortOrder(trk.getSortOrder());

				tempList.add(t);
			}

			mye.setTracks(tempList);

			mye.setEventConfiguration(null);

			listEvent.add(mye);

		}
		return listEvent;
	}

	private List<EventTrack> getJustTracks(org.iqvis.nvolv3.domain.Event event, List<Track> tracks, String code) {

		List<EventTrack> et = new ArrayList<EventTrack>();

		if (event != null && event.getTracks() != null) {

			et = getEventTracks(event.getId(), event.getOrganizerId(), event, tracks, code);

		}

		return et;
	}

	public Texts getAppConfigResponse(AppConfiguration appConf, String code) throws Exception {

		String selected_language = code;

		List<DataCMS> list = new ArrayList<DataCMS>();
		list.addAll(appConf.getGeneral().getTexts() == null ? new ArrayList<DataCMS>() : appConf.getGeneral().getTexts());
		list.addAll(appConf.getSignup().getTexts() == null ? new ArrayList<DataCMS>() : appConf.getSignup().getTexts());

		List<LabelEntities> appData = new ArrayList<LabelEntities>();
		for (DataCMS dataCMS : list) {

			LabelEntities lE = new LabelEntities();
			lE.setKey(dataCMS.getKey());

			List<MultiLingual> ml = null;
			if (dataCMS.getMultiLingual() != null) {
				ml = Utils.getMultiLingualByLangCode(dataCMS.getMultiLingual(), code);
			}
			if (ml != null && !ml.isEmpty()) {
				lE.setText(ml.get(0).getTitle());
			}
			else {
				lE.setText(dataCMS.getDefaultText());
				selected_language = Constants.APPLICATION_DEFAULT_LANGUAGE;
			}
			appData.add(lE);
		}

		List<LabelEntitiesMenu> appMenuData = new ArrayList<LabelEntitiesMenu>();
		List<DataCMSMenu> menuList = appConf.getGeneral().getMenu() == null ? new ArrayList<DataCMSMenu>() : appConf.getGeneral().getMenu();
		for (DataCMSMenu dataCMS : menuList) {
			LabelEntitiesMenu lE = new LabelEntitiesMenu();
			List<MultiLingual> ml = null;
			lE.setKey(dataCMS.getKey());
			lE.setUrl(dataCMS.getUrl());

			if (dataCMS.getMultiLingual() != null) {
				ml = Utils.getMultiLingualByLangCode(dataCMS.getMultiLingual(), code);
			}
			if (ml != null && !ml.isEmpty()) {
				lE.setText(ml.get(0).getTitle());
			}
			else {
				lE.setText(dataCMS.getDefaultText());
				selected_language = Constants.APPLICATION_DEFAULT_LANGUAGE;
			}
			appMenuData.add(lE);
		}

		List<LabelEntities> appMessagesData = new ArrayList<LabelEntities>();

		List<DataCMS> messagesList = appConf.getGeneral().getMessages() == null ? new ArrayList<DataCMS>() : appConf.getGeneral().getMessages();

		for (DataCMS dataCMS : messagesList) {
			LabelEntities lE = new LabelEntities();
			List<MultiLingual> ml = null;
			lE.setKey(dataCMS.getKey());

			if (dataCMS.getMultiLingual() != null) {
				ml = Utils.getMultiLingualByLangCode(dataCMS.getMultiLingual(), code);
			}
			if (ml != null && !ml.isEmpty()) {
				lE.setText(ml.get(0).getTitle());
			}
			else {
				lE.setText(dataCMS.getDefaultText());
				selected_language = Constants.APPLICATION_DEFAULT_LANGUAGE;

			}
			appMessagesData.add(lE);
		}

		Texts appConfRes = new Texts();

		appConfRes.setMessages(appMessagesData);

		appConfRes.setLanguageCode(selected_language);

		appConfRes.setTexts(appData);

		appConfRes.setMenu(appMenuData);

		return appConfRes;
	}

	public List<DataCMSMenu> getMenu() {

		List<DataCMSMenu> list = new ArrayList<DataCMSMenu>();

		DataCMSMenu menu = new DataCMSMenu();

		menu.setDefaultText("Home");

		menu.setKey("Home");

		MultiLingual ling = new MultiLingual();

		ling.setTitle("Home");

		ling.setDescriptionLong("Home");

		ling.setDescriptionShort("Home");

		ling.setLanguageCode("EN");

		List<MultiLingual> lingList = new ArrayList<MultiLingual>();

		lingList.add(ling);

		ling = new MultiLingual();

		ling.setTitle("Home");

		ling.setDescriptionLong("Home");

		ling.setDescriptionShort("Home");

		ling.setLanguageCode("FR");

		lingList.add(ling);

		menu.setMultiLingual(lingList);

		menu.setUrl("https://s3-us-west-1.amazonaws.com/media.nvolv3.com/eventResource/54af89c89ed8aic_tab_gear_blue@2x.png");

		list.add(menu);

		menu = new DataCMSMenu();

		menu.setDefaultText("Setting");

		menu.setKey("Setting");

		ling = new MultiLingual();
		ling.setTitle("Setting");

		ling.setDescriptionLong("Setting");

		ling.setDescriptionShort("Setting");

		ling.setLanguageCode("EN");

		lingList = new ArrayList<MultiLingual>();

		lingList.add(ling);

		ling = new MultiLingual();

		ling.setTitle("Setting");

		ling.setDescriptionLong("Setting");

		ling.setDescriptionShort("Setting");

		ling.setLanguageCode("FR");

		lingList.add(ling);

		menu.setMultiLingual(lingList);

		menu.setUrl("https://s3-us-west-1.amazonaws.com/media.nvolv3.com/eventResource/54af89c89ed8aic_tab_gear_blue@2x.png");

		list.add(menu);

		menu = new DataCMSMenu();

		menu.setDefaultText("About");

		menu.setKey("About");

		ling = new MultiLingual();

		ling.setTitle("About");

		ling.setDescriptionLong("About");

		ling.setDescriptionShort("About");

		ling.setLanguageCode("EN");

		lingList = new ArrayList<MultiLingual>();

		lingList.add(ling);

		ling = new MultiLingual();

		ling.setTitle("About");

		ling.setDescriptionLong("About");

		ling.setDescriptionShort("About");

		ling.setLanguageCode("FR");

		lingList.add(ling);

		menu.setMultiLingual(lingList);

		menu.setUrl("https://s3-us-west-1.amazonaws.com/media.nvolv3.com/eventResource/54af89b1a9e72ic_tab_note_blue@2x.png");

		list.add(menu);

		return list;
	}

	public List<KeynotePersonnel> getAllEventPersonnelList(String eventId, String organizerId, org.iqvis.nvolv3.domain.Event event, String code, boolean keynote) throws Exception {

		// org.iqvis.nvolv3.domain.Event event = eventDao.get(eventId,
		// organizerId);

		List<EventPersonnel> list = event.getEventPersonnels();

		List<String> personnelIds = new ArrayList<String>();

		if (list != null) {

			for (EventPersonnel eventPersonnel : list) {
				if (keynote) {
					if (eventPersonnel.isFeatured()) {

						personnelIds.add(eventPersonnel.getPersonnelId());

					}
				}
				else {
					personnelIds.add(eventPersonnel.getPersonnelId());
				}

			}

			if (personnelIds.size() > 0) {

				List<Personnel> personnels1 = personnelDao.getPersonnelsByIds(personnelIds, organizerId);

				List<KeynotePersonnel> tempPerList = new ArrayList<KeynotePersonnel>();

				if (personnels1 != null) {

					for (Personnel per : personnels1) {

						KeynotePersonnel p = new KeynotePersonnel();

						p.setId(per.getId());

						for (EventPersonnel eventPersonnel : list) {

							if (eventPersonnel.getPersonnelId().equals(per.getId())) {

								p.setKeynote(eventPersonnel.isFeatured());

								p.setSortOrder(eventPersonnel.getSortOrder());

							}

						}

						if (per.getPicture() != null)
							p.setPicture(new Picture(per.getPictureO()));

						p.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(per.getMultiLingual(), code));
						// TODO
						tempPerList.add(p);

					}
				}

				return tempPerList;

			}

		}

		return null;
	}

	class MobileCommentObjectSort implements Comparator<Comment> {

		public int compare(Comment o1, Comment o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2);
		}
	}

}
