package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.EventTrack;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.TrackService;
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
@RequestMapping(Urls.TRACK_BASE_URL)
public class TrackController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_TRACK)
	private TrackService trackService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_EVENT)
	EventService eventService;

	@RequestMapping(value = Urls.ADD_TRACK, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addTrack(@RequestBody @Valid Track track, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add track");

		ResponseMessage response = new ResponseMessage();

		try {

			track.setOrganizerId(organizerId);

			Track addedTrack = trackService.add(track);

			addedTrack = trackService.get(addedTrack.getId(), organizerId);

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Track"));

			response.setRecordId(addedTrack.getId().toString());

			response.setRecord(addedTrack);

			response.setDetails_url(trackService.getTrackDetailUrl(addedTrack, request));

			logger.debug("Track has been added successfully");

		}
		catch (Exception e) {

			logger.debug("Exception while adding track", e);

			if (e.getClass().equals(NotFoundException.class)) {

				throw new NotFoundException(track.getOrganizerId(), "Organizer");

			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_TRACK, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editEvent(@RequestBody @Valid Track track, @PathVariable("id") String trackid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a track");

		ResponseMessage response = new ResponseMessage();

		track.setId(trackid);

		try {
			track.setOrganizerId(organizerId);

			Track editedTrack = trackService.edit(track);

			editedTrack = trackService.get(editedTrack.getId(), organizerId);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Track"));

			response.setRecordId(editedTrack.getId().toString());

			response.setRecord(editedTrack);

			response.setDetails_url(trackService.getTrackDetailUrl(editedTrack, request));

			// Logging Process Bellow

			List<String> l = new ArrayList<String>();

			l.addAll(eventService.getEventIds(editedTrack.getId()));

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

				dataChangeLogService.add(l, "EVENT", "Track", editedTrack.getId(), "Update", EventTrack.class.toString());

			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(trackid, "Track");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_TRACK, method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage editEvent(@PathVariable("id") String trackid, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit a track");

		ResponseMessage response = new ResponseMessage();

		try {

			Track track = trackService.get(trackid, organizerId);

			if (track != null) {

				track.setIsDeleted(true);

				trackService.edit(track);

				// trackService.delete(trackid);

				response.setMessage(String.format(Messages.DELETED_SUCCESS_MESSAGE, "Track"));

				response.setRecordId(trackid);

				response.setDetails_url("");

				List<String> l = new ArrayList<String>();

				l.addAll(eventService.getEventIds(track.getId()));

				if (l.size() > 0) {
					dataChangeLogService.add(l, "EVENT", "Track", track.getId(), "delete", EventTrack.class.toString());
				}

			}
			else {

				throw new NotFoundException(trackid, "Track");
			}

		}
		catch (Exception e) {

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(trackid, "Track");
			}

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.GET_TRACK, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Track getTrack(@PathVariable("id") String trackId, @PathVariable("organizerId") String organizerId, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch a track");

		Track existingTrack = null;

		if (null != trackId && !trackId.equalsIgnoreCase("")) {

			existingTrack = trackService.get(trackId, organizerId);

		}

		if (existingTrack == null) {

			throw new NotFoundException(trackId, "Track");
		}

		return existingTrack;
	}

	@RequestMapping(value = Urls.GET_TRACKS, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// public Page<Track> getTracks(@RequestBody(required = false) @Valid
	// Criteria search, @PathVariable(value = "organizerId") String organizerId,
	// @PathVariable("id") String eventId, Model model, HttpServletRequest
	// request) {
	public Page<Track> getTracks(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all tracks");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		// Page<Track> tracks = trackService.getAll(search, pageAble,
		// organizerId, eventId);
		Page<Track> tracks = trackService.getAll(search, pageAble, organizerId);
		return tracks;
	}

}
