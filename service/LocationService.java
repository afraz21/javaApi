package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link Location} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface LocationService {

	/**
	 * Retrieves a single Location
	 */
	public Location get(String id, String organizerId) throws NotFoundException;

	/**
	 * Adds a new Location
	 */
	public Location add(Location Location) throws Exception;

	/**
	 * Edits an existing Event
	 */
	public Location edit(Location Location, String organizerId) throws Exception;

	/**
	 * Deletes an existing Location
	 */
	public Boolean delete(String id);

	/**
	 * Gets detail url for the entity object
	 */
	public String getLocationDetailUrl(Location location, String organizerId, HttpServletRequest request);

	/**
	 * Get all locations
	 */
	public Page<Location> getAll(Criteria search, Pageable pageAble, String organizerId);

}
