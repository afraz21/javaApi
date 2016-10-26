package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.iqvis.nvolv3.bean.EventTrack;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.EventTrackService;
import org.iqvis.nvolv3.service.TrackService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Urls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.EVENT_BASE_URL)
public class EventTrackController {

	@Resource(name = Constants.SERVICE_EVENT_TRACK)
	EventTrackService eventTrackService;

	@Resource(name = Constants.SERVICE_TRACK)
	TrackService trackService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@RequestMapping(value = Urls.UPDATE_EVENT + "/track", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(HttpServletRequest request, @RequestBody @Valid EventTrack eventTrack, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventId) throws Exception {

		ResponseMessage response = new ResponseMessage();

		try {

			eventTrack = eventTrackService.add(eventTrack, organizerId, eventId);

			response.setMessage("EventTrack added successfully");

			response.setRecordId(eventTrack.getTrackId());

			response.setRecord(trackService.get(eventTrack.getTrackId(), organizerId));

			List<String> l = new ArrayList<String>();

			l.add(eventId);

			dataChangeLogService.add(l, "EVENT", "Track", eventTrack.getTrackId(), Constants.LOG_ACTION_ADD, EventTrack.class.toString());

		}
		catch (NotFoundException e) {

			e.printStackTrace();

			throw new NotFoundException(eventId, "Track");

		}
		catch (Exception e) {

			e.printStackTrace();

			throw new Exception(e);
		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT + "/track", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(HttpServletRequest request, @RequestBody EventTrack eventTrack, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventId) throws Exception {

		ResponseMessage response = new ResponseMessage();
		try {
			eventTrack = eventTrackService.edit(eventTrack, organizerId, eventId);

			response.setMessage("EventTrack edited successfully");

			response.setRecordId(eventTrack.getTrackId());

			response.setRecord(trackService.get(eventTrack.getTrackId(), organizerId));

			List<String> l = new ArrayList<String>();

			l.add(eventId);

			dataChangeLogService.add(l, "EVENT", "Track", eventTrack.getTrackId(), Constants.LOG_ACTION_UPDATE, EventTrack.class.toString());

		}
		catch (NotFoundException e) {
			e.printStackTrace();

			throw new NotFoundException(eventId, "Track");
		}
		catch (Exception e) {
			e.printStackTrace();

			throw new Exception(e);

		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT + "/track/{trackId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage delete(@PathVariable("trackId") String trackId, HttpServletRequest request, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventId) throws Exception {

		ResponseMessage response = new ResponseMessage();

		EventTrack eventTrack = null;

		try {
			if (eventTrack == null)
				eventTrack = new EventTrack();

			eventTrack.setTrackId(trackId);

			eventTrack = eventTrackService.delete(eventTrack, organizerId, eventId);

			response.setMessage("EventTrack edited successfully");

			response.setRecordId(eventTrack.getTrackId());

			response.setRecord(trackService.get(eventTrack.getTrackId(), organizerId));

			List<String> l = new ArrayList<String>();
			l.add(eventId);
			dataChangeLogService.add(l, "EVENT", "Track", eventTrack.getTrackId(), Constants.LOG_ACTION_DELETE, EventTrack.class.toString());

		}
		catch (NotFoundException e) {

			e.printStackTrace();

			throw new NotFoundException(eventId, "Track");

		}
		catch (Exception e) {

			e.printStackTrace();

			throw new Exception(e);

		}

		return response;
	}

	@RequestMapping(value = Urls.UPDATE_EVENT + "/track/{trackId}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public org.iqvis.nvolv3.mobile.bean.EventTrack get(HttpServletRequest request, @PathVariable("trackId") String trackId, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventId) throws Exception {

		Track track = null;

		EventTrack eventTrack = null;

		try {

			org.iqvis.nvolv3.mobile.bean.EventTrack response = new org.iqvis.nvolv3.mobile.bean.EventTrack();

			eventTrack = eventTrackService.get(trackId, organizerId, eventId);

			track = eventTrackService.getEventTrack(trackId, organizerId, eventId);

			response.setColorCode(eventTrack.getColorCode());

			response.setCreatedBy(track.getCreatedBy());

			response.setCreatedDate(track.getCreatedDate());

			response.setId(track.getId());

			response.setIsDeleted(track.getIsDeleted());

			response.setLastModifiedBy(track.getLastModifiedBy());

			response.setLastModifiedDate(track.getLastModifiedDate());

			response.setMultiLingual(track.getMultiLingual());

			response.setName(track.getName());

			response.setOrganizerId(track.getOrganizerId());

			response.setPicture(track.getPictureO());

			response.setSortOrder(eventTrack.getSortOrder().toString());

			response.setVersion(track.getVersion());

			return response;

		}
		catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new NotFoundException(trackId, "Track");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new Exception(e);
		}
	}

	@RequestMapping(value = Urls.UPDATE_EVENT + "/track/list", method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Page<org.iqvis.nvolv3.mobile.bean.EventTrack> getlist(HttpServletRequest request, @RequestBody(required = false) @Valid Criteria search, @PathVariable("organizerId") String organizerId, @PathVariable("id") String eventId, @RequestParam(value = "type", required = false) String test) throws Exception, NotFoundException {

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {

			if (search.getQuery() != null) {

				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		if (test != null && !test.equals("")) {

			return eventTrackService.getEventTracks(search, pageAble, organizerId, eventId);

		}
		else {

			return eventTrackService.getEventSpecificTracks(search, pageAble, organizerId, eventId);
		}
	}
}
