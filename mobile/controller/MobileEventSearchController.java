package org.iqvis.nvolv3.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.EventSearch;
import org.iqvis.nvolv3.domain.Track;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.ListEvent;
import org.iqvis.nvolv3.mobile.service.MobileEventServiceImpl;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.TrackService;
import org.iqvis.nvolv3.utils.AppType;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("mobile/{appId}/partner/{partnerId}")
public class MobileEventSearchController {

	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	private MobileEventServiceImpl mobileEventServiceImpl;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_TRACK)
	private TrackService trackService;

	@RequestMapping(value = "/events", method = { RequestMethod.PUT })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	Page<ListEvent> searchEvent(@RequestBody(required = true) EventSearch search, HttpServletRequest httpRequest, @PathVariable("partnerId") String partnerId, @PathVariable("appId") String appId) throws Exception {

		Pageable pageAble = new PageRequest(0, search.getPageSize());

		if (search != null) {

			if (search.getPageNumber() != null && search.getPageSize() != null) {

				pageAble = new PageRequest(search.getPageNumber() - 1, search.getPageSize());

			}

		}

		search.setPartnerId(partnerId);

		search.setAppId(appId);

		AppConfiguration appConf = appConfiguration_Service.get(appId, partnerId);

		List<String> eventIds = new ArrayList<String>();

		List<String> organizers = new ArrayList<String>();

		if (AppType.GENERAL.toString().equals(appConf.getAppType())) {

			eventIds = eventService.get(Utils.getQueryForEventSearch(search), true);

			organizers = eventService.get(Utils.getQueryForEventSearch(search), false);
		}
		else if (AppType.WHITELABEL.toString().equals(appConf.getAppType())) {

			eventIds = eventService.get(Utils.getQueryForEventSearchOrganizer(search), true);

			organizers = eventService.get(Utils.getQueryForEventSearchOrganizer(search), false);
		}

		List<Track> tracks = new ArrayList<Track>();

		for (String organizerId : organizers) {

			List<Track> organizerTracks = trackService.getOrganizerTracks(organizerId);

			if (organizerTracks != null) {

				tracks.addAll(organizerTracks);
			}
		}

		List<ListEvent> eventList = mobileEventServiceImpl.getAllAppEvents(eventIds, tracks, search.getLanguage());

		eventList = eventList == null ? new ArrayList<ListEvent>() : eventList;

		List<ListEvent> sortedList = new ArrayList<ListEvent>();

		int i = 0;

		for (String eventId : eventIds) {

			for (ListEvent listEvent : eventList) {

				if (eventId.equals(listEvent.getId())) {

					listEvent.setSortOrder(i++);

					sortedList.add(listEvent);
				}

			}
		}

		int total = sortedList.size();

		if (search.getPageNumber() != null) {

			List<List<ListEvent>> pages = Utils.getPages(sortedList, search.getPageSize());

			if ((search.getPageNumber() - 1) < pages.size()) {

				sortedList = pages.get((search.getPageNumber() - 1));
			}
			else {

				sortedList = new ArrayList<ListEvent>();
			}

		}

		return new PageImpl<ListEvent>(sortedList, pageAble, total);

	}

}
