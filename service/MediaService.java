package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link Media} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface MediaService {

	/**
	 * Retrieves a single Media
	 */
	public Media get(String id);

	/**
	 * Adds a new Media
	 */
	public Media add(Media media) throws Exception;

	/**
	 * Edits an existing Media
	 */
	public Media edit(Media media, String mediaId) throws Exception, NotFoundException;

	/**
	 * Gets detail url of media
	 */

	public String getMediaDetailUrl(Media media, HttpServletRequest request);

}
