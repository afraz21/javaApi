package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ActivityPersonnels;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.dao.TimeZoneResponse;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.ActivityFeedBackReport;
import org.iqvis.nvolv3.domain.AnswerObject;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.FeedBackReportResponse;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.EventTrack;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.ActivityService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.EventTrackService;
import org.iqvis.nvolv3.service.LocationService;
import org.iqvis.nvolv3.service.PersonnelService;
import org.iqvis.nvolv3.service.TrackService;
import org.iqvis.nvolv3.service.UserFeedbackAnswerService;
import org.iqvis.nvolv3.service.VenueService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
@RequestMapping(Urls.ACTIVITY_BASE_URL)
public class ActivityController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;

	@Resource(name = Constants.SERVICE_TRACK)
	private TrackService trackService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_PERSONNEL)
	private PersonnelService personnelService;

	@Resource(name = Constants.SERVICE_LOCATION)
	private LocationService locationService;

	@Resource(name = Constants.SERVICE_EVENT_TRACK)
	private EventTrackService eventTrackService;

	@Resource(name = Constants.SERVICE_VENUE)
	private VenueService venueService;

	@RequestMapping(value = Urls.ADD_ACTIVITY, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addActivity(@RequestBody @Valid Activity activity, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add Activity");

		ResponseMessage response = new ResponseMessage();

		try {

			activity.setOrganizerId(organizerId);

			Event event = eventService.get(activity.getEventId(), organizerId);

			if (event == null) {

				throw new NotFoundException(activity.getEventId(), "Event");
			}
			

			Venue venue = null;

			try {

				venue = venueService.get(event.getVenueId(), event.getOrganizerId());
			}
			catch (Exception e) {
			}
			
			if (venue != null && ((venue.getLatitude() != null && venue.getLongitude() != null) && (venue.getLatitude() != 0 && venue.getLongitude() != 0))) {

				Date mergeDate = activity.getEventDate().toDate();
				
				Date mergeTime = activity.getStartTime().toDate();
				
				Date mergeDateTime = Utils.dateTime(mergeDate, mergeTime);
				
				//TimeZoneResponse timeZoneResponse = Utils.getTimeZone(venue.getLatitude(), venue.getLongitude(), activity.getStartTime());
				
				TimeZoneResponse timeZoneResponse = Utils.getTimeZone(venue.getLatitude(), venue.getLongitude(), mergeDateTime);

				System.out.println("Raw offset"+ timeZoneResponse.getRawOffset() +"   DST"+timeZoneResponse.getDstOffset());
				
				double rawOffSet = timeZoneResponse.getRawOffset() / 3600;

				double dstOffSet = timeZoneResponse.getDstOffset() / 3600;

				double sumTotalOffSet = rawOffSet + dstOffSet;

				System.out.println("sum of total offset"+ sumTotalOffSet);
				
				activity.setTimeZoneOffSet(String.format("%.2f", sumTotalOffSet).replace(".", ":"));
				
				//activity.setTimeZoneOffSet("-5:00");

			}
			else {

				activity.setTimeZoneOffSet(event.getTimeZone());
			}

			
			Activity addedActivity = activityService.add(activity, event);

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Activity"));

			response.setRecordId(addedActivity.getId().toString());

			response.setRecord(addedActivity);

			try {

				Location location = locationService.get(addedActivity.getLocation(), organizerId);

				addedActivity.setLocationDetails(location);

				List<EventTrack> listTracks = new ArrayList<EventTrack>();

				for (String track : addedActivity.getTracks()) {

					org.iqvis.nvolv3.mobile.bean.EventTrack eventTrackResponse = new org.iqvis.nvolv3.mobile.bean.EventTrack();

					org.iqvis.nvolv3.bean.EventTrack eventTrack = eventTrackService.get(track, organizerId, activity.getEventId());

					Track mainTrack = eventTrackService.getEventTrack(track, organizerId, activity.getEventId());

					eventTrackResponse.setColorCode(eventTrack.getColorCode());

					eventTrackResponse.setCreatedBy(mainTrack.getCreatedBy());

					eventTrackResponse.setCreatedDate(mainTrack.getCreatedDate());

					eventTrackResponse.setId(mainTrack.getId());

					eventTrackResponse.setIsDeleted(mainTrack.getIsDeleted());

					eventTrackResponse.setLastModifiedBy(mainTrack.getLastModifiedBy());

					eventTrackResponse.setLastModifiedDate(mainTrack.getLastModifiedDate());

					eventTrackResponse.setMultiLingual(mainTrack.getMultiLingual());

					eventTrackResponse.setName(mainTrack.getName());

					eventTrackResponse.setOrganizerId(mainTrack.getOrganizerId());

					eventTrackResponse.setPicture(mainTrack.getPictureO());

					eventTrackResponse.setSortOrder(eventTrack.getSortOrder() != null ? eventTrack.getSortOrder().toString() : null);

					eventTrackResponse.setVersion(mainTrack.getVersion());

					listTracks.add(eventTrackResponse);
				}

				addedActivity.setTrackDetails(listTracks);
			}
			catch (Exception e) {
				logger.debug("Tracks or Location is NULL/NotFoundException");
			}

			response.setDetails_url(activityService.getActivityDetailUrl(addedActivity, request));

			logger.debug("Activity has been added successfully");

			/*
			 * Updating personnel Data after adding the activity
			 */

			if (addedActivity.getPersonnels() != null && addedActivity.getPersonnels().size() > 0) {

				for (ActivityPersonnels activityPersonnels : addedActivity.getPersonnels()) {

					for (int i = 0; i < activityPersonnels.getPersonnels().size(); i++) {

						Personnel personnel = personnelService.get(activityPersonnels.getPersonnels().get(i), organizerId);

						if (personnel != null) {

							if (personnel.getActivities() == null) {

								List<String> newActivities = new ArrayList<String>();

								newActivities.add(addedActivity.getId());

								personnel.setActivities(newActivities);

							}
							else {

								if (!personnel.getActivities().contains(addedActivity.getId())) {

									personnel.getActivities().add(addedActivity.getId());
								}
							}

							personnelService.edit(personnel, personnel.getId());
						}
					}
				}
			}
		}
		catch (Exception e) {

			logger.debug("Exception while adding Activity", e);

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_ACTIVITY, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editEvent(@RequestBody Activity activity, @PathVariable("tid") String eventId, @PathVariable("id") String activityId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a activity");

		ResponseMessage response = new ResponseMessage();

		activity.setOrganizerId(organizerId);

		activity.setId(activityId);
		try {

			Activity editedActivity = activityService.edit(activity, eventId);

			try {
				editedActivity.setLocationDetails(locationService.get(editedActivity.getLocation(), organizerId));
				List<EventTrack> listTracks = new ArrayList<EventTrack>();

				logger.debug("Tracks Size ---------------- : " + editedActivity.getTracks().size());

				if (editedActivity.getTracks() != null) {

					for (String track : editedActivity.getTracks()) {

						org.iqvis.nvolv3.mobile.bean.EventTrack eventTrackResponse = new org.iqvis.nvolv3.mobile.bean.EventTrack();

						org.iqvis.nvolv3.bean.EventTrack eventTrack = eventTrackService.get(track, organizerId, eventId);

						Track mainTrack = eventTrackService.getEventTrack(track, organizerId, eventId);

						eventTrackResponse.setColorCode(eventTrack.getColorCode());

						eventTrackResponse.setCreatedBy(mainTrack.getCreatedBy());

						eventTrackResponse.setCreatedDate(mainTrack.getCreatedDate());

						eventTrackResponse.setId(mainTrack.getId());

						eventTrackResponse.setIsDeleted(mainTrack.getIsDeleted());

						eventTrackResponse.setLastModifiedBy(mainTrack.getLastModifiedBy());

						eventTrackResponse.setLastModifiedDate(mainTrack.getLastModifiedDate());

						eventTrackResponse.setMultiLingual(mainTrack.getMultiLingual());

						eventTrackResponse.setName(mainTrack.getName());

						eventTrackResponse.setOrganizerId(mainTrack.getOrganizerId());

						eventTrackResponse.setPicture(mainTrack.getPictureO());

						eventTrackResponse.setSortOrder(eventTrack.getSortOrder() != null ? eventTrack.getSortOrder().toString() : null);

						eventTrackResponse.setVersion(mainTrack.getVersion());

						logger.debug("ID : " + mainTrack.getId());

						listTracks.add(eventTrackResponse);
					}

					editedActivity.setTrackDetails(listTracks);

				}
				else {

					editedActivity.setTrackDetails(null);
				}
			}
			catch (Exception e) {
				logger.debug("Tracks or Location is NULL/NotFoundException");
			}

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Activity"));

			response.setRecord(editedActivity);

			response.setRecordId(editedActivity.getId().toString());

			response.setDetails_url(activityService.getActivityDetailUrl(editedActivity, request));

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(activityId, "Activity");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_ACTIVITY, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage deleteEvent(@PathVariable("id") String activityId, @PathVariable("tid") String eventId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a activity");

		ResponseMessage response = new ResponseMessage();

		try {

			Activity activity = activityService.get(activityId, eventId);

			if (activity != null) {

				activity.setIsDeleted(true);

				activityService.edit(activity, eventId);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Activity"));

				response.setRecordId(activityId);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(activityId, "Activity");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(activityId, "Activity");
			}
			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_ACTIVITY, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Activity getEvent(@PathVariable("id") String activityId, @PathVariable("tid") String eventId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a activity");

		Activity existingActivity = null;

		if (null != activityId && !activityId.equalsIgnoreCase("")) {

			existingActivity = activityService.get(activityId, eventId);

			try {
				existingActivity.setLocationDetails(locationService.get(existingActivity.getLocation(), organizerId));

				List<EventTrack> listTracks = new ArrayList<EventTrack>();

				for (String track : existingActivity.getTracks()) {

					org.iqvis.nvolv3.mobile.bean.EventTrack response = new org.iqvis.nvolv3.mobile.bean.EventTrack();

					org.iqvis.nvolv3.bean.EventTrack eventTrack = eventTrackService.get(track, organizerId, eventId);

					Track mainTrack = eventTrackService.getEventTrack(track, organizerId, eventId);

					response.setColorCode(eventTrack.getColorCode());

					response.setCreatedBy(mainTrack.getCreatedBy());

					response.setCreatedDate(mainTrack.getCreatedDate());

					response.setId(mainTrack.getId());

					response.setIsDeleted(mainTrack.getIsDeleted());

					response.setLastModifiedBy(mainTrack.getLastModifiedBy());

					response.setLastModifiedDate(mainTrack.getLastModifiedDate());

					response.setMultiLingual(mainTrack.getMultiLingual());

					response.setName(mainTrack.getName());

					response.setOrganizerId(mainTrack.getOrganizerId());

					response.setPicture(mainTrack.getPictureO());

					response.setSortOrder(eventTrack.getSortOrder() != null ? eventTrack.getSortOrder().toString() : null);

					response.setVersion(mainTrack.getVersion());

					listTracks.add(response);
				}

				existingActivity.setTrackDetails(listTracks);

			}
			catch (Exception e) {
				logger.debug("Tracks or Location is NULL/NotFoundException");
			}

		}

		if (existingActivity == null) {

			throw new NotFoundException(activityId, "Activity");

		}
		else {

			if (!existingActivity.getOrganizerId().equals(organizerId)) {

				throw new NotFoundException(activityId, "Activity");
			}

		}
		//
		// if(existingActivity.getIsDeleted()){
		// throw new NotFoundException(existingActivity.getId(), "Activity");
		// }

		return existingActivity;
	}

	@RequestMapping(value = Urls.GET_ACTIVITIES, method = { RequestMethod.PUT, RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Activity> getActivities(@RequestBody(required = false) Criteria search, @PathVariable("organizerId") String organizerId, @PathVariable("tid") String eventId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all Activities");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {

			if (search.getQuery() != null) {

				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
				}
			}
		}

		Page<Activity> activities = activityService.getAll(search, pageAble, organizerId, eventId);

		List<Activity> tempList = activities.getContent();

		for (Activity activity : tempList) {
			Long count;
			try {
				count = userFeedbackAnswerService.getUserFeedBackCount("ACTIVITY", activity.getId());

				activity.setFeedbackCount(count);
			}
			catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		for (Activity activity : tempList) {
			try {

				activity.setLocationDetails(locationService.get(activity.getLocation(), organizerId));

				List<EventTrack> listTracks = new ArrayList<EventTrack>();

				for (String track : activity.getTracks()) {

					org.iqvis.nvolv3.mobile.bean.EventTrack response = new org.iqvis.nvolv3.mobile.bean.EventTrack();

					org.iqvis.nvolv3.bean.EventTrack eventTrack = eventTrackService.get(track, organizerId, eventId);

					Track mainTrack = eventTrackService.getEventTrack(track, organizerId, eventId);

					response.setColorCode(eventTrack.getColorCode());

					response.setCreatedBy(mainTrack.getCreatedBy());

					response.setCreatedDate(mainTrack.getCreatedDate());

					response.setId(mainTrack.getId());

					response.setIsDeleted(mainTrack.getIsDeleted());

					response.setLastModifiedBy(mainTrack.getLastModifiedBy());

					response.setLastModifiedDate(mainTrack.getLastModifiedDate());

					response.setMultiLingual(mainTrack.getMultiLingual());

					response.setName(mainTrack.getName());

					response.setOrganizerId(mainTrack.getOrganizerId());

					response.setPicture(mainTrack.getPictureO());

					response.setSortOrder(eventTrack.getSortOrder() != null ? eventTrack.getSortOrder().toString() : null);

					response.setVersion(mainTrack.getVersion());

					listTracks.add(response);
				}

				activity.setTrackDetails(listTracks);
			}

			catch (Exception e) {
				logger.debug("Tracks or Location is NULL/NotFoundException");
			}
		}

		return new PageImpl<Activity>(tempList, pageAble, activities.getTotalElements());
	}

	/**
	 * 
	 * 
	 * 
	 * **/

	@Resource(name = Constants.USER_FEEDBACK_ANSWER_RESOURCE)
	private UserFeedbackAnswerService userFeedbackAnswerService;

	@RequestMapping(value = Urls.GET_ACTIVITY + "/feedbackstats", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ActivityFeedBackReport getUserFeedbackStats(@PathVariable("id") String activityId, @PathVariable("tid") String eventId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a activity user feedback stats");

		long count = userFeedbackAnswerService.getUserFeedBackCount("ACTIVITY", activityId);

		List<FeedBackReportResponse> report = userFeedbackAnswerService.getUserFeedBackStats("ACTIVITY", activityId, eventId, activityId);

		return new ActivityFeedBackReport(report, count);
	}

	@RequestMapping(value = Urls.GET_ACTIVITY + "/feedbackstats/inputext", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Page<AnswerObject> getUserFeedbackStatsInput(@RequestBody(required = false) Criteria search, @PathVariable("id") String activityId, @PathVariable("tid") String eventId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a activity user feedback stats");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {

			if (search.getQuery() != null) {

				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
				}
			}
		}

		return userFeedbackAnswerService.getUserFeedBackStatsText("ACTIVITY", activityId, eventId, activityId, pageAble, Utils.parseQuery(search, "answers."));
	}

}