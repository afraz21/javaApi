package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(Urls.ADMIN_USER_ROOT)
public class AdminUserController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_USER)
	private UserService userService;
	
	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@RequestMapping(value = Urls.MAIN_ADMIN_LEVEL_USER_URL + Urls.GET_USER, method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/partner", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Object getPartners(@PathVariable("adminId") String userId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a User");

		Object existingPartner = null;

			existingPartner = userService.getAllByUserType("PARTNER");

		return existingPartner;
	}

	@RequestMapping(value = Urls.MAIN_ADMIN_LEVEL_USER_URL, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<User> getUsers(@RequestBody(required = false) @Valid Criteria search, @PathVariable("adminId") String adminId, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to show all users");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		return userService.getAll(search, pageAble);
	}

	@RequestMapping(value = Urls.MAIN_ADMIN_LEVEL_USER_URL, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage postUser(@RequestBody @Valid User user, @PathVariable("adminId") String adminId, HttpServletRequest request) throws Exception {

		ResponseMessage response = new ResponseMessage();
		
		User admin=userService.get(adminId);
		
		user.setPartnerId(admin.getPartnerId());

		User newUser = userService.add(user);

		response.setDetails_url(request.getRequestURL().toString());

		response.setHttpCode(Constants.SUCCESS_CODE);

		response.setMessage("User added successfully");

		response.setRecord(newUser);

		response.setRecordId(newUser.getId());

		return response;
	}
	
	
	

	@RequestMapping(value = Urls.MAIN_ADMIN_LEVEL_USER_URL + Urls.UPDATE_USER, method = RequestMethod.PUT)
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

	@RequestMapping(value = Urls.MAIN_ADMIN_LEVEL_USER_URL + Urls.UPDATE_USER, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage deleteUser(@PathVariable("id") String userId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a user");

		ResponseMessage response = new ResponseMessage();

		try {

			User deletedUser = userService.get(userId);

			deletedUser.setIsDeleted(true);

			userService.edit(deletedUser, userId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "User"));

			response.setRecordId(deletedUser.getId().toString());

			response.setRecord(deletedUser);

			response.setDetails_url(userService.getUserDetailUrl(deletedUser, request));

		}
		catch (Exception e) {

			logger.debug("Exception while Deleting user", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(userId, "User");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}
	
	
	
	@RequestMapping(value = "/app/{appId}/events", method = { RequestMethod.GET,RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Event> getAppEventListCMS(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "appId") String appId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to show all events");

		Pageable pageAble = null;

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
					;
				}
			}
		}

		return eventService.geAppEvents(appId,search,pageAble);

	}

	
	

}
