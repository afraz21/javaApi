package org.iqvis.nvolv3.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.EventSponsor;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.EventSponsorService;
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
@RequestMapping(Urls.EVENT_BASE_URL)
public class EventSponsorController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = "eventSponsorService")
	EventSponsorService eventSponsorService;

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	@RequestMapping(value = Urls.UPDATE_EVENT + "/sponsor", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage ResponseMessage(@RequestBody @Valid EventSponsor sponsor, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId, HttpServletRequest request) throws NotFoundException, Exception {

		ResponseMessage message = new ResponseMessage();

		logger.debug("Creating Event Sponsor");

		Sponsor sponsorRes = eventSponsorService.addEventSponsor(sponsor, eventId, organizerId);

		message.setRecordId(sponsorRes.getId());

		message.setDetails_url(request.getRequestURL().toString());

		message.setMessage(String.format(String.format(Messages.ADD_SUCCESS_MESSAGE, "Sponsor")));

		message.setRecord(sponsorRes);

		logger.debug("Event Sponsor Creation Successfull");

		return message;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT + "/sponsor/{sponsorId}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editSponsor(@PathVariable(value = "sponsorId") String sponsorId, @RequestBody @Valid EventSponsor sponsor, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId, HttpServletRequest request) throws NotFoundException, Exception {

		ResponseMessage message = new ResponseMessage();

		logger.debug("Editing Event Sponsor");

		sponsor.setSponsorId(sponsorId);

		Sponsor sponsorRes = eventSponsorService.editEventSponsor(sponsor, eventId, organizerId);

		Event event = eventService.get(eventId, organizerId);

		if (event.getSponsors() != null) {

			for (EventSponsor eventSponosr : event.getSponsors()) {

				if (eventSponosr.getSponsorId().equals(sponsorRes.getId())) {

					sponsorRes.setSponsorCategoryId(eventSponosr.getSponsorCategoryId());

					sponsorRes.setSortOrder(eventSponosr.getSortOrder());

				}
			}
		}

		message.setRecordId(sponsorRes.getId());

		message.setDetails_url(request.getRequestURL().toString());

		message.setMessage(String.format(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Sponsor")));

		message.setRecord(sponsorRes);

		logger.debug("Event Sponsor Edit Successfull");

		return message;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT + "/sponsor/{sponsorId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage deleteSponsor(@PathVariable(value = "sponsorId") String sponsorId, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId, HttpServletRequest request) throws NotFoundException, Exception {

		ResponseMessage message = new ResponseMessage();

		logger.debug("Deleting Event Personal");

		Sponsor sponsorRes = eventSponsorService.deleteEventSponsor(sponsorId, eventId, organizerId);

		Event event = eventService.get(eventId, organizerId);

		if (event.getSponsors() != null) {

			for (EventSponsor eventSponosr : event.getSponsors()) {

				if (eventSponosr.getSponsorId().equals(sponsorRes.getId())) {

					sponsorRes.setSponsorCategoryId(eventSponosr.getSponsorCategoryId());

					sponsorRes.setSortOrder(eventSponosr.getSortOrder());

				}
			}
		}

		message.setRecordId(sponsorRes.getId());

		message.setDetails_url(request.getRequestURL().toString());

		message.setMessage(String.format(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Sponsor")));

		message.setRecord(sponsorRes);

		logger.debug("Event Sponsor Destruction Successfull");

		return message;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT + "/sponsor/{sponsorId}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Sponsor get(HttpServletRequest request, @PathVariable(value = "sponsorId") String sponsorId, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId) throws NotFoundException, Exception {

		logger.debug("Retrieveing Event Personal");

		Sponsor sponsorRes = eventSponsorService.getEventSponsor(sponsorId, eventId, organizerId);

		Event event = eventService.get(eventId, organizerId);

		if (event.getSponsors() != null) {
			for (EventSponsor eventSponosr : event.getSponsors()) {

				if (eventSponosr.getSponsorId().equals(sponsorRes.getId())) {

					sponsorRes.setSponsorCategoryId(eventSponosr.getSponsorCategoryId());

					sponsorRes.setSortOrder(eventSponosr.getSortOrder());
				}
			}
		}
		logger.debug("Event Sponsor Retrieved Successfuly");

		return sponsorRes;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT + "/sponsor/list", method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Page<Sponsor> getSponsorList(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) throws NotFoundException, Exception {

		logger.debug("Fetching Event Sponsor List");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {

			if (search.getQuery() != null) {

				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Event event = eventService.get(eventId, organizerId);

		Page<Sponsor> sponsors = eventSponsorService.getAll(search, request, pageAble, organizerId, eventId);

		for (Sponsor sponsor : sponsors.getContent()) {

			if (event.getSponsors() != null) {

				for (EventSponsor eventSponosr : event.getSponsors()) {

					if (eventSponosr.getSponsorId().equals(sponsor.getId())) {

						sponsor.setSponsorCategoryId(eventSponosr.getSponsorCategoryId());

						sponsor.setSortOrder(eventSponosr.getSortOrder());

					}

				}

			}

		}

		logger.debug("End Event Sponsor List");

		return new PageImpl<Sponsor>(sponsors.getContent(), pageAble, event.getSponsors() == null ? 0 : event.getSponsors().size());

	}

	public Sponsor getSponsorLog(HttpServletRequest request, @PathVariable(value = "sponsorId") String sponsorId, @PathVariable(value = "organizerId") String organizerId, @PathVariable(value = "id") String eventId) throws NotFoundException, Exception {

		logger.debug("Retrieveing Event Personal");

		Sponsor sponsorRes = eventSponsorService.getEventSponsor(sponsorId, eventId, organizerId);

		Event event = eventService.get(eventId, organizerId);

		if (event.getSponsors() != null) {

			for (EventSponsor eventSponosr : event.getSponsors()) {

				if (eventSponosr.getSponsorId().equals(sponsorRes.getId())) {

					sponsorRes.setSponsorCategoryId(eventSponosr.getSponsorCategoryId());

					sponsorRes.setSortOrder(eventSponosr.getSortOrder());

				}

			}

		}

		logger.debug("Event Sponsor Retrieved Successfuly");

		return sponsorRes;

	}

}
