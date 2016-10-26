package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.News;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link Venue} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface NewsService {

	/**
	 * Retrieves a single News
	 */
	public News get(String id, String organizerId) throws NotFoundException;

	/**
	 * Adds a new News
	 */
	public News add(News news) throws Exception;

	/**
	 * Edits an existing News
	 */
	public News edit(News news) throws Exception;

	/**
	 * Gets detail url for the entity object
	 */
	public String getNewsDetailUrl(News news, HttpServletRequest request);

	/*
	 * 
	 */
	public Page<News> getAll(Criteria userCriteria, String langCode, Pageable pageAble, String organizerId);

}
