package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.EventCampaignPariticipantDao;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
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

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_EVENT_CAMPAIGN_PARTICIPANT)
@Transactional
public class EventCampaignParticiapntServiceImpl implements EventCampaignParticipantService {

	@Autowired
	EventCampaignPariticipantDao eventCampaignParticipantDao;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Autowired
	private EventCampaignService eventCampaignService;

	@Autowired
	private EventService eventService;

	public EventCampaignParticipant get(String campaignParticipantId, String campaignId, String organizerId) throws NotFoundException {

		return eventCampaignParticipantDao.get(campaignParticipantId, campaignId, organizerId);
	}

	public EventCampaignParticipant get(String campaignParticipantId, String eventId) throws NotFoundException {

		return eventCampaignParticipantDao.get(campaignParticipantId, eventId);
	}

	public EventCampaignParticipant add(EventCampaignParticipant eventCampaignParticipant) throws Exception {

		eventCampaignParticipant.setCreatedDate(new DateTime());

		if (eventCampaignParticipant.getCompleteCampaign() == null) {
			eventCampaignParticipant.setCompleteCampaign(false);
		}

		if (eventCampaignParticipant.getIsDeleted() == null) {
			eventCampaignParticipant.setIsDeleted(false);
		}

		if (eventCampaignParticipant.getSchedule() == null) {
			eventCampaignParticipant.setSchedule(false);
		}

		if (eventCampaignParticipant.getIsActive() == null) {
			eventCampaignParticipant.setIsActive(true);
		}

		EventCampaign ec = eventCampaignService.get(eventCampaignParticipant.getCampaignId(), eventCampaignParticipant.getOrganizerId());

		Event event = eventService.get(ec.getEventId());

		if (event != null) {

			if (eventCampaignParticipant.getSchedule()) {

				String str = eventCampaignParticipant.getStartTime();

				DateTime psd = eventCampaignParticipant.getStartDate();

				System.out.println("PSD : " + psd);

				DateTime ped = eventCampaignParticipant.getEndDate();

				System.out.println("PED : " + ped);

				DateTime psdGMT = psd;

				System.out.println("psdGMT : " + psdGMT);

				DateTime pedGMT = ped;

				System.out.println("pedGMT : " + pedGMT);

				String part_start = eventCampaignParticipant.getStartTime();

				String part_end = eventCampaignParticipant.getEndTime();

				String s_p_start = pmToTime24(part_start);

				System.out.println("s_p_start : " + s_p_start);

				String s_p_end = pmToTime24(part_end);

				System.out.println("s_p_end : " + s_p_end);

				if (s_p_start != null && !s_p_start.equals("")) {

					String time[] = s_p_start.split(":");

					int h = Integer.parseInt(time[0]);

					int m = Integer.parseInt(time[1]);

					psdGMT = psdGMT.plusHours(h);

					psdGMT = psdGMT.plusMinutes(m);
				}

				if (s_p_end != null && !s_p_end.equals("")) {

					String time[] = s_p_end.split(":");

					int h = Integer.parseInt(time[0]);

					int m = Integer.parseInt(time[1]);

					pedGMT = pedGMT.plusHours(h);

					pedGMT = pedGMT.plusMinutes(m);
				}

				if (event.getTimeZone() != null && !event.getTimeZone().equals("")) {

					String time_diff = event.getTimeZone();

					System.out.println("Event Time Zone Difference : " + time_diff);

					String str_time_diff[] = time_diff.split(":");

					int len = str_time_diff[0].length();

					System.out.println("Length : " + len);

					Date participantStartTime = Utils.convertStringToTime(str);

					Calendar calPST = Calendar.getInstance();

					calPST.setTime(eventCampaignParticipant.getStartDate().toDate());

					int monthS = calPST.get(Calendar.MONTH);

					int dateS = calPST.get(Calendar.DATE);

					int yearS = calPST.get(Calendar.YEAR);

					participantStartTime.setMonth(monthS);

					participantStartTime.setDate(dateS);

					participantStartTime.setYear(yearS);

					if (len > 2) {

						char plus_minus = str_time_diff[0].charAt(0);

						int h = Integer.parseInt(str_time_diff[0].substring(1, 3));

						int m = Integer.parseInt(str_time_diff[1]);

						System.out.println("Sign : " + plus_minus + " | Hour : " + h + " | Min : " + m);

						if (plus_minus == '+') {

							psdGMT = psdGMT.minusMinutes(m);

							psdGMT = psdGMT.minusHours(h);

							pedGMT = pedGMT.minusMinutes(m);

							pedGMT = pedGMT.minusHours(h);

						}
						else if (plus_minus == '-') {

							psdGMT = psdGMT.plusMinutes(m);

							psdGMT = psdGMT.plusHours(h);

							pedGMT = pedGMT.plusMinutes(m);

							pedGMT = pedGMT.plusHours(h);

						}

					}
				}

				eventCampaignParticipant.setStartDateToGMT(psdGMT);

				System.out.println("SDGMT : " + psdGMT);

				System.out.println("ECP SDGMT : " + eventCampaignParticipant.getStartDateToGMT());

				eventCampaignParticipant.setEndDateToGMT(pedGMT);

				System.out.println("EDGMT : " + pedGMT);

				System.out.println("ECP EDGMT : " + eventCampaignParticipant.getEndDateToGMT());

				DateTime pstGMT = eventCampaignParticipant.getStartDateToGMT();

				DateTime petGMT = eventCampaignParticipant.getEndDateToGMT();

				eventCampaignParticipant.setStartTimeToGMT(pstGMT);

				System.out.println("ECP STGMT : " + eventCampaignParticipant.getStartTimeToGMT());

				eventCampaignParticipant.setEndTimeToGMT(petGMT);

				System.out.println("ECP ETGMT : " + eventCampaignParticipant.getEndTimeToGMT());
			}
		}

		EventCampaignParticipant parts = eventCampaignParticipantDao.add(eventCampaignParticipant);

		System.out.println("New Added Event Campaign Participant After Adding : " + eventCampaignParticipant.getId());

		EventCampaign eventCampaign = eventCampaignService.get(eventCampaignParticipant.getCampaignId(), eventCampaignParticipant.getOrganizerId());

		return parts;
	}

