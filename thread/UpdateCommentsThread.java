package org.iqvis.nvolv3.thread;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.dao.CommentDao;
import org.iqvis.nvolv3.dao.FeedDao;
import org.iqvis.nvolv3.domain.Comment;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.utils.Constants;

public class UpdateCommentsThread implements Runnable {

	protected static Logger logger = Logger.getLogger("controller");

	private CommentDao commentDao;

	private FeedDao feedDao;

	private DataChangeLogService dataChangeLogService;

	private String attendeeId;

	private String imageUrl;

	public UpdateCommentsThread(FeedDao feedDao, DataChangeLogService dataChangeLogService) {
		super();
		this.feedDao = feedDao;
		this.dataChangeLogService = dataChangeLogService;
	}

	public UpdateCommentsThread(CommentDao commentDao, FeedDao feedDao, DataChangeLogService dataChangeLogService, String attendeeId, String imageUrl) {
		super();
		this.commentDao = commentDao;
		this.feedDao = feedDao;
		this.dataChangeLogService = dataChangeLogService;
		this.attendeeId = attendeeId;
		this.imageUrl = imageUrl;
	}

	public void run() {

		logger.debug("start Thread for feed updates");

		List<String> ids = commentDao.getListOfFeedIds(attendeeId);

		for (String id : ids) {

			Feed feed = feedDao.get(id);

			if (feed != null) {
				boolean update=false;
				List<Comment> comments=feed.getComments()==null?new ArrayList<Comment>():feed.getComments();
				
				for (Comment comment : comments) {
					
					if(attendeeId.equals(comment.getAttendeeId()+"")){
						
						comment.setDp(imageUrl);
						
						update=true;
					}
					
				}
				if(update){

					try {
						
						feedDao.edit(feed);
						
						List<String> l = new ArrayList<String>();

						l.add(feed.getEventId());

						if (l.size() > 0 && Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus())) {

							dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feed.getId(), Constants.LOG_ACTION_UPDATE, Feed.class.toString());
						}
					}
					catch (Exception e) {
						
					}
				}
				
			}

		}

	}

}
