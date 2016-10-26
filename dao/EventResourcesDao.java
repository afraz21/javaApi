package org.iqvis.nvolv3.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.EventResource;
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
public class EventResourcesDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * Retrieves all EventResourcess
	 * 
	 * @param Search
	 *            eventSearch
	 * @param HttpServletRequest
	 *            request
	 * @return List<EventResources>
	 */
	public Page<EventResource> getAll(Query query, Pageable pageAble, String organizerId, String eventId) {

		logger.debug("Retrieving all EventResourcess");

		List<EventResource> events = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		query.addCriteria(Criteria.where("eventId").is(eventId));

		events = mongoTemplate.find(query, EventResource.class);

		long total = mongoTemplate.count(query, EventResource.class);

		Page<EventResource> eventPage = new PageImpl<EventResource>(events, pageAble, total);

		return eventPage;
	}

	public List<EventResource> getEventResources(String organizerId, String eventId) {

		List<EventResource> events = null;

		Query query = new Query();

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		query.addCriteria(Criteria.where("eventId").is(eventId));

		events = mongoTemplate.find(query, EventResource.class);

		return events;

	}

	public long count(String organizerId, String eventId) {

		Query query = new Query();

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		query.addCriteria(Criteria.where("eventId").is(eventId));

		long count = mongoTemplate.count(query, EventResource.class);

		return count;

	}

	/**
	 * Retrieves a single EventResources
	 * 
	 * @param String
	 *            id
	 * @return EventResources
	 */
	public EventResource get(String id, String organizerId) {

		logger.debug("Retrieving an existing EventResources");

		Query query = new Query(Criteria.where("id").is(id).and("organizerId").is(organizerId).and("isDeleted").is(false));

		logger.debug(query.getQueryObject());

		EventResource EventResources = mongoTemplate.findOne(query, EventResource.class, MongoDBCollections.EVENT_RESOURCES.toString());

		return EventResources;
	}

	public EventResource get(String id) {

		logger.debug("Retrieving an existing EventResources");

		Query query = new Query(Criteria.where("id").is(id).and("organizerId").and("isDeleted").is(false));

		logger.debug(query.getQueryObject());

		EventResource EventResources = mongoTemplate.findOne(query, EventResource.class, MongoDBCollections.EVENT_RESOURCES.toString());

		return EventResources;
	}

	/**
	 * Adds a new EventResources
	 * 
	 * @param EventResource
	 *            event
	 * @return EventResources
	 */
	public EventResource add(EventResource event) throws Exception {
		logger.debug("Adding a new EventResources");

		try {

			mongoTemplate.insert(event, MongoDBCollections.EVENT_RESOURCES.toString());

			return event;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new event", e);

			throw e;
		}
	}

	/**
	 * Deletes an existing EventResources
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing EventResources");

		try {

			Query query = new Query(Criteria.where("id").is(id));

			mongoTemplate.remove(query);

			return true;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to delete event", e);
			return false;
		}
	}

	/**
	 * Edits an existing EventResources
	 * 
	 * @param EventResource
	 *            event
	 * @param String
	 *            eventId
	 * @return EventResources
	 */
	public EventResource edit(EventResource event) throws Exception, NotFoundException {

		logger.debug("Editing existing EventResources");

		try {

			mongoTemplate.save(event);

			return event;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing event", e);

			throw e;
		}

	}

	public String getEventResourcesDetailUrl(EventResource event, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.EVENT_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, event.getOrganizerId()) + Urls.GET_EVENT.replace(Urls.SECOND_LEVEL_DOMAIN_ID, event.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

}
