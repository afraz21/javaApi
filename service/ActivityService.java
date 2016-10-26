package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for processing {@link Activity} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Service(Constants.SERVICE_ACTIVITY)
public interface ActivityService {

	// public Page<Activity> getAllCMS(Criteria userCriteria, Pageable pageAble,
	// String organizerId, String eventId);

	/**
	 * Retrieves a Activity(s) search/criteria based
	 */
	public Page<Activity> getAll(Criteria userCriteria, Pageable pageAble, String organizerId, String eventId);

	/**
	 * Retrieves a single Activity
	 */
	public Activity get(String id, String eventId) throws NotFoundException;

	/**
	 * Adds a new Activity
	 */
	public Activity add(Activity activity, Event event) throws Exception;

	/**
	 * Deletes an existing Activity
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing Activity
	 */
	public Activity edit(Activity activity, String eventId) throws Exception, NotFoundException;

	/**
	 * Gets detail url of Activity
	 */

	public String getActivityDetailUrl(Activity activity, HttpServletRequest request);

	/**
	 * 
	 * @param id
	 * @return
	 * @throws NotFoundException
	 */
	public Activity getActivityById(String id) throws NotFoundException;

}
