package org.iqvis.nvolv3.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.domain.UserLoginInfo;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.search.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link User} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface UserService {

	public List<User> getAll(Search eventSearch, HttpServletRequest request);

	public User get(String id);

	public User get(String id, String userType);

	public User getByEmail(String userName);

	public User authenticateUser(String userName, String password) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException;

	public User add(User user) throws Exception;

	public Boolean delete(String id);

	public User edit(User user, String userId) throws Exception, NotFoundException;

	public String getUserDetailUrl(User user, HttpServletRequest request);

	public User authenticateUserByIdAndPassword(String userId, String password) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException;

	public Page<User> getAll(Criteria search, Pageable pageAble);

	public User getInclude(String id);

	public User getByEmailOrAccountId(String email, String accountId);

	public User validateUser(UserLoginInfo loginInfo) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NotFoundException;
	
	public Object getAllByUserType(String userType) ;

}
