package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.EventResource;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link EventResource} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface EventResourceService {

	public long count(String organizerId, String eventId);

	/**
	 * Retrieves a event(s) search/criteria based
	 */
	public Page<EventResource> getAll(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String organizerId, String eventId);

	/**
	 * Retrieves a single EventResource
	 */
	public EventResource get(String id, String organizerId);

	/**
	 * Adds a new EventResource
	 */
	public EventResource add(EventResource event) throws Exception;

	/**
	 * Deletes an existing EventResource
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing EventResource
	 */
	public EventResource edit(EventResource event, String eventId, String organizerId) throws Exception, NotFoundException;

	/**
	 * Gets detail url of event
	 */

	public String getEventResourceDetailUrl(EventResource event, HttpServletRequest request);

	public EventResource get(String id);

	/**
	 * Gets All EventResources From EventResource Collection
	 * */

	public List<EventResource> getAllResources(String organizerId, String eventId);

}
