package org.iqvis.nvolv3.objectchangelog.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.iqvis.nvolv3.controller.ActivityController;
import org.iqvis.nvolv3.controller.EventAlertController;
import org.iqvis.nvolv3.controller.EventPersonnelController;
import org.iqvis.nvolv3.controller.EventResourceController;
import org.iqvis.nvolv3.controller.EventSponsorController;
import org.iqvis.nvolv3.controller.EventTrackController;
import org.iqvis.nvolv3.controller.PersonnelController;
import org.iqvis.nvolv3.controller.VendorController;
import org.iqvis.nvolv3.controller.VenueController;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.domain.Comment;
import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.domain.EventResource;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.domain.Vendor;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.Event;
import org.iqvis.nvolv3.mobile.bean.EventSponsor;
import org.iqvis.nvolv3.mobile.bean.EventVendor;
import org.iqvis.nvolv3.mobile.bean.EventVenue;
import org.iqvis.nvolv3.mobile.bean.MobileAttendee;
import org.iqvis.nvolv3.mobile.bean.MobileEventAlert;
import org.iqvis.nvolv3.mobile.bean.MobileEventCampaignParticipants;
import org.iqvis.nvolv3.mobile.bean.MobileEventFeed;
import org.iqvis.nvolv3.mobile.bean.MobileEventFeedComment;
import org.iqvis.nvolv3.mobile.bean.MobileLocation;
import org.iqvis.nvolv3.mobile.bean.PersonnelResponse;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.iqvis.nvolv3.mobile.controller.MobileFeedController;
import org.iqvis.nvolv3.mobile.service.MobileEventService;
import org.iqvis.nvolv3.objectchangelog.dao.DataChangeLogDao;
import org.iqvis.nvolv3.objectchangelog.domain.ChangeDataTrackLogVenue;
import org.iqvis.nvolv3.objectchangelog.domain.ChangeTrackLog;
import org.iqvis.nvolv3.objectchangelog.domain.DataChangeLog;
import org.iqvis.nvolv3.objectchangelog.domain.FeedObject;
import org.iqvis.nvolv3.objectchangelog.domain.SyncDataObject;
import org.iqvis.nvolv3.service.AttendeeService;
import org.iqvis.nvolv3.service.EventCampaignParticipantService;
import org.iqvis.nvolv3.service.EventCampaignService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.FeedService;
import org.iqvis.nvolv3.service.ReferenceDataService;
import org.iqvis.nvolv3.service.SponsorService;
import org.iqvis.nvolv3.service.UserFeedbackQuestionService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
public class DataChangeLogServiceImpl implements DataChangeLogService {

	@Autowired
	DataChangeLogDao dataChangeDao;

	@Autowired
	EventTrackController eventTrackController;

	@Autowired
	EventSponsorController eventSponsorController;

	@Autowired
	VendorController vendorController;

	@Autowired
	EventPersonnelController eventPersonnelController;

	@Autowired
	PersonnelController personnelController;

	@Autowired
	ActivityController activityController;

	@Autowired
	EventResourceController eventResourceController;

	@Autowired
	VenueController venueController;

	@Autowired
	MobileFeedController mobileFeedController;

	@Autowired
	EventAlertController eventAlertController;

	@Resource(name = Constants.SERVICE_ATTENDEE)
	private AttendeeService attendeeService;

