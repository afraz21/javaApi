package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Vendor;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link Vendor} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface VendorService {

	public long count(String eventId, String organizerId);

	/**
	 * Retrieves a single Vendor
	 */
	public Vendor get(String id, String organizerId) throws NotFoundException;

	/**
	 * Adds a new Vendor
	 */
	public Vendor add(Vendor vendor) throws Exception;

	/**
	 * Edits an existing Vendor
	 */
	public Vendor edit(Vendor vendor) throws Exception;

	/**
	 * Deletes an existing Vendor
	 */
	public Boolean delete(String id);

	/**
	 * Gets detail url for the entity object
	 */
	public String getVendorDetailUrl(Vendor vendor, HttpServletRequest request);

	/*
	 * Get all vendors by organizerId
	 */

	public Page<Vendor> getAll(Criteria VendorSearch, HttpServletRequest request, Pageable pageAble, String organizerId);
}