	public EventCampaignParticipant edit(EventCampaignParticipant eventCampaignParticipant) throws Exception {

		EventCampaignParticipant existingEventCampaignParticipant = eventCampaignParticipantDao.get(eventCampaignParticipant.getId(), eventCampaignParticipant.getCampaignId(), eventCampaignParticipant.getOrganizerId());

		if (null == existingEventCampaignParticipant) {

			throw new NotFoundException(eventCampaignParticipant.getId(), "EventCampaignParticipant");

		}
		else {

			if (eventCampaignParticipant.getIsDeleted() != null && !StringUtils.isEmpty(eventCampaignParticipant.getIsDeleted().toString())) {

				existingEventCampaignParticipant.setIsDeleted(eventCampaignParticipant.getIsDeleted());
			}

			existingEventCampaignParticipant.setLastModifiedBy(eventCampaignParticipant.getCreatedBy());

			existingEventCampaignParticipant.setLastModifiedDate(new DateTime());

			if (existingEventCampaignParticipant.getVersion() != null) {

				existingEventCampaignParticipant.setVersion(existingEventCampaignParticipant.getVersion() + 1);
			}

			// if (eventCampaignParticipant.getPicture() != null) {
			//
			// existingEventCampaignParticipant.setPicture(eventCampaignParticipant.getPicture());
			// }

			if (eventCampaignParticipant.getPictureO() != null) {

				existingEventCampaignParticipant.setPicture(eventCampaignParticipant.getPictureO());
			}

			if (eventCampaignParticipant.getOrganizerId() != null) {

				existingEventCampaignParticipant.setOrganizerId(eventCampaignParticipant.getOrganizerId());
			}

			if (eventCampaignParticipant.getStartDate() != null && !StringUtils.isEmpty(eventCampaignParticipant.getStartDate())) {

				existingEventCampaignParticipant.setStartDate(eventCampaignParticipant.getStartDate());
			}

			if (eventCampaignParticipant.getEndDate() != null && !StringUtils.isEmpty(eventCampaignParticipant.getEndDate())) {

				existingEventCampaignParticipant.setEndDate(eventCampaignParticipant.getEndDate());
			}

			if (eventCampaignParticipant.getStartTime() != null && !StringUtils.isEmpty(eventCampaignParticipant.getStartTime())) {

				existingEventCampaignParticipant.setStartTime(eventCampaignParticipant.getStartTime());
			}

			if (eventCampaignParticipant.getEndTime() != null && !StringUtils.isEmpty(eventCampaignParticipant.getEndTime())) {

				existingEventCampaignParticipant.setEndTime(eventCampaignParticipant.getEndTime());
			}

			if (eventCampaignParticipant.getLimit() != null && !StringUtils.isEmpty(eventCampaignParticipant.getLimit())) {

				existingEventCampaignParticipant.setLimit(eventCampaignParticipant.getLimit());
			}

			if (eventCampaignParticipant.getSponsors() != null && !StringUtils.isEmpty(eventCampaignParticipant.getSponsors())) {

				existingEventCampaignParticipant.setSponsors(eventCampaignParticipant.getSponsors());
			}

			if (eventCampaignParticipant.getName() != null && !StringUtils.isEmpty(eventCampaignParticipant.getName())) {

				existingEventCampaignParticipant.setName(eventCampaignParticipant.getName());
			}

			if (eventCampaignParticipant.getIsActive() != null) {

				existingEventCampaignParticipant.setIsActive(eventCampaignParticipant.getIsActive());

			}

			if (null != eventCampaignParticipant.getMultiLingual() && eventCampaignParticipant.getMultiLingual().size() > 0) {

				List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

				if (null != existingEventCampaignParticipant.getMultiLingual()) {
					finalLanguages = Utils.updateMultiLingual(existingEventCampaignParticipant.getMultiLingual(), eventCampaignParticipant.getMultiLingual());

				}
				else {

					finalLanguages = existingEventCampaignParticipant.getMultiLingual();
				}

				existingEventCampaignParticipant.setMultiLingual(finalLanguages);

			}

			// if (eventCampaignParticipant.getOverlayLandscape() != null) {
			//
			// existingEventCampaignParticipant.setOverlayLandscape(eventCampaignParticipant.getOverlayLandscape());
			// }
			//
			// if (eventCampaignParticipant.getOverlayPotrait() != null) {
			//
			// existingEventCampaignParticipant.setOverlayPotrait(eventCampaignParticipant.getOverlayPotrait());
			// }

			if (eventCampaignParticipant.getOverlayLandscapeO() != null) {

				existingEventCampaignParticipant.setOverlayLandscape(eventCampaignParticipant.getOverlayLandscapeO());
			}

			if (eventCampaignParticipant.getOverlayPotraitO() != null) {

				existingEventCampaignParticipant.setOverlayPotrait(eventCampaignParticipant.getOverlayPotraitO());
			}

			if (eventCampaignParticipant.getSortOrder() != null) {

				existingEventCampaignParticipant.setSortOrder(eventCampaignParticipant.getSortOrder());

			}

			if (eventCampaignParticipant.getCompleteCampaign() != null) {

				existingEventCampaignParticipant.setCompleteCampaign(eventCampaignParticipant.getCompleteCampaign());
			}

			if (eventCampaignParticipant.getSchedule() != null) {

				existingEventCampaignParticipant.setSchedule(eventCampaignParticipant.getSchedule());
			}

		}

		EventCampaignParticipant eventCampaignPart = eventCampaignParticipantDao.edit(existingEventCampaignParticipant);
		
		// EventCampaign eventCampaign =
		// eventCampaignService.get(eventCampaignParticipant.getCampaignId(),
		// eventCampaignParticipant.getOrganizerId());

		return eventCampaignPart;
	}

