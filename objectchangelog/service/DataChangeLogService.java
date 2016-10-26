package org.iqvis.nvolv3.objectchangelog.service;

import java.util.List;

import org.iqvis.nvolv3.exceptionHandler.NotFoundException;
import org.iqvis.nvolv3.mobile.bean.MobileEventFeedComment;
import org.iqvis.nvolv3.objectchangelog.domain.ChangeTrackLog;
import org.iqvis.nvolv3.objectchangelog.domain.FeedObject;
import org.iqvis.nvolv3.objectchangelog.domain.SyncDataObject;
import org.joda.time.DateTime;

public interface DataChangeLogService {

	public boolean isChangedConfig(String eventId, String organizerId, DateTime dateTime);

	public void add(List<String> eventId, String event, String subObject, String subObjectId, String action, String subObjectType);

	boolean isChanged(String eventId, String organizerId, DateTime dateTime);

	public ChangeTrackLog getTracksLogList(String eventId, String organizerId, DateTime dateTime, String selectorType, String code, String eventDefault);

	public List<MobileEventFeedComment> getCommentsPullToRefresh(String feedId, String eventId, String organizerId, DateTime date) throws NotFoundException;

	public List<MobileEventFeedComment> loadMore(String feedId, String eventId, String organizerId, DateTime date) throws NotFoundException;

	public SyncDataObject getFeedPullToRefresh(List<FeedObject> feedIds, String eventId, String organizerId, DateTime date) throws NotFoundException;

	public SyncDataObject feedLoadMore(List<FeedObject> feedIds, String eventId, String organizerId, DateTime date) throws NotFoundException;
	
	public ChangeTrackLog getLogListForAttendee(String eventId, String organizerId, DateTime dateTime) ;
	
	public void deleteSubObject(String subObejct,String subObjectId);

}
