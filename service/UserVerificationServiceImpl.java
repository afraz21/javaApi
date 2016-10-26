package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.dao.UserVerificationDao;
import org.iqvis.nvolv3.domain.UserVerification;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(Constants.SERVICE_USER_VERIFICATION)
@Transactional
public class UserVerificationServiceImpl implements UserVerificationService {

	@Autowired
	private UserVerificationDao userVerificationDao;

	public UserVerification get(String id) {

		return userVerificationDao.get(id);
	}

	public UserVerification add(UserVerification userVerification) throws Exception {

		return userVerificationDao.add(userVerification);
	}

	public UserVerification edit(UserVerification userVerification, String userVerificationId) throws Exception, NotFoundException {

		UserVerification existingUserVerification = null;

		if (null != userVerificationId && !StringUtils.isEmpty(userVerificationId)) {

			existingUserVerification = userVerificationDao.get(userVerificationId);
		}

		if (null == existingUserVerification) {

			throw new NotFoundException(userVerificationId, "VerficationId");
		}

		if (userVerification.getIsActive() != null) {

			existingUserVerification.setIsActive(userVerification.getIsActive());
		}

		return userVerificationDao.edit(existingUserVerification);
	}

}
