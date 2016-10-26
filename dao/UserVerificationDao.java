package org.iqvis.nvolv3.dao;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.UserVerification;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.MongoDBCollections;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class UserVerificationDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * Retrieves a single UserVerification
	 * 
	 * @param String
	 *            id
	 * @return UserVerification
	 */
	public UserVerification get(String id) {
		logger.debug("Retrieving an existing UserVerification");

		Query query = new Query(Criteria.where("id").is(id));

		UserVerification user = mongoTemplate.findOne(query, UserVerification.class, MongoDBCollections.USER_VERIFICATION.toString());

		return user;
	}

	/**
	 * Adds a new UserVerification
	 * 
	 * @param UserVerification
	 *            userVerification
	 * @return UserVerification
	 */
	public UserVerification add(UserVerification userVerfication) throws Exception {
		logger.debug("Adding a new User");

		try {

			mongoTemplate.insert(userVerfication, MongoDBCollections.USER_VERIFICATION.toString());

			return userVerfication;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new UserVerification", e);

			throw e;
		}
	}

	/**
	 * Edits an existing UserVerification
	 * 
	 * @param UserVerification
	 * @param String
	 *            eventId
	 * @return UserVerification
	 */
	public UserVerification edit(UserVerification userVerification) throws Exception, NotFoundException {

		logger.debug("Editing existing UserVerification");

		try {

			mongoTemplate.save(userVerification);

			return userVerification;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing UserVerification", e);

			throw e;
		}

	}

}
