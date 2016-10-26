package org.iqvis.nvolv3.mobile.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.controller.MediaController;
import org.iqvis.nvolv3.domain.Activity;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.domain.Connector;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventCampaignParticipant;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.bean.MobileEventFeed;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.networking.controller.FacebookController;
import org.iqvis.nvolv3.networking.controller.TwitterController;
import org.iqvis.nvolv3.objectchangelog.service.PushExtra;
import org.iqvis.nvolv3.push.PushData;
import org.iqvis.nvolv3.push.Query;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.ActivityService;
import org.iqvis.nvolv3.service.AttendeeService;
import org.iqvis.nvolv3.service.EventCampaignParticipantService;
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
import org.scribe.model.Token;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.sun.jersey.api.client.UniformInterfaceException;

@SuppressWarnings("restriction")
@Controller
@RequestMapping("mobile/{appId}/organizer/{organizerId}/")
public class MobileFeedController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.PUSH_LOGGING_SERVICE)
	private PushLoggingService pushLoggingService;

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

	@Resource(name = Constants.SERVICE_ATTENDEE)
	private AttendeeService attendeeService;

	private Runnable runnable;

	@Autowired
	MediaFactory mediaFactory;

	@Resource(name = Constants.SERVICE_EVENT_CAMPAIGN_PARTICIPANT)
	private EventCampaignParticipantService eventCampaignParticipantService;

	@Autowired
	TwitterController twitterController;

	@Autowired
	MediaController mediaController;

	// Event feed List
	@RequestMapping(value = Urls.EVENT_FEEDS_BASE_URL, method = { RequestMethod.GET, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Feed> getEventfeeds(@RequestBody(required = false) @Valid Criteria search, @PathVariable(value = "id") String typeId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all event feed");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());

				}
			}
		}

		Page<Feed> events = feedService.getAll(search, request, pageAble, Constants.EVENT_FEED, typeId, typeId);

		return events;
	}

	// Add Event feed
	@SuppressWarnings("deprecation")
	@RequestMapping(value = Urls.EVENT_FEEDS_BASE_URL, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage add(@RequestParam(required = true, value = "mediaFile") MultipartFile file, @RequestParam(required = false, value = "title") String title, @RequestParam(required = true, value = "type") String type, @RequestParam(required = false, value = "typeId") String typeId, @RequestParam(required = false, value = "description") String description, @RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "email") String email, @PathVariable(value = "id") final String eventId, @RequestParam(required = false, value = "postOnFacebook") final Integer post_on_facebook, @RequestParam(required = false, value = "postFeedOnFacebook") final String post_feed_on_facebook, @RequestParam(required = false, value = "postOnTwitter") final Integer post_on_twitter, @RequestParam(required = false, value = "postFeedOnTwitter") final String post_feed_on_twitter, @RequestParam(required = false, value = "participantId") String participantId,
			@RequestParam(required = true, value = "overlayMode") String overlayMode, @RequestParam(required = true, value = "deviceToken") String deviceToken, @RequestParam(required = false, value = "attendeeId") String attendeeId, final HttpServletRequest request) throws Exception {

		logger.debug("Received request to add event");

		logger.debug("Received EVENT FEED");

		// logger.debug("File name " + file.getName());

		ResponseMessage response = new ResponseMessage();

		try {

			Feed feed = new Feed();

			Event feedEvent = eventService.get(eventId);

			// feedEvent.getEventConfiguration().getFeed(). feed moderated flag

			Attendee profileAttendee = attendeeService.get(attendeeId);

			feed.setAttendeeId(attendeeId);
			
			if (profileAttendee != null) {

				if (profileAttendee.getPictureO() != null) {

					feed.setDp(profileAttendee.getPictureO().getUrl());
				}
				else {

					feed.setDp(profileAttendee.getImageUrl());
				}
			}

			if (feedEvent == null) {

				throw new NotFoundException(eventId, "Event");
			}
			else {

				if (type.equals(Constants.EVENT_FEED)) {

					// if (typeId == null || typeId.equals("")) {

					// if (feedEvent.getIsfeedModerated()) {
					if ((Utils.toEventConfiguration(feedEvent.getEventConfiguration())).getFeed().isEvent_feed_moderation()) {

						feed.setIsActive(false);

						feed.setFeedStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_PENDING);
					}
					else {

						feed.setIsActive(true);

						feed.setFeedStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED);

						feed.setApprovalDate(DateTime.now());
					}

					feed.setTypeId(null);

					feed.setType(Constants.EVENT_FEED);
					// }
				}
				else if (type.equals(Constants.ACTIVITY_FEED)) {

					if ((Utils.toEventConfiguration(feedEvent.getEventConfiguration())).getFeed().isActivity_feed_moderation()) {

						feed.setIsActive(false);

						feed.setFeedStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_PENDING);

					}
					else {

						feed.setIsActive(true);

						feed.setFeedStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED);

						feed.setApprovalDate(DateTime.now());

					}

					feed.setTypeId(typeId);

					feed.setType(Constants.ACTIVITY_FEED);
				}
				else {

					response.setMessage(Messages.FEED_TYPE_ERROR);

					response.setMessageCode(Constants.ERROR_CODE);

					response.setDetails_url(feedService.getFeedDetailUrl(feed, request));

					return response;
					// throw new FeedTypeNotAllowedException(type);
				}
			}

			feed.setParticipantId(participantId);
			// feed.setParticipantId("ff840ebb-0af6-43ee-8519-8da614f37698");

			// feed.setTypeId(typeId);

			// feed.setType(type);

			feed.setEventId(eventId);

			// feed.setTitle(URLDecoder.decode(title, "UTF-8"));

			feed.setDescription(URLDecoder.decode(description + "", "UTF-8"));

			feed.setCreatedByname(URLDecoder.decode(name + "", "UTF-8"));

			feed.setCreatedByEmail(URLDecoder.decode(email + "", "UTF-8"));

			feed.setDeviceToken(URLDecoder.decode(deviceToken + "", "UTF-8"));

			feed.setSource(Constants.APP);

			// if (file != null) {
			//
			// feed.setPicture(uploadImageToServer(file, request));
			//
			// }
			Media overlay = null, midOverlay = null, smallOverlay = null, largeOverlay = null, thumbOverlay = null;

			if (!"".equals(participantId) && participantId != null) {
				EventCampaignParticipant eventCampaign = eventCampaignParticipantService.get(participantId, eventId);

				if ("LANDSCAPE".equals(overlayMode)) {

					// overlay = eventCampaign.getOverlayLandscape();

					// midOverlay = eventCampaign.getOverlayMediumLandscape();
					//
					// smallOverlay = eventCampaign.getOverlaySmallLandscape();
					//
					// largeOverlay = eventCampaign.getOverlayLargeLandscape();
					//
					// thumbOverlay =
					// eventCampaign.getOverlayThumbnailLandscape();

					overlay = eventCampaign.getOverlayLandscapeO();

					midOverlay = eventCampaign.getOverlayMediumLandscapeO();

					smallOverlay = eventCampaign.getOverlaySmallLandscapeO();

					largeOverlay = eventCampaign.getOverlayLargeLandscapeO();

					thumbOverlay = eventCampaign.getOverlayThumbnailLandscapeO();

				}
				else {

					// overlay = eventCampaign.getOverlayPotrait();

					// midOverlay = eventCampaign.getOverlayMediumPotrait();
					//
					// smallOverlay = eventCampaign.getOverlaySmallPotrait();
					//
					// largeOverlay = eventCampaign.getOverlayLargePotrait();
					//
					// thumbOverlay =
					// eventCampaign.getOverlayThumbnailPotrait();

					overlay = eventCampaign.getOverlayPotraitO();

					midOverlay = eventCampaign.getOverlayMediumPotraitO();

					smallOverlay = eventCampaign.getOverlaySmallPotraitO();

					largeOverlay = eventCampaign.getOverlayLargePotraitO();

					thumbOverlay = eventCampaign.getOverlayThumbnailPotraitO();
				}

			}

			final Media overlayF = overlay, midOverlayF = midOverlay, smallOverlayF = smallOverlay, largeOverlayF = largeOverlay, thumbOverlayF = thumbOverlay;

			if (file != null && file.getSize() < 1) {
				file = null;
			}

			final File url = file != null ? Utils.multipartToFile(file, "") : null;

			final File small = file != null ? ImageUtils.createImage(file, ImageSize.SMALL.toString()) : null;

			final File large = file != null ? ImageUtils.createImage(file, ImageSize.LARGE.toString()) : null;

			final File medium = file != null ? ImageUtils.createImage(file, ImageSize.MEDIUM.toString()) : null;

			final File thumb = file != null ? ImageUtils.createImage(file, ImageSize.THUMBNAIL.toString()) : null;

			final String root = request.getRealPath(Urls.ROOT);

			if (file == null || file.getSize() == 0) {
				feed.setPrepared(true);
			}

			final Feed addedFeed = feedService.add(feed);

			final Query query = userDeviceInfoService.getQuery("eventId", eventId, null, true);

			PushData pushData = new PushData();

			query.getWhere().getDeviceToken().get$in().remove(feed.getDeviceToken());

			pushData.setAlert("New Feed Has Been Added By " + feed.getCreatedByname());

			final String mediaUrl = request.getSession().getServletContext().getInitParameter("S3-bucket-prefix"), accessKey = request.getSession().getServletContext().getInitParameter("S3-accessKey"), secretKey = request.getSession().getServletContext().getInitParameter("S3-secretKey"), existingBucketName = request.getSession().getServletContext().getInitParameter("S3-bucket");

			runnable = new Runnable() {

				public void run() {
					Feed feed = feedService.get(addedFeed.getId());

					File urlL = url, smallL = small, largeL = large, mediumL = medium, thumbL = thumb;

					Query lQuery = query;

					try {
						urlL = Utils.applyOverlay(urlL, overlayF.getUrl(), root);

						smallL = Utils.applyOverlay(smallL, smallOverlayF == null ? overlayF.getUrlSmall() : smallOverlayF.getUrl(), root);

						largeL = Utils.applyOverlay(largeL, largeOverlayF == null ? overlayF.getUrlLarge() : largeOverlayF.getUrl(), root);

						mediumL = Utils.applyOverlay(mediumL, midOverlayF == null ? overlayF.getUrlMedium() : midOverlayF.getUrl(), root);

						thumbL = Utils.applyOverlay(thumbL, thumbOverlayF == null ? overlayF.getUrlThumb() : thumbOverlayF.getUrl(), root);

					}
					catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();

					}
					catch (Exception e) {
						// TODO: handle exception
					}

					feed.setPicture(uploadImageToServer(urlL, smallL, largeL, mediumL, thumbL, mediaUrl, accessKey, secretKey, existingBucketName));

					try {

						if (smallL.exists()) {

							smallL.delete();
						}

						if (largeL.exists()) {

							largeL.delete();
						}

						if (mediumL.exists()) {

							mediumL.delete();
						}

						if (thumbL.exists()) {

							thumbL.delete();
						}

					}
					catch (Exception e) {

						e.printStackTrace();
					}

					try {

						feed.setPrepared(true);

						feedService.edit(feed, addedFeed.getId());

						HashMap<String, List<AppConfiguration>> map = new HashMap<String, List<AppConfiguration>>();

						List<AppConfiguration> appList = new ArrayList<AppConfiguration>();

						if (map.containsKey(addedFeed.getEventId())) {
							appList = new ArrayList<AppConfiguration>(map.get(addedFeed.getEventId()));
						}
						else {

							List<String> keys = new ArrayList<String>();

							List<Object> values = new ArrayList<Object>();

							appList = new ArrayList<AppConfiguration>(appConfiguration_Service.get(keys, values));
						}

						List<String> checkList = new ArrayList<String>();
						if (org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus())) {

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

								pushLoggingService.add(lQuery);

							}
						}
						if ((post_on_facebook != null && post_on_facebook == 1) || (post_on_twitter != null && post_on_twitter == 1) || (post_feed_on_twitter != null && post_feed_on_twitter.equals("1")) || (post_feed_on_facebook != null && post_feed_on_facebook.equals("1"))) {

							List<Attendee> listAttendee = attendeeService.get("email", feed.getCreatedByEmail());

							if (listAttendee != null && listAttendee.size() > 0) {

								Attendee attendee = listAttendee.get(0);

								Connector connector = attendee.getConnecterByName(Constants.FACEBOOK);


								if (post_on_facebook != null && post_on_facebook == 1 && connector != null && connector.getAccessToken() != null && connector.getAccessToken() != "") {
									FacebookController.postOnFacebookWall(connector.getAccessToken(), feed.getDescription(), feed.getPictureO().getUrlMedium());
								}
								
								List<String> checkListOrgIds = new ArrayList<String>();
								String socialAppId = "";
								String socialOrgId = "";
								for (AppConfiguration appConfiguration : appList) {

									if ((appConfiguration.getOrganizerId() == null || appConfiguration.getId() == null) || (appConfiguration.getOrganizerId() == "" || appConfiguration.getId() == "")) {

										continue;
									}

									if (checkListOrgIds.contains(appConfiguration.getId())) {
										continue;
									}
									else {
										checkListOrgIds.add(appConfiguration.getId());
									}
									
									socialAppId = appConfiguration.getId();
									socialOrgId = appConfiguration.getOrganizerId();
								}

								connector = attendee.getConnecterByName(Constants.TWITTER);
								
								if (((post_on_twitter != null && post_on_twitter == 1) || (post_feed_on_twitter != null && post_feed_on_twitter.equals("1"))) && connector != null && connector.getAccessToken() != null && connector.getAccessToken() != "") {

									Token token = new Token(connector.getAccessToken(), connector.getSecretToken());

									twitterController.uploadImageAndText(token, feed.getDescription(), feed.getPictureO().getUrl(), socialAppId, socialOrgId);

									if (urlL.exists()) {

										urlL.delete();
									}

								}

							}

						}

					}
					catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};

			PushExtra extra = new PushExtra();

			extra.setObjectType("FEED");

			extra.setObjectId(addedFeed.getId());

			extra.setEventId(eventId);

			pushData.setExtra(extra);

			query.setData(pushData);

			if (file != null && file.getSize() > 0) {

				new Thread(runnable).start();
			}
			else {

				HashMap<String, List<AppConfiguration>> map = new HashMap<String, List<AppConfiguration>>();

				List<AppConfiguration> appList = new ArrayList<AppConfiguration>();

				if (map.containsKey(addedFeed.getEventId())) {
					appList = new ArrayList<AppConfiguration>(map.get(addedFeed.getEventId()));
				}
				else {

					List<String> keys = new ArrayList<String>();

					List<Object> values = new ArrayList<Object>();

					appList = new ArrayList<AppConfiguration>(appConfiguration_Service.get(keys, values));
				}

				List<String> checkList = new ArrayList<String>();
				if (org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus())) {

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

						// System.out.println(appConfiguration.getX_PARSE_APPLICATION_ID()+"-----"+appConfiguration.getX_PARSE_REST_API_KEY());

						Utils.sendAlert(query, appConfiguration.getX_PARSE_APPLICATION_ID(), appConfiguration.getX_PARSE_REST_API_KEY());

						pushLoggingService.add(query);

					}
				}
				System.out.println("Post feed on Twitter Flag: "+post_feed_on_twitter+" "+post_on_twitter);
				if ((post_on_facebook != null && post_on_facebook == 1) || (post_on_twitter != null && post_on_twitter == 1) || (post_feed_on_twitter != null && post_feed_on_twitter.equals("1")) || (post_feed_on_facebook != null && post_feed_on_facebook.equals("1"))) {

					List<Attendee> listAttendee = attendeeService.get("email", feed.getCreatedByEmail());

					if (listAttendee != null && listAttendee.size() > 0) {

						Attendee attendee = listAttendee.get(0);

						Connector connector = attendee.getConnecterByName(Constants.FACEBOOK);

						if (post_on_facebook != null && post_on_facebook == 1 && connector != null && connector.getAccessToken() != null && connector.getAccessToken() != "") {

							FacebookController.postOnFacebookWall(connector.getAccessToken(), feed.getDescription(), feed.getPictureO().getUrlMedium());
						}
						
						String socialAppId = attendee.getAppId().toString();
						String socialOrgId = attendee.getOrganizerId().toString();

						connector = attendee.getConnecterByName(Constants.TWITTER);
						
						if (((post_on_twitter != null && post_on_twitter == 1) || (post_feed_on_twitter != null && post_feed_on_twitter.equals("1"))) && connector != null && connector.getAccessToken() != null && connector.getAccessToken() != "") {

							Token token = new Token(connector.getAccessToken(), connector.getSecretToken());

							twitterController.uploadImageAndText(token, feed.getDescription(), "", socialAppId, socialOrgId);

						}

					}

				}
			}

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Feed"));

			response.setRecordId(addedFeed.getId().toString());

			response.setRecord(new MobileEventFeed(addedFeed));

			response.setMessageCode(Constants.SUCCESS_CODE);

			response.setDetails_url(feedService.getFeedDetailUrl(addedFeed, request));

			logger.debug("Feed has been added successfully");

		}
		catch (UniformInterfaceException ue) {

			ue.printStackTrace();
		}
		catch (Exception e) {

			logger.debug("Exception while adding feed", e);

			response.setMessageCode(Constants.ERROR_CODE);

			throw new Exception(e);
		}

		return response;

	}

	// Activity feed List
	// @RequestMapping(value = Urls.ACTIVITY_FEEDS_BASE_URL, method = {
	// RequestMethod.GET, RequestMethod.PUT })
	// @ResponseStatus(HttpStatus.OK)
	// @ResponseBody
	// public Page<Feed> getActivityfeeds(@RequestBody(required = false) @Valid
	// Criteria search, @PathVariable(value = "id") String typeId, Model model,
	// HttpServletRequest request) {
	//
	// logger.debug("Received request to show all event feed");
	//
	// Pageable pageAble = new PageRequest(0, 20);
	//
	// if (search != null) {
	// if (search.getQuery() != null) {
	// if (search.getQuery().getPageNumber() != null &&
	// search.getQuery().getPageSize() != null) {
	//
	// pageAble = new PageRequest(search.getQuery().getPageNumber() - 1,
	// search.getQuery().getPageSize());
	//
	// }
	// }
	// }
	//
	// Page<Feed> events = feedService.getAll(search, request, pageAble,
	// "activity", typeId);
	//
	// return events;
	// }

	// Add Activity feed
	@RequestMapping(value = Urls.ACTIVITY_FEEDS_BASE_URL, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseMessage addActivityFeed(@RequestParam(required = false, value = "mediaFile") MultipartFile file, @RequestParam(required = false, value = "title") String title, @RequestParam(required = false, value = "description") String description, @RequestParam(required = false, value = "name") String name, @RequestParam(required = false, value = "email") String email, @PathVariable(value = "id") String typeId, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add event");

		logger.debug("Received ACTIVITY FEED");

		ResponseMessage response = new ResponseMessage();

		try {

			Feed feed = new Feed();

			Activity activity = activityService.getActivityById(typeId);

			if (activity == null) {

				throw new NotFoundException(typeId, "Activity");

			}

			Event feedEvent = eventService.get(activity.getEventId());

			if (feedEvent == null) {

				throw new NotFoundException(typeId, "Event");

			}
			else {

				// if (feedEvent.getIsfeedModerated()) {

				if ((Utils.toEventConfiguration(feedEvent.getEventConfiguration())).getFeed().isActivity_feed_moderation()) {

					feed.setIsActive(false);

					feed.setFeedStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_PENDING);

				}
				else {

					feed.setIsActive(true);

					feed.setFeedStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED);

					feed.setApprovalDate(DateTime.now());

				}
			}

			feed.setTypeId(typeId);

			feed.setType(Constants.ACTIVITY_FEED);

			// feed.setEventId(typeId);

			// feed.setTitle(URLDecoder.decode(title, "UTF-8"));

			feed.setDescription(URLDecoder.decode(description + "", "UTF-8"));

			feed.setCreatedByname(URLDecoder.decode(name + "", "UTF-8"));

			feed.setCreatedByEmail(URLDecoder.decode(email + "", "UTF-8"));

			if (file != null) {

				feed.setPicture(uploadImageToServer(file, request));

			}

			Feed addedFeed = feedService.add(feed);

			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Feed"));

			response.setRecordId(addedFeed.getId().toString());

			response.setRecord(addedFeed);

			response.setMessageCode(Constants.SUCCESS_CODE);

			response.setDetails_url(feedService.getFeedDetailUrl(addedFeed, request));

			logger.debug("Feed has been added successfully");

		}
		catch (Exception e) {

			logger.debug("Exception while adding feed", e);
			response.setMessageCode(Constants.ERROR_CODE);
			throw new Exception(e);
		}

		return response;

	}

	/*
	 * Update feed
	 */

	@RequestMapping(value = Urls.FEEDS_EDIT, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseMessage edit(@RequestBody Feed event, @PathVariable("id") String feedid, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to edit an feed");

		ResponseMessage response = new ResponseMessage();

		try {

			Feed editedEvent = feedService.edit(event, feedid);

			response.setMessage(String.format(Messages.UPDATE_SUCCESS_MESSAGE, "Feed"));

			response.setRecordId(editedEvent.getId().toString());

			response.setRecord(editedEvent);

			response.setDetails_url(feedService.getFeedDetailUrl(editedEvent, request));

		}
		catch (Exception e) {

			logger.debug("Exception while updating Feed", e);

			if (e.getClass().equals(NotFoundException.class)) {
				throw new NotFoundException(feedid, "Feed");

			}
			else {

				throw new Exception(e);
			}
		}

		return response;
	}

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

	public Media uploadImageToServer(File file, File small, File large, File medium, File thumb, String mediaUrl, String accessKey, String secretKey, String existingBucketName) {

		String type = "feed";

		try {
			String url = mediaFactory.upload(file, type);

			String urlSmall = mediaFactory.upload(small, type);

			String urlLarge = mediaFactory.upload(large, type);

			String urlMedium = mediaFactory.upload(medium, type);

			String urlThumb = mediaFactory.upload(thumb, type);

			Media media = new Media();

			// media.setId(UUID.randomUUID().toString());

			media.setType(type);

			media.setUrl(url);

			media.setUrlSmall(urlSmall);

			media.setUrlMedium(urlMedium);

			media.setUrlLarge(urlLarge);

			media.setUrlThumb(urlThumb);

			media.setMediaContainer(mediaFactory.getMyClass());

			return mediaService.add(media);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------//

}
