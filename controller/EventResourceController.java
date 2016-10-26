package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.EventResource;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventResourceService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(Urls.EVENT_RESOURCES_BASE_URL)
public class EventResourceController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT_RESOURCE)
	private EventResourceService eventService;

	/*
	 * Create EventResource
	 */

	@RequestMapping(value = Urls.ADD_EVENT_RESOURCES, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addEventResource(@RequestBody @Valid EventResource event, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request, BindingResult result) throws Exception {

		logger.debug("Received request to add event");

		ResponseMessage response = new ResponseMessage();

		if (result.hasErrors()) {

			response.setMessage(result.getFieldError().getDefaultMessage());
			response.setMessageCode(Constants.ERROR_CODE);
			// do something
		}
		else {

			try {
				event.setOrganizerId(organizerId);

				event.setEventId(eventId);

				EventResource addedEventResource = eventService.add(event);

				response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "EventResource"));

				response.setRecordId(addedEventResource.getId().toString());

				response.setRecord(addedEventResource);

				response.setDetails_url(eventService.getEventResourceDetailUrl(addedEventResource, request));

				logger.debug("EventResource has been added successfully");

			}
			catch (Exception e) {

				logger.debug("Exception while adding event", e);

				throw new Exception(e);
			}

			return response;
		}

		return response;
	}

	/*
	 * Get All EventResources with the optional criteria
	 */

	@RequestMapping(value = Urls.GET_EVENT_RESOURCES, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<EventResource> getEventResources(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all events resources");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
					;
				}
			}
		}

		Page<EventResource> events = eventService.getAll(search, request, pageAble, organizerId, eventId);

		return events;
	}

	/*
	 * Update EventResource
	 */

	@RequestMapping(value = Urls.UPDATE_EVENT_RESOURCES, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editEventResource(@RequestBody EventResource eventResource, @PathVariable("tid") String resourceid, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an event");

		ResponseMessage response = new ResponseMessage();

		try {

			eventResource.setOrganizerId(organizerId);

			eventResource.setEventId(eventId);

			EventResource editedEventResource = eventService.edit(eventResource, resourceid, organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "EventResource"));

			response.setRecordId(editedEventResource.getId().toString());
			response.setRecord(editedEventResource);
			response.setDetails_url(eventService.getEventResourceDetailUrl(editedEventResource, request));

		}
		catch (Exception e) {

			logger.debug("Exception while updating event", e);

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(resourceid, "EventResource");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	/*
	 * Delete EventResource
	 */

	@RequestMapping(value = Urls.UPDATE_EVENT_RESOURCES, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage deleteEventResource(@PathVariable("tid") String resourceid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an event");

		ResponseMessage response = new ResponseMessage();

		try {

			EventResource event = eventService.get(resourceid, organizerId);

			if (event != null) {

				event.setIsDeleted(true);

				eventService.edit(event, resourceid, organizerId);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "EventResource"));

				response.setRecordId(resourceid);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(resourceid, "EventResource");
			}

		}
		catch (Exception e) {

			logger.debug("Exception while deleting event", e);

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(resourceid, "EventResource");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

	/*
	 * Get EventResource By Id
	 */

	@RequestMapping(value = Urls.GET_EVENT_RESOURCE, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public EventResource getEventResource(@PathVariable("tid") String resourceid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an event");

		EventResource existingEventResource = null;

		if (null != resourceid && !resourceid.equalsIgnoreCase("")) {

			existingEventResource = eventService.get(resourceid, organizerId);
		}

		if (existingEventResource == null) {
			throw new NotFoundException(resourceid, "EventResource");
		}

		return existingEventResource;
	}

}
