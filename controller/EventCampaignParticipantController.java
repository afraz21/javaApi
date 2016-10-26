package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.EventCampaign;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventCampaignParticipantService;
import org.iqvis.nvolv3.service.EventCampaignService;
import org.iqvis.nvolv3.service.EventService;
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
@RequestMapping(Urls.EVENT_CAMPAIGN_PARTICIPANT_BASE_URL)
public class EventCampaignParticipantController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN_PARTICIPANT)
	private EventCampaignParticipantService eventCampaignParticipantService;

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN)
	private EventCampaignService eventCampaignService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@RequestMapping(value = Urls.ADD_EVENT_CAMPAIGN_PARTICIPANT, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody EventCampaignParticipant eventCampaignParticipant, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String campaignId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add EventCampaignParticipant");

		ResponseMessage response = new ResponseMessage();

		try {

			eventCampaignParticipant.setCampaignId(campaignId);

			List<EventCampaignParticipant> list = eventCampaignParticipantService.getAll(organizerId, eventCampaignParticipant.getCampaignId());

			// System.out.println(list.size()+"-----");

			Integer sortOrder = 0;
			boolean flag = false;
			if (list != null) {

				for (EventCampaignParticipant eventCampaignParticipant2 : list) {

					if (eventCampaignParticipant2.getSortOrder() == null) {

						flag = true;

					}
					if (eventCampaignParticipant2.getSortOrder() != null && sortOrder < eventCampaignParticipant2.getSortOrder()) {

						sortOrder = eventCampaignParticipant2.getSortOrder();
					}
				}
			}

			if (sortOrder == 0 || flag) {

				int sortOrderUpdate = 0;

				if (list != null) {

					for (EventCampaignParticipant eventCampaignParticipant2 : list) {

						// System.out.println(eventCampaignParticipant2.getId()+"-----");

						sortOrderUpdate++;

						eventCampaignParticipant2.setSortOrder(sortOrderUpdate);

						eventCampaignParticipantService.edit(eventCampaignParticipant2);
					}
				}
				sortOrder = sortOrderUpdate;
			}

			eventCampaignParticipant.setSortOrder(sortOrder + 1);

			eventCampaignParticipant.setOrganizerId(organizerId);

			eventCampaignParticipant.setIs_start(false);

			eventCampaignParticipant.setIs_end(false);

			EventCampaignParticipant addedTrack = eventCampaignParticipantService.add(eventCampaignParticipant);

			addedTrack = eventCampaignParticipantService.get(addedTrack.getId(), addedTrack.getCampaignId(), organizerId);

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "EventCampaignParticipant"));

			response.setRecord(addedTrack);

			response.setRecordId(addedTrack.getId().toString());

			response.setDetails_url(eventCampaignParticipantService.getEventCampaignParticipantDetailUrl(addedTrack, request));

			logger.debug("EventCampaignParticipant has been added successfully");

			response.setMessage("Participant Added Successfully");
		}
		catch (Exception e) {

			logger.debug("Exception while adding EventCampaignParticipant", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(eventCampaignParticipant.getOrganizerId(), "EventCampaignParticipant");

			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT_CAMPAIGN_PARTICIPANT, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody EventCampaignParticipant eventCampaignParticipant, @PathVariable("id") String eventCampaignId, @PathVariable("tid") String eventCampaignParticipantId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a EventCampaignParticipant");

		ResponseMessage response = new ResponseMessage();

		eventCampaignParticipant.setCampaignId(eventCampaignId);

		eventCampaignParticipant.setId(eventCampaignParticipantId);

		try {

			eventCampaignParticipant.setOrganizerId(organizerId);

			EventCampaign eventCampaign = eventCampaignService.get(eventCampaignId, organizerId);

			EventCampaignParticipant editedTrack = eventCampaignParticipantService.edit(eventCampaignParticipant);

			editedTrack = eventCampaignParticipantService.get(editedTrack.getId(), editedTrack.getCampaignId(), organizerId);

			if (eventCampaign != null) {

				if (!editedTrack.getIsActive()) {

					List<String> l = new ArrayList<String>();

					l.add(eventCampaign.getEventId());

					l.remove("");

					if (l.size() > 0) {

						dataChangeLogService.add(l, "EVENT", Constants.EVENT_CAMPAIGN__LOG_KEY, eventCampaignParticipant.getId(), Constants.LOG_ACTION_DELETE, EventCampaign.class.toString());
					}

				}
			}

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "EventCampaignParticipant"));

			response.setRecord(editedTrack);

			response.setRecordId(editedTrack.getId().toString());

			response.setDetails_url(eventCampaignParticipantService.getEventCampaignParticipantDetailUrl(editedTrack, request));

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(eventCampaignParticipantId, "EventCampaignParticipant");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT_CAMPAIGN_PARTICIPANT, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String eventCampaignId, @PathVariable("tid") String eventCampaignParticipantId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to delete a EventCampaignParticipant");

		ResponseMessage response = new ResponseMessage();

		try {

			EventCampaignParticipant eventCampaignParticipant = eventCampaignParticipantService.get(eventCampaignParticipantId, eventCampaignId, organizerId);

			if (eventCampaignParticipant != null) {

				eventCampaignParticipant.setIsDeleted(true);

				eventCampaignParticipantService.edit(eventCampaignParticipant);

				EventCampaign eventCampaign = eventCampaignService.get(eventCampaignId, organizerId);

				List<String> l = new ArrayList<String>();

				l.add(eventCampaign.getEventId());

				dataChangeLogService.add(l, "EVENT", Constants.EVENT_CAMPAIGN__LOG_KEY, eventCampaignParticipant.getId(), Constants.LOG_ACTION_DELETE, eventCampaignParticipant.getClass().toString());

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "EventCampaignParticipant"));

				response.setRecordId(eventCampaignParticipantId);

				response.setDetails_url("");

			}
			else {

				throw new NotFoundException(eventCampaignParticipantId, "EventCampaignParticipant");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(eventCampaignParticipantId, "EventCampaignParticipant");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_EVENT_CAMPAIGN_PARTICIPANT, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public EventCampaignParticipant getTrack(@PathVariable("id") String eventCampaignId, @PathVariable("tid") String eventCampaignParticipantId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a EventCampaignParticipant");

		EventCampaignParticipant existingTrack = null;

		if (null != eventCampaignParticipantId && !eventCampaignParticipantId.equalsIgnoreCase("")) {

			existingTrack = eventCampaignParticipantService.get(eventCampaignParticipantId, eventCampaignId, organizerId);

		}

		if (existingTrack == null) {

			throw new NotFoundException(eventCampaignParticipantId, "EventCampaignParticipant");
		}

		return existingTrack;
	}

	@RequestMapping(value = Urls.GET_EVENT_CAMPAIGN_PARTICIPANTS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<EventCampaignParticipant> getAll(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, @PathVariable("id") String eventCampaignId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all EventCampaignParticipant");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<EventCampaignParticipant> participants = eventCampaignParticipantService.getAll(search, pageAble, organizerId, eventCampaignId);

		return participants;
	}

}
