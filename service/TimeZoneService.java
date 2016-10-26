package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.TimeZone;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link TimeZone} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface TimeZoneService {

	/**
	 * Retrieves a single TimeZone
	 */
	public TimeZone get(String id) throws NotFoundException;

	/**
	 * Adds a new TimeZone
	 */
	public TimeZone add(TimeZone timeZone) throws Exception;

	/**
	 * Edits an existing TimeZone
	 */
	public TimeZone edit(TimeZone timeZone) throws Exception;

	/**
	 * Deletes an existing TimeZone
	 */
	public Boolean delete(String id);

	/**
	 * Gets detail url for the entity object
	 */
	public String getTimeZoneDetailUrl(TimeZone timeZone, HttpServletRequest request);

	/*
	 * Get all TimeZone
	 */

	public Page<TimeZone> getAll(Criteria timeZoneSearch, HttpServletRequest request, Pageable pageAble);
}
