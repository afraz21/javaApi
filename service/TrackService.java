package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link Track} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface TrackService {

	public List<Track> getOrganizerTracks(String organizerId);

	/**
	 * Retrieves a single Track
	 */
	public Track get(String id, String organizerId) throws NotFoundException;

	/**
	 * Adds a new Track
	 */
	public Track add(Track track) throws Exception;

	/**
	 * Edits an existing Event
	 */
	public Track edit(Track track) throws Exception;

	/**
	 * Deletes an existing Track
	 */
	public Boolean delete(String id);

	/**
	 * Retrieves track(s) search/criteria based
	 */
	public Page<Track> getAll(Criteria search, Pageable pageAble, String organizerId);

	/**
	 * Gets detail url for the entity object
	 */
	public String getTrackDetailUrl(Track track, HttpServletRequest request);

}
