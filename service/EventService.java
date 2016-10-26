package org.iqvis.nvolv3.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.EventPersonnel;
import org.iqvis.nvolv3.bean.EventSpeakers;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventSelective;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.domain.UpdateEventPersonnelList;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.EventTrack;
import org.iqvis.nvolv3.search.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

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

public interface EventService {

	public List<String> getEventIdsBySelector(String trackId, String selector);

	public List<String> getEventIdsByQuestionnair(String questionairId);

	public List<String> getEventIds(String trackId);

	/**
	 * Retrieves a event(s) search/criteria based
	 */
	public Page<Event> getAll(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String organizerId);

	/**
	 * Retrieves a single Event
	 */
	public Event get(String id, String organizerId);

	/**
	 * Adds a new Event
	 */
	public Event add(Event event) throws Exception;

	/**
	 * Deletes an existing Event
	 */
	public Boolean delete(String id);

	/**
	 * Edits an existing Event
	 */
	public Event edit(Event event, String eventId, String organizerId) throws Exception, NotFoundException;

	/**
	 * Gets detail url of event
	 */

	public String getEventDetailUrl(Event event, HttpServletRequest request);

	/**
	 * 
	 * @param organizerId
	 * @param eventId
	 * @return
	 */
	public boolean validateEventAssociationWithOrganizer(String organizerId, String eventId);

	/**
	 * Retrieves a All event(s)
	 */
	public Page<Event> getAllEvents(HttpServletRequest request, Pageable pageAble, String organizerId);

	/**
	 * Retrieves a All event(s) Speakers
	 */
	public Page<EventSpeakers> getAllEventsSpeakers(HttpServletRequest request, Pageable pageAble, String organizerId, String code);

	/**
	 * Retrieves a All event Speakers
	 */
	public EventSpeakers getEventSpeakers(HttpServletRequest request, String eventId, String organizerId, String code);

	/**
	 * 
	 * @param eventId
	 * @param organizerId
	 * @return
	 */

	public List<org.iqvis.nvolv3.mobile.bean.EventSponsor> getEventSponsors(String eventId, String organizerId);

	/**
	 * @param eventId
	 * @param organizerId
	 * @return
	 */

	public List<EventTrack> getEventTracks(String eventId, String organizerId) throws NotFoundException;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Event get(String id);

	/**
	 * 
	 * @param eventSearch
	 * @param request
	 * @param pageAble
	 * @param organizerId
	 * @return
	 */
	public Page<org.iqvis.nvolv3.bean.Personnel> getEventPersonnels(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String organizerId, String eventId);

	/**
	 * 
	 * @param eventPersonnel
	 * @param eventId
	 * @param organizerId
	 * @return
	 * @throws Exception
	 */
	public EventPersonnel add(EventPersonnel eventPersonnel, String eventId, String organizerId) throws Exception;

	/**
	 * 
	 * @param eventPersonnel
	 * @param eventId
	 * @param organizerId
	 * @return
	 * @throws Exception
	 */
	public EventPersonnel edit(EventPersonnel eventPersonnel, String eventId, String organizerId) throws Exception;

	/**
	 * '
	 * 
	 * @param eventPersonnelId
	 * @param eventId
	 * @param organizerId
	 * @return
	 * @throws Exception
	 */
	public String delete(String eventPersonnelId, String eventId, String organizerId) throws Exception;

	/**
	 * 
	 * @param eventPersonnelId
	 * @param eventId
	 * @param organizerId
	 * @return
	 * @throws Exception
	 */

	public org.iqvis.nvolv3.bean.Personnel get(String eventPersonnelId, String eventId, String organizerId) throws Exception;

	public org.iqvis.nvolv3.bean.Personnel get(EventPersonnel ep,Personnel personnel ,String eventId, String organizerId) throws Exception;
	/**
	 * 
	 * @param pId
	 * @param eventId
	 * @return
	 * @throws Exception
	 */

	public List<Activity> getPersonnelsActvities(String pId, String eventId) throws Exception;

	public void test();

	public List<String> get(Query query, boolean isIds);

	public Page<Event> geAppEvents(String appId, Criteria eventSearch, Pageable pageAble);

	public List<Event> getEvents(List<String> ids);
	
	public List<EventSelective> getActiveEvents();
	
	public List<EventPersonnel> getEventPersonnels(String id, String eventId, String organizerId) throws NotFoundException ;
	
	public List<EventPersonnel> updateBulk(UpdateEventPersonnelList eventPersonnelPostList, String eventId, String organizerId) throws Exception;
	
	

}
