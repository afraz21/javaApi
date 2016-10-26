package org.iqvis.nvolv3.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.bean.ResponseMessage;
import org.iqvis.nvolv3.domain.Attendee;
import org.iqvis.nvolv3.domain.Comment;
import org.iqvis.nvolv3.domain.DeviceInfo;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.domain.Like;
import org.iqvis.nvolv3.domain.PushNotificationConfiguration;
import org.iqvis.nvolv3.mobile.app.config.beans.AppConfiguration;
import org.iqvis.nvolv3.mobile.app.config.service.AppConfigService;
import org.iqvis.nvolv3.mobile.service.CommentService;
import org.iqvis.nvolv3.mobile.service.UserDeviceInfoService;
import org.iqvis.nvolv3.objectchangelog.service.PushExtra;
import org.iqvis.nvolv3.push.PushData;
import org.iqvis.nvolv3.push.Query;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.service.AttendeeService;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.FeedService;
import org.iqvis.nvolv3.service.PushLoggingService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Messages;
import org.iqvis.nvolv3.utils.Urls;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("restriction")
@Controller
@RequestMapping(Urls.MOBILE_COMMENT_BASE)
public class CommentController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.PUSH_LOGGING_SERVICE)
	private PushLoggingService pushLoggingService;

	@Resource(name = Constants.SERVICE_COMMENT)
	private CommentService commentsService;

	@Resource(name = Constants.SERVICE_FEED)
	private FeedService feedService;

	@Resource(name = Constants.SERVICE_USER_DEVICE_INFO_SERVICE)
	private UserDeviceInfoService userDeviceInfoService;

	@Resource(name = Constants.SERVICE_APP_CONFIG)
	private AppConfigService appConfiguration_Service;
	
	@Resource(name = Constants.SERVICE_ATTENDEE)
	private AttendeeService attendeeService;

	@RequestMapping(value = Urls.FEEDS_COMMENT, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public @ResponseBody ResponseMessage add(@RequestBody @Valid Comment feedComment, @PathVariable("id") String feedId, @PathVariable("eventId") String eventId, Model model, HttpServletRequest request) throws Exception {

		logger.debug("Received request to add Activity");

		ResponseMessage response = new ResponseMessage();

		try {

			Feed feed = feedService.get(feedId);
			
			if(feedComment.getAttendeeId()!=null && !feedComment.getAttendeeId().equals("")){
				
				Attendee profileAttendee = attendeeService.get(feedComment.getAttendeeId());
				
				if(profileAttendee.getPictureO()!=null){
				
					feedComment.setDp(profileAttendee.getPictureO().getUrl());
				}
				else{
					
					feedComment.setDp(profileAttendee.getImageUrl());
				}
				
			}

			List<String> deviceTokens = new ArrayList<String>();

			if (feed.getLikes() != null) {

				for (Like like : feed.getLikedBy()) {

					if (like.getDeviceToken() != null && !StringUtils.isEmpty(like.getDeviceToken())) {

						if (!deviceTokens.contains(like.getDeviceToken())) {

							deviceTokens.add(like.getDeviceToken());
						}

					}

				}
			}

			if (feed != null) {
				if (feed.getComments() != null) {

					for (Comment comment : feed.getComments()) {

						if (comment.getDeviceToken() != null && !StringUtils.isEmpty(comment.getDeviceToken())) {

							if (!deviceTokens.contains(comment.getDeviceToken())) {

								deviceTokens.add(comment.getDeviceToken());
							}
						}

					}
				}

				if (feed.getDeviceToken() != null && !deviceTokens.contains(feed.getDeviceToken())) {

					deviceTokens.add(feed.getDeviceToken());
				}

				List<DeviceInfo> deviceInfos = userDeviceInfoService.get(deviceTokens);

				deviceTokens = new ArrayList<String>();

				if (deviceInfos != null) {

					for (DeviceInfo deviceInfo : deviceInfos) {
						deviceInfo.setPushNotificationConfiguration(deviceInfo.getPushNotificationConfiguration() == null ? new PushNotificationConfiguration() : deviceInfo.getPushNotificationConfiguration());

						if (feed.getDeviceToken() != null && feed.getDeviceToken() != "" && feed.getDeviceToken().equals(deviceInfo.getDeviceToken())) {
							if (deviceInfo.getPushNotificationConfiguration().isOnMyFeed() == true) {

								deviceTokens.add(deviceInfo.getDeviceToken());
							}
						}

						// ||deviceInfo.getPushNotificationConfiguration().isOnMyFeed()

						if (deviceInfo.getPushNotificationConfiguration().isCommentNotify()) {
							deviceTokens.add(deviceInfo.getDeviceToken());
						}

					}

				}

			}

			Comment addedActivity = commentsService.add(feedComment, feedId);

			if (deviceTokens.size() > 0) {

				while (deviceTokens.contains(feedComment.getDeviceToken())) {

					deviceTokens.remove(feedComment.getDeviceToken());
				}

				Query query = Utils.getQuery(deviceTokens);

				PushData pushData = new PushData();

				pushData.setAlert(feedComment.getCreatedByname() + " Commented On Feed");

				PushExtra extra = new PushExtra();

				extra.setObjectId(feedId);

				extra.setObjectType("FEED_COMMENT");

				extra.setEventId(eventId);

				pushData.setExtra(extra);

				query.setData(pushData);

				List<AppConfiguration> appList = new ArrayList<AppConfiguration>();

				Event event = eventService.get(eventId);

				if (event.getLinkedAppIds() != null && event.getLinkedAppIds().size() > 0) {

					appList = (appConfiguration_Service.get(event.getLinkedAppIds()) == null) ? new ArrayList<AppConfiguration>() : appConfiguration_Service.get(event.getLinkedAppIds());
				}

				List<String> checkList = new ArrayList<String>();

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
			response.setMessage(String.format(Messages.ADD_SUCCESS_MESSAGE, "Comment"));

			response.setRecordId(addedActivity.getId().toString());

			response.setDetails_url(commentsService.getCommentDetailUrl(addedActivity, request));

			response.setMessageCode(Constants.SUCCESS_CODE);

			logger.debug("Comment has been added successfully");

		}
		catch (Exception e) {

			logger.debug("Exception while adding Comment", e);

			response.setMessageCode(Constants.ERROR_CODE);

			throw e;
		}

		return response;
	}

	@RequestMapping(value = Urls.FEEDS_COMMENT, method = { RequestMethod.PUT, RequestMethod.GET })
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Page<Comment> getComments(@RequestBody(required = false) Criteria search, @PathVariable("id") String feedId, Model model, HttpServletRequest request) {

		logger.debug("Received request to show all comments");

		Pageable pageAble = new PageRequest(0, 20);

		if (search != null) {
			if (search.getQuery() != null) {
				if (search.getQuery().getPageNumber() != null && search.getQuery().getPageSize() != null) {

					pageAble = new PageRequest(search.getQuery().getPageNumber() - 1, search.getQuery().getPageSize());
				}
			}
		}

		Page<Comment> activities = commentsService.getAll(search, pageAble, feedId);

		return activities;
	}

}
