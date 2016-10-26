package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Venue;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.ActivityService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.LocationService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.LOCATION_BASE_URL)
public class LocationController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_LOCATION)
	private LocationService locationService;

	@Resource(name = Constants.SERVICE_VENUE)
	private VenueService venueService;

	@RequestMapping(value = Urls.ADD_LOCATION, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestBody @Valid Location location, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add location");

		ResponseMessage response = new ResponseMessage();

		try {

			if (venueService.get(location.getVenueId(), organizerId) != null) {

				Location addedLocation = locationService.add(location);

				addedLocation = locationService.get(addedLocation.getId(), organizerId);

				response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Location"));

				response.setRecordId(addedLocation.getId().toString());

				response.setDetails_url(locationService.getLocationDetailUrl(addedLocation, organizerId, request));

				response.setRecord(addedLocation);

				this.loggingProcess(addedLocation.getId(), addedLocation, false);

				logger.debug(String.format(Messages.ADD_SUCCESS_MESSAGE, "Location"));

			}
			else {

				throw new NotFoundException(location.getVenueId(), "Venue");
			}

		}
		catch (Exception e) {

			logger.debug("Exception while adding location", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(location.getVenueId(), "Venue");

			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_LOCATION, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody @Valid Location location, @PathVariable("id") String locationid, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a location");

		ResponseMessage response = new ResponseMessage();

		location.setId(locationid);

		try {

			Location editedLocation = locationService.edit(location, organizerId);

			editedLocation = locationService.get(editedLocation.getId(), organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Location"));

			response.setRecordId(editedLocation.getId().toString());

			response.setRecord(editedLocation);

			response.setDetails_url(locationService.getLocationDetailUrl(editedLocation, organizerId, request));

			this.loggingProcess(locationid, editedLocation, false);

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(locationid, "Location");

			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_LOCATION, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("id") String locationid, Model model, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a Location");

		ResponseMessage response = new ResponseMessage();

		try {

			Location location = new Location();

			location.setIsDeleted(true);

			location.setId(locationid);

			Location editedLocation = locationService.edit(location, organizerId);

			response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Location"));

			response.setRecordId(locationid);

			response.setRecord(editedLocation);

			response.setDetails_url(request.getRequestURL().toString());

			this.loggingProcess(locationid, editedLocation, true);

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(locationid, "Location");

			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_LOCATION, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Location get(@PathVariable("id") String Id, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a Location");

		Location existingLocation = null;

		if (null != Id && !StringUtils.isEmpty(Id)) {

			existingLocation = locationService.get(Id, organizerId);
		}

		if (existingLocation == null) {

			throw new NotFoundException(Id, "Location");
		}

		return existingLocation;
	}

	@RequestMapping(value = Urls.GET_LOCATIONS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Location> getLocations(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all Locations");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<Location> locations = locationService.getAll(search, pageAble, organizerId);

		return locations;
	}

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;

	private void onLocationDeletePostProcess(List<String> list, String locationId, boolean isDelete) {

		for (String eventId : list) {

			org.iqvis.nvolv3.domain.Event event = eventService.get(eventId);

			if (event != null && event.getActivities() != null) {

				for (Activity activity : event.getActivities()) {

					try {

						if (!activity.getIsDeleted()) {

							if (activity.getLocation() != null && activity.getLocation().equals(locationId)) {
								if (isDelete) {
									activity.setLocation(null);

									activity.setLocationDetails(null);
								}
								activityService.edit(activity, eventId);
							}

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

		}
	}

	private void loggingProcess(String locationid, Location location, boolean isDelete) {
		List<String> l = new ArrayList<String>();

		List<String> eventList = eventService.getEventIdsBySelector(location.getVenueId(), "venueId");

		if (eventList != null) {

			l.addAll(eventList);

			if (l.size() > 0) {

				dataChangeLogService.add(l, "EVENT", Constants.VENUE_LOG_KEY, location.getVenueId(), Constants.LOG_ACTION_UPDATE, Venue.class.toString());

				this.onLocationDeletePostProcess(l, locationid, isDelete);

			}
		}
	}

}
