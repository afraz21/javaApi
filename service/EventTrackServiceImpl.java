package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.EventTrack;
import org.iqvis.nvolv3.dao.EventDao;
import org.iqvis.nvolv3.dao.EventTrackDao;
import org.iqvis.nvolv3.dao.TracksDao;
import org.iqvis.nvolv3.dao.UserDao;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.domain.User;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.lambdaj.Lambda;

@Service(Constants.SERVICE_EVENT_TRACK)
@Transactional
public class EventTrackServiceImpl implements EventTrackService {

	@Autowired
	private EventTrackDao eventTrackDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private TracksDao trackDao;

	@Autowired
	private EventDao eventDao;

	protected static Logger logger = Logger.getLogger("service");

	public EventTrack add(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception {
		// TODO Auto-generated method stub

		return eventTrackDao.add(eventTrack, organizerId, eventId);

	}

	public EventTrack get(String eventTrack, String organizerId, String eventId) throws NotFoundException, Exception {
		// TODO Auto-generated method stub

		return eventTrackDao.get(eventTrack, organizerId, eventId);

	}

	public EventTrack edit(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception {
		// TODO Auto-generated method stub
		return eventTrackDao.edit(eventTrack, organizerId, eventId);
	}

	public EventTrack delete(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception {
		// TODO Auto-generated method stub
		return eventTrackDao.delete(eventTrack, organizerId, eventId);

	}

	public Track getEventTrack(String eventTrack, String organizerId, String eventId) throws NotFoundException, Exception {

		User user = userDao.get(organizerId);

		java.util.List<Track> list = user.getTracks();

		for (Track track : list) {

			if (new String(track.getId()).equals(eventTrack)) {

				return track;

			}

		}

		throw new NotFoundException(eventTrack, "EventTrack");
	}

	public Page<org.iqvis.nvolv3.mobile.bean.EventTrack> getEventTracks(org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId, String eventId) {

		Page<Track> page = trackDao.getAll(Utils.parseQuery(search, "tracks."), search, pageAble, organizerId);

		List<Track> list = page.getContent();

		Event event = eventDao.get(eventId, organizerId);

		List<EventTrack> trackList = event.getTracks();

		List<org.iqvis.nvolv3.mobile.bean.EventTrack> finalList = new ArrayList<org.iqvis.nvolv3.mobile.bean.EventTrack>();

		for (Track track : list) {

			// if (track.getIsDeleted() == false) {

			EventTrack etrack;

			try {

				etrack = Lambda.select(trackList, Lambda.having(Lambda.on(EventTrack.class).getTrackId(), Matchers.equalToIgnoringCase(track.getId()))).get(0);

				logger.debug("Organizer Track : " + track.getId() + " is associated with event : " + eventId);

			}
			catch (Exception e) {

				logger.debug("Organizer Track : " + track.getId() + " not associated with event : " + eventId);

				etrack = null;
			}

			if (etrack != null) {

				org.iqvis.nvolv3.mobile.bean.EventTrack temp = new org.iqvis.nvolv3.mobile.bean.EventTrack();

				temp.setColorCode(etrack.getColorCode());

				temp.setId(etrack.getTrackId());

				temp.setSortOrder(etrack.getSortOrder().toString());

				temp.setCreatedBy(track.getCreatedBy());

				temp.setCreatedDate(track.getCreatedDate());

				temp.setIsDeleted(track.getIsDeleted());

				temp.setLastModifiedBy(track.getLastModifiedBy());

				temp.setLastModifiedDate(track.getLastModifiedDate());

				temp.setMultiLingual(track.getMultiLingual());

				temp.setName(track.getName());

				temp.setOrganizerId(track.getOrganizerId());

				temp.setPicture(track.getPictureO());

				temp.setVersion(track.getVersion());
				
				temp.setActivityCount(trackDao.countTrackActivities(eventId, track.getId()));

				finalList.add(temp);

			}

			// }
		}
		return new PageImpl<org.iqvis.nvolv3.mobile.bean.EventTrack>(finalList, pageAble, page.getTotalElements());

	}

	public PageImpl<org.iqvis.nvolv3.mobile.bean.EventTrack> getEventSpecificTracks(org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId, String eventId) {

		Page<EventTrack> page = trackDao.getAllEventTracks(Utils.parseQuery(search, "tracks."), search, pageAble, organizerId, eventId);

		// Page<Track> tPage = trackDao.getAll(Utils.parseQuery(search,
		// "tracks."), search, pageAble, organizerId);

		List<EventTrack> list = page.getContent();

		// List<Track> tList = tPage.getContent();

		/*
		 * Event event = eventDao.get(eventId, organizerId);
		 * 
		 * //List<EventTrack> trackList = event.getTracks();
		 */
		List<org.iqvis.nvolv3.mobile.bean.EventTrack> finalList = new ArrayList<org.iqvis.nvolv3.mobile.bean.EventTrack>();

		for (EventTrack et : list) {

			Track track = null;
			try {
				track = trackDao.get(et.getTrackId(), organizerId);

				// if (track.getIsDeleted() == false) {
				org.iqvis.nvolv3.mobile.bean.EventTrack temp = new org.iqvis.nvolv3.mobile.bean.EventTrack();

				temp.setColorCode(et.getColorCode());

				temp.setId(et.getTrackId());

				temp.setSortOrder(et.getSortOrder().toString());

				temp.setCreatedBy(track.getCreatedBy());

				temp.setCreatedDate(track.getCreatedDate());

				temp.setIsDeleted(track.getIsDeleted());

				temp.setLastModifiedBy(track.getLastModifiedBy());

				temp.setLastModifiedDate(track.getLastModifiedDate());

				temp.setMultiLingual(track.getMultiLingual());

				temp.setName(track.getName());

				temp.setOrganizerId(track.getOrganizerId());

				temp.setPicture(track.getPictureO());

				temp.setVersion(track.getVersion());
				
				temp.setActivityCount(trackDao.countTrackActivities(eventId, track.getId()));

				finalList.add(temp);
			}
			// }
			catch (NotFoundException e) {

				e.printStackTrace();
			}

		}

		return new PageImpl<org.iqvis.nvolv3.mobile.bean.EventTrack>(finalList, pageAble, page.getTotalElements());

	}

}
