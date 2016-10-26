package org.iqvis.nvolv3.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.exceptionHandler.UserTypeNotAllowedException;
import org.iqvis.nvolv3.search.Search;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.UserType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.USER_BASE_URL)
public class UserController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;

	@RequestMapping(value = Urls.GET_USERS, method = { RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<User> getUsers(@RequestBody(required = false) Search search, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all users");

		List<User> users = userService.getAll(search, request);

		return users;
	}

	@RequestMapping(value = Urls.ADD_USER, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addUser(@RequestBody @Valid User user, Model model, HttpServletRequest request, BindingResult result) throws Exception {

		logger.debug("Received request to add user");

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

				User alreadyExist = userService.getByEmailOrAccountId(user.getEmail(), user.getAccountId());

				if (null != alreadyExist) {

					logger.debug("Email (" + user.getEmail() + ") already exist.");

					response.setMessage("Email (" + user.getEmail() + ") already exist.");

					response.setRecord(alreadyExist);

					return response;
				}

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

	@RequestMapping(value = Urls.UPDATE_USER, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editUser(@RequestBody User user, @PathVariable("id") String userId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a user");

		ResponseMessage response = new ResponseMessage();

		try {

			User editedUser = userService.edit(user, userId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "User"));

			response.setRecordId(editedUser.getId().toString());

			response.setRecord(editedUser);

			response.setDetails_url(userService.getUserDetailUrl(editedUser, request));

		}
		catch (Exception e) {

			logger.debug("Exception while updating user", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(userId, "User");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_USER, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public User getUser(@PathVariable("id") String userId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a User");

		User existingUser = null;

		if (null != userId && !userId.equalsIgnoreCase("")) {

			existingUser = userService.get(userId);
		}

		if (existingUser == null) {
			throw new NotFoundException(userId, "User");
		}

		return existingUser;
	}
}
