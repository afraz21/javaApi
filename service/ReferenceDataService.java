package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;

/**
 * Service for processing {@link Reference Data} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface ReferenceDataService {

	/**
	 * Retrieves a Reference Data(s) search/criteria based
	 */
	public Page<ReferenceData> getAll(Criteria reference_DataSearch, HttpServletRequest request, Pageable pageAble);

	/**
	 * Retrieves a single Reference Data
	 */
	public ReferenceData get(String type, String organizerId);

	/**
	 * Adds a new Reference Data
	 */
	public ReferenceData add(ReferenceData referenceData) throws Exception;

	/**
	 * Deletes an existing Reference Data
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing Reference Data
	 */
	public ReferenceData edit(ReferenceData referenceData, String reference_DataI_d) throws Exception, NotFoundException;

	/**
	 * Gets detail url of Reference Data
	 */

	public String getReferenceDataDetailUrl(ReferenceData referenceData, HttpServletRequest request);

	public ReferenceData get(String id, String selector, boolean f);

	public ReferenceData get(String type);

}
