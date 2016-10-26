package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.dao.EventAlertDao;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.objectchangelog.service.PushExtra;
import org.iqvis.nvolv3.push.PushData;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_EVENT_ALERT)
@Transactional
@PropertySource("classpath:constants.properties")
public class EventAlertServiceImpl implements EventAlertService {
	
	@Autowired Environment environment;

	@Autowired
	EventAlertDao eventAlertDao;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;
	
	public PushData getAlertPushDate(EventAlert eventAdvert){
		
		PushData pushData = new PushData();
		
		PushExtra extra = new PushExtra();
		
		pushData.setAlert(eventAdvert.getTitle());

		extra.setObjectId(eventAdvert.getId());

		extra.setObjectType("ALERT");

		extra.setEventId(eventAdvert.getEventId());

		pushData.setExtra(extra);
				
//		pushData.setBadge("Increment");
		
		pushData.setBadge(environment.getProperty(Constants.BADGE));
		
		return pushData;
	}

	public EventAlert get(String id, String organizerId) throws NotFoundException {

		return eventAlertDao.get(id, organizerId);
	}

	public EventAlert add(EventAlert eventAdvert) throws Exception {

		eventAdvert.setCreatedDate(new DateTime());

		if (eventAdvert.getIsActive() == null) {

			eventAdvert.setIsActive(true);

		}

		if (eventAdvert.getIsDeleted() == null) {

			eventAdvert.setIsDeleted(false);
		}

		if (eventAdvert.getIsSent() == null) {

			eventAdvert.setIsSent(false);
		}

		if (eventAdvert.getSendAsPush() == null) {

			eventAdvert.setSendAsPush(false);
		}

		Event event = eventService.get(eventAdvert.getEventId(), eventAdvert.getOrganizerId());

		if (eventAdvert.getStartDate() != null) {

			DateTime startDateTime = eventAdvert.getStartDate();

			if (eventAdvert.getStartTime() != null && eventAdvert.getStartTime() != "") {

				startDateTime = Utils.setTimeToDate(eventAdvert.getStartTime(), startDateTime);

			}

			DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z");

			eventAdvert.setScheduledTimeDefault(formater.parseDateTime(Utils.toGMT(startDateTime, "00:00") + " Z"));

			eventAdvert.setScheduledTimeGMT(Utils.toGMT(startDateTime, event.getTimeZone()));

			DateTimeFormatter formatGMT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").withZoneUTC();
			;
			
			eventAdvert.setScheduledDateTimeGMT(formatGMT.parseDateTime(eventAdvert.getAlertScheduledTime()));

		}

		if (eventAdvert.getSentNow() != null) {

			if (eventAdvert.getSentNow()) {

				String tempTimeZone = "00:00";

				if (event.getTimeZone() != null) {

					String[] timeZone = event.getTimeZone().split(":");

					timeZone[0] = (Integer.parseInt(timeZone[0]) * -1) + "";

					tempTimeZone = timeZone[0] + ":" + timeZone[1];

				}

				String startDateTime = Utils.toGMT(DateTime.now(DateTimeZone.UTC), tempTimeZone) + " Z";

				DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z");

				eventAdvert.setStartDate(formater.parseDateTime(startDateTime));

				eventAdvert.setScheduledTimeDefault(formater.parseDateTime(startDateTime));

				eventAdvert.setScheduledTimeGMT(Utils.toGMT(DateTime.now(DateTimeZone.UTC), "00:00"));

				DateTimeFormatter formatGMT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").withZoneUTC();
				;

				eventAdvert.setScheduledDateTimeGMT(formatGMT.parseDateTime(eventAdvert.getAlertScheduledTime()));

			}

		}

		if (eventAdvert.getExpiryTime() != null) {

			eventAdvert.setExpiryTimeGMT(Utils.toGMT(eventAdvert.getExpiryTime(), event.getTimeZone()));

		}

		EventAlert eventAlert = eventAlertDao.add(eventAdvert);

		List<String> l = new ArrayList<String>();

		l.add(eventAlert.getEventId());

		if (eventAdvert.getSentNow() != null && eventAdvert.getSentNow() == true) {

			dataChangeLogService.add(l, "EVENT", Constants.EVENT_ALERT_LOG_KEY, eventAlert.getId(), Constants.LOG_ACTION_ADD, eventAlert.getClass().toString());
		}
		return eventAlert;
	}

