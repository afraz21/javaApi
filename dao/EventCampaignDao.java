package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class EventCampaignDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	private EventService eventService;

	/**
	 * Retrieves all EventsCompaign
	 * 
	 * @param Search
	 *            eventSearch
	 * @param HttpServletRequest
	 *            request
	 * @return List<Event>
	 */
	public Page<EventCampaign> getAll(Query query, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all Event Campaign");

		List<EventCampaign> eventCampaigns = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		eventCampaigns = mongoTemplate.find(query, EventCampaign.class);

		long total = mongoTemplate.count(query, EventCampaign.class);

		Page<EventCampaign> eventPage = new PageImpl<EventCampaign>(eventCampaigns, pageAble, total);

		return eventPage;
	}

	public List<EventCampaign> getAll(String organizerId, String eventId, String type) {

		logger.debug("Retrieving all Event Campaign");

		List<EventCampaign> eventCampaigns = null;

		Query query = new Query();

		query.addCriteria(Criteria.where("eventId").is(eventId));

		query.addCriteria(Criteria.where("campaignType").is(type));

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		eventCampaigns = mongoTemplate.find(query, EventCampaign.class);

		return eventCampaigns;
	}

	/**
	 * Retrieves a single EventCompaign
	 * 
	 * @param String
	 *            id
	 * @return Event
	 */
	public EventCampaign get(String id, String organizerId) {

		logger.debug("Retrieving an existing Event Campaign");

		Query query = new Query(Criteria.where("id").is(id).and("organizerId").is(organizerId).and("isDeleted").is(false));

		logger.debug(query.getQueryObject());

		EventCampaign eventCampaign = mongoTemplate.findOne(query, EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());

		return eventCampaign;
	}
	
	public EventCampaign get(String id) {

		logger.debug("Retrieving an existing Event Campaign");

		Query query = new Query(Criteria.where("id").is(id).and("isDeleted").is(false));

		logger.debug(query.getQueryObject());

		EventCampaign eventCampaign = mongoTemplate.findOne(query, EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());

		return eventCampaign;
	}
	
	public List<EventCampaign> get(String selector, Object value) {

		logger.debug("Retrieving an existing Event Campaign");

		Query query = new Query(new Criteria().and("isDeleted").is(false).and("isActive").is(true).orOperator(Criteria.where(selector).is(value),Criteria.where(selector).exists((Boolean)value)));
		
		logger.debug(query.getQueryObject());

		List<EventCampaign> eventCampaign = mongoTemplate.find(query, EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());

		return eventCampaign;
	}
	
	
	public List<EventCampaign> getALL() {

		logger.debug("Retrieving an existing Event Campaign");

		Query query = new Query(new Criteria().and("isDeleted").is(false).and("isActive").is(true));
		
		logger.debug(query.getQueryObject());

		List<EventCampaign> eventCampaign = mongoTemplate.find(query, EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());

		return eventCampaign;
	}
	
	public List<EventCampaign> getPrepared(String selector, Object value) {

		logger.debug("Retrieving an existing Event Campaign");

		Query query = new Query(new Criteria().and("ended").is(false).and("isDeleted").is(false).and("isActive").is(true).orOperator(Criteria.where(selector).is(value),Criteria.where(selector).exists((Boolean)value)));
		
		logger.debug(query.getQueryObject());

		List<EventCampaign> eventCampaign = mongoTemplate.find(query, EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());

		return eventCampaign;
	}
	
	public long count(String id, String organizerId) {

		logger.debug("Retrieving an existing Event Campaign");

		Query query = new Query(Criteria.where("eventId").is(id).and("organizerId").is(organizerId).and("isDeleted").is(false));

		logger.debug(query.getQueryObject());

		long count = mongoTemplate.count(query, EventCampaign.class, MongoDBCollections.EVENT_CAMPAIGN.toString());

		return count;
	}

	/**
	 * Adds a new EventCompaign
	 * 
	 * @param EventCompaign
	 *            eventCompaign
	 * @return EventCompaign
	 */
	public EventCampaign add(EventCampaign eventCampaign) throws Exception {
		logger.debug("Adding a new EventCompaign");

		try {

			mongoTemplate.insert(eventCampaign, MongoDBCollections.EVENT_CAMPAIGN.toString());

			return eventCampaign;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new EventCompaign", e);

			throw e;
		}
	}

	/**
	 * Deletes an existing Event
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing Event");

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
	 * Edits an existing Event
	 * 
	 * @param Event
	 *            event
	 * @param String
	 *            eventId
	 * @return Event
	 */
	public EventCampaign edit(EventCampaign eventCampaign) throws Exception, NotFoundException {

		logger.debug("Editing existing Event");

		try {

			mongoTemplate.save(eventCampaign);

			return eventCampaign;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing event", e);

			throw e;
		}

	}

	public String getEventCampaignDetailUrl(EventCampaign eventCampaign, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.EVENT_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, eventCampaign.getOrganizerId()) + Urls.GET_EVENT.replace(Urls.SECOND_LEVEL_DOMAIN_ID, eventCampaign.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	public List<EventCampaign> getAllEventCampaign(String eventId) {

		List<EventCampaign> ecpList = new ArrayList<EventCampaign>();

		Query query = new Query();

		query.addCriteria(Criteria.where("eventId").is(eventId).and("isDeleted").is(false).and("isActive").is(true));

		ecpList = mongoTemplate.find(query, EventCampaign.class);

		return ecpList;
	}

	public List<EventCampaign> getAllEventCampaign() {

		List<EventCampaign> ecpList = new ArrayList<EventCampaign>();

		Query query = new Query();

		// query.addCriteria(Criteria.where("isDeleted").is(false).and("isActive").is(true));

		// query.addCriteria(Criteria.where("isDeleted").is(false).and("isActive").is(true).and("participants.is_start").is(false));

		query.addCriteria(Criteria.where("isDeleted").is(false).and("isActive").is(true).orOperator(Criteria.where("participants.is_start").is(false), Criteria.where("participants.is_end").is(false)));

		ecpList = mongoTemplate.find(query, EventCampaign.class);

		return ecpList;
	}
}
