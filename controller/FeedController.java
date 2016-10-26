package org.iqvis.nvolv3.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.FeedStatus;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.objectchangelog.service.PushExtra;
import org.iqvis.nvolv3.push.PushData;
import org.iqvis.nvolv3.push.Query;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.ActivityService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.FeedService;
import org.iqvis.nvolv3.service.MediaService;
import org.iqvis.nvolv3.service.PushLoggingService;
import org.iqvis.nvolv3.upload.factory.MediaFactory;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.ImageSize;
import org.iqvis.nvolv3.utils.ImageUtils;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("restriction")
@Controller
@RequestMapping("organizer/{organizerId}/")
public class FeedController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	@Resource(name = Constants.SERVICE_FEED)
	private FeedService feedService;

	@Resource(name = Constants.SERVICE_MEDIA)
	private MediaService mediaService;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_ACTIVITY)
	private ActivityService activityService;
	
	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;
	
	@Resource(name = Constants.PUSH_LOGGING_SERVICE)
	private PushLoggingService pushLoggingService;

	@Autowired
	MediaFactory mediaFactory;

	// Event feed List
	@RequestMapping(value = Urls.EVENT_FEEDS_BASE_URL, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Feed> getEventfeeds(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "id") String eventId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all event feed");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<Feed> events = feedService.getAllCMS(search, request, pageAble, "event", eventId);

		return events;
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = Urls.EVENT_FEEDS_BASE_URL + "/{feedId}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage update(@RequestBody(required = false) @Valid FeedStatus fs, @PathVariable(value = "feedId") String feedId, @PathVariable(value = "id") String typeId, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add event");

		// String status_flag=request.getParameter("feedStatus");
		//
		// System.out.println(status_flag);

		ResponseMessage response = new ResponseMessage();

		try {

			Feed feed = feedService.get(feedId);

			Event feedEvent = eventService.get(typeId, organizerId);

			String prev_status = feed.getFeedStatus().toString();

			String current_status = fs.getFeedStatus().toString();

			System.out.println("Previous Feed Status = " + prev_status + " | New Feed Status : " + current_status);

			if (feedEvent == null) {

				throw new NotFoundException(typeId, "Event");
			}
			else {

				if ((!fs.getIsActive()) && fs.getFeedStatus().equals(Constants.FEED_STATUS_APPROVED)) {

					System.out.println("Approval Date Time : " + new DateTime().now());

					feed.setIsActive(true);

					feed.setFeedStatus(fs.getFeedStatus());

					feed.setApprovalDate(new DateTime().now());

				}
				else {

					feed.setIsActive(false);

					feed.setFeedStatus(fs.getFeedStatus());

					feed.setApprovalDate(null);
				}

				// feed.setFeedStatus(fs.getFeedStatus());
				//
				// feed.setApprovalDate(new DateTime().now());

				List<String> l = new ArrayList<String>();

				l.clear();

				l.add(feed.getEventId());

				if (prev_status.equals(Constants.FEED_STATUS_PENDING) && current_status.equals(Constants.FEED_STATUS_APPROVED)) {
					// insert
					if (l.size() > 0) {
						
						dataChangeLogService.deleteSubObject(Constants.FEED_LOG_KEY, feedId);

						dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feedId, Constants.LOG_ACTION_UPDATE, Feed.class.toString());

						System.out.println("Feed Data Change Log (P --- A)" + Constants.LOG_ACTION_UPDATE);

					}
				}
				// else if (prev_status.equals(Constants.FEED_STATUS_PENDING) &&
				// (current_status.equals(Constants.FEED_STATUS_REJECTED) ||
				// current_status.equals(Constants.FEED_STATUS_DEFERRED))) {
				// // delete
				// if (l.size() > 0) {
				//
				// dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY,
				// feedId, Constants.LOG_ACTION_DELETE, Feed.class.toString());
				//
				// System.out.println("Feed Data Change Log (P --- R,D)"+
				// Constants.LOG_ACTION_DELETE);
				// }
				// }
				else if (prev_status.equals(Constants.FEED_STATUS_APPROVED) && (current_status.equals(Constants.FEED_STATUS_REJECTED) || current_status.equals(Constants.FEED_STATUS_DEFERRED))) {
					// delete
					if (l.size() > 0) {
						
						dataChangeLogService.deleteSubObject(Constants.FEED_LOG_KEY, feedId);
						
						dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feedId, Constants.LOG_ACTION_DELETE, Feed.class.toString());

						System.out.println("Feed Data Change Log (A --- R,D)" + Constants.LOG_ACTION_DELETE);
					}
				}
				else if ((prev_status.equals(Constants.FEED_STATUS_REJECTED) || prev_status.equals(Constants.FEED_STATUS_DEFERRED)) && (current_status.equals(Constants.FEED_STATUS_APPROVED))) {
					// insert
					if (l.size() > 0) {
						
						dataChangeLogService.deleteSubObject(Constants.FEED_LOG_KEY, feedId);

						dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feedId, Constants.LOG_ACTION_UPDATE, Feed.class.toString());

						System.out.println("Feed Data Change Log (R,D --- A)" + Constants.LOG_ACTION_UPDATE);
					}
				}

			}

			Feed editedFeed = feedService.edit(feed, feedId);
			
			if (Constants.FEED_STATUS_APPROVED.equals(editedFeed.getFeedStatus())) {
				
				// TODO Send Push Notification Of Pending Feeds
				
				final Query query = userDeviceInfoService.getQuery("eventId", editedFeed.getEventId(), null,true);
				
				PushData pushData = new PushData();

				query.getWhere().getDeviceToken().get$in().remove(editedFeed.getDeviceToken());

				pushData.setAlert("New Feed Has Been Added By " + editedFeed.getCreatedByname());

				List<AppConfiguration> appList = new ArrayList<AppConfiguration>();

				List<String> keys = new ArrayList<String>();

				List<Object> values = new ArrayList<Object>();

				appList = new ArrayList<AppConfiguration>(appConfiguration_Service.get(keys, values));

				List<String> checkList = new ArrayList<String>();
				
				PushExtra extra = new PushExtra();

				extra.setObjectType("FEED");

				extra.setObjectId(editedFeed.getId());

				extra.setEventId(editedFeed.getEventId());

				pushData.setExtra(extra);

				query.setData(pushData);

				for (AppConfiguration appConfiguration : appList) {

					if ((appConfiguration.getX_PARSE_APPLICATION_ID() == null || appConfiguration.getX_PARSE_REST_API_KEY() == null) || (appConfiguration.getX_PARSE_APPLICATION_ID() == "" || appConfiguration.getX_PARSE_REST_API_KEY() == "")) {

						continue;
					}

					if (checkList.contains(appConfiguration.getX_PARSE_APPLICATION_ID())) {
						continue;
					}
					else {
						checkList.add(appConfiguration.getX_PARSE_APPLICATION_ID());
					}

					Utils.sendAlert(query, appConfiguration.getX_PARSE_APPLICATION_ID(), appConfiguration.getX_PARSE_REST_API_KEY());

					pushLoggingService.add(query);

				}

			}

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Feed"));

			response.setRecordId(editedFeed.getId().toString());

			response.setRecord(editedFeed);

			response.setMessageCode(Constants.SUCCESS_CODE);

			response.setDetails_url(feedService.getFeedDetailUrl(editedFeed, request));

			logger.debug("Feed has been updated successfully");

		}
		catch (Exception e) {

			logger.debug("Exception while Updating feed", e);

			response.setMessageCode(Constants.ERROR_CODE);

			throw new Exception(e);
		}

		return response;

	}

	// Add Event feed
	// @RequestMapping(value = Urls.EVENT_FEEDS_BASE_URL, method =
	// RequestMethod.POST)
	// @ResponseBody
	// @ResponseStatus(value = HttpStatus.CREATED)
	// public ResponseMessage add(@RequestParam(required = false, value =
	// "mediaFile") MultipartFile file, @RequestParam(required = false, value =
	// "title") String title, @RequestParam(required = false, value =
	// "description") String description, @RequestParam(required = false, value
	// = "name") String name, @RequestParam(required = false, value = "email")
	// String email, @PathVariable(value = "id") String typeId,
	// HttpServletRequest request) throws Exception {
	//
	// logger.debug("Received request to add event");
	//
	// logger.debug("File name " + file.getName());
	// ResponseMessage response = new ResponseMessage();
	//
	// try {
	//
	// Feed feed = new Feed();
	//
	// Event feedEvent = eventService.get(typeId);
	//
	// if (feedEvent == null) {
	//
	// throw new NotFoundException(typeId, "Event");
	// }
	// else {
	//
	// if (feedEvent.getIsfeedModerated()) {
	//
	// feed.setIsActive(false);
	//
	// feed.setStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_PENDING);
	// }
	// else {
	//
	// feed.setIsActive(true);
	//
	// feed.setStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED);
	// }
	// }
	//
	// feed.setTypeId(typeId);
	//
	// feed.setType("event");
	//
	// feed.setEventId(typeId);
	//
	// // feed.setTitle(URLDecoder.decode(title, "UTF-8"));
	//
	// feed.setDescription(URLDecoder.decode(description + "", "UTF-8"));
	//
	// feed.setCreatedByname(URLDecoder.decode(name + "", "UTF-8"));
	//
	// feed.setCreatedByEmail(URLDecoder.decode(email + "", "UTF-8"));
	//
	// if (file != null) {
	//
	// feed.setPicture(uploadImageToServer(file, request));
	//
	// }
	//
	// Feed addedFeed = feedService.add(feed);
	//
	// response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Feed"));
	//
	// response.setRecordId(addedFeed.getId().toString());
	//
	// response.setRecord(addedFeed);
	//
	// response.setMessageCode(Constants.SUCCESS_CODE);
	//
	// response.setDetails_url(feedService.getFeedDetailUrl(addedFeed,
	// request));
	//
	// logger.debug("Feed has been added successfully");
	//
	// }
	// catch (Exception e) {
	//
	// logger.debug("Exception while adding feed", e);
	//
	// response.setMessageCode(Constants.ERROR_CODE);
	//
	// throw new Exception(e);
	// }
	//
	// return response;
	//
	// }

	// Activity feed List
	@RequestMapping(value = Urls.ACTIVITY_FEEDS_BASE_URL, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Feed> getActivityfeeds(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "id") String typeId, @PathVariable(value = "organizerId") String organizerId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all event feed");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<Feed> events = feedService.getAllCMS(search, request, pageAble, "activity", typeId);

		return events;
	}

	// activity feed editing
	@SuppressWarnings("static-access")
	@RequestMapping(value = Urls.ACTIVITY_FEEDS_BASE_URL, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage updateActivityFeed(@RequestBody(required = false) @Valid FeedStatus fs, @PathVariable(value = "feedId") String feedId, @PathVariable(value = "id") String typeId, @PathVariable(value = "organizerId") String organizerId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add event");

		ResponseMessage response = new ResponseMessage();

		try {

			Feed feed = new Feed();

			String prev_status = feed.getFeedStatus().toString();

			String current_status = fs.getFeedStatus().toString();

			// Activity activity = activityService.getActivityById(typeId);
			Activity activity = activityService.get(typeId, organizerId);

			if (activity == null) {

				throw new NotFoundException(typeId, "Activity");

			}

			Event feedEvent = eventService.get(activity.getEventId());

			if (feedEvent == null) {

				throw new NotFoundException(typeId, "Event");

			}
			else {

				if (fs.getIsActive() && fs.getFeedStatus().equals(Constants.FEED_STATUS_APPROVED)) {

					feed.setIsActive(fs.getIsActive());

					feed.setFeedStatus(fs.getFeedStatus());

					feed.setApprovalDate(new DateTime().now());

				}
				else {

					feed.setIsActive(false);

					feed.setFeedStatus(fs.getFeedStatus());

				}

				// if (feedEvent.getIsfeedModerated()) {
				//
				// feed.setIsActive(false);
				// }
				// else {
				//
				// feed.setIsActive(true);
				// }

				System.out.println("Previous Feed Status = " + prev_status + " | New Feed Status : " + current_status);

				// feed.setFeedStatus(fs.getFeedStatus());
				//
				// feed.setApprovalDate(new DateTime().now());

				List<String> l = new ArrayList<String>();

				l.clear();

				l.add(feed.getEventId());

				if (prev_status.equals(Constants.FEED_STATUS_PENDING) && current_status.equals(Constants.FEED_STATUS_APPROVED)) {
					// insert
					if (l.size() > 0) {

						dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feedId, Constants.LOG_ACTION_UPDATE, Feed.class.toString());

						System.out.println("Feed Data Change Log (P --- A)" + Constants.LOG_ACTION_UPDATE);

					}
				}
				else if (prev_status.equals(Constants.FEED_STATUS_APPROVED) && (current_status.equals(Constants.FEED_STATUS_REJECTED) || current_status.equals(Constants.FEED_STATUS_DEFERRED))) {
					// delete
					if (l.size() > 0) {

						dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feedId, Constants.LOG_ACTION_DELETE, Feed.class.toString());

						System.out.println("Feed Data Change Log (A --- R,D)" + Constants.LOG_ACTION_DELETE);
					}
				}
				else if ((prev_status.equals(Constants.FEED_STATUS_REJECTED) || prev_status.equals(Constants.FEED_STATUS_DEFERRED)) && (current_status.equals(Constants.FEED_STATUS_APPROVED))) {
					// insert
					if (l.size() > 0) {

						dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feedId, Constants.LOG_ACTION_UPDATE, Feed.class.toString());

						System.out.println("Feed Data Change Log (R,D --- A)" + Constants.LOG_ACTION_UPDATE);
					}
				}

			}

			Feed editedFeed = feedService.add(feed);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Feed"));

			response.setRecordId(editedFeed.getId().toString());

			response.setRecord(editedFeed);

			response.setMessageCode(Constants.SUCCESS_CODE);

			response.setDetails_url(feedService.getFeedDetailUrl(editedFeed, request));

			logger.debug("Feed has been added successfully");

		}
		catch (Exception e) {

			logger.debug("Exception while adding feed", e);
			response.setMessageCode(Constants.ERROR_CODE);
			throw new Exception(e);
		}

		return response;

	}

	// Add Activity feed
	// @RequestMapping(value = Urls.ACTIVITY_FEEDS_BASE_URL, method =
	// RequestMethod.POST)
	// @ResponseBody
	// @ResponseStatus(value = HttpStatus.CREATED)
	// public ResponseMessage addActivityFeed(@RequestParam(required = false,
	// value = "mediaFile") MultipartFile file, @RequestParam(required = false,
	// value = "title") String title, @RequestParam(required = false, value =
	// "description") String description, @RequestParam(required = false, value
	// = "name") String name, @RequestParam(required = false, value = "email")
	// String email, @PathVariable(value = "id") String typeId,
	// HttpServletRequest request) throws Exception {
	//
	// logger.debug("Received request to add event");
	//
	// ResponseMessage response = new ResponseMessage();
	//
	// try {
	//
	// Feed feed = new Feed();
	//
	// Activity activity = activityService.getActivityById(typeId);
	//
	// if (activity == null) {
	//
	// throw new NotFoundException(typeId, "Activity");
	//
	// }
	//
	// Event feedEvent = eventService.get(activity.getEventId());
	//
	// if (feedEvent == null) {
	//
	// throw new NotFoundException(typeId, "Event");
	//
	// }
	// else {
	//
	// if (feedEvent.getIsfeedModerated()) {
	//
	// feed.setIsActive(false);
	// }
	// else {
	//
	// feed.setIsActive(true);
	// }
	// }
	//
	// feed.setTypeId(typeId);
	//
	// feed.setType("activity");
	//
	// // feed.setEventId(typeId);
	//
	// // feed.setTitle(URLDecoder.decode(title, "UTF-8"));
	//
	// feed.setDescription(URLDecoder.decode(description + "", "UTF-8"));
	//
	// feed.setCreatedByname(URLDecoder.decode(name + "", "UTF-8"));
	//
	// feed.setCreatedByEmail(URLDecoder.decode(email + "", "UTF-8"));
	//
	// if (file != null) {
	//
	// feed.setPicture(uploadImageToServer(file, request));
	//
	// }
	//
	// Feed addedFeed = feedService.add(feed);
	//
	// response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Feed"));
	//
	// response.setRecordId(addedFeed.getId().toString());
	//
	// response.setRecord(addedFeed);
	//
	// response.setMessageCode(Constants.SUCCESS_CODE);
	//
	// response.setDetails_url(feedService.getFeedDetailUrl(addedFeed,
	// request));
	//
	// logger.debug("Feed has been added successfully");
	//
	// }
	// catch (Exception e) {
	//
	// logger.debug("Exception while adding feed", e);
	// response.setMessageCode(Constants.ERROR_CODE);
	// throw new Exception(e);
	// }
	//
	// return response;
	//
	// }

	/*
	 * Update feed
	 */

	// @RequestMapping(value = Urls.FEEDS_EDIT, method = RequestMethod.PUT)
	// @ResponseBody
	// @ResponseStatus(value = HttpStatus.OK)
	// public ResponseMessage edit(@RequestBody Feed event, @PathVariable("id")
	// String feedid, Model model, HttpServletRequest request) throws Exception
	// {
	//
	// logger.debug("Received request to edit an feed");
	//
	// ResponseMessage response = new ResponseMessage();
	//
	// try {
	//
	// Feed editedEvent = feedService.edit(event, feedid);
	//
	// response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE,
	// "Feed"));
	//
	// response.setRecordId(editedEvent.getId().toString());
	//
	// response.setRecord(editedEvent);
	//
	// response.setDetails_url(feedService.getFeedDetailUrl(editedEvent,
	// request));
	//
	// }
	// catch (Exception e) {
	//
	// logger.debug("Exception while updating Feed", e);
	//
	// if (e.getClass().equals(NotFoundException.class)) {
	// throw new NotFoundException(feedid, "Feed");
	//
	// }
	// else {
	//
	// throw new Exception(e);
	// }
	// }
	//
	// return response;
	// }

	@RequestMapping(value = Urls.FEEDS_EDIT, method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Feed get(@PathVariable("id") String feedid, Model model, HttpServletRequest request) throws NotFoundException {

		logger.debug("Received request to fetch an feed");

		Feed existingFeed = null;

		if (null != feedid && !feedid.equalsIgnoreCase("")) {

			existingFeed = feedService.get(feedid);
		}

		if (existingFeed == null) {
			throw new NotFoundException(feedid, "Feed");
		}

		return existingFeed;
	}

	public Media uploadImageToServer(MultipartFile file, HttpServletRequest request) {

		String type = "feed";

		try {
			String url = mediaFactory.upload(Utils.multipartToFile(file, ""), type);

			String urlSmall = mediaFactory.upload(ImageUtils.createImage(file, ImageSize.SMALL.toString()), type);

			String urlLarge = mediaFactory.upload(ImageUtils.createImage(file, ImageSize.LARGE.toString()), type);

			String urlMedium = mediaFactory.upload(ImageUtils.createImage(file, ImageSize.MEDIUM.toString()), type);

			String urlThumb = mediaFactory.upload(ImageUtils.createImage(file, ImageSize.THUMBNAIL.toString()), type);

			Media media = new Media();

			// media.setId(UUID.randomUUID().toString());

			media.setType(type);

			media.setUrl(url);

			media.setUrlSmall(urlSmall);

			media.setUrlMedium(urlMedium);

			media.setUrlLarge(urlLarge);

			media.setUrlThumb(urlThumb);

			return mediaService.add(media);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
