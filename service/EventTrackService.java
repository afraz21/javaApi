package org.iqvis.nvolv3.service;

import org.iqvis.nvolv3.bean.EventTrack;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventTrackService {

	EventTrack add(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception;

	EventTrack get(String eventTrack, String organizerId, String eventId) throws NotFoundException, Exception;

	EventTrack edit(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception;

	EventTrack delete(EventTrack eventTrack, String organizerId, String eventId) throws NotFoundException, Exception;

	Track getEventTrack(String eventTrack, String organizerId, String eventId) throws NotFoundException, Exception;

	Page<org.iqvis.nvolv3.mobile.bean.EventTrack> getEventTracks(org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId, String eventId);

	Page<org.iqvis.nvolv3.mobile.bean.EventTrack> getEventSpecificTracks(org.iqvis.nvolv3.search.Criteria search, Pageable pageAble, String organizerId, String eventId);

}
