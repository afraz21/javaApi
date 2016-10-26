package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
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
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.DBObject;

@SuppressWarnings("restriction")
@Repository
public class ActivityDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Autowired
	TracksDao trackDao;

	/**
	 * Retrieves a single Activity
	 * 
	 * @throws NotFoundException
	 * @param Activity
	 *            id
	 * @return Activity activity
	 */
	public Activity get(String id, String eventId) throws NotFoundException {

		logger.debug("Retrieving an existing Activity");

		Query query = new Query(Criteria.where("activities._id").is(id).and("isDeleted").is(false).and("id").is(eventId));

		query.fields().include("activities.$");

		Event event = mongoTemplate.findOne(query, Event.class, MongoDBCollections.EVENT.toString());

		if (event != null) {

			if (event.getActivities() != null && event.getActivities().size() > 0) {

				return event.getActivities().get(0);

			}
			else {

				throw new NotFoundException("", "Record");
			}
		}
		else {

			throw new NotFoundException("", "Record");
		}
	}

	public Activity getActivityById(String id) throws NotFoundException {

		logger.debug("Retrieving an existing Activity");

		Query query = new Query(Criteria.where("activities._id").is(id).and("isDeleted").is(false));

		query.fields().include("activities.$");

		Event event = mongoTemplate.findOne(query, Event.class, MongoDBCollections.EVENT.toString());

		if (event != null) {

			if (event.getActivities() != null && event.getActivities().size() > 0) {

				return event.getActivities().get(0);

			}
			else {

				throw new NotFoundException("", "Record");
			}
		}
		else {

			throw new NotFoundException("", "Record");
		}
	}

	/**
	 * Adds a new Activity
	 * 
	 * @param Activity
	 *            activity
	 * @return Activity activity
	 */
	public Activity add(Activity activity, Event event) throws Exception {
		logger.debug("Adding a new Activity");

		try {

			// Event e = mongoTemplate.findOne(new
			// Query(Criteria.where("id").is(activity.getEventId()).and("organizerId").is(activity.getOrganizerId())),
			// Event.class);

			if (event != null) {

				activity.setId(UUID.randomUUID().toString());

				if (null != event.getActivities()) {

					event.getActivities().add(activity);

				}
				else {

					List<Activity> activities = new ArrayList<Activity>();

					activities.add(activity);

					event.setActivities(activities);

				}

//				event.setVenue(null);

				mongoTemplate.save(event);

			}
			else {

				throw new NotFoundException(activity.getEventId(), "Event");
			}

			return activity;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new activity", e);

			throw e;
		}
	}

	/**
	 * Edits an existing Activity
	 * 
	 * @param Activity
	 *            activity
	 * @return Activity activity
	 */
	public Activity edit(Activity activity) throws Exception {
		logger.debug("Editing existing Activity");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newActivityRec = (DBObject) converter.convertToMongoType(activity);

			Query query = Query.query(Criteria.where("activities._id").is(activity.getId()));

			Update update = new Update().set("activities.$", newActivityRec);

			mongoTemplate.updateFirst(query, update, Event.class);

			return activity;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing activity", e);
			
			throw e;
		}

	}

	/**
	 * Deletes an existing Activity
	 * 
	 * @param String
	 *            id
	 * @return boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing activity");

		try {

			Query query = Query.query(Criteria.where("activities._id").is(id));

			Update update = new Update().set("activities.$.isDeleted", true);

			mongoTemplate.updateFirst(query, update, Event.class);

			return true;

		}
		catch (Exception e) {
			logger.error("An error has occurred while trying to delete activity", e);
			return false;
		}
	}

	/**
	 * Gets all session matching provided criteria
	 * 
	 * @param Search
	 *            search
	 * @param HtppServletRequest
	 *            request
	 * @return List<Activity>
	 */

	public Page<Activity> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId, String eventId) {

		logger.debug("Retrieving all Activities");

		String collection = "activities";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(Criteria.where("id").is(eventId)));

		operations.add(Aggregation.unwind(collection));

		operations.add(Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false).and("organizerId").is(organizerId)));

		System.out.println("Activity Pagination Query : ");

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

		if (pageAble.getOffset() > 0) {
			operations.add(Aggregation.skip(pageAble.getOffset()));
		}

		operations.add(Aggregation.limit(pageAble.getPageSize()));

		//Aggregation aggregation = Aggregation.newAggregation(operations).withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
		
		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Activity> result = mongoTemplate.aggregate(aggregation, Event.class, Activity.class);

		DBObject object = result.getRawResults();

		logger.debug(result.getRawResults());

		logger.debug(userCriteria.getCriteriaObject());

		logger.debug(aggregation.toString());

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Activity> activities = new ArrayList<Activity>();

		for (DBObject dbObject : dbObjects) {

			DBObject activity = (DBObject) dbObject.get(collection);

			String id = activity.get("_id").toString();

			Activity obj = mongoTemplate.getConverter().read(Activity.class, (DBObject) dbObject.get(collection));

			obj.setId(id);

			activities.add(obj);
		}

		// Total Records Counting
		// Aggregation aggregationForTotalCount =
		// Aggregation.newAggregation(Aggregation.unwind(collection),
		// Aggregation.match(userCriteria));
		AggregationOptions options = Aggregation.newAggregationOptions().allowDiskUse(true).build();
		
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("id").is(eventId)),
				Aggregation.project(collection,"organizerId"),
				Aggregation.unwind(collection), 
				Aggregation.match(userCriteria)
			).withOptions(options);
		
		
		AggregationResults<Activity> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, Event.class, Activity.class);

		System.out.println("Activity Pagination Query : " + aggregationForTotalCount);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");
		Page<Activity> activityPage = new PageImpl<Activity>(activities, pageAble, total.size());

		return activityPage;

	}

	public String getActivityDetailUrl(Activity activity, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.ACTIVITY_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, activity.getOrganizerId()) + Urls.GET_ACTIVITY.replace(Urls.SECOND_LEVEL_DOMAIN_ID, activity.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

}
