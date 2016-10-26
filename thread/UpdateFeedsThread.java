package org.iqvis.nvolv3.thread;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.iqvis.nvolv3.dao.FeedDao;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.objectchangelog.service.DataChangeLogService;
import org.iqvis.nvolv3.utils.Constants;

public class UpdateFeedsThread implements Runnable {

	protected static Logger logger = Logger.getLogger("controller");
	
	// @Autowired
	private FeedDao feedDao;

	// @Resource(name = Constants.SERVICE_DATA_CHAGE_LOG_SERVCIE)
	private DataChangeLogService dataChangeLogService;

	private String attendeeId;

	private String imageUrl;

	public UpdateFeedsThread(String attendeeId, String imageUrl) {
		super();
		this.attendeeId = attendeeId;
		this.imageUrl = imageUrl;
	}

	public UpdateFeedsThread(FeedDao feedDao, DataChangeLogService dataChangeLogService, String attendeeId, String imageUrl) {
		super();
		this.feedDao = feedDao;
		this.dataChangeLogService = dataChangeLogService;
		this.attendeeId = attendeeId;
		this.imageUrl = imageUrl;
	}

	public void run() {
		
		logger.debug("start Thread for feed updates");

		feedDao.updateFeedsAttendeeDp(attendeeId, imageUrl);

		List<String> ids = feedDao.getListOfFeedIds(attendeeId);

		for (String id : ids) {

			try {
				List<String> l = new ArrayList<String>();

				Feed feed = feedDao.get(id);

				l.add(feed.getEventId());

				if (l.size() > 0 && Constants.FEED_STATUS_APPROVED.equals(feed.getFeedStatus())) {

					dataChangeLogService.add(l, "EVENT", Constants.FEED_LOG_KEY, feed.getId(), Constants.LOG_ACTION_UPDATE, Feed.class.toString());
				}
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		logger.debug("end Thread for feed updates");

	}

}
