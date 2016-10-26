package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.EventCampaignDao;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(Constants.SERVICE_EVENT_CAMPAIGN)
@Transactional
public class EventCampaignServiceImpl implements EventCampaignService {

	@Autowired
	private EventCampaignDao eventDao;

	@Autowired
	private EventService eventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	public Page<EventCampaign> getAll(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String organizerId) {

		return eventDao.getAll(Utils.parseCriteria(eventSearch, ""), pageAble, organizerId);
	}

	public EventCampaign get(String id, String organizerId) {

		return eventDao.get(id, organizerId);
	}

	public EventCampaign add(EventCampaign eventCampaign) throws Exception {

		if (eventCampaign.getIsActive() == null) {

			eventCampaign.setIsActive(true);
		}

		if (eventCampaign.getIsDeleted() == null) {

			eventCampaign.setIsDeleted(false);
		}

		if (eventCampaign.getCompleteEvent() == null) {

			eventCampaign.setCompleteEvent(false);
		}

		if (eventCampaign.getSchedule() == null) {

			eventCampaign.setSchedule(false);
		}

		return eventDao.add(eventCampaign);
	}

	public Boolean delete(String id) {

		return eventDao.delete(id);
	}

	public EventCampaign edit(EventCampaign eventCampaign, String campaignId, String organizerId) throws Exception, NotFoundException {

		EventCampaign existingEventCampaign = null;

		if (null != campaignId && !StringUtils.isEmpty(campaignId)) {

			existingEventCampaign = eventDao.get(campaignId, organizerId);
		}

		if (null == existingEventCampaign) {

			throw new NotFoundException(campaignId, "EventCampaign");
		}

		if (null != eventCampaign.getCampaignCode() && !StringUtils.isEmpty(eventCampaign.getCampaignCode())) {
			existingEventCampaign.setCampaignCode(eventCampaign.getCampaignCode());
		}

		if (eventCampaign.getEventId() != null) {

			existingEventCampaign.setEventId(eventCampaign.getEventId());
		}

		if (null != eventCampaign.getMultiLingual() && eventCampaign.getMultiLingual().size() > 0) {
			List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

			if (null != existingEventCampaign.getMultiLingual()) {
				finalLanguages = Utils.updateMultiLingual(existingEventCampaign.getMultiLingual(), eventCampaign.getMultiLingual());
			}
			else {

				finalLanguages = eventCampaign.getMultiLingual();
			}

			existingEventCampaign.setMultiLingual(finalLanguages);
		}

		if (eventCampaign.getIsDeleted() != null && !StringUtils.isEmpty(eventCampaign.getIsDeleted())) {

			existingEventCampaign.setIsDeleted(eventCampaign.getIsDeleted());
		}

		if (eventCampaign.getIsActive() != null && !StringUtils.isEmpty(eventCampaign.getIsActive())) {

			existingEventCampaign.setIsActive(eventCampaign.getIsActive());
		}

		if (eventCampaign.getStartDate() != null && !StringUtils.isEmpty(eventCampaign.getStartDate())) {

			existingEventCampaign.setStartDate(eventCampaign.getStartDate());
		}

		if (eventCampaign.getEndDate() != null && !StringUtils.isEmpty(eventCampaign.getEndDate())) {

			existingEventCampaign.setEndDate(eventCampaign.getEndDate());
		}

		if (eventCampaign.getStartTime() != null && !StringUtils.isEmpty(eventCampaign.getStartTime())) {

			existingEventCampaign.setStartTime(eventCampaign.getStartTime());
		}

		if (eventCampaign.getEndTime() != null && !StringUtils.isEmpty(eventCampaign.getEndTime())) {

			existingEventCampaign.setEndTime(eventCampaign.getEndTime());
		}

		if (eventCampaign.getLimit() != null && !StringUtils.isEmpty(eventCampaign.getLimit())) {

			existingEventCampaign.setLimit(eventCampaign.getLimit());
		}

		if (eventCampaign.getCampaignType() != null && !StringUtils.isEmpty(eventCampaign.getCampaignType())) {

			existingEventCampaign.setCampaignType(eventCampaign.getCampaignType());
		}

		if (eventCampaign.getCompleteEvent() != null && !StringUtils.isEmpty(eventCampaign.getCompleteEvent())) {

			existingEventCampaign.setCompleteEvent(eventCampaign.getCompleteEvent());
		}

		if (eventCampaign.getSchedule() != null && !StringUtils.isEmpty(eventCampaign.getSchedule())) {

			existingEventCampaign.setSchedule(eventCampaign.getSchedule());
		}

		if (eventCampaign.getName() != null && !StringUtils.isEmpty(eventCampaign.getName())) {

			existingEventCampaign.setName(eventCampaign.getName());
		}

		existingEventCampaign.setLastModifiedBy(eventCampaign.getCreatedBy());

		EventCampaign deletedCampaign = eventDao.edit(existingEventCampaign);

		if (existingEventCampaign.getIsDeleted() == true || eventCampaign.getIsActive()==false) {

			if (existingEventCampaign.getParticipants() != null) {

				List<EventCampaignParticipant> list = existingEventCampaign.getParticipants();

				for (EventCampaignParticipant eventCampaignParticipant : list) {
					try {
						List<String> l = new ArrayList<String>();

						l.add(existingEventCampaign.getEventId());

						dataChangeLogService.add(l, "EVENT", Constants.EVENT_CAMPAIGN__LOG_KEY, eventCampaignParticipant.getId(), Constants.LOG_ACTION_DELETE, eventCampaignParticipant.getClass().toString());
					}
					catch (Exception e) {

					}
				}

			}

		}

		// if (eventCampaign != null) {}
		return deletedCampaign;

	}

	public String getEvenCampaignDetailUrl(EventCampaign eventCampaign, HttpServletRequest request) {

		return eventDao.getEventCampaignDetailUrl(eventCampaign, request);
	}

	public List<EventCampaign> getAllEventCampaign(String eventId) {
		// TODO Auto-generated method stub
		return eventDao.getAllEventCampaign(eventId);
	}

	public List<EventCampaign> getAllEventCampaign() {
		// TODO Auto-generated method stub
		return eventDao.getAllEventCampaign();
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

	public long count(String id, String organizerId) {
		// TODO Auto-generated method stub
		return eventDao.count(id, organizerId);
	}
}
