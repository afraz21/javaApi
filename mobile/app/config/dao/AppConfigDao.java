package org.iqvis.nvolv3.mobile.app.config.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.AppListItem;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

@SuppressWarnings("restriction")
@Repository
public class AppConfigDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * Retrieves all AppConfiguration
	 * 
	 * @param Search
	 *            AppConfiguration
	 * @param HttpServletRequest
	 *            request
	 * @return List<AppConfiguration>
	 */
	public org.springframework.data.domain.Page<AppConfiguration> getAll(Query query, String organizerId, Pageable pageAble) {

		logger.debug("Retrieving all AppConfigurationObjects");

		List<AppConfiguration> refrenceDataList = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		refrenceDataList = mongoTemplate.find(query, AppConfiguration.class);

		long total = mongoTemplate.count(query, AppConfiguration.class);

		org.springframework.data.domain.Page<AppConfiguration> referenceDataPage = new org.springframework.data.domain.PageImpl<AppConfiguration>(refrenceDataList, pageAble, total);

		return referenceDataPage;
	}

	public List<AppConfiguration> get(List<String> keys, List<Object> values) {
		logger.debug("Retrieving all AppConfigurationObjects");

		List<AppConfiguration> refrenceDataList = null;

		Query query = new Query();

		int i = 0;

		for (String key : keys) {

			query.addCriteria(Criteria.where(key).is(values.get(i)));

			i++;
		}

		refrenceDataList = mongoTemplate.find(query, AppConfiguration.class);

		return refrenceDataList;
	}

	public List<AppConfiguration> get(List<String> ids) {
		logger.debug("Retrieving all AppConfigurationObjects");

		List<AppConfiguration> refrenceDataList = null;

		Query query = new Query();

		query.addCriteria(Criteria.where("isDeleted").is(false).and("id").in(ids));

		refrenceDataList = mongoTemplate.find(query, AppConfiguration.class);

		return refrenceDataList;
	}

	public org.springframework.data.domain.Page<AppListItem> get(List<String> keys, List<Object> values, Criteria search, List<Order> orderByList, Pageable pageAble, String orgnaizerId) {

		logger.debug("Retrieving all AppConfigurationObjects");

		List<AppConfiguration> refrenceDataList = null;
		Query query = new Query();

		if (search != null) {

			query.addCriteria(search);
		}

		if (null != pageAble) {

			query.with(pageAble);
		}

		if (orderByList != null && orderByList.size() > 0) {

			query.with(new Sort(orderByList));
		}

		int i = 0;

		for (String key : keys) {

			query.addCriteria(Criteria.where(key).is(values.get(i)));

			i++;
		}

		refrenceDataList = mongoTemplate.find(query, AppConfiguration.class);

		if (refrenceDataList == null)
			refrenceDataList = new ArrayList<AppConfiguration>();

		long total = mongoTemplate.count(query, AppConfiguration.class);

		org.springframework.data.domain.Page<AppListItem> referenceDataPage = new org.springframework.data.domain.PageImpl<AppListItem>(orgnaizerId == null ? AppListItem.toList(refrenceDataList) : AppListItem.toListPartner(refrenceDataList, orgnaizerId), pageAble, total);

		return referenceDataPage;

	}

	/**
	 * Retrieves a single AppConfiguration
	 * 
	 * @param String
	 *            id
	 * @return AppConfiguration
	 */
	public AppConfiguration get(String appId, String organizerId) {

		// logger.debug("Retrieving an existing AppConfigurationObject");

		Query query = new Query(Criteria.where("id").is(appId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		// logger.debug(query.getQueryObject());

		AppConfiguration appObject = mongoTemplate.findOne(query, AppConfiguration.class, MongoDBCollections.APP_CONFIGURATION.toString());

		return appObject;
	}

	public AppConfiguration getAppObject(String appId) {

		logger.debug("Retrieving an existing AppConfigurationObject");

		Query query = new Query(Criteria.where("id").is(appId).and("isDeleted").is(false));

		logger.debug(query.getQueryObject());

		AppConfiguration appObject = mongoTemplate.findOne(query, AppConfiguration.class, MongoDBCollections.APP_CONFIGURATION.toString());

		return appObject;
	}

	/**
	 * Adds a new AppConfiguration
	 * 
	 * @param AppConfigurationOld
	 *            AppConfiguration
	 * @return AppConfiguration
	 */
	public AppConfiguration add(AppConfiguration appConfiguration) throws Exception {
		logger.debug("Adding a new AppConfiguration");

		try {

			mongoTemplate.insert(appConfiguration, MongoDBCollections.APP_CONFIGURATION.toString());

			return appConfiguration;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new AppConfiguration", e);

			throw e;
		}
	}

	/**
	 * Deletes an existing AppConfiguration
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing AppConfiguration");

		try {

			Query query = new Query(Criteria.where("id").is(id));

			mongoTemplate.remove(query);

			return true;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to delete AppConfiguration", e);
			return false;
		}
	}

	/**
	 * Edits an existing AppConfiguration
	 * 
	 * @param AppConfigurationOld
	 *            AppConfiguration
	 * @param String
	 *            AppConfiguration
	 * @return AppConfiguration
	 */
	public AppConfiguration edit(AppConfiguration appConfiguration) throws Exception, NotFoundException {

		logger.debug("Editing existing AppConfiguration");

		try {

			logger.debug("Skipplable : ----------- " + appConfiguration.getProfile_screen_skippable());

			mongoTemplate.save(appConfiguration);

			return appConfiguration;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing AppConfiguration", e);

			throw e;
		}

	}

	public String getAppConfigurationDetailUrl(AppConfiguration referenceData, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.REFERENCE_DATA_BASE_URL;

		replaceUrlToken += Urls.REFERENCE_DATA_GET.replace("{id}", referenceData.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	@SuppressWarnings("unchecked")
	public Object addETC(String appConfigurationId, String organizerId, Object appConfiguration) {
		try {

			final String collection = "etc.";

			Query query = Query.query(Criteria.where("organizerId").is(organizerId).and("id").is(appConfigurationId));

			Gson gson = new Gson();

			Map<String, Object> map = new HashMap<String, Object>();

			map = (Map<String, Object>) gson.fromJson(gson.toJson(appConfiguration), map.getClass());

			final Update update = new Update();

			Set<String> keys = map.keySet();

			for (String key : keys) {

				Object value = map.get(key);

				if (value != null) {

					if (String.class.toString().equals(value.getClass().toString()) && ((String) value).equals("")) {

						value = null;

					}

				}

				if (value == null) {

					update.unset(collection + key);
				}
				else {

					update.set(collection + key, value);
				}
			}

			mongoTemplate.updateFirst(query, update, AppConfiguration.class);

			AppConfiguration appConfigurationObject = mongoTemplate.findOne(query, AppConfiguration.class);

			return appConfigurationObject.getEtc();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
