package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.bean.Personnel;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.UpdateEventPersonnelList;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.PersonnelService;
import org.iqvis.nvolv3.service.UserService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
@RequestMapping(Urls.EVENT_BASE_URL)
public class EventPersonnelController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_PERSONNEL)
	private PersonnelService personnelService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@RequestMapping(value = Urls.GET_EVENT_ASSOCIATED_PERSONNELS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<org.iqvis.nvolv3.bean.Personnel> getEventPersonnels(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, @PathVariable("id") String eventid, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all personnels");

		Pageable pageAble = new PageRequest(0, 10);

		if (search != null) {

			if (search.getQuery() != null) {

				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}

			}

		}

		Page<org.iqvis.nvolv3.bean.Personnel> events = eventService.getEventPersonnels(search, request, pageAble, organizerId, eventid);

		return events;
	}

	@RequestMapping(value = Urls.ADD_EVENT_ASSOCIATED_PERSONNELS, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid EventPersonnel personal, @PathVariable("organizerId") String organizerId, Model model, @PathVariable("id") String eventid, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add personal in event");

		ResponseMessage response = new ResponseMessage();

		try {

			EventPersonnel addedSpeaker = eventService.add(personal, eventid, organizerId);

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "EventPersonnel"));

			response.setRecordId(addedSpeaker.getPersonnelId());

			Personnel p = eventService.get(addedSpeaker.getPersonnelId(), eventid, organizerId);

			response.setRecord(p);

			response.setDetails_url("");

			logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "EventPersonnel"));

			List<String> l = new ArrayList<String>();

			l.add(eventid);

			dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, addedSpeaker.getPersonnelId(), Constants.LOG_ACTION_ADD, EventPersonnel.class.toString());

		}
		catch (Exception e) {

			logger.debug("Exception while adding personnel", e);

			throw e;
		}

		return response;
	}

	@Autowired
	UserService userService;

	@RequestMapping(value = Urls.UPDATE_EVENT_ASSOCIATED_PERSONNELS, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody EventPersonnel personal, @PathVariable("tid") String personalId, @PathVariable("id") String eventid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a personnel");
		System.out.println(personal);
		ResponseMessage response = new ResponseMessage();

		try {

			personal.setPersonnelId(personalId);

			personal = eventService.edit(personal, eventid, organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "EventPersonnel"));

			Personnel p = eventService.get(personal.getPersonnelId(), eventid, organizerId);

			response.setRecord(p);

			response.setRecordId(personal.getPersonnelId());

			response.setDetails_url("");

			List<String> l = new ArrayList<String>();
			l.add(eventid);
			dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, personalId, Constants.LOG_ACTION_UPDATE, EventPersonnel.class.toString());

		}
		catch (Exception e) {

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT_ASSOCIATED_PERSONNELS, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("tid") String personalid, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventid, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a Personnel");

		ResponseMessage response = new ResponseMessage();

		try {

			eventService.delete(personalid, eventid, organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "EventPersonnel"));

			response.setRecordId(personalid);

			response.setDetails_url("");

			List<String> l = new ArrayList<String>();
			l.add(eventid);
			dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, personalid, Constants.LOG_ACTION_DELETE, EventPersonnel.class.toString());

		}
		catch (Exception e) {

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT_ASSOCIATED_PERSONNELS, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Personnel get(@PathVariable("tid") String personalid, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventid, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a Personnel");

		Personnel p = eventService.get(personalid, eventid, organizerId);

		if (p == null) {

			throw new NotFoundException(personalid, "EventPersonnel");
		}

		return p;
	}

	public EventPersonnel getEventPersonnel(String eventId, String id, String organizerId) {
		Event event = eventService.get(eventId, organizerId);
		if (event.getEventPersonnels() != null) {
			for (EventPersonnel eventPersonnel : event.getEventPersonnels()) {
				if (id.equals(eventPersonnel.getPersonnelId())) {
					return eventPersonnel;
				}

			}
		}
		return null;
	}

	@RequestMapping(value = Urls.GET_EVENT_PERSONNELS_ACTIVITIES, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Page<Activity> getActivities(@PathVariable("pid") String personalid, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to get a Personnel Activities");

		List<Activity> actvitiyList = new ArrayList<Activity>();

		Pageable pageAble = new PageRequest(0, 20);

		Page<Activity> activityPage = new PageImpl<Activity>(actvitiyList, pageAble, actvitiyList.size());

		org.iqvis.nvolv3.domain.Personnel existingPersonnel = null;

		if (personalid != null && !personalid.equalsIgnoreCase("")) {

			existingPersonnel = personnelService.get(personalid, organizerId);
		}

		if (existingPersonnel != null) {

			if (existingPersonnel.getActivities().size() > 0) {

				for (int i = 0; i < existingPersonnel.getActivities().size(); i++) {

					actvitiyList = eventService.getPersonnelsActvities(personalid, eventId);

					// if (actvitiyList == null && actvitiyList.size() < 1) {
					//
					// throw new NotFoundException(personalid,
					// "EventPersonnel Activities");
					// }
				}

				activityPage = new PageImpl<Activity>(actvitiyList, pageAble, actvitiyList.size());
			}
		}
		else {

			throw new NotFoundException(personalid, "EventPersonnel Activities");

		}

		return activityPage;

	}

	@RequestMapping(value = "/{id}/event-personnels/massupdate", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage massEdit(@RequestBody @Valid UpdateEventPersonnelList personalList, @PathVariable("id") String eventid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a personnel");

		ResponseMessage response = new ResponseMessage();

		try {

			List<EventPersonnel> dbEventPersonnels = eventService.updateBulk(personalList, eventid, organizerId);

			/*
			 * for (EventPersonnel eventPersonnel : personalList.getData()) {
			 * 
			 * EventPersonnel personal = eventService.edit(eventPersonnel,
			 * eventid, organizerId);
			 * 
			 * List<String> l = new ArrayList<String>();
			 * 
			 * l.add(eventid);
			 * 
			 * dataChangeLogService.add(l, "EVENT",
			 * Constants.EVENT_PERSONNEL_LOG_KEY, personal.getPersonnelId(),
			 * Constants.LOG_ACTION_UPDATE, EventPersonnel.class.toString()); }
			 */

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "EventPersonnel"));

			List<Personnel> list = new ArrayList<Personnel>();

			List<String> personnelsIds = new ArrayList<String>();

			for (EventPersonnel eventPersonnel : personalList.getData()) {

				personnelsIds.add(eventPersonnel.getPersonnelId());

			}

			List<org.iqvis.nvolv3.domain.Personnel> listOrignalPersonnel = personnelService.getPersonnelsByIds(personnelsIds, organizerId);

			for (EventPersonnel eventP : dbEventPersonnels) {

				for (org.iqvis.nvolv3.domain.Personnel oP : listOrignalPersonnel) {

					if (oP.getId().equals(eventP.getPersonnelId())) {

						Personnel p = eventService.get(eventP, oP, eventid, organizerId);

						list.add(p);
					}

				}

			}

			
			/*
			 * for (EventPersonnel eventPersonnel : personalList.getData()) {
			 * 
			 * Personnel p = eventService.get(eventPersonnel.getPersonnelId(),
			 * eventid, organizerId);
			 * 
			 * list.add(p); }
			 */
			response.setRecord(list);

			response.setRecordId(personalList.getCreatedBy());

			response.setDetails_url("");

		}
		catch (Exception e) {

			throw e;
		}

		return response;
	}

}
