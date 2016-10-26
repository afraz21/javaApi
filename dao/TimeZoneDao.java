package org.iqvis.nvolv3.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.TimeZone;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class TimeZoneDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * Retrieves a TimeZone
	 * 
	 * @param String
	 *            id
	 * @return TimeZone
	 */
	public TimeZone get(String id) throws NotFoundException {

		Query query = new Query(Criteria.where("id").is(id).and("isDeleted").is(false));

		TimeZone timeZone = mongoTemplate.findOne(query, TimeZone.class, MongoDBCollections.TIME_ZONE.toString());

		return timeZone;
	}

	/**
	 * Adds a new TimeZone
	 * 
	 * @param TimeZone
	 *            TimeZone
	 * @return TimeZone
	 */
	public TimeZone add(TimeZone timeZone) throws Exception {
		logger.debug("Adding a new TimeZone");

		try {

			mongoTemplate.insert(timeZone, MongoDBCollections.TIME_ZONE.toString());

			return timeZone;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to add new TimeZone", e);
			throw e;
		}
	}

	/**
	 * Edits an existing Vendor
	 * 
	 * @param Vendor
	 *            vendor
	 * @return Vendor
	 */
	public TimeZone edit(TimeZone timeZone) throws Exception {
		logger.debug("Editing existing TimeZone");

		try {

			mongoTemplate.save(timeZone);

			return timeZone;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing TimeZone", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing TimeZone
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing Vendor");

		try {

			Query query = Query.query(Criteria.where("id").is(id));

			mongoTemplate.remove(query, TimeZone.class);

			return true;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to delete TimeZone", e);

			return false;
		}
	}

	public Page<TimeZone> getAll(Query query, Pageable pageAble) {

		logger.debug("Retrieving all TimeZone");

		List<TimeZone> timeZones = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		timeZones = mongoTemplate.find(query, TimeZone.class);

		long total = mongoTemplate.count(query, TimeZone.class);

		Page<TimeZone> timeZonePage = new PageImpl<TimeZone>(timeZones, pageAble, total);

		return timeZonePage;
	}

	public String getTimeZoneDetailUrl(TimeZone timeZone, HttpServletRequest request) {

		String detailUrl = "";

		String replaceUrlToken = Urls.TIME_ZONE_BASE_URL + Urls.UPDATE_TIME_ZONE;

		replaceUrlToken = replaceUrlToken.replace(Urls.UPDATE_TIME_ZONE, timeZone.getId());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}
}
