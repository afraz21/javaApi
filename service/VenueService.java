package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link Venue} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface VenueService {

	/**
	 * Retrieves a single Venue
	 */
	public Venue get(String id, String organizerId) throws NotFoundException;

	/**
	 * Adds a new Venue
	 */
	public Venue add(Venue venue) throws Exception;

	/**
	 * Edits an existing Event
	 */
	public Venue edit(Venue venue) throws Exception;

	/**
	 * Deletes an existing Venue
	 */
	public Boolean delete(String id);

	/**
	 * Gets detail url for the entity object
	 */
	public String getVenueDetailUrl(Venue venue, HttpServletRequest request);

	/*
	 * 
	 */
	public Page<Venue> getAll(Criteria userCriteria, Pageable pageAble, String organizerId);

	public void onVenueDelete(String venueId);

}
