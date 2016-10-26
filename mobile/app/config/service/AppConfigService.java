package org.iqvis.nvolv3.mobile.app.config.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.AppListItem;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link AppConfiguration} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface AppConfigService {

	/**
	 * Retrieves a AppConfiguration Data(s) search/criteria based
	 */
	public Page<AppConfiguration> getAll(Criteria reference_DataSearch, String organizerId, HttpServletRequest request, Pageable pageAble);

	/**
	 * Retrieves a single AppConfiguration
	 */
	public AppConfiguration get(String appId, String organizerId);

	/**
	 * Adds a new AppConfiguration
	 */
	public AppConfiguration add(AppConfiguration appConfiguration) throws Exception;

	/**
	 * Deletes an existing AppConfiguration
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing AppConfiguration
	 */
	public AppConfiguration edit(AppConfiguration appConfiguration, String appId) throws Exception, NotFoundException;

	/**
	 * Gets detail url of AppConfiguration
	 */

	public String getAppConfigurationDetailUrl(AppConfiguration appConfiguration, HttpServletRequest request);

	public AppConfiguration getAppObject(String appId);

	public Object addETC(String appConfigurationId, String organizerId, Object appConfiguration);

	/**
	 * 
	 * @param keys
	 *            list of keys in mongodb collection
	 * 
	 * @param values
	 *            list of values you want to filter data in mongodb collection
	 * 
	 * @throws NullPointerException
	 *             it can return NullPointerException
	 * 
	 * */
	public List<AppConfiguration> get(List<String> keys, List<Object> values);

	/**
	 * @param eventId
	 *            put eventId here from which you want to get application
	 *            configurations id
	 *
	 * @param organizerId
	 *            organizerId will be id of user form which this eventId is
	 *            associated
	 * 
	 * @return List<String> getIdsByEvent will return list of appConfiguration
	 *         ids
	 * 
	 * */
	public List<String> getIdsByEvent(String eventId, String organizerId);

	public org.springframework.data.domain.Page<AppListItem> getWithSearch(List<String> keys, List<Object> values, Criteria search, Pageable pageAble, String organizerId);

	public List<AppConfiguration> get(List<String> ids);

}
