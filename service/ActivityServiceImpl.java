package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.ActivityPersonnels;
import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.ActivityDao;
import org.iqvis.nvolv3.dao.FeedDao;
import org.iqvis.nvolv3.dao.TimeZoneResponse;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.UserFeedbackQuestion;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ch.lambdaj.Lambda;

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_ACTIVITY)
@Transactional
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private FeedDao feedDao;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_PERSONNEL)
	private PersonnelService personnelService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_LOCATION)
	private LocationService locationService;

	@Resource(name = Constants.SERVICE_VENUE)
	private VenueService venueService;

	public Page<Activity> getAll(Criteria userCriteria, Pageable pageAble, String organizerId, String eventId) {

		return activityDao.getAll(Utils.parseQuery(userCriteria, "activities."), userCriteria, pageAble, organizerId, eventId);
	}

	public Activity get(String id, String eventId) throws NotFoundException {

		return activityDao.get(id, eventId);
	}

	public Activity add(Activity activity, Event event) throws Exception {

		if (activity.getIsActive() == null) {

			activity.setIsActive(true);
		}

		if (activity.getIsDeleted() == null) {

			activity.setIsDeleted(false);
		}

		activity.setCreatedDate(new DateTime());

		//
		// adding activity personnel to event if it is not in event personnel
		//

		List<EventPersonnel> personnelsForEvent = new ArrayList<EventPersonnel>();

		List<String> personnelsIdListForEvent = new ArrayList<String>();

		if (activity.getPersonnels() != null) {

			for (ActivityPersonnels activityGroupOfPersonnel : activity.getPersonnels()) {

				for (String personnelId : activityGroupOfPersonnel.getPersonnels()) {

					List<EventPersonnel> eventPersonnel = Lambda.select(event.getEventPersonnels(), Lambda.having(Lambda.on(EventPersonnel.class).getPersonnelId(), Matchers.equalTo(personnelId)));

					if (eventPersonnel == null || eventPersonnel.size() == 0) {

						EventPersonnel ePersonnel = new EventPersonnel();

						ePersonnel.setPersonnelId(personnelId);

						ePersonnel.setFeatured(false);

						personnelsForEvent.add(ePersonnel);

						personnelsIdListForEvent.add(personnelId);

						// TODO Add Personnel Logging information

						List<String> l = new ArrayList<String>();

						l.add(activity.getEventId());

						if (l.size() > 0) {
							dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, personnelId, Constants.LOG_ACTION_ADD, Personnel.class.toString());

						}
					}
				}

			}
		}

		if (event.getEventPersonnels() == null) {

			event.setEventPersonnels(personnelsForEvent);

		}
		else {

			event.getEventPersonnels().addAll(personnelsForEvent);

		}
		// Creation of new activity
		Activity addedActivity = activityDao.add(activity, event);

		// Adding activity in personnel

		if (personnelsIdListForEvent.size() > 0) {

			List<Personnel> organizerPersonnels = personnelService.getPersonnelsByIds(personnelsIdListForEvent, event.getOrganizerId());

			for (Personnel personnel : organizerPersonnels) {

				if (personnel.getActivities() != null) {

					personnel.getActivities().add(addedActivity.getId());

					personnelService.edit(personnel, personnel.getId());
				}
			}

		}

		// Logging Start
		List<String> l = new ArrayList<String>();

		l.add(event.getId());

		dataChangeLogService.add(l, "EVENT", Constants.ACTIVITY_LOG_KEY, activity.getId(), Constants.LOG_ACTION_ADD, Activity.class.toString());

		return addedActivity;

	}

	public Boolean delete(String id) {

		return activityDao.delete(id);
	}

	public Activity edit(Activity activity, String eventId) throws Exception, NotFoundException {

		List<ActivityPersonnels> oldActivityPersonnels = null;

		Activity existingActivity = get(activity.getId(), eventId);

		if (null == existingActivity) {

			throw new NotFoundException(activity.getId(), "Activity");

		}
		else {

			if (existingActivity.getPersonnels() != null) {

				oldActivityPersonnels = existingActivity.getPersonnels();
			}

			if (!existingActivity.getOrganizerId().equals(activity.getOrganizerId())) {

				throw new NotFoundException(activity.getId(), "Activity");
			}

			activity.setLastModifiedDate(new DateTime());

			activity.setLastModifiedBy(activity.getCreatedBy());

			if (activity.getIsDeleted() != null && !StringUtils.isEmpty(activity.getIsDeleted().toString())) {

				existingActivity.setIsDeleted(activity.getIsDeleted());
			}

			if (activity.getEventId() != null && activity.getEventId().equals(0)) {

				existingActivity.setEventId(activity.getEventId());
			}

			if (activity.getName() != null && !StringUtils.isEmpty(activity.getName())) {

				existingActivity.setName(activity.getName());
			}

			if (activity.getEndTime() != null && !StringUtils.isEmpty(activity.getEndTime().toString())) {

				existingActivity.setEndTime(activity.getEndTime());
			}

			if (activity.getStartTime() != null && !StringUtils.isEmpty(activity.getStartTime().toString())) {

				existingActivity.setStartTime(activity.getStartTime());
			}

			// if (activity.getLocation() != null &&
			// !StringUtils.isEmpty(activity.getLocation().toString())) {

			existingActivity.setLocation(activity.getLocation());
			// }

			if (activity.getStartTime() != null && !StringUtils.isEmpty(activity.getStartTime().toString())) {

				Event event = eventService.get(activity.getEventId(), activity.getOrganizerId());

				Venue venue = null;

				try {
					try {
						venue = venueService.get(event.getVenueId(), event.getOrganizerId());
					}
					catch (Exception e) {

					}
					if (venue != null && ((venue.getLatitude() != null && venue.getLongitude() != null) && (venue.getLatitude() != 0 && venue.getLongitude() != 0))) {

						Date mergeDate = activity.getEventDate().toDate();
						
						Date mergeTime = activity.getStartTime().toDate();
						
						Date mergeDateTime = Utils.dateTime(mergeDate, mergeTime);
						
						
						TimeZoneResponse timeZoneResponse = Utils.getTimeZone(venue.getLatitude(), venue.getLongitude(), mergeDateTime);

						double rawOffSet = timeZoneResponse.getRawOffset() / 3600;

						double dstOffSet = timeZoneResponse.getDstOffset() / 3600;

						double sumTotalOffSet = rawOffSet + dstOffSet;

						existingActivity.setTimeZoneOffSet(String.format("%.2f", sumTotalOffSet).replace(".", ":"));
						
						//existingActivity.setTimeZoneOffSet("-5:00");

					}
					else {

						existingActivity.setTimeZoneOffSet(event.getTimeZone());
					}
				}
				catch (Exception e) {

					e.printStackTrace();
				}

			}

			if (activity.getOrganizerId() != null && !StringUtils.isEmpty(activity.getOrganizerId().toString())) {

				existingActivity.setOrganizerId(activity.getOrganizerId());
			}

			if (activity.getIsActive() != null && !StringUtils.isEmpty(activity.getIsActive().toString())) {

				existingActivity.setIsActive(activity.getIsActive());
			}

			// if (activity.getPersonnels() != null &&
			// activity.getPersonnels().size() > 0) {

			List<EventPersonnel> personnelsForEvent = new ArrayList<EventPersonnel>();

			if (activity.getPersonnels() != null) {

				for (ActivityPersonnels eventPersonnel : activity.getPersonnels()) {

					if (existingActivity.getPersonnels() != null) {

						for (ActivityPersonnels ePersonnel : existingActivity.getPersonnels()) {

							if (ePersonnel.getTypeId().equals(eventPersonnel.getTypeId())) {

								List<String> eIds = ePersonnel.getPersonnels() == null ? new ArrayList<String>() : ePersonnel.getPersonnels();

								List<String> Ids = eventPersonnel.getPersonnels() == null ? new ArrayList<String>() : eventPersonnel.getPersonnels();

								for (String id : Ids) {
									if (!eIds.contains(id)) {
										// TODO bind personnel with event and
										// also add logging
										// information

										EventPersonnel evPersonnel = new EventPersonnel();

										evPersonnel.setPersonnelId(id);

										evPersonnel.setFeatured(false);

										personnelsForEvent.add(evPersonnel);

										// TODO Add Personnel Logging
										// information

										List<String> l = new ArrayList<String>();

										l.add(activity.getEventId());

										if (l.size() > 0) {
											dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, id, Constants.LOG_ACTION_ADD, Personnel.class.toString());

										}

									}
								}

							}

						}
					}

				}

			}

			Event event = eventService.get(activity.getEventId(), activity.getOrganizerId());

			if (event.getEventPersonnels() == null) {

				event.setEventPersonnels(personnelsForEvent);

			}
			else {

				event.getEventPersonnels().addAll(personnelsForEvent);

			}

			eventService.edit(event, activity.getEventId(), activity.getOrganizerId());

			existingActivity.setPersonnels(activity.getPersonnels());

			// }

			// if (activity.getTracks() != null && activity.getTracks().size() >
			// 0) {

			existingActivity.setTracks(activity.getTracks());

			// }else{
			//
			// existingActivity.setTracks(null);
			// }

			if (activity.getEventDate() != null) {

				existingActivity.setEventDate(activity.getEventDate());
			}

			if (null != activity.getMultiLingual() && activity.getMultiLingual().size() > 0) {

				List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

				if (null != existingActivity.getMultiLingual()) {

					finalLanguages = Utils.updateMultiLingual(existingActivity.getMultiLingual(), activity.getMultiLingual());
				}
				else {

					finalLanguages = activity.getMultiLingual();
				}

				existingActivity.setMultiLingual(finalLanguages);

			}

			existingActivity.setLastModifiedBy(activity.getCreatedBy());

			if (existingActivity.getVersion() != null) {

				existingActivity.setVersion(existingActivity.getVersion() + 1);
			}

			if (activity.getType() != null && !StringUtils.isEmpty(activity.getType())) {

				existingActivity.setType(activity.getType());
			}

			if (activity.getSuperType() != null && !StringUtils.isEmpty(activity.getSuperType())) {

				existingActivity.setSuperType(activity.getSuperType());
			}

			if (activity.getQuestionnaireType() != null) {
				existingActivity.setQuestionnaireType(activity.getQuestionnaireType());
			}

			if (activity.getQuestionnaireId() != null) {

				if (!activity.getQuestionnaireId().equals(existingActivity.getQuestionnaireId()) && !"".equals(activity.getQuestionnaireId())) {
					List<String> l = new ArrayList<String>();

					l.add(existingActivity.getEventId());

					l.remove("");

					if (l.size() > 0) {

						dataChangeLogService.add(l, "EVENT", Constants.LOG_USER_FEEDBACK_QUESTION, activity.getQuestionnaireId(), Constants.LOG_ACTION_UPDATE, UserFeedbackQuestion.class.toString());

					}
				}
				existingActivity.setQuestionnaireId(activity.getQuestionnaireId());
			}

		}

		existingActivity = activityDao.edit(existingActivity);

		try {
			updatePerosonalActivities(existingActivity.getId(), existingActivity.getOrganizerId(), oldActivityPersonnels, existingActivity.getPersonnels());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Logging Start
		List<String> l = new ArrayList<String>();

		if (!existingActivity.getIsDeleted()) {
			l.add(eventId);

			if (l.size() > 0) {
				dataChangeLogService.add(l, "EVENT", Constants.ACTIVITY_LOG_KEY, activity.getId(), Constants.LOG_ACTION_UPDATE, Activity.class.toString());

			}
		}
		else {
			l.add(eventId);

			if (l.size() > 0) {
				dataChangeLogService.add(l, "EVENT", Constants.ACTIVITY_LOG_KEY, activity.getId(), Constants.LOG_ACTION_DELETE, Activity.class.toString());

			}
		}

		return existingActivity;
	}

	public String getActivityDetailUrl(Activity activity, HttpServletRequest request) {

		return activityDao.getActivityDetailUrl(activity, request);
	}

	public void updatePerosonalActivities(String activityId, String organizerId, List<ActivityPersonnels> oldPersonnels, List<ActivityPersonnels> newPersonnels) {

		// Removing the personnel activities
		if (oldPersonnels != null) {

			for (ActivityPersonnels activityPersonnels : oldPersonnels) {

				for (int i = 0; i < activityPersonnels.getPersonnels().size(); i++) {

					Personnel personnel = null;

					try {

						personnel = personnelService.get(activityPersonnels.getPersonnels().get(i), organizerId);

					}
					catch (NotFoundException e) {
						e.printStackTrace();
					}

					if (personnel != null) {

						if (personnel.getActivities() != null) {

							if (personnel.getActivities().contains(activityId)) {

								personnel.getActivities().remove(activityId);
							}
						}

						try {
							personnelService.edit(personnel, personnel.getId().toString());
						}
						catch (NotFoundException e) {

							e.printStackTrace();
						}
						catch (Exception e) {

							e.printStackTrace();
						}
					}
				}
			}
		}
		// Adding Personnel activities
		if (newPersonnels != null && newPersonnels.size() > 0) {

			for (ActivityPersonnels activityPersonnels : newPersonnels) {

				for (int i = 0; i < activityPersonnels.getPersonnels().size(); i++) {

					Personnel personnel = null;
					try {
						personnel = personnelService.get(activityPersonnels.getPersonnels().get(i), organizerId);
					}
					catch (NotFoundException e) {
						e.printStackTrace();
					}

					if (personnel != null) {

						if (personnel.getActivities() == null) {

							List<String> newActivities = new ArrayList<String>();

							newActivities.add(activityId);

							personnel.setActivities(newActivities);

						}
						else {

							if (!personnel.getActivities().contains(activityId)) {

								personnel.getActivities().add(activityId);
							}
						}

						try {
							personnelService.edit(personnel, personnel.getId());
						}
						catch (NotFoundException e) {
							e.printStackTrace();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public Activity getActivityById(String id) throws NotFoundException {

		return activityDao.getActivityById(id);
	}

	// public Page<Activity> getAllCMS(Criteria userCriteria, Pageable pageAble,
	// String organizerId, String eventId) {
	// // TODO Auto-generated method stub
	// return feedDao.getAllCMS(Utils.parseCriteria(userCriteria, ""), pageAble,
	// type, typeId);
	// }
}
