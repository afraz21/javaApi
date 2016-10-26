package org.iqvis.nvolv3.service;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.bean.EventSponsor;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventSponsorService {

	Sponsor addEventSponsor(EventSponsor eventSponsor, String eventId, String organizerId) throws NotFoundException, Exception;

	Sponsor editEventSponsor(EventSponsor eventSponsor, String eventId, String organizerId) throws NotFoundException, Exception;

	Sponsor deleteEventSponsor(String sponsorId, String eventId, String organizerId) throws NotFoundException, Exception;

	Sponsor getEventSponsor(String eventSponsorId, String eventId, String organizerId) throws NotFoundException, Exception;

	public Page<Sponsor> getAll(org.iqvis.nvolv3.search.Criteria search, HttpServletRequest request, Pageable pageAble, String organizerId, String eventId);

}
