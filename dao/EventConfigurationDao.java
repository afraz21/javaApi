package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventConfigurationWrapper;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.mongodb.DBObject;

@Repository
public class EventConfigurationDao {

	protected static Logger logger = Logger.getLogger("EventConfigurationDao");

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private EventDao eventDao;

	@SuppressWarnings("unchecked")
	public Object add(String eventId, String organizerId, Object eventConfiguration) {
		try {

			final String collection = "eventConfiguration.";

			Query query = Query.query(Criteria.where("organizerId").is(organizerId).and("id").is(eventId));

			Gson gson = new Gson();

			Map<String, Object> map = new HashMap<String, Object>();

			map = (Map<String, Object>) gson.fromJson(gson.toJson(eventConfiguration), map.getClass());

			final Update update = new Update();

			Set<String> keys = map.keySet();

			for (String key : keys) {

				if (map.get(key) != null) {

					update.set(collection + key, map.get(key));
				}
			}

			mongoTemplate.updateFirst(query, update, Event.class);

			Event event = mongoTemplate.findOne(query, Event.class);

			return event.getEventConfiguration();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Object get(String eventId, String organizerId) throws NotFoundException {
		Query query = Query.query(Criteria.where("organizerId").is(organizerId).and("id").is(eventId));

		Event event = mongoTemplate.findOne(query, Event.class);

		if (event == null) {
			throw new NotFoundException(eventId, "Event");
		}

		if (event.getEventConfiguration() == null) {
			throw new NotFoundException(eventId, "EventConfiguration");
		}

		return event.getEventConfiguration();
	}

	public EventConfigurationWrapper getGlobalEventConfiguration() {

		Query query = Query.query(Criteria.where("mobileAPI").is(true).and("eventConfiguration").exists(true)).limit(1);

		logger.debug(query);

		EventConfigurationWrapper wrapperConfiguration = mongoTemplate.findOne(query, EventConfigurationWrapper.class, MongoDBCollections.EVENT_CONFIGURATION.toString());

		return wrapperConfiguration;
	}

	public Object getGlobalFeedbackConfiguration() {

		Query query = Query.query(Criteria.where("feedbackQuestionnaireUIControls").exists(true)).limit(1);

		logger.debug(query);

		Object feedbackConfiguration = mongoTemplate.findOne(query, Object.class, MongoDBCollections.EVENT_CONFIGURATION.toString());

		return feedbackConfiguration;
	}

	public String getQuestionType(String value) {

		String collection = "feedbackQuestionnaireUIControls";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(Criteria.where(collection + ".value").is(value)));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Object> result = mongoTemplate.aggregate(aggregation, MongoDBCollections.EVENT_CONFIGURATION.toString(), Object.class);

		DBObject object = result.getRawResults();

		logger.debug(result.getRawResults());

		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		for (DBObject dbObject : dbObjects) {

			// System.out.println("value is : "+((DBObject)dbObject.get(collection)).get("type"));

			return ((DBObject) dbObject.get(collection)).get("type").toString();

		}

		logger.debug("From feedback Types");

		return null;

	}
	public Boolean updateEventConfiguration(String organizerId, String eventId, String field, Object value) {

		Query query = new Query();

		query.addCriteria(Criteria.where("id").is(eventId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		Update update = new Update();

		update.set(field, value);
		
		mongoTemplate.updateFirst(query, update, Event.class);

		return true;
	}

	public Object getEventCOnfigurationField(String organizerId, String eventId, String field) {

		Query query = new Query();

		query.addCriteria(Criteria.where("id").is(eventId).and("organizerId").is(organizerId).and("isDeleted").is(false));

		query.fields().include(field);

		return null;
	}

}
