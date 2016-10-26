package org.iqvis.nvolv3.dao;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.Question;
import org.iqvis.nvolv3.domain.UserFeedbackQuestion;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class UserFeedbackQuestionDao {

	protected static Logger logger = Logger.getLogger("data access object");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	public UserFeedbackQuestion add(UserFeedbackQuestion userFeedbackQuestion) {

		if (userFeedbackQuestion.getQuestions() != null) {

			for (Question feedbackQuestion : userFeedbackQuestion.getQuestions()) {

				feedbackQuestion.setId(UUID.randomUUID().toString());
			}
		}

		mongoTemplate.insert(userFeedbackQuestion);

		return userFeedbackQuestion;
	}

	public UserFeedbackQuestion get(String id) {

		Query query = new Query();

		query.addCriteria(Criteria.where("id").is(id).and("isDeleted").is(false));

		UserFeedbackQuestion userFeedbackQuestion = mongoTemplate.findOne(query, UserFeedbackQuestion.class);

		return userFeedbackQuestion;
	}

	public UserFeedbackQuestion edit(UserFeedbackQuestion userFeedbackQuestion, String id) throws NotFoundException {

		mongoTemplate.save(userFeedbackQuestion);

		UserFeedbackQuestion userFeedBackQuestions = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("id").is(id).and("isDeleted").is(false)), UserFeedbackQuestion.class);

		return userFeedBackQuestions;
	}

	public List<UserFeedbackQuestion> getAll(String organizerId) {

		Query query = new Query();

		query.addCriteria(Criteria.where("organizerId").is(organizerId).and("isDeleted").is(false));

		List<UserFeedbackQuestion> userFeedbackQuestion = this.getAll(query);

		return userFeedbackQuestion;
	}

	public List<UserFeedbackQuestion> getAll(Query query) {

		List<UserFeedbackQuestion> userFeedbackQuestion = mongoTemplate.find(query, UserFeedbackQuestion.class);

		return userFeedbackQuestion;
	}

}
