package org.iqvis.nvolv3.dao;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.domain.UserLoginInfo;
import org.iqvis.nvolv3.exceptionHandler.AccountIdAlreadyExists;
import org.iqvis.nvolv3.exceptionHandler.EmailAlreadyExists;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("restriction")
@Repository
public class UserDao {

	protected static Logger logger = Logger.getLogger("service");

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * Retrieves all Users
	 * 
	 * @param Query
	 *            query
	 * @return List<User>
	 */
	public List<User> getAll(Query query) {

		logger.debug("Retrieving all Users");

		List<User> users = mongoTemplate.find(query, User.class);

		return users;
	}

	public Page<User> getAll(Query query, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble) {

		logger.debug("Retrieving all Users");

		List<Order> orderByList = new ArrayList<Order>();

		if (null != search && null != search.getQuery().getOrderBy() && search.getQuery().getOrderBy().size() > 0) {

			for (OrderBy orderBy : search.getQuery().getOrderBy()) {

				if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {

					orderByList.add(new Order(Direction.ASC, orderBy.getField()));

				}
				else {

					orderByList.add(new Order(Direction.DESC, orderBy.getField()));
				}
			}

		}

		List<User> countUser = mongoTemplate.find(query, User.class);

		if (orderByList != null && orderByList.size() > 0) {
			query.with(new Sort(orderByList));
		}

		query.with(pageAble);

		List<User> users = mongoTemplate.find(query, User.class);

		return new PageImpl<User>(users, pageAble, countUser.size());
	}

	/**
	 * Retrieves a single User
	 * 
	 * @param String
	 *            id
	 * @return User
	 */
	public User get(String id) {
		// logger.debug("Retrieving an existing User");

		Query query = new Query(Criteria.where("id").is(id).and("isDeleted").is(false));

		// logger.debug(query);

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		return user;
	}

	public User getInclude(String id) {
		logger.debug("Retrieving an existing User");

		Query query = new Query(Criteria.where("id").is(id).and("isDeleted").is(false));
//
//		query.fields().include("_id");

		logger.debug(query);

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		return user;
	}

	public User get(String id, String userType) {
		logger.debug("Retrieving an existing User");

		Query query = new Query(Criteria.where("id").is(id).and("isDeleted").is(false).and("userTypes").is(userType));

		query.fields().include("id");
		
		logger.debug(query);

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		return user;
	}

	/**
	 * Retrieves a single User
	 * 
	 * @param String
	 *            userName
	 * @return User
	 */
	public User getByEmail(String email) {
		logger.debug("Retrieving an existing User");

		Query query = new Query(Criteria.where("email").is(email));

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		return user;
	}

	public User getByEmailOrAccountId(String email, String accountId) {
		logger.debug("Retrieving an existing User");

		Query query = new Query(Criteria.where("isDeleted").is(false).orOperator(Criteria.where("accountId").is(accountId),Criteria.where("email").is(email)));

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		return user;
	}

	/**
	 * Adds a new User
	 * 
	 * @param User
	 *            user
	 * @return User
	 */
	public User add(User user) throws Exception {

		logger.debug("Adding a new User");

		User existingUser = this.getByEmailOrAccountId(user.getEmail(), user.getAccountId());

		if (existingUser != null) {

			if (existingUser.getUserType() != null && existingUser.getUserType().size() > 0 && !existingUser.getUserType().contains(user.getUserTypeCurrent())) {

				existingUser.setUserType(user.getUserTypeCurrent());

				mongoTemplate.save(existingUser);

				return existingUser;

			}
			else {
				
				if((user.getEmail()+"").equals(existingUser.getEmail())){
					throw new EmailAlreadyExists();	
				}
				else{
					throw new AccountIdAlreadyExists();
				}
//				throw new AlreadyExistException(user.getEmail(), "User");
			}
		}

		try {

			mongoTemplate.insert(user, MongoDBCollections.USER.toString());

			return user;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to add new user", e);

			throw e;
		}
	}

	/**
	 * Deletes an existing User
	 * 
	 * @param String
	 *            id
	 * @return Boolean
	 */
	public Boolean delete(String id) {
		logger.debug("Deleting existing user");

		try {

			Query query = new Query(Criteria.where("id").is(id));

			mongoTemplate.remove(query);

			return true;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to delete user", e);

			return false;
		}
	}

	/**
	 * Edits an existing User
	 * 
	 * @param Useruser
	 * @param String
	 *            eventId
	 * @return User
	 */
	public User edit(User user) throws Exception, NotFoundException {

		logger.debug("Editing existing user");

		try {

			mongoTemplate.save(user);

			return user;

		}
		catch (Exception e) {

			logger.error("An error has occurred while trying to edit existing user", e);

			throw e;
		}

	}

	/**
	 * Retrieves a single User
	 * 
	 * @param String
	 *            userName
	 * @param String
	 *            password
	 * @return User
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public User authenticateUser(String email, String password) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		logger.debug("Authenticating existing User");

		Query query = new Query(Criteria.where("accountId").regex(Pattern.compile(email, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)).and("password").is(password));

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		return user;
	}

	public User authenticateUserByIdAndPassword(String userId, String password) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		logger.debug("Authenticating existing User");

		Query query = new Query(Criteria.where("id").is(userId).and("password").is(password));

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());

		return user;
	}
	
	public Object getAllByUserType(String userType) {
		logger.debug("Authenticating existing Partners");

		Query query = new Query(Criteria.where("userTypes").is(userType));
		
		query.fields().include("firstName");

		logger.debug(query);
		
		Object user = mongoTemplate.find(query, Object.class, MongoDBCollections.USER.toString());

		return user;
	}

	public String getUserDetailUrl(User user, HttpServletRequest request) {
		String detailUrl = "";

		String replaceUrlToken = Urls.USER_BASE_URL;

		replaceUrlToken += Urls.GET_USER.replace("{id}", user.getId().toString());

		detailUrl = Utils.getURLWithContextPath(request) + replaceUrlToken;

		return detailUrl;
	}

	public User validateUser(UserLoginInfo loginInfo) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NotFoundException {
		logger.debug("Authenticating existing User");

		Query query = new Query(Criteria.where("accountId").is(loginInfo.getAccountId()).and("password").is(loginInfo.getPassword()));

		User user = mongoTemplate.findOne(query, User.class, MongoDBCollections.USER.toString());
		
		if(user==null){
			
			throw new NotFoundException(loginInfo.getAccountId(), "User");
		}

		return user;
	}

}