	public String getEventCampaignParticipantDetailUrl(EventCampaignParticipant eventCampaignParticipant, HttpServletRequest request) {

		return eventCampaignParticipantDao.getEventCampaignParticipantDetailUrl(eventCampaignParticipant, request);
	}

	public Page<EventCampaignParticipant> getAll(Criteria search, Pageable pageAble, String organizerId, String campaignId) {

		return eventCampaignParticipantDao.getAll(Utils.parseQuery(search, "participants."), search, pageAble, organizerId, campaignId);
	}

	public EventCampaignParticipant getEventCampaignParticipant(String pId) {

		return eventCampaignParticipantDao.getEventCampaignParticipant(pId);
	}

	public List<EventCampaignParticipant> getAll(String organizerId, String campaignId) {

		return eventCampaignParticipantDao.getAll(organizerId, campaignId);
	}

	public List<EventCampaignParticipant> getAllEventCampaignParticipants() {

		// Query query = new Query();
		//
		// query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("isActive").is(true).and("isDeleted").is(false));

		List<EventCampaignParticipant> list = eventCampaignParticipantDao.getAll();

		return list;

	}

	public List<EventCampaignParticipant> getAllParticipant(String organizerId, String campaignId) {

		return eventCampaignParticipantDao.getAllParticipants(organizerId, campaignId);
	}

	public String pmToTime24(String pmTime) {

		String pm[] = pmTime.split(" ");

		String time_str = "";

		if (pm[1].equalsIgnoreCase("pm")) {

			String time[] = pm[0].split(":");

			int h = Integer.parseInt(time[0]);

			int m = Integer.parseInt(time[1]);

			int hour = 12 + h;

			time_str = hour + ":" + m;
		}
		else {

			time_str = pm[0];
		}

		return time_str;
	}
}
