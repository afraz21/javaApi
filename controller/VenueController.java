package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.ActivityService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.VenueService;
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
@RequestMapping(Urls.VENUE_BASE_URL)
public class VenueController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_VENUE)
	private VenueService venueService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	@RequestMapping(value = Urls.ADD_VENUE, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid Venue venue, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add venue");

		ResponseMessage response = new ResponseMessage();

		try {

			venue.setOrganizerId(organizerId);

			Venue addedVenue = venueService.add(venue);

			addedVenue = venueService.get(addedVenue.getId(), organizerId);

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Venue"));

			response.setRecordId(addedVenue.getId().toString());

			response.setRecord(addedVenue);

			response.setDetails_url(venueService.getVenueDetailUrl(addedVenue, request));

			logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "Venue"));

		}
		catch (Exception e) {

			logger.debug("Exception while adding venue", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(venue.getOrganizerId(), "Organizer");

			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_VENUE, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody @Valid Venue venue, @PathVariable(value = "organizerId") String organizerId, @PathVariable("id") String venueid, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a track");

		ResponseMessage response = new ResponseMessage();

		venue.setId(venueid);

		try {

			venue.setOrganizerId(organizerId);

			Venue editedVenue = venueService.edit(venue);

			editedVenue = venueService.get(editedVenue.getId(), organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Venue"));

			response.setRecordId(editedVenue.getId().toString());

			response.setRecord(editedVenue);

			response.setDetails_url(venueService.getVenueDetailUrl(editedVenue, request));

			List<String> l = new ArrayList<String>();

			if (!editedVenue.getIsDeleted()) {

				l.addAll(eventService.getEventIdsBySelector(editedVenue.getId(), "venueId"));

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

					dataChangeLogService.add(l, "EVENT", Constants.VENUE_LOG_KEY, editedVenue.getId(), Constants.LOG_ACTION_UPDATE, Venue.class.toString());

				}
			}
			else {

				l.addAll(eventService.getEventIdsBySelector(editedVenue.getId(), "venueId"));

				if (l.size() > 0) {

					dataChangeLogService.add(l, "EVENT", Constants.VENUE_LOG_KEY, editedVenue.getId(), Constants.LOG_ACTION_DELETE, Venue.class.toString());

					venueService.onVenueDelete(venue.getId());

				}
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(venueid, "Venue");

			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_VENUE, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String venueid, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a Venue");

		ResponseMessage response = new ResponseMessage();

		try {

			Venue venue = venueService.get(venueid, organizerId);

			if (venue != null) {

				venue.setIsDeleted(true);

				venueService.edit(venue);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Venue"));

				response.setRecordId(venueid);

				response.setDetails_url("");

				List<String> l = new ArrayList<String>();

				l.addAll(eventService.getEventIdsBySelector(venue.getId(), "venueId"));

				if (l.size() > 0) {

					dataChangeLogService.add(l, "EVENT", Constants.VENUE_LOG_KEY, venue.getId(), Constants.LOG_ACTION_DELETE, Venue.class.toString());

				}

				// venueService.onVenueDelete(venue.getId());

				this.onVenueDeletePostProcess(l);

			}
			else {

				throw new NotFoundException(venueid, "Venue");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(venueid, "Venue");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_VENUE, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Venue get(@PathVariable("id") String Id, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a Venue");

		Venue existingVenue = null;

		if (null != Id && !Id.equalsIgnoreCase("")) {

			existingVenue = venueService.get(Id, organizerId);

		}

		if (existingVenue == null) {

			throw new NotFoundException(Id, "Venue");
		}

		return existingVenue;
	}

	@RequestMapping(value = Urls.GET_VENUES, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Venue> getVenues(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all venues");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<Venue> venues = venueService.getAll(search, pageAble, organizerId);

		return venues;
	}

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;

	private void onVenueDeletePostProcess(List<String> list) {

		for (String eventId : list) {

			org.iqvis.nvolv3.domain.Event event = eventService.get(eventId);

			if (event != null && event.getActivities() != null) {

				System.out.println(event.getId() + "-----------------");

				for (Activity activity : event.getActivities()) {

					try {

						if (!activity.getIsDeleted()) {

							activity.setLocation(null);
							activity.setLocationDetails(null);
							activity.setTimeZoneOffSet(event.getTimeZone());
							activityService.edit(activity, eventId);
							System.out.println("Success + ++++++++++++++++++++++++");
						}
					}
					catch (NotFoundException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();

					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}

				}

			}

			try {

				event.setVenueId(null);

				event.setVenue(null);

				eventService.edit(event, eventId, event.getOrganizerId());

				List<String> l = new ArrayList<String>();

				l.add(event.getId());

				dataChangeLogService.add(l, "EVENT", "", "", Constants.LOG_ACTION_UPDATE, event.getClass().toString());
			}
			catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
