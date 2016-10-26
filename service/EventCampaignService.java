package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link EventCampaign} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface EventCampaignService {

	public long count(String id, String organizerId);

	/**
	 * Retrieves a EventCampaign(s) search/criteria based
	 */
	public Page<EventCampaign> getAll(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String organizerId);

	/**
	 * Retrieves a single EventCampaign
	 */
	public EventCampaign get(String id, String organizerId);

	/**
	 * Adds a new EventCampaign
	 */
	public EventCampaign add(EventCampaign event) throws Exception;

	/**
	 * Deletes an existing EventCampaign
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing EventCampaign
	 */
	public EventCampaign edit(EventCampaign eventCampaign, String eventId, String organizerId) throws Exception, NotFoundException;

	/**
	 * Gets detail url of EventCampaign
	 */

	public String getEvenCampaignDetailUrl(EventCampaign eventCampaign, HttpServletRequest request);

	/**
	 * Get Event All Campaigns
	 */

	public List<EventCampaign> getAllEventCampaign(String eventId);

	/*
	 * 
	 */
	public List<EventCampaign> getAllEventCampaign();

}
