package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.Like;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
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

import com.mongodb.DBObject;

@SuppressWarnings("restriction")
@Repository
public class LikeDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * Retrieves a single Likes
	 * 
	 * @throws NotFoundException
	 * @param Likes
	 *            id
	 * @return Likes activity
	 */
	public Like get(String id) throws NotFoundException {

		logger.debug("Retrieving an existing Likes");

		Query query = new Query(Criteria.where("likedBy._id").is(id).and("isDeleted").is(false));

		query.fields().include("likedBy.$");

		Feed event = mongoTemplate.findOne(query, Feed.class, MongoDBCollections.FEED.toString());

		if (event != null) {

			if (event.getLikes() != null && event.getLikedBy().size() > 0) {

				return event.getLikedBy().get(0);

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
	 * Adds a new Likes
	 * 
	 * @param Like
	 *            activity
	 * @return Likes activity
	 */
	public Like add(Like activity, String feedId) throws Exception {
		logger.debug("Adding a new Likes");

		try {

			Feed e = mongoTemplate.findOne(new Query(Criteria.where("id").is(feedId)), Feed.class);

			if (e != null) {

				activity.setId(UUID.randomUUID().toString());

				// activity.setCreatedDate(new DateTime());

				if (null != e.getLikes()) {
					e.getLikedBy().add(activity);

				}
				else {

					List<Like> activities = new ArrayList<Like>();

					activities.add(activity);

					e.setLikedBy(activities);

				}

				mongoTemplate.save(e);

			}
			else {

				throw new NotFoundException(activity.getId(), "Event");
			}

			return activity;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new activity", e);

			throw e;
		}
	}

	/**
	 * Edits an existing Likes
	 * 
	 * @param Like
	 *            activity
	 * @return Likes activity
	 */
	public Like edit(Like activity) throws Exception {
		logger.debug("Editing existing Likes");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newLikesRec = (DBObject) converter.convertToMongoType(activity);

			Query query = Query.query(Criteria.where("likedBy._id").is(activity.getId()));

			Update update = new Update().set("likedBy.$", newLikesRec);

			mongoTemplate.updateFirst(query, update, Feed.class);

			return activity;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing activity", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing Likes
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
	 * @return List<Likes>
	 */

	public Page<Like> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all Activities");

		String collection = "likedBy";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false).and("organizerId").is(organizerId)));

		operations.add(Aggregation.unwind(collection));

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

		operations.add(Aggregation.skip(pageAble.getOffset()));

		operations.add(Aggregation.limit(pageAble.getPageSize()));

		Aggregation aggregation = Aggregation.newAggregation(operations);

		AggregationResults<Like> result = mongoTemplate.aggregate(aggregation, Feed.class, Like.class);

		DBObject object = result.getRawResults();

		logger.debug(result.getRawResults());

		logger.debug(userCriteria.getCriteriaObject());

		logger.debug(aggregation.toString());

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Like> activities = new ArrayList<Like>();

		for (DBObject dbObject : dbObjects) {

			DBObject activity = (DBObject) dbObject.get(collection);

			String id = activity.get("_id").toString();

			Like obj = mongoTemplate.getConverter().read(Like.class, (DBObject) dbObject.get(collection));

			obj.setId(id);

			activities.add(obj);
		}

		// Total Records Counting
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind(collection), Aggregation.match(userCriteria));

		AggregationResults<Like> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, Feed.class, Like.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		Page<Like> activityPage = new PageImpl<Like>(activities, pageAble, total.size());

		return activityPage;

	}

	public String getLikesDetailUrl(Like activity, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.ACTIVITY_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, activity.getId()) + Urls.GET_ACTIVITY.replace(Urls.SECOND_LEVEL_DOMAIN_ID, activity.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

}
