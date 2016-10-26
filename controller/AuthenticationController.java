package org.iqvis.nvolv3.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.domain.UserVerification;
import org.iqvis.nvolv3.email.EmailService;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.service.UserVerificationService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.AUTH_BASE_URL)
public class AuthenticationController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;

	@Resource(name = Constants.SERVICE_USER_VERIFICATION)
	private UserVerificationService userVerificationService;

	@Resource(name = Constants.SERVICE_EMAIL)
	private EmailService emailService;

	@RequestMapping(value = Urls.LOGIN, method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage authenticateUser(@RequestBody(required = false) User user, Model model, HttpServletRequest request) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

		logger.debug("Received request for user authentication");

		ResponseMessage responseMessage = new ResponseMessage();
		
		if (null != user.getAccountId() && null != user.getPassword()) {
			
			
			User authUser = userService.authenticateUser(user.getAccountId(), Utils.getSHA512Hash(user.getPassword()));

			if (authUser == null) {

				logger.debug("Authentication failed");
				responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
				responseMessage.setMessage(Messages.UNAUTHORIZED_USER);

			}
			else {

				logger.debug("Authentication successfull");
				responseMessage.setMessageCode(Constants.AUTHORIZED_USER_CODE);
				responseMessage.setMessage(Messages.AUTHORIZED);
				responseMessage.setDetails_url(userService.getUserDetailUrl(authUser, request));
				responseMessage.setRecordId(authUser.getId());

			}

		}
		else {

			responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
			responseMessage.setMessage(Messages.UNAUTHORIZED_USER);
		}

		return responseMessage;
	}

	@RequestMapping(value = Urls.CHANGE_PASSWORD, method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage changePassword(@RequestBody(required = false) User user, Model model, HttpServletRequest request) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

		logger.debug("Received request for user authentication");

		ResponseMessage responseMessage = new ResponseMessage();

		if (null != user.getId() && null != user.getPassword()) {
			User authUser = userService.get(user.getId());

			if (authUser == null) {

				logger.debug("Authentication failed");
				responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
				responseMessage.setMessage(Messages.UNAUTHORIZED_USER);

			}
			else {

				if (Utils.getSHA512Hash(user.getPassword()).equals(authUser.getPassword())) {

					if (user.getNewPassword() != null && !user.getNewPassword().equals("")) {

						authUser.setPassword(user.getNewPassword());

						try {
							userService.edit(authUser, authUser.getId());

							logger.debug("Password Updated");
							responseMessage.setMessageCode(Constants.SUCCESS_CODE);
							responseMessage.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Password"));
							responseMessage.setDetails_url(userService.getUserDetailUrl(authUser, request));
							responseMessage.setRecordId(authUser.getId());

						}
						catch (NotFoundException e) {
							e.printStackTrace();
						}
						catch (Exception e) {
							e.printStackTrace();
						}

					}
					else {

						logger.debug("Authentication failed");
						responseMessage.setMessageCode(Constants.ERROR_CODE);
						responseMessage.setMessage(String.format(Messages.INVALID_MESSAGE, "New Password"));

					}

				}
				else {

					logger.debug("Authentication failed");
					responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
					responseMessage.setMessage(String.format(Messages.PASSWORD_MESSAGE, "Old password"));
				}
			}

		}
		else {

			responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
			responseMessage.setMessage(Messages.UNAUTHORIZED_USER);
		}

		return responseMessage;
	}

	@RequestMapping(value = Urls.FORGOT_PASSWORD, method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage forgotPassword(@RequestBody(required = false) User user, Model model, HttpServletRequest request) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

		logger.debug("Received request for user authentication");

		ResponseMessage responseMessage = new ResponseMessage();

		if (null != user.getEmail()) {

			User authUser = userService.getByEmail(user.getEmail());

			if (authUser == null) {

				logger.debug("Authentication failed");
				responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
				responseMessage.setMessage(Messages.UNAUTHORIZED_USER);

			}
			else {

				UserVerification userVerification = new UserVerification();

				userVerification.setUserId(authUser.getId());

				try {
					userVerification = userVerificationService.add(userVerification);
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				String verficationLink = Constants.VERIFICAITON_FORM + userVerification.getId();

				verficationLink = Constants.VERFICATION_EMAIL_TEMPLATE.replace("[LINK]", verficationLink);
				verficationLink = verficationLink.replace("[NAME]", authUser.toString());

				emailService.sendEmail(authUser.getEmail(), "Nvolv3 - Account password reset information", verficationLink);

				responseMessage.setMessageCode(Constants.SUCCESS_CODE);
				responseMessage.setMessage("Verfication email sent successfully");
				responseMessage.setDetails_url("");
				responseMessage.setRecordId("");
			}

		}
		else {

			responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
			responseMessage.setMessage(Messages.UNAUTHORIZED_USER);
		}

		return responseMessage;
	}

	@RequestMapping(value = Urls.EMAIL_VERFICATION, method = { RequestMethod.POST })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage verfication(@RequestBody(required = false) User user, @PathVariable("id") String verficationId, Model model, HttpServletRequest request) throws NotFoundException, Exception {

		logger.debug("Received request for user authentication");

		ResponseMessage responseMessage = new ResponseMessage();

		if (null != user.getPassword()) {

			UserVerification authUserVerification = userVerificationService.get(verficationId);

			if (authUserVerification == null || authUserVerification.getIsActive() == false) {

				logger.debug("Authentication failed");
				responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
				responseMessage.setMessage(Messages.UNAUTHORIZED_USER);

			}
			else {

				User existingUser = userService.get(authUserVerification.getUserId());

				existingUser.setPassword(user.getPassword());

				userService.edit(existingUser, existingUser.getId());

				authUserVerification.setIsActive(false);

				userVerificationService.edit(authUserVerification, authUserVerification.getId());

				responseMessage.setMessageCode(Constants.SUCCESS_CODE);
				responseMessage.setMessage("Password has been updated successfully");
				responseMessage.setDetails_url("");
				responseMessage.setRecordId("");
			}

		}
		else {

			responseMessage.setMessageCode(Constants.UNAUTHORIZED_USER_CODE);
			responseMessage.setMessage(Messages.UNAUTHORIZED_USER);
		}

		return responseMessage;
	}

}
