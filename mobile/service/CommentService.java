package org.iqvis.nvolv3.mobile.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Comment;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for processing {@link Comment} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Service(Constants.SERVICE_COMMENT)
public interface CommentService {

	/**
	 * Retrieves a Comment(s) search/criteria based
	 */
	public Page<Comment> getAll(Criteria userCriteria, Pageable pageAble, String organizerId);

	/**
	 * Retrieves a single Comment
	 */
	public Comment get(String id) throws NotFoundException;

	/**
	 * Adds a new Comment
	 */
	public Comment add(Comment activity, String feedId) throws Exception;

	/**
	 * Deletes an existing Comment
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing Comment
	 */
	public Comment edit(Comment activity) throws Exception, NotFoundException;

	/**
	 * Gets detail url of Comment
	 */

	public String getCommentDetailUrl(Comment activity, HttpServletRequest request);

}
