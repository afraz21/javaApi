package org.iqvis.nvolv3.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.AnswerObject;
import org.iqvis.nvolv3.domain.FeedBackAnswerCount;
import org.iqvis.nvolv3.domain.UserFeedback;
import org.iqvis.nvolv3.utils.Constants;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.mongodb.DBObject;

@SuppressWarnings("restriction")
@Repository
public class UserFeedbackAnswerDao {

	protected static Logger logger = Logger.getLogger("data access object");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	public UserFeedback add(UserFeedback userFeedback) {

		userFeedback.setCreatedDate(DateTime.now(DateTimeZone.UTC));

		mongoTemplate.insert(userFeedback);

		return userFeedback;
	}

	public List<UserFeedback> getTotalFeedBacks(String objectType, String objectId) {

		Query query = new Query();

		query.addCriteria(Criteria.where("objectType").is(objectType).and("objectId").is(objectId));

		List<UserFeedback> userFeedBacks = mongoTemplate.find(query, UserFeedback.class);

		return userFeedBacks;
	}

	public long getTotalFeedBackCount(String objectType, String objectId) {

		Query query = new Query();

		query.addCriteria(Criteria.where("objectType").is(objectType).and("objectId").is(objectId));

		long userFeedBackCount = mongoTemplate.count(query, UserFeedback.class);

		return userFeedBackCount;
	}

	public List<FeedBackAnswerCount> getFeedBackStats(String objectId, String objectType) {

		List<AggregationOperation> aggregation = new ArrayList<AggregationOperation>();

		aggregation.add(Aggregation.match(Criteria.where("objectId").is(objectId)));

		aggregation.add(Aggregation.unwind("answers"));

		aggregation.add(Aggregation.match(Criteria.where("answers.questionType").ne(Constants.INPUT_TEXT)));

		aggregation.add(Aggregation.project("answers"));

		aggregation.add(Aggregation.group("$answers.answerCode", "$answers.questionId", "$answers.questionType", "$answers.questionKey").count().as("count"));

		System.out.println(Aggregation.newAggregation(aggregation));

		AggregationResults<Object> result = mongoTemplate.aggregate(Aggregation.newAggregation(aggregation), UserFeedback.class, Object.class);

		DBObject objectCount = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		List<FeedBackAnswerCount> finalList = new ArrayList<FeedBackAnswerCount>();

		for (DBObject dbObject : total) {

			Gson gson = new Gson();

			FeedBackAnswerCount obj = mongoTemplate.getConverter().read(FeedBackAnswerCount.class, (DBObject) dbObject);

			finalList.add(obj);
		}

		return finalList;

	}

	public List<FeedBackAnswerCount> getFeedBackStatsInputText(String objectId, String objectType) {

		List<AggregationOperation> aggregation = new ArrayList<AggregationOperation>();

		aggregation.add(Aggregation.match(Criteria.where("objectId").is(objectId)));

		aggregation.add(Aggregation.unwind("answers"));

		aggregation.add(Aggregation.match(Criteria.where("answers.questionType").is(Constants.INPUT_TEXT)));

		aggregation.add(Aggregation.project("answers"));

		aggregation.add(Aggregation.group("$answers.questionId", "$answers.questionType", "$answers.questionKey").count().as("count"));

		System.out.println(Aggregation.newAggregation(aggregation));

		AggregationResults<Object> result = mongoTemplate.aggregate(Aggregation.newAggregation(aggregation), UserFeedback.class, Object.class);

		DBObject objectCount = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		List<FeedBackAnswerCount> finalList = new ArrayList<FeedBackAnswerCount>();

		for (DBObject dbObject : total) {

			Gson gson = new Gson();

			FeedBackAnswerCount obj = mongoTemplate.getConverter().read(FeedBackAnswerCount.class, (DBObject) dbObject);

			finalList.add(obj);
		}

		return finalList;

	}

	public List<AnswerObject> getFeedBackRatingStartStats(String objectId, String objectType) {

		List<AggregationOperation> aggregation = new ArrayList<AggregationOperation>();

		aggregation.add(Aggregation.match(Criteria.where("objectId").is(objectId)));

		aggregation.add(Aggregation.unwind("answers"));

		aggregation.add(Aggregation.match(Criteria.where("answers.questionType").is(Constants.RATING_STARS)));

		aggregation.add(Aggregation.project("answers"));

		System.out.println(Aggregation.newAggregation(aggregation));

		AggregationResults<Object> result = mongoTemplate.aggregate(Aggregation.newAggregation(aggregation), UserFeedback.class, Object.class);

		DBObject objectCount = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		List<AnswerObject> finalList = new ArrayList<AnswerObject>();

		for (DBObject dbObject : total) {

			Gson gson = new Gson();

			System.out.println(gson.toJson(dbObject));

			AnswerObject obj = mongoTemplate.getConverter().read(AnswerObject.class, (DBObject) dbObject.get("answers"));

			finalList.add(obj);
		}

		return finalList;

	}

	public Page<AnswerObject> getFeedBackStatsInputText(String objectId, String objectType, Pageable pageAble, Criteria query) {

		List<AggregationOperation> aggregation = new ArrayList<AggregationOperation>();

		aggregation.add(Aggregation.match(Criteria.where("objectId").is(objectId)));

		aggregation.add(Aggregation.unwind("answers"));

		aggregation.add(Aggregation.match(Criteria.where("answers.questionType").is(Constants.INPUT_TEXT)));

		aggregation.add(Aggregation.project("answers"));

		aggregation.add(Aggregation.match(query));

		System.out.println(Aggregation.newAggregation(aggregation));

		AggregationResults<Object> result = mongoTemplate.aggregate(Aggregation.newAggregation(aggregation), UserFeedback.class, Object.class);

		DBObject objectCount = result.getRawResults();

		@SuppressWarnings("unchecked")
		List<DBObject> total = (List<DBObject>) objectCount.get("result");

		int size = total.size();

		if (pageAble.getOffset() > 0) {
			aggregation.add(Aggregation.skip(pageAble.getOffset()));
		}

		aggregation.add(Aggregation.limit(pageAble.getPageSize()));

		result = mongoTemplate.aggregate(Aggregation.newAggregation(aggregation), UserFeedback.class, Object.class);

		objectCount = result.getRawResults();

		total = (List<DBObject>) objectCount.get("result");

		List<AnswerObject> finalList = new ArrayList<AnswerObject>();

		for (DBObject dbObject : total) {

			Gson gson = new Gson();

			System.out.println(gson.toJson(dbObject));

			AnswerObject obj = mongoTemplate.getConverter().read(AnswerObject.class, (DBObject) dbObject.get("answers"));

			finalList.add(obj);
		}

		return new PageImpl(finalList, pageAble, size);

	}

}