	@Resource(name = Constants.MOBILE_SERVICE_EVENT)
	private MobileEventService mobileEventService;

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN)
	private EventCampaignService eventCampaignService;

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN_PARTICIPANT)
	private EventCampaignParticipantService eventCampaignParticipantService;

	@Resource(name = Constants.SERVICE_REFERENCE_DATA)
	ReferenceDataService referenceDataService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_FEED)
	private FeedService feedService;

	@Resource(name = Constants.SERVICE_SPONSOR)
	private SponsorService sponserService;

	@Resource(name = Constants.USER_FEEDBACK_QUESTION_RESOURCE)
	private UserFeedbackQuestionService userFeedbackQuestionService;

	public void deleteSubObject(String subObejct, String subObjectId) {

		dataChangeDao.deleteSubObject(subObejct, subObjectId);
		;
	}

	public void add(List<String> eventId, String event, String subObject, String subObjectId, String action, String subObjectType) {

		@SuppressWarnings("static-access")
		DataChangeLog logObject = new DataChangeLog(new DateTime().now(), eventId, event, action, subObject, subObjectId, action, subObjectType);

		dataChangeDao.add(logObject);

	}

	public boolean isChanged(String eventId, String organizerId, DateTime dateTime) {

		return dataChangeDao.isChanged(eventId, organizerId, dateTime);
	}

	public boolean isChangedConfig(String eventId, String organizerId, DateTime dateTime) {

		return dataChangeDao.isChangedEventConfig(eventId, organizerId, dateTime);
	}

	public ChangeTrackLog getTracksLogList(String eventId, String organizerId, DateTime dateTime, String selectorType, String code, String eventDefault) {

		org.iqvis.nvolv3.domain.Event dbEvent = null;

		ChangeTrackLog trackLog = (selectorType.equals(Constants.VENUE_LOG_KEY) ? new ChangeDataTrackLogVenue() : new ChangeTrackLog());

		List<DataChangeLog> list = dataChangeDao.getLogList(eventId, organizerId, dateTime, selectorType);

		list = Utils.getUnique(list);

		List<Object> insertList = new ArrayList<Object>();

		List<Object> updateList = new ArrayList<Object>();

		List<Object> deleteList = new ArrayList<Object>();

		for (DataChangeLog dataChangeLog : list) {

			if (dataChangeLog.getSubObjectAction().equals(Constants.LOG_ACTION_ADD)) {
				try {
					Object track = null;

					if ((dataChangeLog.getSubObject() + "").equals(Constants.TRACK_LOG_KEY)) {

						track = eventTrackController.get(null, dataChangeLog.getSubObjectId(), organizerId, eventId);

						org.iqvis.nvolv3.mobile.bean.EventTrack tempTrack = (org.iqvis.nvolv3.mobile.bean.EventTrack) track;

						if (tempTrack.getMultiLingual() != null) {
							tempTrack.setMultiLingual(Utils.getMultiLingualByLangCode(tempTrack.getMultiLingual(), code, eventDefault));
						}

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_SPONSOR_LOG_KEY)) {

						track = eventSponsorController.get(null, dataChangeLog.getSubObjectId(), organizerId, eventId);

						Sponsor tempSponsor = (Sponsor) track;

						if (tempSponsor.getMultiLingual() != null) {

							tempSponsor.setMultiLingual(Utils.getMultiLingualByLangCode(tempSponsor.getMultiLingual(), code, eventDefault));

						}

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_ALERT_LOG_KEY)) {

						track = eventAlertController.get(dataChangeLog.getSubObjectId(), organizerId, null, null);

						MobileEventAlert mobileAlerts = new MobileEventAlert((EventAlert) track);

						mobileAlerts.setGmtTime(((EventAlert) track).getAlertScheduledTime());

						track = mobileAlerts;

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_VENDOR_LOG_KEY)) {

						track = vendorController.get(dataChangeLog.getSubObjectId(), organizerId);

						Vendor v = (Vendor) track;

						Sponsor s = sponserService.get(v.getSponsorId());

						// v.setPicture(s.getPicture());

						v.setPicture(s.getPictureO());

						if (v.getMultiLingual() != null) {

							v.setMultiLingual(Utils.getMultiLingualByLangCode(v.getMultiLingual(), code, eventDefault));

						}

						// track = null;
						// if (v.getEventId().equals(eventId)) {
						// track = v;
						// }

						track = v;

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_PERSONNEL_LOG_KEY)) {

						track = personnelController.get(dataChangeLog.getSubObjectId(), organizerId, null, null);

						Personnel tempPersonnel = (Personnel) track;

						if (dbEvent == null) {

							dbEvent = eventService.get(eventId);
						}

						// Event event = mobileEventService.get(eventId,
						// organizerId, code);

						PersonnelResponse object = new PersonnelResponse(tempPersonnel, dbEvent.getEventPersonnels());

						if (object.getId().equals(tempPersonnel.getId())) {

							track = object;

						}

						/*
						 * Event event = mobileEventService.get(eventId,
						 * organizerId, code);
						 * 
						 * if (event.getPersonnels() != null) {
						 * 
						 * for (PersonnelResponse object :
						 * event.getPersonnels()) {
						 * 
						 * if (object.getId().equals(tempPersonnel.getId())) {
						 * 
						 * track = object;
						 * 
						 * break;
						 * 
						 * } } }
						 */
						// if (tempPersonnel.getMultiLingual() != null) {
						//
						// tempPersonnel.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(tempPersonnel.getMultiLingual(),
						// code));
						//
						// }

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.ACTIVITY_LOG_KEY)) {

						track = activityController.getEvent(dataChangeLog.getSubObjectId(), eventId, organizerId, null, null);

						Activity activity = (Activity) track;

						if (activity.getMultiLingual() != null) {

							activity.setMultiLingual(Utils.getMultiLingualByLangCode(activity.getMultiLingual(), code, eventDefault));

						}

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_RESOURCE_MAP_LOG_KEY)) {

						track = eventResourceController.getEventResource(dataChangeLog.getSubObjectId(), organizerId, null, null);

						EventResource resource = (EventResource) track;

						if (resource.getMultiLingual() != null) {

							resource.setMultiLingual(Utils.getMultiLingualByLangCode(resource.getMultiLingual(), code, eventDefault));

						}

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_OTHER_RESOURCE_MAP_LOG_KEY)) {

						track = eventResourceController.getEventResource(dataChangeLog.getSubObjectId(), organizerId, null, null);

						EventResource resource = (EventResource) track;

						if (resource.getMultiLingual() != null) {

							resource.setMultiLingual(Utils.getMultiLingualByLangCode(resource.getMultiLingual(), code, eventDefault));

						}

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.VENUE_LOG_KEY)) {

						track = venueController.get(dataChangeLog.getSubObjectId(), organizerId, null, null);

						Venue ven = (Venue) track;

						EventVenue eventVenue = new EventVenue(ven, code, eventDefault);

						ChangeDataTrackLogVenue tempVen = (ChangeDataTrackLogVenue) trackLog;

						List<MobileLocation> locationList = new ArrayList<MobileLocation>();

						if (ven.getLocations() != null) {

							for (Location location : ven.getLocations()) {

								MobileLocation tempLocation = new MobileLocation();

								tempLocation.setId(location.getId());

								if (location.getMultiLingual() != null) {

									tempLocation.setMultilingual(Utils.getMultiLingualByLangCode(location.getMultiLingual(), code, eventDefault));

								}
								if (!location.getIsDeleted()) {

									locationList.add(tempLocation);

								}
							}

						}

						tempVen.setLocations(locationList);

						trackLog = tempVen;

						track = eventVenue;

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.LOG_USER_FEEDBACK_QUESTION)) {
						track = userFeedbackQuestionService.get(dataChangeLog.getSubObjectId());

					}
					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_CAMPAIGN__LOG_KEY)) {

						System.out.println("Sub Object Id : " + dataChangeLog.getSubObjectId());

						EventCampaignParticipant ecp = eventCampaignParticipantService.get(dataChangeLog.getSubObjectId(), dataChangeLog.getEventIds().get(0));

						System.out.println("Sub Object Id : " + dataChangeLog.getSubObjectId() + " | Campaign ID" + ecp.getCampaignId());

						EventCampaign cmp = eventCampaignService.get(ecp.getCampaignId(), organizerId);

						List<MobileEventCampaignParticipants> participants = new ArrayList<MobileEventCampaignParticipants>();

						List<EventCampaignParticipant> listTemp = cmp.getParticipants();

						for (EventCampaignParticipant eventCampaignParticipant : listTemp) {

							MobileEventCampaignParticipants tempObject = new MobileEventCampaignParticipants();

							if (eventCampaignParticipant.isPrepared() != true) {
								continue;
							}

							if (eventCampaignParticipant.getIs_end() == true) {

								continue;
							}

							if (eventCampaignParticipant.getId().equals(dataChangeLog.getSubObjectId())) {

								tempObject.setCampaignId(eventCampaignParticipant.getCampaignId());

								// tempObject.setLogo(new
								// Picture(eventCampaignParticipant.getPicture()));

								tempObject.setLogo(new Picture(eventCampaignParticipant.getPictureO()));

								if (eventCampaignParticipant.getMultiLingual() != null) {

									tempObject.setMultiLingual(Utils.getMultiLingualByLangCode(eventCampaignParticipant.getMultiLingual(), code, eventDefault));

								}

								// tempObject.setOverlayLandscape(new
								// Picture(eventCampaignParticipant.getOverlayLandscape()));
								//
								// tempObject.setOverlayPotrait(new
								// Picture(eventCampaignParticipant.getOverlayPotrait()));

								tempObject.setOverlayLandscape(new Picture(eventCampaignParticipant.getOverlayLandscapeO()));

								tempObject.setOverlayPotrait(new Picture(eventCampaignParticipant.getOverlayPotraitO()));

								tempObject.setParticipantId(eventCampaignParticipant.getId());

								tempObject.setSponsorId(eventCampaignParticipant.getSponsors());

								tempObject.setSortOrder(eventCampaignParticipant.getSortOrder());

								participants.add(tempObject);

							}
						}

						insertList.addAll(participants);

						trackLog.setInsert(insertList);

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.FEED_LOG_KEY)) {

						track = mobileFeedController.get(dataChangeLog.getSubObjectId(), null, null);

						Feed feed = (Feed) track;

						if (!feed.isPrepared() || !feed.getIsActive() || !Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus()))
							continue;

						MobileEventFeed mobilefeed = new MobileEventFeed();

						mobilefeed.setCommentsCount(feed.getCommentCount());

						// mobilefeed.setLogo(new Picture(feed.getPicture()));
						mobilefeed.setLogo(new Picture(feed.getPictureO()));

						mobilefeed.setLikes(feed.getLikes());

						mobilefeed.setCreatedDate(feed.getCreatedDate());

						mobilefeed.setEventId(feed.getEventId());

						mobilefeed.setId(feed.getId());

						mobilefeed.setType(feed.getType());

						mobilefeed.setTypeId(feed.getTypeId());

						mobilefeed.setText(feed.getDescription());

						mobilefeed.setFeedStatus(feed.getFeedStatus());

						mobilefeed.setCreatedByEmail(feed.getCreatedByEmail());

						mobilefeed.setCreatedByname(feed.getCreatedByname());

						mobilefeed.setLastModifiedDate(feed.getLastModifiedDate());

						mobilefeed.setSource(feed.getSource());

						mobilefeed.setDp(feed.getDp());

						mobilefeed.setAttendeeId(feed.getAttendeeId());

						List<MobileEventFeedComment> listComment = new ArrayList<MobileEventFeedComment>();

						if (feed.getComments() != null) {
							for (Comment comment : feed.getComments()) {
								MobileEventFeedComment commentTemp = new MobileEventFeedComment();

								commentTemp.setCreatedByname(comment.getCreatedByname());

								commentTemp.setCreatedDate(comment.getCreatedDate());

								commentTemp.setId(comment.getId());

								commentTemp.setText(comment.getText());

								commentTemp.setDp(comment.getDp());

								commentTemp.setAttendeeId(comment.getAttendeeId());

								if (comment.getCreatedDate().compareTo(dateTime) > 0)
									listComment.add(commentTemp);

							}
						}

						// mobilefeed.setActivityId();

						mobilefeed.setComments(listComment);

						track = mobilefeed;

					}

					boolean flag = false;
					for (Object object : insertList) {
						if (object.equals(track)) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_SPONSOR_LOG_KEY)) {

							Sponsor spon = (Sponsor) track;

							EventSponsor sponsor = new EventSponsor();

							sponsor.setBusinessCategory(spon.getBusinessCategory());

							sponsor.setCreatedBy(spon.getCreatedBy());

							sponsor.setCreatedDate(spon.getCreatedDate());

							sponsor.setEmail(spon.getEmail());

							sponsor.setSponsorCategoryId(spon.getSponsorCategoryId());

							sponsor.setFirstName(spon.getFirstName());

							sponsor.setId(spon.getId());

							sponsor.setInvite(spon.getInvite());

							// sponsor.setPicture(spon.getPicture());

							sponsor.setPicture(spon.getPictureO());

							sponsor.setIsActive(spon.getIsActive());

							sponsor.setIsDeleted(spon.getIsDeleted());

							sponsor.setLastModifiedBy(spon.getLastModifiedBy());

							sponsor.setLastModifiedDate(spon.getLastModifiedDate());

							sponsor.setLastName(spon.getLastName());

							if (spon.getMultiLingual() != null) {
								sponsor.setMultiLingual(Utils.getMultiLingualByLangCode(spon.getMultiLingual(), code, eventDefault));
							}

							sponsor.setName(spon.getName());

							sponsor.setOrganizerId(spon.getOrganizerId());

							sponsor.setPhone(spon.getPhone());

							track = sponsor;
							// trackLog.setCategories(LogUtlity.getCategories(spon.getSponsorCategoryId(),
							// trackLog, referenceDataService,
							// Constants.LOG_UTLITY_KEY_SPONSOR_TYPE));

						}

						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_VENDOR_LOG_KEY)) {

							Vendor ven = (Vendor) track;

							EventVendor vend = new EventVendor(ven);

							track = vend;

							// trackLog.setCategories(LogUtlity.getCategories(spon.getVendorCategoryId(),
							// trackLog, referenceDataService,
							// Constants.LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY));

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_PERSONNEL_LOG_KEY)) {

							// Personnel spon = (Personnel) track;

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.ACTIVITY_LOG_KEY)) {

							// Personnel spon = (Personnel) track;
							Activity activity = (Activity) track;
							if (activity != null) {

								Activity temp = (Activity) track;

								org.iqvis.nvolv3.mobile.bean.Activity tempAc = new org.iqvis.nvolv3.mobile.bean.Activity();

								tempAc.setId(temp.getId());

								tempAc.setName(temp.getName());

								tempAc.setOrganizerId(temp.getOrganizerId());

								tempAc.setStartTime(temp.getStartTime());

								tempAc.setEndTime(temp.getEndTime());

								tempAc.setEventDate(temp.getEventDate());

								tempAc.setTracks(temp.getTracks());

								if (temp.getMultiLingual() != null) {

									tempAc.setMultiLingual(Utils.getMultiLingualByLangCode(temp.getMultiLingual(), code, eventDefault));

								}

								tempAc.setLocation(temp.getLocationDetails());

								tempAc.setType(temp.getType());

								tempAc.setInner(temp);

								tempAc.setQuestionnaireId(temp.getQuestionnaireId());

								tempAc.setQuestionnaireType(temp.getQuestionnaireType());

								tempAc.setTimeZoneOffSet(temp.getTimeZoneOffSet());

								tempAc.setCheckIn(temp.getCheckIn());
								
								track = tempAc;
							}
						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_RESOURCE_MAP_LOG_KEY)) {

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_OTHER_RESOURCE_MAP_LOG_KEY)) {

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.VENUE_LOG_KEY)) {

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.FEED_LOG_KEY)) {

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.LOG_USER_FEEDBACK_QUESTION)) {

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_ALERT_LOG_KEY)) {

						}

						if (!updateList.contains(track) && !insertList.contains(track)) {

							insertList.add(track);

						}

					}

				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (dataChangeLog.getSubObjectAction().equals(Constants.LOG_ACTION_UPDATE)) {
				try {

					Object track = null;

					if ((dataChangeLog.getSubObject() + "").equals(Constants.TRACK_LOG_KEY)) {

						track = eventTrackController.get(null, dataChangeLog.getSubObjectId(), organizerId, eventId);

						org.iqvis.nvolv3.mobile.bean.EventTrack tempTrack = (org.iqvis.nvolv3.mobile.bean.EventTrack) track;

						if (tempTrack.getMultiLingual() != null) {
							tempTrack.setMultiLingual(Utils.getMultiLingualByLangCode(tempTrack.getMultiLingual(), code, eventDefault));
						}

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_SPONSOR_LOG_KEY)) {

						track = eventSponsorController.get(null, dataChangeLog.getSubObjectId(), organizerId, eventId);

						Sponsor tempSponsor = (Sponsor) track;

						if (tempSponsor.getMultiLingual() != null) {

							tempSponsor.setMultiLingual(Utils.getMultiLingualByLangCode(tempSponsor.getMultiLingual(), code, eventDefault));
							;

						}

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_ALERT_LOG_KEY)) {

						track = eventAlertController.get(dataChangeLog.getSubObjectId(), organizerId, null, null);

						MobileEventAlert mobileAlerts = new MobileEventAlert((EventAlert) track);

						mobileAlerts.setGmtTime(((EventAlert) track).getAlertScheduledTime());

						track = mobileAlerts;

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_VENDOR_LOG_KEY)) {

						track = vendorController.get(dataChangeLog.getSubObjectId(), organizerId);

						Vendor v = (Vendor) track;

						Sponsor s = sponserService.get(v.getSponsorId());

						// v.setPicture(s.getPicture());

						v.setPicture(s.getPictureO());

						if (v.getMultiLingual() != null) {

							v.setMultiLingual(Utils.getMultiLingualByLangCode(v.getMultiLingual(), code, eventDefault));

						}
						//
						// track=null;
						// if(v.getEventId().equals(eventId)){
						// track = v;
						// }

						track = v;

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_PERSONNEL_LOG_KEY)) {

						track = personnelController.get(dataChangeLog.getSubObjectId(), organizerId, null, null);

						Personnel tempPersonnel = (Personnel) track;

						if (dbEvent == null) {
							dbEvent = eventService.get(eventId);
						}

						// Event event = mobileEventService.get(eventId,
						// organizerId, code);

						PersonnelResponse object = new PersonnelResponse(tempPersonnel, dbEvent.getEventPersonnels());

						if (object.getId().equals(tempPersonnel.getId())) {

							track = object;

						}

						/*
						 * if (dbEvent.getPersonnels() != null) {
						 * 
						 * for (PersonnelResponse object :
						 * event.getPersonnels()) {
						 * 
						 * if (object.getId().equals(tempPersonnel.getId())) {
						 * 
						 * track = object;
						 * 
						 * break;
						 * 
						 * } } }
						 */
						// if (tempPersonnel.getMultiLingual() != null) {
						//
						// tempPersonnel.setMultiLingual(Utils.getMultiLingualPersonnelInformationByLangCode(tempPersonnel.getMultiLingual(),
						// code));
						//
						// }

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.ACTIVITY_LOG_KEY)) {

						track = activityController.getEvent(dataChangeLog.getSubObjectId(), eventId, organizerId, null, null);

						Activity activity = (Activity) track;

						if (activity.getMultiLingual() != null) {

							activity.setMultiLingual(Utils.getMultiLingualByLangCode(activity.getMultiLingual(), code, eventDefault));

						}

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_RESOURCE_MAP_LOG_KEY)) {

						track = eventResourceController.getEventResource(dataChangeLog.getSubObjectId(), organizerId, null, null);

						EventResource resource = (EventResource) track;

						if (resource.getMultiLingual() != null) {

							resource.setMultiLingual(Utils.getMultiLingualByLangCode(resource.getMultiLingual(), code, eventDefault));

						}

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_OTHER_RESOURCE_MAP_LOG_KEY)) {

						track = eventResourceController.getEventResource(dataChangeLog.getSubObjectId(), organizerId, null, null);

						EventResource resource = (EventResource) track;

						if (resource.getMultiLingual() != null) {

							resource.setMultiLingual(Utils.getMultiLingualByLangCode(resource.getMultiLingual(), code, eventDefault));

						}

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.VENUE_LOG_KEY)) {

						track = venueController.get(dataChangeLog.getSubObjectId(), organizerId, null, null);

						Venue ven = (Venue) track;

						EventVenue eventVenue = new EventVenue(ven, code, eventDefault);

						ChangeDataTrackLogVenue tempVen = (ChangeDataTrackLogVenue) trackLog;

						List<MobileLocation> locationList = new ArrayList<MobileLocation>();

						if (ven.getLocations() != null) {

							for (Location location : ven.getLocations()) {

								MobileLocation tempLocation = new MobileLocation();

								tempLocation.setId(location.getId());

								if (location.getMultiLingual() != null) {

									tempLocation.setMultilingual(Utils.getMultiLingualByLangCode(location.getMultiLingual(), code, eventDefault));

								}

								if (!location.getIsDeleted()) {

									locationList.add(tempLocation);

								}

							}

						}

						tempVen.setLocations(locationList);

						trackLog = tempVen;

						track = eventVenue;

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.LOG_USER_FEEDBACK_QUESTION)) {

						System.out.println(dataChangeLog.getSubObjectId());

						track = userFeedbackQuestionService.get(dataChangeLog.getSubObjectId());

					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_CAMPAIGN__LOG_KEY)) {

						String participantId = dataChangeLog.getSubObjectId();

						EventCampaignParticipant ecp = eventCampaignParticipantService.get(dataChangeLog.getSubObjectId(), dataChangeLog.getEventIds().get(0));

						EventCampaign cmp = eventCampaignService.get(ecp.getCampaignId(), organizerId);

						List<MobileEventCampaignParticipants> participants = new ArrayList<MobileEventCampaignParticipants>();

						List<EventCampaignParticipant> listTemp = cmp.getParticipants();

						for (EventCampaignParticipant eventCampaignParticipant : listTemp) {

							MobileEventCampaignParticipants tempObject = new MobileEventCampaignParticipants();

							if (eventCampaignParticipant.getId().equals(dataChangeLog.getSubObjectId())) {

								// System.out.println("Sub Object Id : " +
								// dataChangeLog.getSubObjectId() +
								// " | Particpant ID : " +
								// eventCampaignParticipant.getId());

								if (eventCampaignParticipant.isPrepared() != true) {
									continue;
								}

								if (eventCampaignParticipant.getIs_end() == true) {

									continue;
								}

								// if(eventCampaignParticipant.get)

								tempObject.setCampaignId(eventCampaignParticipant.getCampaignId());

								// tempObject.setLogo(new
								// Picture(eventCampaignParticipant.getPicture()));

								tempObject.setLogo(new Picture(eventCampaignParticipant.getPictureO()));

								if (eventCampaignParticipant.getMultiLingual() != null) {

									tempObject.setMultiLingual(Utils.getMultiLingualByLangCode(eventCampaignParticipant.getMultiLingual(), code, eventDefault));

								}

								// tempObject.setOverlayLandscape(new
								// Picture(eventCampaignParticipant.getOverlayLandscape()));
								//
								// tempObject.setOverlayPotrait(new
								// Picture(eventCampaignParticipant.getOverlayPotrait()));

								tempObject.setOverlayLandscape(new Picture(eventCampaignParticipant.getOverlayLandscapeO()));

								tempObject.setOverlayPotrait(new Picture(eventCampaignParticipant.getOverlayPotraitO()));

								// tempObject.setParticipantId(tempObject.getParticipantId());

								tempObject.setParticipantId(eventCampaignParticipant.getId());

								tempObject.setSponsorId(eventCampaignParticipant.getSponsors());

								tempObject.setSortOrder(eventCampaignParticipant.getSortOrder());

								participants.add(tempObject);
							}
						}

						updateList.addAll(participants);

						// trackLog.setDelete(deleteList);

						trackLog.setUpdate(updateList);

						// trackLog.setInsert(updateList);

						// return trackLog;
					}

					else if ((dataChangeLog.getSubObject() + "").equals(Constants.FEED_LOG_KEY)) {

						track = mobileFeedController.get(dataChangeLog.getSubObjectId(), null, null);

						Feed feed = (Feed) track;

						if (!feed.isPrepared() || !feed.getIsActive() || !Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus()))
							continue;
						MobileEventFeed mobilefeed = new MobileEventFeed();

						mobilefeed.setCommentsCount(feed.getCommentCount());

						// mobilefeed.setLogo(new Picture(feed.getPicture()));
						mobilefeed.setLogo(new Picture(feed.getPictureO()));

						mobilefeed.setLikes(feed.getLikes());

						mobilefeed.setCreatedDate(feed.getCreatedDate());

						mobilefeed.setEventId(feed.getEventId());

						mobilefeed.setId(feed.getId());

						mobilefeed.setType(feed.getType());

						mobilefeed.setTypeId(feed.getTypeId());

						mobilefeed.setFeedStatus(feed.getFeedStatus());

						mobilefeed.setCreatedByEmail(feed.getCreatedByEmail());

						mobilefeed.setCreatedByname(feed.getCreatedByname());

						mobilefeed.setText(feed.getDescription());

						mobilefeed.setLastModifiedDate(feed.getLastModifiedDate());

						mobilefeed.setSource(feed.getSource());

						mobilefeed.setDp(feed.getDp());

						mobilefeed.setAttendeeId(feed.getAttendeeId());

						List<MobileEventFeedComment> listComment = new ArrayList<MobileEventFeedComment>();

						if (feed.getComments() != null) {

							for (Comment comment : feed.getComments()) {

								MobileEventFeedComment commentTemp = new MobileEventFeedComment();

								commentTemp.setCreatedByname(comment.getCreatedByname());

								commentTemp.setCreatedDate(comment.getCreatedDate());

								commentTemp.setId(comment.getId());

								commentTemp.setText(comment.getText());

								if (comment.getCreatedDate().compareTo(dateTime) > 0)

									listComment.add(commentTemp);
							}
						}

						// mobilefeed.setActivityId();

						mobilefeed.setComments(listComment);

						track = mobilefeed;

					}

					boolean flag = false;

					for (Object object : updateList) {

						if (object.equals(track)) {

							flag = true;

							break;

						}
					}
					if (!flag) {
						if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_SPONSOR_LOG_KEY)) {

							Sponsor spon = (Sponsor) track;

							EventSponsor sponsor = new EventSponsor();

							sponsor.setSponsorCategoryId(spon.getSponsorCategoryId());

							sponsor.setBusinessCategory(spon.getBusinessCategory());

							sponsor.setCreatedBy(spon.getCreatedBy());

							sponsor.setCreatedDate(spon.getCreatedDate());

							sponsor.setEmail(spon.getEmail());

							sponsor.setFirstName(spon.getFirstName());

							sponsor.setId(spon.getId());

							sponsor.setInvite(spon.getInvite());

							// sponsor.setPicture(spon.getPicture());

							sponsor.setPicture(spon.getPictureO());

							sponsor.setIsActive(spon.getIsActive());

							sponsor.setIsDeleted(spon.getIsDeleted());

							sponsor.setLastModifiedBy(spon.getLastModifiedBy());

							sponsor.setLastModifiedDate(spon.getLastModifiedDate());

							sponsor.setLastName(spon.getLastName());

							if (spon.getMultiLingual() != null) {
								sponsor.setMultiLingual(Utils.getMultiLingualByLangCode(spon.getMultiLingual(), code, eventDefault));
							}

							sponsor.setName(spon.getName());

							sponsor.setOrganizerId(spon.getOrganizerId());

							sponsor.setPhone(spon.getPhone());

							track = sponsor;

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_VENDOR_LOG_KEY)) {

							Vendor ven = (Vendor) track;

							EventVendor vend = new EventVendor(ven);

							track = vend;

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_PERSONNEL_LOG_KEY)) {

							// Personnel spon = (Personnel) track;

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.ACTIVITY_LOG_KEY)) {

							// Personnel spon = (Personnel) track;
							Activity activity = (Activity) track;

							if (activity != null) {

								Activity temp = (Activity) track;

								org.iqvis.nvolv3.mobile.bean.Activity tempAc = new org.iqvis.nvolv3.mobile.bean.Activity();

								tempAc.setId(temp.getId());

								tempAc.setName(temp.getName());

								tempAc.setOrganizerId(temp.getOrganizerId());

								tempAc.setStartTime(temp.getStartTime());

								tempAc.setEndTime(temp.getEndTime());

								tempAc.setEventDate(temp.getEventDate());

								tempAc.setTracks(temp.getTracks());

								if (temp.getMultiLingual() != null) {

									tempAc.setMultiLingual(Utils.getMultiLingualByLangCode(temp.getMultiLingual(), code, eventDefault));

								}

								tempAc.setTimeZoneOffSet(temp.getTimeZoneOffSet());

								tempAc.setLocation(temp.getLocationDetails());

								tempAc.setType(temp.getType());

								tempAc.setInner(temp);
								tempAc.setQuestionnaireId(temp.getQuestionnaireId());

								tempAc.setQuestionnaireType(temp.getQuestionnaireType());
								
								tempAc.setCheckIn(temp.getCheckIn());
								
								track = tempAc;
							}
						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_RESOURCE_MAP_LOG_KEY)) {

						}

						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_OTHER_RESOURCE_MAP_LOG_KEY)) {

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.VENUE_LOG_KEY)) {

							// EventVenue eventVenue = new EventVenue();
							// Venue venue = (Venue) track;

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.FEED_LOG_KEY)) {

						}

						else if ((dataChangeLog.getSubObject() + "").equals(Constants.EVENT_ALERT_LOG_KEY)) {

						}
						else if ((dataChangeLog.getSubObject() + "").equals(Constants.LOG_USER_FEEDBACK_QUESTION)) {

						}

						if (!updateList.contains(track) && !insertList.contains(track)) {

							updateList.add(track);

						}

					}

				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (dataChangeLog.getSubObjectAction().equals(Constants.LOG_ACTION_DELETE)) {

				if (!deleteList.contains(dataChangeLog.getSubObjectId()))

					deleteList.add(dataChangeLog.getSubObjectId());

			}
		}

		trackLog.setDelete(deleteList);

		trackLog.setUpdate(updateList);

		trackLog.setInsert(insertList);

		return trackLog;
	}

	public List<MobileEventFeedComment> getCommentsPullToRefresh(String feedId, String eventId, String organizerId, DateTime date) throws NotFoundException {

		Feed feed = mobileFeedController.get(feedId, null, null);

		List<MobileEventFeedComment> listComment = new ArrayList<MobileEventFeedComment>();

		if (feed.getComments() != null) {

			for (Comment comment : feed.getComments()) {

				MobileEventFeedComment commentTemp = new MobileEventFeedComment();

				commentTemp.setCreatedByname(comment.getCreatedByname());

				commentTemp.setCreatedDate(comment.getCreatedDate());

				commentTemp.setId(comment.getId());

				commentTemp.setText(comment.getText());

				commentTemp.setDp(comment.getDp());

				commentTemp.setAttendeeId(comment.getAttendeeId());

				if (commentTemp.getCreatedDate().compareTo(date) > 0) {

					listComment.add(commentTemp);

				}

				else {

					continue;

				}
			}
		}

		return listComment;
	}

	public List<MobileEventFeedComment> loadMore(String feedId, String eventId, String organizerId, DateTime date) throws NotFoundException {
		// TODO Auto-generated method stub

		Feed feed = mobileFeedController.get(feedId, null, null);

		List<MobileEventFeedComment> listComment = new ArrayList<MobileEventFeedComment>();

		if (feed.getComments() != null) {

			for (Comment comment : feed.getComments()) {

				MobileEventFeedComment commentTemp = new MobileEventFeedComment();

				commentTemp.setCreatedByname(comment.getCreatedByname());

				commentTemp.setCreatedDate(comment.getCreatedDate());

				commentTemp.setId(comment.getId());

				commentTemp.setText(comment.getText());

				commentTemp.setDp(comment.getDp());

				commentTemp.setAttendeeId(comment.getAttendeeId());

				if (commentTemp.getCreatedDate().compareTo(date) < 0) {

					listComment.add(commentTemp);

				}

				else {

					continue;

				}
			}
		}

		return (listComment.size() > 10 ? listComment.subList(0, 10) : listComment);
	}

	public SyncDataObject getFeedPullToRefresh(List<FeedObject> feedIds, String eventId, String organizerId, DateTime date) throws NotFoundException {

		if (feedIds == null || feedIds.size() <= 0) {

		}
		else {
			Collections.sort(feedIds, new FeedObjectSortDesc());

			date = feedIds.get(feedIds.size() - 1).getCreationDate();
		}

		List<MobileEventFeed> tempFeedList = new ArrayList<MobileEventFeed>();
		List<Feed> list = feedService.getAll(eventId);
		for (Feed feed : list) {
			MobileEventFeed tempFeeds = new MobileEventFeed();

			tempFeeds.setEventId(feed.getEventId());

			tempFeeds.setCommentsCount(feed.getCommentCount());

			tempFeeds.setCreatedDate(feed.getCreatedDate());

			tempFeeds.setId(feed.getId());

			tempFeeds.setLastModifiedDate(feed.getLastModifiedDate());

			tempFeeds.setLikes(feed.getLikes());

			// tempFeeds.setLogo(new Picture(feed.getPicture()));
			tempFeeds.setLogo(new Picture(feed.getPictureO()));

			tempFeeds.setText(feed.getTitle());

			tempFeeds.setType(feed.getType());

			tempFeeds.setTypeId(feed.getTypeId());

			tempFeeds.setCreatedByEmail(feed.getCreatedByEmail());

			tempFeeds.setCreatedByname(feed.getCreatedByname());

			List<MobileEventFeedComment> listComment = new ArrayList<MobileEventFeedComment>();

			if (feed.getComments() != null) {

				for (Comment comment : feed.getComments()) {

					MobileEventFeedComment commentTemp = new MobileEventFeedComment();

					commentTemp.setCreatedByname(comment.getCreatedByname());

					commentTemp.setCreatedDate(comment.getCreatedDate());

					commentTemp.setId(comment.getId());

					commentTemp.setText(comment.getText());

					commentTemp.setDp(comment.getDp());

					commentTemp.setAttendeeId(comment.getAttendeeId());

					if (commentTemp.getCreatedDate().compareTo(date) > 0) {

						listComment.add(commentTemp);

					}

					else {

						continue;

					}

				}

			}

			tempFeeds.setComments(listComment);

			if (feed.getCreatedDate().compareTo(date) > 0 || feed.getLastModifiedDate().compareTo(date) > 0)

				tempFeedList.add(tempFeeds);

			else

				continue;

		}

		List<MobileEventFeed> tempFeeds = null;

		List<FeedObject> deleteFeeds = null;

		if (tempFeedList.size() + feedIds.size() > Constants.THRESHOLD) {

			tempFeeds = tempFeedList.subList(tempFeedList.size() - Constants.FEED_LIMIT, tempFeedList.size() - 1);

		}
		else {

			tempFeeds = tempFeedList;

		}
		if ((feedIds != null && feedIds.size() > 0) && feedIds.size() > Constants.FEED_LIMIT) {

			deleteFeeds = feedIds.subList(0, Constants.FEED_LIMIT);

		}
		else {
			// deleteFeeds = feedIds;
		}

		List<String> deleteMe = new ArrayList<String>();

		if (deleteFeeds != null) {

			for (FeedObject feedObject : deleteFeeds) {

				deleteMe.add(feedObject.getFeedId());

			}

		}

		SyncDataObject syncData = new SyncDataObject(null, tempFeeds, deleteMe);

		return syncData;
	}

	public SyncDataObject feedLoadMore(List<FeedObject> feedIds, String eventId, String organizerId, DateTime date) throws NotFoundException {

		if (feedIds == null || feedIds.size() <= 0) {

		}
		else {
			Collections.sort(feedIds, new FeedObjectSort());

			for (FeedObject feedObject : feedIds) {

				System.out.println(feedObject.getFeedId());

			}

			date = feedIds.get(feedIds.size() - 1).getCreationDate();

		}

		List<MobileEventFeed> tempFeedList = new ArrayList<MobileEventFeed>();

		List<Feed> list = feedService.getOlderByDate(eventId, date);

		for (Feed feed : list) {

			MobileEventFeed tempFeeds = new MobileEventFeed();

			tempFeeds.setEventId(feed.getEventId());

			tempFeeds.setCommentsCount(feed.getCommentCount());

			tempFeeds.setCreatedDate(feed.getCreatedDate());

			tempFeeds.setId(feed.getId());

			tempFeeds.setLastModifiedDate(feed.getLastModifiedDate());

			tempFeeds.setLikes(feed.getLikes());

			// tempFeeds.setLogo(new Picture(feed.getPicture()));

			tempFeeds.setLogo(new Picture(feed.getPictureO()));

			tempFeeds.setText(feed.getDescription());

			tempFeeds.setType(feed.getType());

			tempFeeds.setTypeId(feed.getTypeId());

			tempFeeds.setCreatedByEmail(feed.getCreatedByEmail());

			tempFeeds.setCreatedByname(feed.getCreatedByname());

			tempFeeds.setSource(feed.getSource());

			tempFeeds.setDp(feed.getDp());

			List<MobileEventFeedComment> listComment = new ArrayList<MobileEventFeedComment>();

			if (feed.getComments() != null) {
				for (Comment comment : feed.getComments()) {

					MobileEventFeedComment commentTemp = new MobileEventFeedComment();

					commentTemp.setCreatedByname(comment.getCreatedByname());

					commentTemp.setCreatedDate(comment.getCreatedDate());

					commentTemp.setId(comment.getId());

					commentTemp.setText(comment.getText());

					commentTemp.setDp(comment.getDp());

					commentTemp.setAttendeeId(comment.getAttendeeId());

//					if (commentTemp.getCreatedDate().compareTo(date) <= 0) {

						listComment.add(commentTemp);

//					}
//
//					else {
//
//						continue;
//
//					}
				}
			}

			tempFeeds.setComments(listComment);

			// || feed.getLastModifiedDate().compareTo(date) > 0

//			if (feed.getCreatedDate().compareTo(date) < 0) {

				tempFeedList.add(tempFeeds);
//			}
//			else {
//				continue;
//			}

		}

		List<MobileEventFeed> tempFeeds = null;

		// List<FeedObject> deleteFeeds = null;

//		if (tempFeedList.size() + feedIds.size() > Constants.THRESHOLD) {
//			tempFeeds = tempFeedList.subList(tempFeedList.size() - Constants.FEED_LIMIT, tempFeedList.size() - 1);
//		}
//		else {
			tempFeeds = tempFeedList;
//		}
//		if ((feedIds != null && feedIds.size() > 0) && feedIds.size() > Constants.FEED_LIMIT) {
			// deleteFeeds = feedIds.subList(0, Constants.FEED_LIMIT);
//		}
//		else {
			// /deleteFeeds = feedIds;
//		}

		List<String> deleteMe = new ArrayList<String>();
		// if(deleteFeeds!=null){
		// for (FeedObject feedObject : deleteFeeds) {
		// deleteMe.add(feedObject.getFeedId());
		// }
		// }

		SyncDataObject syncData = new SyncDataObject(tempFeeds, null, deleteMe);

		return syncData;
	}

	public ChangeTrackLog getLogListForAttendee(String eventId, String organizerId, DateTime dateTime) {

		List<DataChangeLog> list = dataChangeDao.getLogList(eventId, organizerId, dateTime, Constants.LOG_ATTENDEE_PROFILE);

		List<String> checkList = new ArrayList<String>();

		List<Object> delete = new ArrayList<Object>();

		List<Object> insert = new ArrayList<Object>();

		List<Object> update = new ArrayList<Object>();

		for (DataChangeLog dataChangeLog : list) {

			if (checkList.contains(dataChangeLog.getSubObjectId())) {
				continue;
			}
			else {
				checkList.add(dataChangeLog.getSubObjectId());
			}

			Attendee attendee = attendeeService.get(dataChangeLog.getSubObjectId());

			MobileAttendee mAttendee = null;

			if (attendee != null) {

				mAttendee = new MobileAttendee(attendee);
			}

			if (Constants.LOG_ACTION_ADD.equals(dataChangeLog.getAction())) {

				if (mAttendee != null) {

					insert.add(mAttendee);
				}
			}
			else if (Constants.LOG_ACTION_UPDATE.equals(dataChangeLog.getAction())) {

				if (mAttendee != null) {

					update.add(mAttendee);
				}
			}
			else if (Constants.LOG_ACTION_DELETE.equals(dataChangeLog.getAction())) {

				delete.add(dataChangeLog.getSubObjectId());
			}

		}

		return new ChangeTrackLog(insert, update, delete);
	}

	class FeedObjectSort implements Comparator<FeedObject> {

		public int compare(FeedObject o1, FeedObject o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2);
		}
	}

	class FeedObjectSortDesc implements Comparator<FeedObject> {

		public int compare(FeedObject o1, FeedObject o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2) * -1;
		}
	}

}
