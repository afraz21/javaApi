package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.domain.UserVerification;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link UserVerificationService} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface UserVerificationService {

	public UserVerification get(String id);

	public UserVerification add(UserVerification userVerification) throws Exception;

	public UserVerification edit(UserVerification userVerification, String verificationId) throws Exception, NotFoundException;

}
