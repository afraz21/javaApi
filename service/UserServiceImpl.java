package org.iqvis.nvolv3.service;

import java.awt.print.Pageable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.dao.UserDao;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.domain.UserLoginInfo;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Search;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(Constants.SERVICE_USER)
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public List<User> getAll(Search eventSearch, HttpServletRequest request) {

		Query query = new Query();

		return userDao.getAll(query);
	}

	public List<User> getAll(Criteria userCriteria, org.iqvis.nvolv3.search.Criteria search, Pageable pageAble) {

		return userDao.getAll(Utils.parseCriteria(search, ""));
	}

	public User get(String id) {

		return userDao.get(id);
	}

	public User getInclude(String id) {

		return userDao.getInclude(id);
	}

	public User add(User user) throws Exception {

		user.setPassword(Utils.getSHA512Hash(user.getPassword()));

		if (null == user.getLanguages()) {

			List<String> languages = new ArrayList<String>();

			languages.add("EN");

			user.setLanguages(languages);
		}

		return userDao.add(user);
	}

	public Boolean delete(String id) {

		return userDao.delete(id);
	}

	public User edit(User user, String userId) throws Exception, NotFoundException {

		User existingUser = null;

		if (null != userId && !StringUtils.isEmpty(userId)) {

			existingUser = userDao.get(userId);
		}

		if (null == existingUser) {

			throw new NotFoundException(userId, "User");
		}

		if (null != user.getContactNumber() && !StringUtils.isEmpty(user.getContactNumber())) {
			existingUser.setContactNumber(user.getContactNumber());
		}

		if (null != user.getFirstName() && !StringUtils.isEmpty(user.getFirstName())) {
			existingUser.setFirstName(user.getFirstName());
		}

		if (null != user.getLastName() && !StringUtils.isEmpty(user.getLastName())) {
			existingUser.setLastName(user.getLastName());
		}
		if (null != user.getAddress1() && !StringUtils.isEmpty(user.getAddress1())) {
			existingUser.setAddress1(user.getAddress1());
		}

		if (null != user.getOrganizerCompanyLogoO()) {

			existingUser.setOrganizerCompanyLogo(user.getOrganizerCompanyLogoO());
		}

		if (null != user.getAddress2() && !StringUtils.isEmpty(user.getAddress2())) {
			existingUser.setAddress2(user.getAddress2());
		}

		if (null != user.getCountryCode() && !StringUtils.isEmpty(user.getCountryCode())) {

			existingUser.setCountryCode(user.getCountryCode());
		}

		if (null != user.getStateCode() && !StringUtils.isEmpty(user.getStateCode())) {

			existingUser.setStateCode(user.getStateCode());
		}

		if (null != user.getCity() && !StringUtils.isEmpty(user.getCity())) {
			existingUser.setCity(user.getCity());
		}

		if (null != user.getState() && !StringUtils.isEmpty(user.getState())) {
			existingUser.setState(user.getState());
		}

		if (null != user.getZip() && !StringUtils.isEmpty(user.getZip())) {
			existingUser.setZip(user.getZip());
		}

		if (null != user.getCountry() && !StringUtils.isEmpty(user.getCountry())) {
			existingUser.setCountry(user.getCountry());
		}

		if (null != user.getUserType()) {

			existingUser.setUserTypes(user.getUserType());
		}

		if (null != user.getPassword() && !StringUtils.isEmpty(user.getPassword())) {

			existingUser.setPassword(Utils.getSHA512Hash(user.getPassword()));
		}

		if (null != user.getUserStatus() && !StringUtils.isEmpty(user.getUserStatus())) {

			existingUser.setUserStatus(user.getUserStatus());
		}

		if (user.getLanguages() != null && user.getLanguages().size() > 0) {

			existingUser.setLanguages(user.getLanguages());
		}

		if (user.getIsDeleted() != null) {
			existingUser.setIsDeleted(user.getIsDeleted());
		}

		existingUser.setLastModifiedBy(user.getCreatedBy());

		return userDao.edit(existingUser);
	}

	public String getUserDetailUrl(User user, HttpServletRequest request) {

		return userDao.getUserDetailUrl(user, request);
	}

	public User getByEmail(String email) {

		return userDao.getByEmail(email);
	}

	public User getByEmailOrAccountId(String email, String accountId) {

		return userDao.getByEmailOrAccountId(email, accountId);
	}

	public User authenticateUser(String email, String password) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

		return userDao.authenticateUser(email, password);
	}

	public User authenticateUserByIdAndPassword(String userId, String password) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		return userDao.authenticateUserByIdAndPassword(userId, password);
	}

	public User get(String id, String userType) {

		return userDao.get(id, userType);
	}

	public Page<User> getAll(org.iqvis.nvolv3.search.Criteria search, org.springframework.data.domain.Pageable pageAble) {
		// TODO Auto-generated method stub
		return userDao.getAll(Utils.parseCriteria(search, ""), search, pageAble);
	}

	public User validateUser(UserLoginInfo loginInfo) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NotFoundException {

		return userDao.validateUser(loginInfo);
	}

	public Object getAllByUserType(String userType) {

		return userDao.getAllByUserType(userType);
	}

}
