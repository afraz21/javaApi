package org.iqvis.nvolv3.mobile.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Like;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for processing {@link Like} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Service(Constants.SERVICE_LIKE)
public interface LikeService {

	/**
	 * Retrieves a Like(s) search/criteria based
	 */
	public Page<Like> getAll(Criteria userCriteria, Pageable pageAble, String organizerId);

	/**
	 * Retrieves a single Like
	 */
	public Like get(String id) throws NotFoundException;

	/**
	 * Adds a new Like
	 */
	public Like add(Like activity, String feedId) throws Exception;

	/**
	 * Deletes an existing Like
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing Like
	 */
	public Like edit(Like activity) throws Exception, NotFoundException;

	/**
	 * Gets detail url of Like
	 */

	public String getLikeDetailUrl(Like activity, HttpServletRequest request);

}
