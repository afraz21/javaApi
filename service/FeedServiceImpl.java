package org.iqvis.nvolv3.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.dao.FeedDao;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.exceptionHandler.FeedStatusNotAllowedException;
import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.search.Criteria;
import org.iqvis.nvolv3.utils.Constants;
import org.iqvis.nvolv3.utils.FeesStatusType;
import org.iqvis.nvolv3.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.joda.time.DateTime;

@SuppressWarnings("restriction")
@Service(Constants.SERVICE_FEED)
@Transactional
public class FeedServiceImpl implements FeedService {

	@Autowired
	private FeedDao feedDao;

	@Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	public Page<Feed> getAllCMS(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String type, String typeId) {

		return feedDao.getAllCMS(Utils.parseCriteria(eventSearch, ""), pageAble, type, typeId);
	}

	public Page<Feed> getAll(Criteria eventSearch, HttpServletRequest request, Pageable pageAble, String type, String typeId, String eventId) {

		return feedDao.getAll(Utils.parseCriteria(eventSearch, ""), pageAble, type, typeId, eventId);
	}

	public Feed get(String id) {

		return feedDao.get(id);
	}

	public Feed add(Feed feed) throws Exception {

		if (feed.getIsDeleted() == null) {

			feed.setIsDeleted(false);
		}

		Feed addedFeed = feedDao.add(feed);

		List<String> l = new ArrayList<String>();

		l.add(feed.getEventId());

		if (l.size() > 0 && Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus())) {
			dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feed.getId(), Constants.LOG_ACTION_ADD, Feed.class.toString());
		}

		return addedFeed;
	}

	public Feed edit(Feed feed, String feedId) throws Exception, NotFoundException {

		Feed existingFeed = null;

		if (null != feedId && !StringUtils.isEmpty(feedId)) {

			existingFeed = feedDao.get(feedId);
		}

		if (null == existingFeed) {

			throw new NotFoundException(feedId, "Feed");
		}

		if (feed.getPicture() != null && !StringUtils.isEmpty(feed.getPicture())) {

			// existingFeed.setPicture(feed.getPicture());

			existingFeed.setPicture(feed.getPictureO());
		}

		if (feed.getIsDeleted() != null && !StringUtils.isEmpty(feed.getIsDeleted())) {

			existingFeed.setIsDeleted(feed.getIsDeleted());
		}

		if (feed.getIsActive() != null && !StringUtils.isEmpty(feed.getIsActive())) {

			existingFeed.setIsActive(feed.getIsActive());
		}

		if (feed.getTitle() != null) {

			existingFeed.setTitle(feed.getTitle());
		}

		if (feed.getDescription() != null) {

			existingFeed.setDescription(feed.getDescription());
		}

		if (feed.getLikes() != null) {

			existingFeed.setLikes(feed.getLikes());
		}

		if (feed.getDislikes() != null) {

			existingFeed.setDislikes(feed.getDislikes());

		}

		existingFeed.setPrepared(feed.isPrepared());

		if (feed.getFeedStatus() != null) {

			if (FeesStatusType.valueOf(FeesStatusType.class, feed.getFeedStatus()) != null) {

				existingFeed.setFeedStatus(feed.getFeedStatus());

			}
			else {

				throw new FeedStatusNotAllowedException(feed.getFeedStatus());
			}
		}

		existingFeed.setApprovalDate(feed.getApprovalDate());

		existingFeed.setLastModifiedBy(feed.getCreatedBy());

		Feed editedFeed = feedDao.edit(existingFeed);

		List<String> l = new ArrayList<String>();

		l.add(feed.getEventId());

		if (l.size() > 0 && Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus())) {

			dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, editedFeed.getId(), Constants.LOG_ACTION_UPDATE, Feed.class.toString());
		}

		return editedFeed;

	}

	public String getFeedDetailUrl(Feed feed, HttpServletRequest request) {

		return feedDao.getFeedDetailUrl(feed, request);
	}

	public List<Feed> getAll(String eventId) {

		return feedDao.getAll(eventId);
	}
	
	public List<Feed> getOlderByDate(String eventId, DateTime date) {
		
		return feedDao.getOlderByDate(eventId, date);
	}

	public Feed getLatestTwitterFeed() {

		return feedDao.getLatestTwitterFeed();
	}

	public boolean isExits(long socialMediaId, String eventId) {

		return feedDao.getSocialMediaFeed(socialMediaId, eventId) == null ? false : true;
	}

}
