package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ActivityPersonnels;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.ActivityService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.PersonnelService;
import org.iqvis.nvolv3.service.ReferenceDataService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
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
@RequestMapping(Urls.PERSONNEL_BASE_URL)
public class PersonnelController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_PERSONNEL)
	private PersonnelService personnelService;

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	@Resource(name = Constants.SERVICE_REFERENCE_DATA)
	private ReferenceDataService reference_Data_Service;

	@RequestMapping(value = Urls.ADD_PERSONNEL, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid Personnel personal, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add personal");

		ResponseMessage response = new ResponseMessage();

		try {

			personal.setOrganizerId(organizerId);

			Personnel addedSpeaker = personnelService.add(personal);

			addedSpeaker = personnelService.get(addedSpeaker.getId(), organizerId);

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Personnel"));

			response.setRecordId(addedSpeaker.getId().toString());

			response.setRecord(addedSpeaker);

			response.setDetails_url(personnelService.getPersonnelDetailUrl(addedSpeaker, request));

			logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "Personnel"));

			List<String> l = new ArrayList<String>();

			l.addAll(eventService.getEventIdsBySelector(addedSpeaker.getId(), "eventPersonnels.personnelId"));

			if (l.size() > 0) {
				dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, addedSpeaker.getId(), Constants.LOG_ACTION_ADD, Personnel.class.toString());

			}

		}
		catch (Exception e) {

			logger.debug("Exception while adding personnel", e);

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(personal.getOrganizerId(), "Organizer");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_PERSONNEL, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody Personnel personal, @PathVariable("id") String personalId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a personnel");

		ResponseMessage response = new ResponseMessage();

		personal.setId(personalId);

		try {
			personal.setOrganizerId(organizerId);

			Personnel editedSpeaker = personnelService.edit(personal, personalId);

			editedSpeaker = personnelService.get(editedSpeaker.getId(), organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Personnel"));

			response.setRecordId(editedSpeaker.getId().toString());

			response.setRecord(editedSpeaker);

			response.setDetails_url(personnelService.getPersonnelDetailUrl(editedSpeaker, request));

			List<String> l = new ArrayList<String>();

			l.addAll(eventService.getEventIdsBySelector(editedSpeaker.getId(), "eventPersonnels.personnelId"));

			if (l.size() > 0) {

				// for (String id : l) {
				// Event event=eventService.get(id, organizerId);
				//
				// event.setAppLevelChange(true);
				//
				// event.setEventLevelChange(true);
				//
				// eventService.edit(event, id, organizerId);
				//
				// }

				dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, editedSpeaker.getId(), Constants.LOG_ACTION_UPDATE, Personnel.class.toString());

			}

		}
		catch (Exception e) {

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_PERSONNEL, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String personalid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a Personnel");

		ResponseMessage response = new ResponseMessage();

		try {

			Personnel personal = personnelService.get(personalid, organizerId);

			if (personal != null) {

				personal.setIsDeleted(true);

				personnelService.edit(personal, personalid);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Personnel"));

				response.setRecordId(personalid);

				response.setDetails_url("");

				List<String> l = new ArrayList<String>();

				l.addAll(eventService.getEventIdsBySelector(personal.getId(), "eventPersonnels.personnelId"));

				if (l.size() > 0) {
					dataChangeLogService.add(l, "EVENT", Constants.EVENT_PERSONNEL_LOG_KEY, personal.getId(), Constants.LOG_ACTION_DELETE, Personnel.class.toString());

				}

			}
			else {

				throw new NotFoundException(personalid, "Personnel");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(personalid, "Personnel");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_PERSONNEL, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Personnel get(@PathVariable("id") String Id, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a Personnel");

		Personnel existingPersonal = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingPersonal = personnelService.get(Id, organizerId);
		}

		if (existingPersonal == null) {
			throw new NotFoundException(Id, "Personnel");
		}

		return existingPersonal;

	}

	@RequestMapping(value = Urls.GET_PERSONNEL_ACTIVITIES, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Page<Activity> getPeronsonalActivities(@PathVariable("id") String Id, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a Personnel Activities");

		List<Activity> activities = new ArrayList<Activity>();

		Pageable pageAble = new PageRequest(0, 20);

		Page<Activity> activityPage = new PageImpl<Activity>(activities, pageAble, activities.size());

		Personnel existingPersonal = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingPersonal = personnelService.get(Id, organizerId);
		}

		if (existingPersonal != null) {

			if (existingPersonal.getActivities().size() > 0) {

				for (int i = 0; i < existingPersonal.getActivities().size(); i++) {

					try {

						Activity activity = activityService.getActivityById(existingPersonal.getActivities().get(i));

						if (activity.getPersonnels() != null) {

							for (ActivityPersonnels personnel : activity.getPersonnels()) {

								ReferenceData referenceData = reference_Data_Service.get(Constants.PERSONNEL_TYPE, organizerId);

								if (referenceData != null && referenceData.getData() != null) {

									for (Data data : referenceData.getData()) {

										if (data.getId().equals(personnel.getTypeId())) {

											personnel.setPersonnelType(data.getName());
											
											break;
										}
									}

								}

							}
						}

						activities.add(activity);

					}
					catch (Exception e) {
						// TODO: handle exception
					}
				}

				activityPage = new PageImpl<Activity>(activities, pageAble, activities.size());
			}

		}
		else {

			throw new NotFoundException(Id, "Personnel");
		}

		return activityPage;

	}

	@RequestMapping(value = Urls.GET_PERSONNELS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Personnel> getPersonnels(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all personnels");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
				}
			}
		}

		Page<Personnel> personnels = personnelService.getAll(search, pageAble, organizerId);

		return personnels;
	}

}
