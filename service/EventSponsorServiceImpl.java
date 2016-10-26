package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.EventSponsor;
import org.iqvis.nvolv3.dao.SponsorDao;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.lambdaj.Lambda;

@SuppressWarnings("restriction")
@Service("eventSponsorService")
@Transactional
public class EventSponsorServiceImpl implements EventSponsorService {

	protected static Logger logger = Logger.getLogger("Service");

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_SPONSOR)
	private SponsorService sponsorService;

	@Autowired
	private SponsorDao sponsorDao;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	public Sponsor addEventSponsor(EventSponsor eventSponsor, String eventId, String organizerId) throws NotFoundException, Exception {

		Event event = eventService.get(eventId, organizerId);

		List<EventSponsor> listSponsor = (event.getSponsors() == null ? new ArrayList<EventSponsor>() : event.getSponsors());

		EventSponsor eventS = null;

		try {

			eventS = Lambda.select(listSponsor, Lambda.having(Lambda.on(EventSponsor.class).getSponsorId(), Matchers.equalToIgnoringCase(eventSponsor.getSponsorId()))).get(0);
		}
		catch (Exception e) {

		}
		logger.debug("Sponsor Exist In Event");

		if (eventS == null) {
			listSponsor.add(eventSponsor);

			event.setSponsors(listSponsor);

			eventService.edit(event, eventId, organizerId);
		}

		Sponsor sponsor = sponsorService.get(eventSponsor.getSponsorId());

		List<String> l = new ArrayList<String>();
		l.add(eventId);
		dataChangeLogService.add(l, "EVENT", Constants.EVENT_SPONSOR_LOG_KEY, sponsor.getId(), "ADD", EventSponsor.class.toString());

		return sponsor;
	}

	public Sponsor editEventSponsor(EventSponsor eventSponsor, String eventId, String organizerId) throws NotFoundException, Exception {

		Event event = eventService.get(eventId, organizerId);

		List<EventSponsor> tempSponsor = new ArrayList<EventSponsor>();

		List<EventSponsor> listSponsor = (event.getSponsors() == null ? new ArrayList<EventSponsor>() : event.getSponsors());

		boolean flag = false;

		for (EventSponsor eventSponsor2 : listSponsor) {

			if (eventSponsor2.getSponsorId().equals(eventSponsor.getSponsorId())) {

				eventSponsor.setVersion(eventSponsor2.getVersion() + 1);

				tempSponsor.add(eventSponsor);

				flag = true;

			}

			else {

				tempSponsor.add(eventSponsor2);
			}
		}

		if (!flag) {
			throw new NotFoundException(eventSponsor.getSponsorId(), "EventSponsor");
		}

		event.setSponsors(tempSponsor);

		eventService.edit(event, eventId, organizerId);

		Sponsor sponsor = sponsorService.get(eventSponsor.getSponsorId());

		List<String> l = new ArrayList<String>();
		l.add(event.getId());
		dataChangeLogService.add(l, "EVENT", Constants.EVENT_SPONSOR_LOG_KEY, sponsor.getId(), "Update", EventSponsor.class.toString());

		return sponsor;
	}

	public Sponsor deleteEventSponsor(String sponsorId, String eventId, String organizerId) throws NotFoundException, Exception {

		Event event = eventService.get(eventId, organizerId);

		List<EventSponsor> tempSponsor = new ArrayList<EventSponsor>();

		List<EventSponsor> listSponsor = (event.getSponsors() == null ? new ArrayList<EventSponsor>() : event.getSponsors());

		boolean flag = false;

		for (EventSponsor eventSponsor2 : listSponsor) {
			if (eventSponsor2.getSponsorId().equals(sponsorId)) {

				flag = true;

			}
			else {

				tempSponsor.add(eventSponsor2);

			}
		}

		if (!flag) {
			throw new NotFoundException(sponsorId, "EventSponsor");
		}

		event.setSponsors(tempSponsor);

		eventService.edit(event, eventId, organizerId);

		Sponsor sponsor = sponsorService.get(sponsorId);

		List<String> l = new ArrayList<String>();
		l.add(event.getId());
		dataChangeLogService.add(l, "EVENT", Constants.EVENT_SPONSOR_LOG_KEY, sponsor.getId(), "delete", EventSponsor.class.toString());

		return sponsor;
	}

	public Sponsor getEventSponsor(String eventSponsorId, String eventId, String organizerId) throws NotFoundException, Exception {

		Event event = eventService.get(eventId, organizerId);

		Sponsor sponsor = null;

		List<EventSponsor> listSponsor = (event.getSponsors() == null ? new ArrayList<EventSponsor>() : event.getSponsors());
		try {
			EventSponsor eventS = Lambda.select(listSponsor, Lambda.having(Lambda.on(EventSponsor.class).getSponsorId(), Matchers.equalToIgnoringCase(eventSponsorId))).get(0);

			sponsor = sponsorService.get(eventS.getSponsorId());
		}
		catch (Exception e) {

		}
		return sponsor;

	}

	public Page<Sponsor> getAll(org.iqvis.nvolv3.search.Criteria search, HttpServletRequest request, Pageable pageAble, String organizerId, String eventId) {

		Event event = eventService.get(eventId, organizerId);

		List<Sponsor> list = sponsorService.getAll(Utils.parseCriteria(search, ""), organizerId);

		List<Sponsor> listTemp = new ArrayList<Sponsor>();

		if (event.getSponsors() != null) {

			for (EventSponsor sponsor : event.getSponsors()) {

				try {

					Sponsor s = Lambda.select(list, Lambda.having(Lambda.on(Sponsor.class).getId(), Matchers.equalTo(sponsor.getSponsorId()))).get(0);

					listTemp.add(s);

					System.out.println("Success Message " + sponsor.getSponsorId());
				}
				catch (Exception e) {
					System.out.println("Exception Message " + sponsor.getSponsorId());
				}
			}
		}

		try {

			if (pageAble.getPageSize() < listTemp.size()) {

				listTemp = listTemp.subList(pageAble.getOffset(), (pageAble.getOffset() + pageAble.getPageSize()) > listTemp.size() ? listTemp.size() : (pageAble.getOffset() + pageAble.getPageSize()));

			}

		}
		catch (Exception e) {

			listTemp = new ArrayList<Sponsor>();

		}

		System.out.println("Size of List " + listTemp.size());

		PageImpl<Sponsor> tempPage = new PageImpl<Sponsor>(listTemp, pageAble, event.getSponsors() == null ? 0 : event.getSponsors().size());

		return tempPage;

	}

}
