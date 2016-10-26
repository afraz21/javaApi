package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.push.PushData;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link EventAlert} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface EventAlertService {

	public long count(String id, String organizerId) throws NotFoundException;

	/**
	 * Retrieves a single EventAlert
	 */
	public EventAlert get(String id, String organizerId) throws NotFoundException;

	/**
	 * Adds a new EventAlert
	 */
	public EventAlert add(EventAlert eventAlert) throws Exception;

	/**
	 * Edits an existing EventAlert
	 */
	public EventAlert edit(EventAlert eventAlert) throws Exception;

	/**
	 * Deletes an existing EventAlert
	 */
	public Boolean delete(String id);

	/**
	 * Gets detail url for the entity object
	 */
	public String getEventAdvertDetailUrl(EventAlert eventAlert, HttpServletRequest request);

	/*
	 * 
	 */
	public Page<EventAlert> getAll(Criteria eventAlertSearch, HttpServletRequest request, Pageable pageAble, String organizerId);

	/*
	 * 
	 * 
	 */

	public Page<EventAlert> getEventAlertsByEventIds(List<String> eventIds, Pageable pageAble) throws NotFoundException;

	public Page<EventAlert> getEventAlertsByEventId(String eventId, String organizerId, Pageable pageAble) throws NotFoundException;

	public List<EventAlert> getListEventAlertsByEventId(String eventId, String organizerId);

	public List<EventAlert> getPendingAlerts();

	public PushData getAlertPushDate(EventAlert eventAdvert);
}
