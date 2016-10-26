package org.iqvis.nvolv3.mobile.service;

import java.util.List;

import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.Event;
import org.iqvis.nvolv3.mobile.bean.EventTrack;
import org.iqvis.nvolv3.mobile.bean.EventVendor;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Service for processing {@link Event} objects. Uses Spring's
 * {@link MongoTemplate} to perform CRUD operations.
 * <p>
 * For a complete reference to MongoDB see http://www.mongodb.org/
 * <p>
 * For a complete reference to Spring Data MongoDB see
 * http://www.springsource.org/spring-data
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public interface MobileEventService {

	public List<String> getCampaignsOnly(String organizerId, String eventId);

	List<Personnel> getEventPersonnelList(String eventId, String organizerId, org.iqvis.nvolv3.domain.Event event, String code, boolean keynote) throws NotFoundException;

	List<Personnel> getEventPersonnelList(String eventId, String organizerId, String code, boolean keynote) throws NotFoundException;

	/**
	 * Retrieves a event(s) search/criteria based
	 * 
	 * @throws Exception
	 */
	public Page<Event> getAll(Criteria eventSearch, String code, Pageable pageAble, String organizerId) throws NotFoundException, Exception;

	/**
	 * Retrieves a single Event
	 * 
	 * @throws Exception
	 */
	public Event get(String id, String organizerId, String code) throws NotFoundException, Exception;

	public boolean validateEventAssociationWithOrganizer(String organizerId, String eventId);

	public List<EventTrack> getEventTracks(String eventId, String organizerId, org.iqvis.nvolv3.domain.Event event, List<Track> tracks, String code);

	public List<EventTrack> getEventTracks(String eventId, String organizerId, String code);

	public List<Personnel> getEventPersonnels(String eventId, String organizerId, org.iqvis.nvolv3.domain.Event event, String code);

	public List<Personnel> getEventPersonnels(String eventId, String organizerId, String code);

	public List<EventVendor> getEventVendors(String eventId, String organizerId, String code);

	public List<org.iqvis.nvolv3.mobile.bean.EventSponsor> getEventSponsors(String eventId, String organizerId, String code);

	public Page<Event> getAllAppEvents(Pageable pageAble, List<String> eventIds, String langCode) throws NotFoundException, Exception;

	public List<org.iqvis.nvolv3.mobile.bean.Activity> getEventActivities(String eventId, String organizerId, String code);

}
