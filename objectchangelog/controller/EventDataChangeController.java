package org.iqvis.nvolv3.objectchangelog.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.controller.FeedController;
import org.iqvis.nvolv3.dao.ThemeDao;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.MobileEventFeed;
import org.iqvis.nvolv3.mobile.bean.MobileEventFeedComment;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.iqvis.nvolv3.mobile.service.MobileEventService;
import org.iqvis.nvolv3.objectchangelog.domain.ChangeTrackLog;
import org.iqvis.nvolv3.objectchangelog.domain.EventUpdateResponse;
import org.iqvis.nvolv3.objectchangelog.domain.FeedQuery;
import org.iqvis.nvolv3.objectchangelog.domain.LogQueryObject;
import org.iqvis.nvolv3.objectchangelog.domain.MobileReferenceData;
import org.iqvis.nvolv3.objectchangelog.domain.OldCommentListObject;
import org.iqvis.nvolv3.objectchangelog.domain.SyncDataObject;
import org.iqvis.nvolv3.objectchangelog.response.bean.Event;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.service.EventAlertService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.ReferenceDataService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
public class EventDataChangeController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	AppConfigService appConfigService;

	@Resource(name = Constants.SERVICE_REFERENCE_DATA)
	ReferenceDataService referenceDataService;

	@Resource(name = Constants.SERVICE_EVENT_ALERT)
	private EventAlertService eventAlertService;

	@Autowired
	FeedController feedController;

	@Resource(name = Constants.MOBILE_SERVICE_EVENT)
	private MobileEventService mobileEventService;

	@Autowired
	private ThemeDao themeDao;

	@RequestMapping(value = "mobile/{appId}/organizer/{organizerId}/attendee/sync/", method = { RequestMethod.PUT, RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody EventUpdateResponse getAttendeeChanges(@RequestBody @Valid LogQueryObject query, @PathVariable("appId") String appId, HttpServletRequest request) throws Exception {

		ChangeTrackLog eventAttendees = dataChangeLogService.getLogListForAttendee(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime());

		return new EventUpdateResponse(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, eventAttendees);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(value = "mobile/{appId}/organizer/{organizerId}/events/sync/{code}", method = { RequestMethod.PUT, RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody EventUpdateResponse getEventChanges(@RequestBody @Valid LogQueryObject query, @PathVariable("code") String code, @PathVariable("appId") String appId, HttpServletRequest request) throws Exception {
		org.iqvis.nvolv3.domain.Event existingEvent = null;

		org.iqvis.nvolv3.domain.Event temEv = eventService.get(query.getEventId(), query.getOrganizerId());

		Event responseEvent = null;

		boolean flag = true;

		AppConfiguration appConfig = appConfigService.get(appId, query.getOrganizerId());

		String oldCode = code;

		String eventDefault = Constants.APPLICATION_DEFAULT_LANGUAGE;

		if (temEv != null && Utils.toEventConfiguration(temEv.getEventConfiguration()) != null && Utils.toEventConfiguration(temEv.getEventConfiguration()).getDefault_lang() != null) {

			eventDefault = Utils.toEventConfiguration(temEv.getEventConfiguration()).getDefault_lang().getKey();

		}

		if (code == null && code == "") {
			// org.iqvis.nvolv3.domain.Event ev =
			// eventService.get(query.getEventId(), query.getOrganizerId());

			org.iqvis.nvolv3.domain.Event ev = temEv;

			if (ev != null && Utils.toEventConfiguration(ev.getEventConfiguration()) != null && Utils.toEventConfiguration(ev.getEventConfiguration()).getDefault_lang() != null) {

				code = Utils.toEventConfiguration(ev.getEventConfiguration()).getDefault_lang().getKey();

			}
		}

		// org.iqvis.nvolv3.domain.Event evTemp =
		// eventService.get(query.getEventId(), query.getOrganizerId());

		org.iqvis.nvolv3.domain.Event evTemp = temEv;

		if (evTemp.getMultiLingual() != null) {
			List<MultiLingual> mList = Utils.getMultiLingualByLangCode(evTemp.getMultiLingual(), code);

			if (mList.size() <= 0) {
				if (evTemp != null && Utils.toEventConfiguration(evTemp.getEventConfiguration()) != null && Utils.toEventConfiguration(evTemp.getEventConfiguration()).getDefault_lang() != null) {

					code = Utils.toEventConfiguration(evTemp.getEventConfiguration()).getDefault_lang().getKey();

				}
			}

		}

		if (appConfig != null) {

			for (String event : appConfig.getEvents()) {

				if (event.equals(query.getEventId())) {

					flag = false;

					break;

				}
			}

			if (flag) {
				return new EventUpdateResponse(null);
			}

		}
		if (dataChangeLogService.isChanged(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime())) {

			// existingEvent = eventService.get(query.getEventId(),
			// query.getOrganizerId());

			existingEvent = temEv;

			if (existingEvent != null) {

				existingEvent.setMultiLingual(Utils.getMultiLingualByLangCode(existingEvent.getMultiLingual(), code));

				responseEvent = new Event(existingEvent);

			}

		}

		// org.iqvis.nvolv3.mobile.bean.Event mobileEvent =
		// mobileEventService.get(query.getEventId(), query.getOrganizerId(),
		// code);

		Object eventConfiguration = null;

		boolean flagGoAlways = false;

		if (dataChangeLogService.isChangedConfig(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime()) || flagGoAlways) {
			// eventConfiguration = mobileEvent.getEventConfiguration();

			eventConfiguration = temEv.getEventConfiguration();

			Object theme = null;

			String themeId = Utils.getThemeId(eventConfiguration);

			if (themeId != null && themeId != "") {

				theme = themeDao.get(themeId);

			}

			eventConfiguration = Utils.filterForEventConfiguration(eventConfiguration, code, theme);

		}

		List<String> camps = mobileEventService.getCampaignsOnly(temEv.getOrganizerId(), temEv.getId());

		ChangeTrackLog trackLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.TRACK_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog sponsorLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.EVENT_SPONSOR_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog vendorLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.VENDOR_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog personnelLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.EVENT_PERSONNEL_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog activityLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.ACTIVITY_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog mapsLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.EVENT_RESOURCE_MAP_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog resourcesLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.EVENT_OTHER_RESOURCE_MAP_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog venueLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.VENUE_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog campsLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.EVENT_CAMPAIGN__LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog feedsLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.FEED_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog alertsLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.EVENT_ALERT_LOG_KEY, oldCode, eventDefault);

		ChangeTrackLog userFeedBackQuestionniarLog = dataChangeLogService.getTracksLogList(query.getEventId(), query.getOrganizerId(), query.getSyncDateTime(), Constants.LOG_USER_FEEDBACK_QUESTION, oldCode, eventDefault);

		ReferenceData rfd = referenceDataService.get(Constants.LOG_UTLITY_KEY_PERSONNEL_TYPE, query.getOrganizerId());

		// org.iqvis.nvolv3.mobile.bean.Event existingMobileEvent =
		// mobileEventService.get(query.getEventId(), query.getOrganizerId(),
		// code);

		// List<EventAlert> tempFeedList =
		// eventAlertService.getListEventAlertsByEventId(query.getEventId(),
		// query.getOrganizerId());
		//
		// List<Object> temp = new ArrayList<Object>();
		//
		// if (temp != null) {
		//
		// for (EventAlert eventAlert : tempFeedList) {
		//
		// MobileEventAlert t = new MobileEventAlert(eventAlert);
		//
		// t.setGmtTime(eventAlert.getAlertScheduledTime());
		//
		// if (DateTime.now(DateTimeZone.UTC).compareTo(t.getStartDate()) >= 0
		// &&
		// DateTime.now(DateTimeZone.UTC).compareTo(eventAlert.getExpiryTimeGMT())
		// < 0) {
		//
		// temp.add(t);
		//
		// }
		//
		// }
		// }

		// ChangeTrackLog alertsTempraryCase = new ChangeTrackLog(null, temp,
		// alertsLog.getDelete(), null, null);

		Object personnelType = null;
		if (rfd != null) {

			if (null != rfd.getData()) {

				List<MobileReferenceData> tempL = new ArrayList<MobileReferenceData>();

				for (Data data : rfd.getData()) {

					MobileReferenceData t = new MobileReferenceData();

					t.setId(data.getId());

					if (data.getMultiLingual() != null) {

						t.setMultilingual(Utils.getMultiLingualByLangCode(data.getMultiLingual(), oldCode, eventDefault));

					}

					t.setSortOrder(data.getSortOrder());

					tempL.add(t);

				}

				personnelType = tempL;
			}
		}

		ChangeTrackLog personnelTypeTemp = new ChangeTrackLog(null, (List<Object>) personnelType, null);

		rfd = referenceDataService.get(Constants.LOG_UTLITY_KEY_VENDOR_BUSINESS_CATEGORY, query.getOrganizerId());

		Object vendorType = null;

		if (rfd != null) {

			if (null != rfd.getData()) {

				List<MobileReferenceData> tempL = new ArrayList<MobileReferenceData>();

				for (Data data : rfd.getData()) {

					MobileReferenceData t = new MobileReferenceData();

					t.setId(data.getId());

					if (data.getMultiLingual() != null) {

						t.setMultilingual(Utils.getMultiLingualByLangCode(data.getMultiLingual(), oldCode, eventDefault));

					}

					t.setSortOrder(data.getSortOrder());

					tempL.add(t);
				}
				vendorType = tempL;
			}
		}

		ChangeTrackLog vendorTypeTemp = new ChangeTrackLog(null, (List<Object>) vendorType, null);

		rfd = referenceDataService.get(Constants.LOG_UTLITY_KEY_SPONSOR_TYPE, query.getOrganizerId());

		Object sponsorType = null;

		if (rfd != null) {

			if (null != rfd.getData()) {

				List<MobileReferenceData> tempL = new ArrayList<MobileReferenceData>();

				for (Data data : rfd.getData()) {

					MobileReferenceData t = new MobileReferenceData();

					t.setId(data.getId());

					if (data.getMultiLingual() != null) {

						t.setMultilingual(Utils.getMultiLingualByLangCode(data.getMultiLingual(), oldCode, eventDefault));

					}

					t.setSortOrder(data.getSortOrder());

					tempL.add(t);

				}

				sponsorType = tempL;
			}
		}

		ChangeTrackLog sponsorTypeTemp = new ChangeTrackLog(null, (List<Object>) sponsorType, null);

		// rfd = referenceDataService.get(Constants.,
		// existingEvent.getOrganizerId());
		//
		// Object activityType = null;
		//
		// if (null != rfd.getData()) {
		// List<MobileLocation> tempL = new ArrayList<MobileLocation>();
		// for (Data data : rfd.getData()) {
		// MobileLocation t = new MobileLocation();
		// t.setId(data.getId());
		// t.setMultilingual(data.getMultiLingual());
		// tempL.add(t);
		// }
		// activityType = tempL;
		// }

		if (responseEvent != null && responseEvent.getMultiLingual() != null) {

			responseEvent.setMultiLingual(Utils.getMultiLingualByLangCode(responseEvent.getMultiLingual(), oldCode, eventDefault));

		}

		/**
		 * Put "alertsLog" in place of "alertsTempraryCase" when uncomment above
		 * line of code with fetch logging of alerts
		 * **/

		return new EventUpdateResponse(code, responseEvent, trackLog, sponsorLog, vendorLog, personnelLog, activityLog, personnelTypeTemp, sponsorTypeTemp, vendorTypeTemp, mapsLog, resourcesLog, venueLog, campsLog, feedsLog, camps, eventConfiguration, alertsLog, userFeedBackQuestionniarLog);
		//return new EventUpdateResponse(code, responseEvent, trackLog, sponsorLog, vendorLog, personnelLog, activityLog, personnelTypeTemp, sponsorTypeTemp, vendorTypeTemp, mapsLog, resourcesLog, venueLog, campsLog, feedsLog, null, eventConfiguration, alertsLog, userFeedBackQuestionniarLog);

	}

	/*
	 * // @RequestMapping(value = "/loadmore/{appId}/{code}", method = { //
	 * RequestMethod.PUT, RequestMethod.GET })
	 * 
	 * @RequestMapping(value =
	 * "mobile/{appId}/organizer/{organizerId}/events/sync_comments_load/{code}"
	 * , method = { RequestMethod.PUT, RequestMethod.GET })
	 * 
	 * @ResponseStatus(HttpStatus.OK) public @ResponseBody CommentRefreshData
	 * loadMore(@RequestBody @Valid LogQueryObject query, @PathVariable("code")
	 * String code, @PathVariable("appId") String appId, HttpServletRequest
	 * request) throws Exception {
	 * 
	 * Feed feed = feedController.get(query.getFeedId(), null, null);
	 * 
	 * MobileEventFeed mobilefeed = new MobileEventFeed();
	 * 
	 * mobilefeed.setCommentsCount(feed.getCommentCount());
	 * 
	 * mobilefeed.setLogo(new Picture(feed.getPicture()));
	 * 
	 * mobilefeed.setLikes(feed.getLikes());
	 * 
	 * mobilefeed.setCreatedDate(feed.getCreatedDate());
	 * 
	 * mobilefeed.setEventId(feed.getEventId());
	 * 
	 * mobilefeed.setId(feed.getId());
	 * 
	 * mobilefeed.setType(feed.getType());
	 * 
	 * mobilefeed.setText(feed.getTitle());
	 * 
	 * mobilefeed.setLastModifiedDate(feed.getLastModifiedDate());
	 * 
	 * return new CommentRefreshData(mobilefeed,
	 * dataChangeLogService.loadMore(query.getFeedId(), query.getEventId(),
	 * query.getOrganizerId(), query.getSyncDateTime()));
	 * 
	 * }
	 */

	// / Added by naveed

	@SuppressWarnings("unused")
	@RequestMapping(value = "mobile/{appId}/organizer/{organizerId}/events/sync_comments_load/{code}", method = { RequestMethod.PUT, RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody EventUpdateResponse loadMore(@RequestBody LogQueryObject query, @PathVariable("code") String code, @PathVariable("appId") String appId, HttpServletRequest request) throws Exception {

		Feed feed = feedController.get(query.getFeedId(), null, null);

		MobileEventFeed mobilefeed = new MobileEventFeed();

		mobilefeed.setCommentsCount(feed.getCommentCount());

		// mobilefeed.setLogo(new Picture(feed.getPicture()));
		mobilefeed.setLogo(new Picture(feed.getPictureO()));

		mobilefeed.setLikes(feed.getLikes());

		mobilefeed.setCreatedDate(feed.getCreatedDate());

		mobilefeed.setEventId(feed.getEventId());

		mobilefeed.setId(feed.getId());

		mobilefeed.setType(feed.getType());

		mobilefeed.setTypeId(feed.getTypeId());

		mobilefeed.setText(feed.getDescription());

		mobilefeed.setCreatedByEmail(feed.getCreatedByEmail());

		mobilefeed.setCreatedByname(feed.getCreatedByname());

		mobilefeed.setLastModifiedDate(feed.getLastModifiedDate());

		mobilefeed.setSource(feed.getSource());

		mobilefeed.setDp(feed.getDp());

		mobilefeed.setAttendeeId(feed.getAttendeeId());

		if (query.getList() == null || query.getList().size() <= 0) {

		}
		else {
			Collections.sort(query.getList(), new CommentObjectSortDesc());

			for (OldCommentListObject feedObject : query.getList()) {
				// /System.out.println(feedObject.getFeedId());
			}

			query.setSyncDateTime(query.getList().get(0).getCreationDate());
		}

		mobilefeed.setComments(dataChangeLogService.loadMore(query.getFeedId(), query.getEventId(), query.getOrganizerId(), query.getSyncDateTime()));

		List<Object> tempList = new ArrayList<Object>();

		tempList.add(mobilefeed);

		if (mobilefeed == null || mobilefeed.getComments() == null || mobilefeed.getComments().size() <= 0) {
			tempList = null;
		}

		return new EventUpdateResponse(null, null, null, null, null, null, null, null, null, null, null, null, null, new SyncDataObject(tempList, null, null), null, null);

		// return new FeedCommentResponse(new SyncDataObject(null, tempList,
		// null));
	}

	/*
	 * // @RequestMapping(value = "/pulltorefresh/{appId}/{code}", method = { //
	 * RequestMethod.PUT, RequestMethod.GET })
	 * 
	 * @RequestMapping(value =
	 * "mobile/{appId}/organizer/{organizerId}/events/sync_comments_pull/{code}"
	 * , method = { RequestMethod.PUT, RequestMethod.GET })
	 * 
	 * @ResponseStatus(HttpStatus.OK) public @ResponseBody CommentRefreshData
	 * pullToRefresh(@RequestBody @Valid LogQueryObject query,
	 * 
	 * @PathVariable("code") String code, @PathVariable("appId") String appId,
	 * HttpServletRequest request) throws Exception {
	 * 
	 * Feed feed = feedController.get(query.getFeedId(), null, null);
	 * 
	 * MobileEventFeed mobilefeed = new MobileEventFeed();
	 * 
	 * mobilefeed.setCommentsCount(feed.getCommentCount());
	 * 
	 * mobilefeed.setLogo(new Picture(feed.getPicture()));
	 * 
	 * mobilefeed.setLikes(feed.getLikes());
	 * 
	 * mobilefeed.setCreatedDate(feed.getCreatedDate());
	 * 
	 * mobilefeed.setEventId(feed.getEventId());
	 * 
	 * mobilefeed.setId(feed.getId());
	 * 
	 * mobilefeed.setType(feed.getType());
	 * 
	 * mobilefeed.setText(feed.getTitle());
	 * 
	 * mobilefeed.setLastModifiedDate(feed.getLastModifiedDate());
	 * 
	 * return new CommentRefreshData(mobilefeed,
	 * dataChangeLogService.getCommentsPullToRefresh(query.getFeedId(),
	 * query.getEventId(), query.getOrganizerId(), query.getSyncDateTime())); }
	 */

	@RequestMapping(value = "mobile/{appId}/organizer/{organizerId}/events/sync_comments_pull/{code}", method = { RequestMethod.PUT, RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody EventUpdateResponse pullToRefresh(@RequestBody LogQueryObject query, @PathVariable("code") String code, @PathVariable("appId") String appId, HttpServletRequest request) throws Exception {

		Feed feed = feedController.get(query.getFeedId(), null, null);

		MobileEventFeed mobilefeed = new MobileEventFeed();

		mobilefeed.setCommentsCount(feed.getCommentCount());

		// mobilefeed.setLogo(new Picture(feed.getPicture()));
		mobilefeed.setLogo(new Picture(feed.getPictureO()));

		mobilefeed.setLikes(feed.getLikes());

		mobilefeed.setCreatedDate(feed.getCreatedDate());

		mobilefeed.setEventId(feed.getEventId());

		mobilefeed.setId(feed.getId());

		mobilefeed.setType(feed.getType());

		mobilefeed.setTypeId(feed.getTypeId());

		mobilefeed.setText(feed.getDescription());

		mobilefeed.setCreatedByEmail(feed.getCreatedByEmail());

		mobilefeed.setCreatedByname(feed.getCreatedByname());

		mobilefeed.setLastModifiedDate(feed.getLastModifiedDate());

		mobilefeed.setSource(feed.getSource());

		mobilefeed.setDp(feed.getDp());

		mobilefeed.setAttendeeId(feed.getAttendeeId());

		if (query.getList() == null || query.getList().size() <= 0) {

		}
		else {
			Collections.sort(query.getList(), new CommentObjectSortDesc());

			for (@SuppressWarnings("unused")
			OldCommentListObject feedObject : query.getList()) {
				// /System.out.println(feedObject.getFeedId());
			}

			query.setSyncDateTime(query.getList().get(query.getList().size() - 1).getCreationDate());
		}

		mobilefeed.setComments(dataChangeLogService.getCommentsPullToRefresh(query.getFeedId(), query.getEventId(), query.getOrganizerId(), query.getSyncDateTime()));

		List<Object> tempList = new ArrayList<Object>();

		tempList.add(mobilefeed);

		if (mobilefeed == null || mobilefeed.getComments() == null || mobilefeed.getComments().size() <= 0) {

			mobilefeed.setComments(new ArrayList<MobileEventFeedComment>());
			// tempList = null;
		}

		return new EventUpdateResponse(null, null, null, null, null, null, null, null, null, null, null, null, null, new SyncDataObject(null, tempList, null), null, null);

		// return new FeedCommentResponse(new SyncDataObject(null, tempList,
		// null));
	}

	// @RequestMapping(value = "/feedloadmore/{appId}/{code}", method = {
	// RequestMethod.PUT, RequestMethod.GET })
	@RequestMapping(value = "mobile/{appId}/organizer/{organizerId}/events/sync_feeds_load/{code}", method = { RequestMethod.PUT, RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody EventUpdateResponse feedLoadMore(@RequestBody FeedQuery query, @PathVariable("code") String code, @PathVariable("appId") String appId, HttpServletRequest request) throws Exception {

		return new EventUpdateResponse(null, null, null, null, null, null, null, null, null, null, null, null, null, dataChangeLogService.feedLoadMore(query.getList(), query.getEventId(), query.getOrganizerId(), query.getSyncDateTime()), null, null);

	}

	// @RequestMapping(value = "/feedpulltorefresh/{appId}/{code}", method = {
	// RequestMethod.PUT, RequestMethod.GET })
	@RequestMapping(value = "mobile/{appId}/organizer/{organizerId}/events/sync_feeds_pull/{code}", method = { RequestMethod.PUT, RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody EventUpdateResponse feedPullToRefresh(@RequestBody FeedQuery query, @PathVariable("code") String code, @PathVariable("appId") String appId, HttpServletRequest request) throws Exception {

		return new EventUpdateResponse(null, null, null, null, null, null, null, null, null, null, null, null, null, dataChangeLogService.getFeedPullToRefresh(query.getList(), query.getEventId(), query.getOrganizerId(), query.getSyncDateTime()), null, null);

	}

	class CommentObjectSort implements Comparator<OldCommentListObject> {

		public int compare(OldCommentListObject o1, OldCommentListObject o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2);
		}
	}

	class CommentObjectSortDesc implements Comparator<OldCommentListObject> {

		public int compare(OldCommentListObject o1, OldCommentListObject o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2) * -1;
		}
	}
}
