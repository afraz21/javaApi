package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Service for processing {@link Sponsor} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface SponsorService {

	/**
	 * Retrieves a single Sponsor
	 */
	public Sponsor get(String id);

	/**
	 * Adds a new Sponsor
	 */
	public Sponsor add(Sponsor sponser) throws Exception;

	/**
	 * Edits an existing Event
	 */
	public Sponsor edit(Sponsor sponser) throws Exception;

	/**
	 * Deletes an existing Sponsor
	 */
	public Boolean delete(String id);

	/**
	 * Gets detail url for the entity object
	 */
	public String getSponserDetailUrl(Sponsor sponser, HttpServletRequest request);

	/*
	 * Get all sponsers by organizerId
	 */

	public Page<Sponsor> getAll(Criteria sponsorSearch, HttpServletRequest request, Pageable pageAble, String organizerId);

	public List<Sponsor> getAll(Query query, String organizerId);

}
