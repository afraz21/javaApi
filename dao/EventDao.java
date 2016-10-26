package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.ActivityPersonnels;
import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventSelective;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.KeyValueString;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
import org.iqvis.nvolv3.service.ConstantFactoryImpl;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import ch.lambdaj.Lambda;

import com.google.gson.Gson;
import com.mongodb.DBObject;

@SuppressWarnings("restriction")
@Repository
public class EventDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	private PersonnelDao personnelDao;

	@Autowired
	private VenueDao venueDao;

	// @Autowired
	Integer EVENT_SEARCH_PREVIOUS_DAYS = ConstantFactoryImpl.EVENT_SEARCH_PREVIOUS_DAYS;

	public List<Event> getEventsContainsTrack(String value, String selector) {

		Query query = new Query();

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where(selector).is(value));

		return mongoTemplate.find(query, Event.class);
	}

	public List<Event> getEvents(List<String> ids) {

		Query query = new Query();

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("isDeleted").in(false));

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("id").in(ids));

		return mongoTemplate.find(query, Event.class);
	}

	public List<EventSelective> getActiveEvents() {

		Query query = new Query();

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("isDeleted").in(false));

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("isActive").in(true));

		query.fields().include("id");

		query.fields().include("socialMediaHashTags");

		query.fields().include("isActive");

		query.fields().include("eventConfiguration");

		query.fields().include("twitterFeedPullStartDate");

		query.fields().include("twitterFeedPullEndDate");

		return mongoTemplate.find(query, EventSelective.class);
	}

	/**
	 * Retrieves all Events
	 * 
	 * @param Search
	 *            eventSearch
	 * @param HttpServletRequest
	 *            request
	 * @return List<Event>
	 */
	public Page<Event> getAll(Query query, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all Events");

		List<Event> events = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		query.addCriteria(Criteria.where("organizerId").is(organizerId));

		events = mongoTemplate.find(query, Event.class);

		// /logger.debug(query.getQueryObject());

		long total = mongoTemplate.count(query, Event.class);

		Page<Event> eventPage = new PageImpl<Event>(events, pageAble, total);

		return eventPage;
	}

	public Page<Event> getAll(String appId, Query query, Pageable pageAble) {

		logger.debug("Retrieving all Events");

		List<Event> events = null;

		if (null != pageAble) {

			query.with(pageAble);
		}

		query.addCriteria(Criteria.where("linkedApp.appId").is(appId));

		logger.debug(query);

		events = mongoTemplate.find(query, Event.class);

		// /logger.debug(query.getQueryObject());

		long total = mongoTemplate.count(query, Event.class);

		Page<Event> eventPage = new PageImpl<Event>(events, pageAble, total);

		return eventPage;
	}

	/**
	 * Retrieves a single Event
	 * 
	 * @param String
	 *            id
	 * @return Event
	 */
	public Event get(String id, String organizerId) {

		// logger.debug("Retrieving an existing Event");

		Query query = new Query(Criteria.where("id").is(id).and("organizerId").is(organizerId).and("isDeleted").is(false));

		// logger.debug(query.getQueryObject());

		Event Event = mongoTemplate.findOne(query, Event.class, MongoDBCollections.EVENT.toString());

		return Event;
	}

	public Event get(String id) {

		logger.debug("Retrieving an existing Event");

		Query query = new Query(Criteria.where("id").is(id).and("isDeleted").is(false));

		logger.debug(query.getQueryObject());

		Event Event = mongoTemplate.findOne(query, Event.class, MongoDBCollections.EVENT.toString());

		return Event;
	}

	public List<String> get(Query query, boolean isIds) {

		logger.debug("Retrieving an existing Event");

		logger.debug(query.getQueryObject());

		List<Event> events = mongoTemplate.find(query, Event.class, MongoDBCollections.EVENT.toString());

		List<String> ids = new ArrayList<String>();

		List<String> organizers = new ArrayList<String>();

		events = Utils.sortList(events, EVENT_SEARCH_PREVIOUS_DAYS);

		for (Event event : events) {

			ids.add(event.getId());

			if (!organizers.contains(event.getOrganizerId())) {

				organizers.add(event.getOrganizerId());
			}

		}

		if (isIds) {
			return ids;
		}
		return organizers;
	}

	public List<Event> searchEvents(Query query, boolean isIds) {

		logger.debug("Retrieving an existing Event");

		logger.debug(query.getQueryObject());

		List<Event> events = mongoTemplate.find(query, Event.class, MongoDBCollections.EVENT.toString());

		return events;
	}

	/**
	 * Adds a new Event
	 * 
	 * @param Event
	 *            event
	 * @return Event
	 */
	public Event add(Event event) throws Exception {
		logger.debug("Adding a new Event");

		try {

			// event.setVenue(null);

			mongoTemplate.insert(event, MongoDBCollections.EVENT.toString());

			return event;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new event", e);

			throw e;
		}
	}

	public void updateVenue(Venue venue, String venueId) {

		Query query = new Query();

		query.addCriteria(Criteria.where("venueId").is(venueId));

		Update update = new Update();

		update.set("venue", venue);

		mongoTemplate.updateMulti(query, update, Event.class);

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
	public Event edit(Event event) throws Exception, NotFoundException {

		logger.debug("Editing existing Event");

		try {

			List<String> list = event.getLanguages();

			List<KeyValueString> listtemp = new ArrayList<KeyValueString>();

			for (String lang : list) {

				KeyValueString tempV = new KeyValueString();

				tempV.setKey(lang);

				tempV.setText(new Locale(lang).getDisplayLanguage());

				listtemp.add(tempV);
			}

			event.setSupported_languages(listtemp);

			// event.setVenue(null);

			System.out.println(event.getId() + "-----------------------");

			mongoTemplate.save(event);

			return event;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing event", e);

			throw e;
		}

	}

	public boolean validateEventAssociationWithOrganizer(String organizerId, String eventId) {

		Query query = new Query(Criteria.where("organizerId").is(organizerId).and("id").is(eventId));

		Event event = mongoTemplate.findOne(query, Event.class, MongoDBCollections.EVENT.toString());

		if (null == event) {

			return false;
		}

		return true;
	}

	public String getEventDetailUrl(Event event, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.EVENT_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, event.getOrganizerId()) + Urls.GET_EVENT.replace(Urls.SECOND_LEVEL_DOMAIN_ID, event.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	/**
	 * Retrieves all Events Speakers
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @return List<EventSpeakers>
	 */
	public Page<Event> getAllEvents(Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all Events");

		List<Event> events = null;

		Query query = new Query();

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("organizerId").is(organizerId).and("isDeleted").is(false));

		if (null != pageAble) {

			query.with(pageAble);
		}

		events = mongoTemplate.find(query, Event.class);

		long total = mongoTemplate.count(query, Event.class);

		Page<Event> eventPage = new PageImpl<Event>(events, pageAble, total);

		return eventPage;
	}

	public Page<Event> getAllAppEvents(Pageable pageAble, List<String> eventIds) {

		logger.debug("Retrieving all Events");

		List<Event> events = new ArrayList<Event>();

		Query query = new Query();

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("id").in(eventIds).and("isDeleted").is(false));

		if (null != pageAble) {

			query.with(pageAble);
		}

		events = mongoTemplate.find(query, Event.class);

		long total = mongoTemplate.count(query, Event.class);

		Page<Event> eventPage = new PageImpl<Event>(events, pageAble, total);

		return eventPage;
	}

	public EventPersonnel add(EventPersonnel eventPersonnel, String eventId, String organizerId) throws Exception {
		logger.debug("Adding a new Activity");

		try {

			Event e = mongoTemplate.findOne(new Query(Criteria.where("id").is(eventId).and("organizerId").is(organizerId)), Event.class);

			if (e != null) {

				if (null != e.getEventPersonnels()) {
					e.getEventPersonnels().add(eventPersonnel);

				}
				else {

					List<EventPersonnel> activities = new ArrayList<EventPersonnel>();

					activities.add(eventPersonnel);

					e.setEventPersonnels(activities);

				}

				// e.setVenue(null);

				mongoTemplate.save(e);

			}
			else {

				throw new NotFoundException(eventId, "Event");
			}

			return eventPersonnel;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new activity", e);

			throw e;
		}
	}

	public EventPersonnel edit(EventPersonnel eventPersonnel, String eventId, String organizerId) throws Exception {
		logger.debug("Editing existing Activity");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newActivityRec = (DBObject) converter.convertToMongoType(eventPersonnel);

			Query query = Query.query(Criteria.where("eventPersonnels.personnelId").is(eventPersonnel.getPersonnelId()).and("id").is(eventId).and("organizerId").is(organizerId));

			Update update = new Update().set("eventPersonnels.$", newActivityRec);

			mongoTemplate.updateFirst(query, update, Event.class);

			return eventPersonnel;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing activity", e);

			throw e;
		}

	}

	public String delete(String eventPersonnelId, String eventId, String organizerId) throws Exception {
		logger.debug("delete existing eventPersonnel");

		try {

			Query query = new Query(Criteria.where("id").is(eventId));

			Event event = mongoTemplate.findOne(query, Event.class, MongoDBCollections.EVENT.toString());

			if (event != null) {

				if (event.getEventPersonnels() != null && event.getEventPersonnels().size() > 0) {

					// removing the personnel from event-personnel array
					List<EventPersonnel> eventPersonnels = Lambda.select(event.getEventPersonnels(), Lambda.having(Lambda.on(EventPersonnel.class).getPersonnelId(), Matchers.equalTo(eventPersonnelId)));

					if (eventPersonnels.size() > 0) {

						for (int i = 0; i < event.getEventPersonnels().size(); i++) {

							if (event.getEventPersonnels().get(i) != null && event.getEventPersonnels().get(i).getPersonnelId().equals(eventPersonnelId)) {

								event.getEventPersonnels().remove(i);

								break;
							}

						}

					}

					// removing the personnel from event activities array

					// also removing that activity from organizer personnel
					// object
					Personnel personnel = personnelDao.get(eventPersonnelId, organizerId);

					if (event.getActivities() != null && event.getActivities().size() > 0) {

						for (Activity eventActivity : event.getActivities()) {

							for (ActivityPersonnels activityPersonnel : eventActivity.getPersonnels()) {

								List<String> activityPersonnels = Lambda.select(activityPersonnel.getPersonnels(), Matchers.equalTo(eventPersonnelId));

								if (activityPersonnels != null && activityPersonnels.size() > 0) {

									activityPersonnel.getPersonnels().remove(eventPersonnelId);

									if (personnel != null) {

										personnel.getActivities().remove(eventActivity.getId());
									}

								}
							}
						}
					}

					// event.setVenue(null);

					// updating event
					edit(event);

					// / updating personnel
					if (personnel != null) {

						personnelDao.edit(personnel);
					}
				}
				else {

					throw new NotFoundException(eventPersonnelId, "EventPersonnel");
				}
			}

			return eventPersonnelId;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to delete existing aeventPersonnel", e);

			throw e;
		}

	}

	public List<EventPersonnel> getEventPersonnels(String id, String eventId, String organizerId) throws NotFoundException {

		logger.debug("Retrieving an existing Activity");

		// Query query = new
		// Query(Criteria.where("eventPersonnels.personnelId").is(id).and("id").is(eventId));

		Query query = new Query(Criteria.where("id").is(eventId));

		query.fields().include("eventPersonnels");

		Event event = mongoTemplate.findOne(query, Event.class, MongoDBCollections.EVENT.toString());

		if (event != null) {

			if (event.getEventPersonnels() != null && event.getEventPersonnels().size() > 0) {

				return event.getEventPersonnels();

			}
			else {

				return null;
			}
		}
		else {

			throw new NotFoundException("", "Record");
		}
	}

	public EventPersonnel get(String id, String eventId, String organizerId) throws NotFoundException {

		logger.debug("Retrieving an existing Activity");

		// Query query = new
		// Query(Criteria.where("eventPersonnels.personnelId").is(id).and("id").is(eventId));

		Query query = new Query(Criteria.where("id").is(eventId));

		query.fields().include("eventPersonnels");

		Event event = mongoTemplate.findOne(query, Event.class, MongoDBCollections.EVENT.toString());

		if (event != null) {

			if (event.getEventPersonnels() != null && event.getEventPersonnels().size() > 0) {

				List<EventPersonnel> eventPersonnels = Lambda.select(event.getEventPersonnels(), Lambda.having(Lambda.on(EventPersonnel.class).getPersonnelId(), Matchers.equalTo(id)));

				if (eventPersonnels.size() > 0) {

					return eventPersonnels.get(0);

				}
				else {

					return null;
				}

			}
			else {

				return null;
			}
		}
		else {

			throw new NotFoundException("", "Record");
		}
	}

	@SuppressWarnings("unchecked")
	public List<EventPersonnel> getAllEventPersonnels(Criteria criteria, org.iqvis.nvolv3.search.Criteria search, String eventId, Pageable pageAble) {

		String collection = "eventPersonnels";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("id").is(eventId).and("isDeleted").is(false)));

		operations.add(Aggregation.project(collection));
		
		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(criteria));

		if (pageAble != null && pageAble.getOffset() > 0) {

			operations.add(Aggregation.skip(pageAble.getOffset()));
		}
		if (pageAble != null) {
			operations.add(Aggregation.limit(pageAble.getPageSize()));
		}
		List<Order> orderByList = new ArrayList<Order>();

		if (null != search && null != search.getQuery().getOrderBy() && search.getQuery().getOrderBy().size() > 0) {

			for (OrderBy orderBy : search.getQuery().getOrderBy()) {

				if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {

					orderByList.add(new Order(Direction.ASC, collection + "." + orderBy.getField()));

				}
				else {

					orderByList.add(new Order(Direction.DESC, collection + "." + orderBy.getField()));
				}
			}

			operations.add(Aggregation.sort(new Sort(orderByList)));

		}

		Aggregation aggregation = Aggregation.newAggregation(operations);

		aggregation.withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
		
		AggregationResults<EventPersonnel> result = mongoTemplate.aggregate(aggregation, Event.class, EventPersonnel.class);

		DBObject object = result.getRawResults();

		logger.debug(result.getRawResults());

		logger.debug(criteria.getCriteriaObject());

		logger.debug(aggregation.toString());

		
		
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		Gson gson = new Gson();

		List<EventPersonnel> eventPersonnels = new ArrayList<EventPersonnel>();

		for (DBObject dbObject : dbObjects) {

			DBObject personnel = (DBObject) dbObject.get(collection);

			String id = personnel.get("personnelId").toString();

			Boolean featured = (Boolean) personnel.get("isFeatured");

			Integer sortOrder = (Integer) personnel.get("sortOrder");

			EventPersonnel eventPersonnel = gson.fromJson(gson.toJson(dbObject), EventPersonnel.class);

			eventPersonnel.setPersonnelId(id);

			eventPersonnel.setSortOrder(sortOrder);

			eventPersonnel.setFeatured(featured);

			eventPersonnels.add(eventPersonnel);

		}

		return eventPersonnels;
	}

	public List<Event> getAppEventsApp(List<String> eventIds) {

		// logger.debug("Retrieving all Events");

		List<Event> events = new ArrayList<Event>();

		Query query = new Query();

		query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("id").in(eventIds).and("isDeleted").is(false));

		events = mongoTemplate.find(query, Event.class);

		// long total = mongoTemplate.count(query, Event.class);

		// Page<Event> eventPage = new PageImpl<Event>(events, pageAble, total);

		return events;
	}

	@SuppressWarnings("unchecked")
	public List<String> getPersonnelActivities(String pId, String eventId) {

		logger.debug("Reteriving All Personnel Activities");

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("_id").is(eventId)));

		operations.add(Aggregation.project("activities"));

		operations.add(Aggregation.unwind("activities"));

		operations.add(Aggregation.unwind("activities.personnels"));

		operations.add(Aggregation.match(Criteria.where("activities.personnels.personnels").is(pId)));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		System.out.println("Aggregation Query : " + operations);

		AggregationResults<Activity> result = mongoTemplate.aggregate(aggregation, Event.class, Activity.class);

		List<String> str_activity = new ArrayList<String>();

		for (Activity res : result) {

			System.out.println("Aggregation Query Result : " + res.getId());

		}

		DBObject object = result.getRawResults();

		System.out.println("Aggregation Query Result : " + result.getRawResults());

		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		for (DBObject dbObject : dbObjects) {

			System.out.println("DBObject : " + dbObject.toString());

			// Activity activity =
			// mongoTemplate.getConverter().read(Activity.class, (DBObject)
			// dbObject.get(MongoDBCollections.ACTIVITY.toString()));

			System.out.println("Activity Id : " + dbObject.get("activities"));

			DBObject obj = (DBObject) dbObject.get("activities");

			String act = obj.get("_id").toString();

			System.out.println("dbObj : " + act);

			str_activity.add(act);
			// activityList.add(activity);
		}

		// db.event.aggregate([
		// {$match:{_id:ObjectId("54d1f0e1e4b0f29b1bb0eaee")}}
		// ,{$project:{activities:1}}
		// ,{$unwind:"$activities"}
		// ,{$project:{"activities.name":1,"activities.personnels":1,"activities._id":1}}
		// ,{$match:{"activities.personnels.personnels":"be09df79-5448-4678-b397-1d45d615b24f"}}
		// ])

		return str_activity;

	}

	public void test() {

		Query query = new Query();

		query.addCriteria(Criteria.where("_id").is("test"));

		mongoTemplate.find(query, Object.class, "testCollection");
	}

	public List<Event> geAppEvents(String appId) {

		logger.debug("Reteriving All app events");

		Query query = new Query(Criteria.where("linkedApp.appId").is(appId));

		List<Event> events = mongoTemplate.find(query, Event.class);

		logger.debug(query.getQueryObject());

		return events;

	}
	
	
	
	public Boolean updateEvent(String organizerId, String eventId, String field, Object value) {

		Query query = new Query();

		query.addCriteria(Criteria.where("id").is(eventId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		Update update = new Update();

		update.set(field, value);
		
		mongoTemplate.updateFirst(query, update, Event.class);

		logger.debug(query.getQueryObject());
		
		return true;
	}

	
	
	

}
