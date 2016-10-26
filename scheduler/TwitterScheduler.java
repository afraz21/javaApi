package org.iqvis.nvolv3.scheduler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.domain.EventFeedConfiguration;
import org.iqvis.nvolv3.domain.EventSelective;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.oauth.service.OAuthServiceProvider;
import org.iqvis.nvolv3.service.EventService;
import org.iqvis.nvolv3.service.FeedService;
import org.iqvis.nvolv3.service.MediaService;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.Utils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import com.google.gson.Gson;

@SuppressWarnings("restriction")
@RequestMapping("scheduler")
@Controller
public class TwitterScheduler {

	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	@Qualifier("twitterServiceProvider")
	private OAuthServiceProvider twitterServiceProvider;

	@Resource(name = Constants.SERVICE_EVENT)
	private EventService eventService;

	@Resource(name = Constants.SERVICE_FEED)
	private FeedService feedService;

	@Resource(name = Constants.SERVICE_MEDIA)
	private MediaService mediaService;

	@RequestMapping(value = "/twitter", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public List<Status> fetchTweets(HttpServletRequest request) {

		logger.debug("start fetching tweets from twitter");

		List<EventSelective> activeEvents = eventService.getActiveEvents();

		String hashTags = EventSelective.hashTagListToString(activeEvents);

		logger.debug("hashtags : " + hashTags);

		if (hashTags.equals("")) {

			logger.debug("there is no hashtag defined against any active event");

			return new ArrayList<Status>();
		}

		Token token = new Token("3220167163-GlBahYmf79Nw8AgdG28MP8Kjk3sjM9ZswgFo438", "kDorbbFk0bOeksdfZrfkTv4zB4wf8EIUzQwwXEsJb49Zz");

		Feed latestFeed = feedService.getLatestTwitterFeed();

		List<Status> tweets = twitterServiceProvider.searchTweets(token, hashTags, latestFeed);
		
		logger.debug("count of tweets get from twitter "+tweets.size());
		
		Gson gson=new Gson();
		
		logger.debug("size of tweets get from twitter "+gson.toJson(tweets).getBytes().length/1024);

		for (Status status : tweets) {

			for (EventSelective eventSelective : activeEvents) {

				EventFeedConfiguration eventFeedConfiguration = Utils.toEventConfiguration(eventSelective.getEventConfiguration()).getFeed();

				if (eventSelective.getTwitterFeedPullStartDate() != null && eventSelective.getTwitterFeedPullEndDate() != null) {

					DateTime now = DateTime.now(DateTimeZone.UTC);

					if (now.isBefore(eventSelective.getTwitterFeedPullStartDate()) || now.isAfter(eventSelective.getTwitterFeedPullEndDate())) {

						continue;
					}
				}

				if (!eventFeedConfiguration.isTwitter_feed_pull_enabled()) {

					continue;
				}

				if (eventSelective.getSocialMediaHashTags() != null && eventSelective.getSocialMediaHashTags().size() > 0) {

					for (HashtagEntity hastTagEntity : status.getHashtagEntities()) {

						if (eventSelective.getSocialMediaHashTags().contains("#" + hastTagEntity.getText())) {

							Feed feed = new Feed(status);

							if (eventFeedConfiguration.isTwitter_feed_moderation_enabled()) {

								feed.setIsActive(false);

								feed.setFeedStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_PENDING);
							}
							else {

								feed.setIsActive(true);

								feed.setFeedStatus(org.iqvis.nvolv3.utils.Constants.FEED_STATUS_APPROVED);

								feed.setApprovalDate(DateTime.now());
							}

							feed.setEventId(eventSelective.getId());

							try {

								if (!feedService.isExits(feed.getSocialMediaId(), eventSelective.getId())) {

									if (feed.getPictureO() != null) {

										mediaService.add(feed.getPictureO());
									}

									feedService.add(feed);
								}
							}
							catch (Exception e) {

								e.printStackTrace();
							}
						}
					}
				}
			}

		}

		tweets=null;
		
		activeEvents=null;
		
		hashTags=null;
		
		token=null;
		
		gson=null;
		
		latestFeed=null;
		
		System.gc();
		
		return tweets;
	}
}
