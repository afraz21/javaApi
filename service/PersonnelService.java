package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link Personnel} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface PersonnelService {

	public List<Personnel> getOrganizerPersonnelsList(String organizerId) throws NotFoundException;

	/**
	 * Retrieves a single Personnel
	 */
	public Personnel get(String id, String organizerId) throws NotFoundException;

	/**
	 * Adds a new Personnel
	 */
	public Personnel add(Personnel personnel) throws Exception;

	/**
	 * Deletes an existing Personnel
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing Personnel
	 */
	public Personnel edit(Personnel personnel, String personnelId) throws Exception, NotFoundException;

	/**
	 * Gets all Personals by Organizer
	 */
	public Page<Personnel> getAll(Criteria userCriteria, Pageable pageAble, String organizerId);

	/**
	 * Gets detail url of Personnel
	 */

	public String getPersonnelDetailUrl(Personnel personnel, HttpServletRequest request);

	public List<Personnel> getPersonnelsByIds(List<String> ids, String organizerId) throws NotFoundException;
}