	public EventAlert edit(EventAlert eventAlert) throws Exception {

		EventAlert existingEventAlert = get(eventAlert.getId(), eventAlert.getOrganizerId());

		if (null == existingEventAlert) {

			throw new NotFoundException(eventAlert.getId(), "EventAlert");

		}
		else {

			existingEventAlert.setLastModifiedDate(new DateTime());

			existingEventAlert.setLastModifiedBy(eventAlert.getCreatedBy());

			if (eventAlert.getIsDeleted() != null && !StringUtils.isEmpty(eventAlert.getIsDeleted().toString())) {
				existingEventAlert.setIsDeleted(eventAlert.getIsDeleted());
			}

			if (eventAlert.getIsActive() != null && !StringUtils.isEmpty(eventAlert.getIsActive())) {
				existingEventAlert.setIsActive(eventAlert.getIsActive());
			}

			// if (eventAlert.getPicture() != null &&
			// !StringUtils.isEmpty(eventAlert.getPicture())) {
			//
			// existingEventAlert.setPicture(eventAlert.getPicture());
			// }

			if (eventAlert.getPictureO() != null && !StringUtils.isEmpty(eventAlert.getPictureO())) {

				existingEventAlert.setPicture(eventAlert.getPictureO());
			}

			if (eventAlert.getStartDate() != null && !StringUtils.isEmpty(eventAlert.getStartDate())) {

				existingEventAlert.setStartDate(eventAlert.getStartDate());
			}

			if (eventAlert.getStartTime() != null && !StringUtils.isEmpty(eventAlert.getStartTime())) {

				existingEventAlert.setStartTime(eventAlert.getStartTime());
			}

			if (eventAlert.getIsSent() != null && !StringUtils.isEmpty(eventAlert.getIsSent())) {

				existingEventAlert.setIsSent(eventAlert.getIsSent());
			}

			if (eventAlert.getSendAsPush() != null && !StringUtils.isEmpty(eventAlert.getSendAsPush())) {

				existingEventAlert.setSendAsPush(eventAlert.getSendAsPush());
			}

			if (eventAlert.getTitle() != null) {

				existingEventAlert.setTitle(eventAlert.getTitle());
			}

			if (eventAlert.getDescription() != null) {

				existingEventAlert.setDescription(eventAlert.getDescription());
			}

			if (eventAlert.getSummary() != null) {

				existingEventAlert.setSummary(eventAlert.getSummary());
			}

			if (eventAlert.getSponsorId() != null) {

				existingEventAlert.setSponsorId(eventAlert.getSponsorId());
			}

			if (eventAlert.getPersonnelId() != null) {

				existingEventAlert.setPersonnelId(eventAlert.getPersonnelId());
			}

			if (eventAlert.getActivityId() != null) {

				existingEventAlert.setActivityId(eventAlert.getActivityId());
			}

			if (eventAlert.getExpiryTime() != null) {

				existingEventAlert.setExpiryTime(eventAlert.getExpiryTime());

			}

		}

		Event event = eventService.get(existingEventAlert.getEventId(), existingEventAlert.getOrganizerId());

		if (existingEventAlert.getStartDate() != null) {

			DateTime startDateTime = existingEventAlert.getStartDate();

			if (existingEventAlert.getStartTime() != null && existingEventAlert.getStartTime() != "") {

				startDateTime = Utils.setTimeToDate(existingEventAlert.getStartTime(), startDateTime);

			}

			DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z");

			existingEventAlert.setScheduledTimeDefault(formater.parseDateTime(Utils.toGMT(startDateTime, "00:00") + " Z"));

			existingEventAlert.setScheduledTimeGMT(Utils.toGMT(startDateTime, event.getTimeZone() == null ? "00:00" : event.getTimeZone()));

			DateTimeFormatter formatGMT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").withZoneUTC();

			existingEventAlert.setScheduledDateTimeGMT(formatGMT.parseDateTime(existingEventAlert.getAlertScheduledTime()));

		}

		if (existingEventAlert.getExpiryTime() != null) {

			existingEventAlert.setExpiryTimeGMT(Utils.toGMT(existingEventAlert.getExpiryTime(), event.getTimeZone()));

		}

		EventAlert editedEventAlert = eventAlertDao.edit(existingEventAlert);

		List<String> l = new ArrayList<String>();

		l.add(editedEventAlert.getEventId());

		// Event eventTemp = eventService.get(editedEventAlert.getEventId(),
		// editedEventAlert.getOrganizerId());
		//
		// eventTemp.setEventLevelChange(true);
		//
		// try {
		// eventService.edit(event, editedEventAlert.getEventId(),
		// editedEventAlert.getOrganizerId());
		// }
		// catch (NotFoundException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		if (editedEventAlert.getIsSent() != null && editedEventAlert.getIsSent() == true) {

			dataChangeLogService.add(l, "EVENT", Constants.EVENT_ALERT_LOG_KEY, editedEventAlert.getId(), editedEventAlert.getIsDeleted() ? Constants.LOG_ACTION_DELETE : Constants.LOG_ACTION_UPDATE, editedEventAlert.getClass().toString());
		}
		return editedEventAlert;
	}

	public Boolean delete(String id) {

		return eventAlertDao.delete(id);
	}

	public String getEventAdvertDetailUrl(EventAlert eventAlert, HttpServletRequest request) {

		return eventAlertDao.getEventAdvertDetailUrl(eventAlert, request);
	}

	public Page<EventAlert> getAll(Criteria eventAdvertSearch, HttpServletRequest request, Pageable pageAble, String organizerId) {

		return eventAlertDao.getAll(Utils.parseCriteria(eventAdvertSearch, ""), pageAble, organizerId);
	}

	public Page<EventAlert> getEventAlertsByEventIds(List<String> eventIds, Pageable pageAble) throws NotFoundException {

		return eventAlertDao.getEventAlertByEventIds(eventIds, pageAble);
	}

	public Page<EventAlert> getEventAlertsByEventId(String eventId, String organizerId, Pageable pageAble) throws NotFoundException {
		return eventAlertDao.getEventAlertsByEventId(eventId, organizerId, pageAble);
	}

	public List<EventAlert> getListEventAlertsByEventId(String eventId, String organizerId) {
		return eventAlertDao.getListEventAlertsByEventId(eventId, organizerId);
	}

	public List<EventAlert> getPendingAlerts() {

		Query query = new Query();

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("isSent").is(false).and("isDeleted").is(false));

		List<EventAlert> list = eventAlertDao.getAll(query);

		return list;
	}

	public long count(String id, String organizerId) throws NotFoundException {
		// TODO Auto-generated method stub
		return eventAlertDao.count(id, organizerId);
	}

}
