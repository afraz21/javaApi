package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.domain.UserLoginInfo;
import org.iqvis.nvolv3.exceptionHandler.UserTypeNotAllowedException;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.UserType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserSignUpSignInController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;

	@RequestMapping(value = "signup/", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addUser(@RequestBody @Valid User user, Model model, HttpServletRequest request, BindingResult result) throws Exception {

		logger.debug("Received request to signup user");

		ResponseMessage response = new ResponseMessage();

		if (null == UserType.valueOf(UserType.class, user.getUserTypeCurrent() + "")) {

			throw new UserTypeNotAllowedException(user.getUserTypeCurrent());
		}

		if (result.hasErrors()) {

			response.setMessage(result.getFieldError().getDefaultMessage());
			response.setMessageCode(Constants.ERROR_CODE);

		}
		else {

			try {

				if (user.getAccountId() == null || user.getAccountId() == "") {

					user.setAccountId(user.getEmail());
				}

				// User alreadyExist =
				// userService.getByEmailOrAccountId(user.getEmail(),user.getAccountId());
				//
				// if (null != alreadyExist) {
				//
				// logger.debug("Email (" + user.getEmail() +
				// ") already exist.");
				//
				// response.setMessage("Email (" + user.getEmail() +
				// ") already exist.");
				//
				// response.setRecord(alreadyExist);
				//
				// return response;
				// }

				User addedUser = userService.add(user);

				response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "User"));

				response.setRecordId(addedUser.getId().toString());

				response.setRecord(addedUser);

				response.setDetails_url(userService.getUserDetailUrl(addedUser, request));

				logger.debug("User has been added successfully");

			}
			catch (Exception e) {

				logger.debug("Exception while adding user", e);

				throw new Exception(e);
			}

			return response;
		}

		return response;
	}

	@RequestMapping(value = "signin/", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage signin(@RequestBody @Valid UserLoginInfo loginInfo, Model model, HttpServletRequest request, BindingResult result) throws Exception {

		logger.debug("Authenticating existing User");

		ResponseMessage response = new ResponseMessage();

		response.setHttpCode(Constants.SUCCESS_CODE);

		response.setMessageCode(Constants.SUCCESS_CODE);

		response.setMessage("user has been authenticated successfully");

		response.setDetails_url(request.getRequestURI());

		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		
		User user =null;
		
		try {
			
			user = userService.validateUser(loginInfo);
		}
		catch (Exception e) {

		}
		authenticationResponse.setAuthenticated(user == null ? false : true);

		if (user != null) {

			authenticationResponse.setOrganizerApproved(user.getOrganizerApproved());

			authenticationResponse.setSponsorApproved(user.getSponsorApproved());

			authenticationResponse.setUserType(user.getUserType());
			
			response.setRecordId(user.getId());
		}

		response.setRecord(authenticationResponse);

		

		return response;

	}

}
