package org.iqvis.nvolv3.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.EventAlert;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class EventAlertDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	UserDao userDao;

	@Autowired
	MediaDao mediaDao;

	/**
	 * Retrieves a single Advert
	 * 
	 * @param String
	 *            id
	 * @return EventAdvert
	 */
	public EventAlert get(String id, String organizerId) throws NotFoundException {

		Query query = new Query(Criteria.where("id").is(id).and("organizerId").is(organizerId).and("isDeleted").is(false));

		EventAlert eventAdvert = mongoTemplate.findOne(query, EventAlert.class, MongoDBCollections.EVENT_ALERT.toString());

		return eventAdvert;
	}

	public long count(String id, String organizerId) throws NotFoundException {

		Query query = new Query(Criteria.where("eventId").is(id).and("organizerId").is(organizerId).and("isDeleted").is(false));

		long count = mongoTemplate.count(query, EventAlert.class, MongoDBCollections.EVENT_ALERT.toString());

		return count;
	}

	/**
	 * Adds a new EventAdvert
	 * 
	 * @param EventAlert
	 *            EventAdvert
	 * @return EventAdvert
	 */
	public EventAlert add(EventAlert eventAlert) throws Exception {
		logger.debug("Adding a new Event Advert");

		try {

			mongoTemplate.insert(eventAlert, MongoDBCollections.EVENT_ALERT.toString());

			return eventAlert;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to add event advert", e);
			throw e;
		}
	}

	/**
	 * Edits an existing EventAdvert
	 * 
	 * @param EventAlert
	 *            EventAdvert
	 * @return EventAdvert
	 */
	public EventAlert edit(EventAlert eventAlert) throws Exception {
		logger.debug("Editing existing event advert");

		try {

			mongoTemplate.save(eventAlert);

			return eventAlert;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing vendor", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing EventAdvert
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("EventAdvert");

		try {

			Query query = Query.query(Criteria.where("id").is(id));

			mongoTemplate.remove(query, EventAlert.class);

			return true;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to delete advert", e);

			return false;
		}
	}

	public Page<EventAlert> getAll(Query query, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all event Advert");

		List<EventAlert> eventAdverts = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		logger.debug(query);

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		eventAdverts = mongoTemplate.find(query, EventAlert.class);

		long total = mongoTemplate.count(query, EventAlert.class);

		Page<EventAlert> eventAdvertPage = new PageImpl<EventAlert>(eventAdverts, pageAble, total);

		return eventAdvertPage;
	}

	public List<EventAlert> getAll(Query query) {

		logger.debug("Retrieving all event Advert");

		List<EventAlert> eventAdverts = null;

		logger.debug(query);

		eventAdverts = mongoTemplate.find(query, EventAlert.class);

		return eventAdverts;
	}

	public String getEventAdvertDetailUrl(EventAlert alert, HttpServletRequest request) {

		String detailUrl = "";

		String replaceUrlToken = Urls.VENDOR_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, alert.getOrganizerId()) + Urls.GET_VENDOR.replace(Urls.SECOND_LEVEL_DOMAIN_ID, alert.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	public Page<EventAlert> getEventAlertByEventIds(List<String> eventIds, Pageable pageAble) throws NotFoundException {

		logger.debug("Retrieving an existing event by ids" + eventIds.toString());

		Query query = new Query(Criteria.where("eventId").in(eventIds).and("isDeleted").is(false));

		query.with(new Sort(Sort.Direction.ASC, "startDate"));

		List<EventAlert> eventAdverts = mongoTemplate.find(query, EventAlert.class, MongoDBCollections.EVENT_ALERT.toString());

		long total = mongoTemplate.count(query, EventAlert.class);

		Page<EventAlert> eventAdvertPage = new PageImpl<EventAlert>(eventAdverts, pageAble, total);

		return eventAdvertPage;

	}

	public Page<EventAlert> getEventAlertsByEventId(String eventId, String organizerId, Pageable pageAble) throws NotFoundException {

		logger.debug("Retrieving an existing event by id" + eventId.toString());

		Query query = new Query(Criteria.where("eventId").is(eventId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		query.with(new Sort(Sort.Direction.ASC, "startDate"));

		List<EventAlert> eventAdverts = mongoTemplate.find(query, EventAlert.class, MongoDBCollections.EVENT_ALERT.toString());

		long total = mongoTemplate.count(query, EventAlert.class);

		Page<EventAlert> eventAdvertPage = new PageImpl<EventAlert>(eventAdverts, pageAble, total);

		return eventAdvertPage;

	}

	public List<EventAlert> getListEventAlertsByEventId(String eventId, String organizerId) {

		logger.debug("Retrieving an existing event by id" + eventId.toString());

		Query query = new Query(Criteria.where("eventId").is(eventId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		query.with(new Sort(Sort.Direction.ASC, "startDate"));

		List<EventAlert> eventAdverts = mongoTemplate.find(query, EventAlert.class, MongoDBCollections.EVENT_ALERT.toString());

		return eventAdverts;

	}
}
