package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.dao.EventResourcesDao;
import org.iqvis.nvolv3.domain.EventResource;
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

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_EVENT_RESOURCE)
@Transactional
public class EventResourceServiceImpl implements EventResourceService {

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Autowired
	private EventResourcesDao eventDao;

	public Page<EventResource> getAll(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String organizerId, String eventId) {

		return eventDao.getAll(Utils.parseCriteria(eventSearch, ""), pageAble, organizerId, eventId);
	}

	public EventResource get(String id, String organizerId) {

		return eventDao.get(id, organizerId);
	}

	public EventResource add(EventResource event) throws Exception {

		if (event.getIsActive() == null) {

			event.setIsActive(true);
		}

		if (event.getIsDeleted() == null) {

			event.setIsDeleted(false);
		}

		event = eventDao.add(event);

		List<String> l = new ArrayList<String>();

		// if (!spon.getIsDeleted()) {
		// l.addAll(eventService.getEventIdsBySelector(spon.getId(),
		// "sponsors.sponsorId"));
		//
		// if (l.size() > 0) {
		// dataChangeLogService.add(l, "EVENT", Constants.EVENT_SPONSOR_LOG_KEY,
		// spon.getId(), Constants.LOG_ACTION_UPDATE, Sponsor.class.toString());
		//
		// }
		// }
		// else {
		// l.addAll(eventService.getEventIdsBySelector(spon.getId(),
		// "sponsors.sponsorId"));
		l.add(event.getEventId());
		if (l.size() > 0) {
			dataChangeLogService.add(l, "EVENT", event.getType().equals("map") ? Constants.EVENT_RESOURCE_MAP_LOG_KEY : Constants.EVENT_OTHER_RESOURCE_MAP_LOG_KEY, event.getId(), Constants.LOG_ACTION_ADD, EventResource.class.toString());

		}
		// }

		return event;
	}

	public Boolean delete(String id) {

		return eventDao.delete(id);
	}

	public EventResource edit(EventResource event, String eventid, String organizerId) throws Exception, NotFoundException {

		EventResource existingEventResource = null;

		if (null != eventid && !StringUtils.isEmpty(eventid)) {

			existingEventResource = eventDao.get(eventid, organizerId);
		}

		if (null == existingEventResource) {

			throw new NotFoundException(eventid, "EventResource");
		}

		if (event.getIsActive() != null) {

			existingEventResource.setIsActive(event.getIsActive());
		}

		if (event.getIsDeleted() != null) {

			existingEventResource.setIsDeleted(event.getIsDeleted());
		}

		if (event.getPicture() != null) {
			existingEventResource.setPicture(event.getPicture());

		}

		if (event.getName() != null) {

			existingEventResource.setName(event.getName());
		}

		if (event.getMultiLingual() != null && event.getMultiLingual().size() > 0) {

			List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>();

			if (null != existingEventResource.getMultiLingual()) {
				finalLanguages = Utils.updateMultiLingual(existingEventResource.getMultiLingual(), event.getMultiLingual());

			}
			else {

				finalLanguages = event.getMultiLingual();
			}

			existingEventResource.setMultiLingual(finalLanguages);

		}

		if (event.getType() != null) {

			existingEventResource.setType(event.getType());
		}

		if (event.getAttachmentURL() != null) {

			existingEventResource.setAttachmentURL(event.getAttachmentURL());
		}

		List<String> l = new ArrayList<String>();

		l.add(event.getEventId());
		if (l.size() > 0) {
			dataChangeLogService.add(l, "EVENT", existingEventResource.getType().equals("map") ? Constants.EVENT_RESOURCE_MAP_LOG_KEY : Constants.EVENT_OTHER_RESOURCE_MAP_LOG_KEY, existingEventResource.getId(), existingEventResource.getIsDeleted() ? Constants.LOG_ACTION_DELETE : Constants.LOG_ACTION_UPDATE, EventResource.class.toString());

		}

		return eventDao.edit(existingEventResource);

	}

	public String getEventResourceDetailUrl(EventResource event, HttpServletRequest request) {

		return eventDao.getEventResourcesDetailUrl(event, request);
	}

	public EventResource get(String id) {

		return eventDao.get(id);
	}

	public List<EventResource> getAllResources(String organizerId, String eventId) {

		return eventDao.getEventResources(organizerId, eventId);

	}

	public long count(String organizerId, String eventId) {
		return eventDao.count(organizerId, eventId);
	}

}
