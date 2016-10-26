package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.joda.time.DateTime;

/**
 * Service for processing {@link Event} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface FeedService {

	public List<Feed> getAll(String eventId);
	
	public List<Feed> getOlderByDate(String eventId, DateTime date);

	public Page<Feed> getAllCMS(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String type, String typeId);

	/**
	 * Retrieves a event(s) search/criteria based
	 */
	public Page<Feed> getAll(Criteria feedSearch, HttpServletRequest request, Pageable pageAble, String type, String typeId, String eventId);

	/**
	 * Retrieves a single Feed
	 */
	public Feed get(String id);

	/**
	 * Adds a new Feed
	 */
	public Feed add(Feed feed) throws Exception;

	/**
	 * Edits an existing Feed
	 */
	public Feed edit(Feed event, String feedid) throws Exception, NotFoundException;

	/**
	 * Gets detail url of Feed
	 */

	public String getFeedDetailUrl(Feed feed, HttpServletRequest request);

	public Feed getLatestTwitterFeed();
	
	public boolean isExits(long socialMediaId, String eventId);
}
