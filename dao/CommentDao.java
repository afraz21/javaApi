package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Comment;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
import org.iqvis.nvolv3.utils.Constants;
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
public class CommentDao {

	public List<String> getListOfFeedIds(String attendeeId) {
		
		List<String> ids=new ArrayList<String>();

		Query query = new Query();

		query.addCriteria(Criteria.where("comments.attendeeId").is(attendeeId));

		List<Feed> list=mongoTemplate.find(query, Feed.class);
		
		if(list!=null){
			
			for (Feed feed : list) {
				
				if(!ids.contains(feed.getId())){
					
					ids.add(feed.getId());
				}
			}
		}
		
		return ids;
	}

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	/**
	 * Retrieves a single Comments
	 * 
	 * @throws NotFoundException
	 * @param Comments
	 *            id
	 * @return Comments activity
	 */
	public Comment get(String id) throws NotFoundException {

		logger.debug("Retrieving an existing Comments");

		Query query = new Query(Criteria.where("comments._id").is(id).and("isDeleted").is(false));

		query.fields().include("comments.$");

		Feed event = mongoTemplate.findOne(query, Feed.class, MongoDBCollections.FEED.toString());

		if (event != null) {

			if (event.getComments() != null && event.getComments().size() > 0) {

				return event.getComments().get(0);

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
	 * Adds a new Comments
	 * 
	 * @param Comment
	 *            activity
	 * @return Comments activity
	 */
	public Comment add(Comment comment, String feedId) throws Exception {
		logger.debug("Adding a new Comments");

		try {

			Feed e = mongoTemplate.findOne(new Query(Criteria.where("id").is(feedId)), Feed.class);

			if (e != null) {

				comment.setId(UUID.randomUUID().toString());

				// activity.setCreatedDate(new DateTime());

				if (null != e.getComments()) {
					e.getComments().add(comment);

				}
				else {

					List<Comment> comments = new ArrayList<Comment>();

					comments.add(comment);

					e.setComments(comments);

				}

				mongoTemplate.save(e);

			}
			else {

				throw new NotFoundException(comment.getId(), "Event");
			}

			List<String> l = new ArrayList<String>();

			l.add(e.getEventId());

			if (l.size() > 0) {
				dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, e.getId(), Constants.LOG_ACTION_UPDATE, Feed.class.toString());
			}

			return comment;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new activity", e);

			throw e;
		}
	}

	/**
	 * Edits an existing Comments
	 * 
	 * @param Comment
	 *            activity
	 * @return Comments activity
	 */
	public Comment edit(Comment activity) throws Exception {
		logger.debug("Editing existing Comments");

		try {

			MongoConverter converter = mongoTemplate.getConverter();

			DBObject newCommentsRec = (DBObject) converter.convertToMongoType(activity);

			Query query = Query.query(Criteria.where("comments._id").is(activity.getId()));

			Update update = new Update().set("comments.$", newCommentsRec);

			mongoTemplate.updateFirst(query, update, Feed.class);

			return activity;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing activity", e);

			throw e;
		}

	}

	/**
	 * Deletes an existing Comments
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
	 * @return List<Comments>
	 */

	public Page<Comment> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId) {

		logger.debug("Retrieving all Activities");

		String collection = "comments";

		List<AggregationOperation> operations = new ArrayList<AggregationOperation>();

		operations.add(Aggregation.match(userCriteria.and(collection + ".isDeleted").is(false)));

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

		AggregationResults<Comment> result = mongoTemplate.aggregate(aggregation, Feed.class, Comment.class);

		DBObject object = result.getRawResults();

		logger.debug(result.getRawResults());

		logger.debug(userCriteria.getCriteriaObject());

		logger.debug(aggregation.toString());

		@SuppressWarnings("unchecked")
		List<DBObject> dbObjects = (List<DBObject>) object.get("result");

		List<Comment> activities = new ArrayList<Comment>();

		for (DBObject dbObject : dbObjects) {

			DBObject activity = (DBObject) dbObject.get(collection);

			String id = activity.get("_id").toString();

			Comment obj = mongoTemplate.getConverter().read(Comment.class, (DBObject) dbObject.get(collection));

			obj.setId(id);

			activities.add(obj);
		}

		// Total Records Counting
		Aggregation aggregationForTotalCount = Aggregation.newAggregation(Aggregation.unwind(collection), Aggregation.match(userCriteria));

		AggregationResults<Comment> resultCount = this.mongoTemplate.aggregate(aggregationForTotalCount, Feed.class, Comment.class);

		DBObject objectCount = resultCount.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		Page<Comment> activityPage = new PageImpl<Comment>(activities, pageAble, total.size());

		return activityPage;

	}

	public String getCommentsDetailUrl(Comment activity, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.ACTIVITY_BASE_URL;

		replaceUrlToken += Urls.TOP_LEVEL.replace(Urls.TOP_LEVEL_ORGANIZER_ID, activity.getId()) + Urls.GET_ACTIVITY.replace(Urls.SECOND_LEVEL_DOMAIN_ID, activity.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

}
