package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link EventCampaignParticipant} objects. Uses
 * Spring's {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface EventCampaignParticipantService {

	/**
	 * Retrieves a single eventCampaignParticipant
	 */
	public EventCampaignParticipant get(String id, String campaignId, String organizerId) throws NotFoundException;

	/**
	 * Adds a new eventCampaignParticipant
	 */
	public EventCampaignParticipant add(EventCampaignParticipant eventCampaignParticipant) throws Exception;

	/**
	 * Edits an existing eventCampaignParticipant
	 */
	public EventCampaignParticipant edit(EventCampaignParticipant eventCampaignParticipant) throws Exception;

	/**
	 * Retrieves eventCampaignParticipant(s) search/criteria based
	 */
	public Page<EventCampaignParticipant> getAll(Criteria search, Pageable pageAble, String organizerId, String campaignId);

	/**
	 * Gets detail url for the entity object
	 */
	public String getEventCampaignParticipantDetailUrl(EventCampaignParticipant eventCampaignParticipant, HttpServletRequest request);

	public EventCampaignParticipant getEventCampaignParticipant(String pId);

	public List<EventCampaignParticipant> getAll(String organizerId, String campaignId);

	public EventCampaignParticipant get(String campaignParticipantId, String eventId) throws NotFoundException;

	public List<EventCampaignParticipant> getAllEventCampaignParticipants();

	public List<EventCampaignParticipant> getAllParticipant(String organizerId, String campaignId);

}
