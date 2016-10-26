package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

public class FeedActivityData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5036422478232592414L;

	@NotEmpty(message = "Feed Status should ot be null")
	private String activityName;

	private DateTime startTime;

	private DateTime endTime;

	private List<FeedActivityTrackData> feedActivityTrackDataList;

	public List<FeedActivityTrackData> getFeedActivityTrackDataList() {
		return feedActivityTrackDataList;
	}

	public void setFeedActivityTrackDataList(List<FeedActivityTrackData> feedActivityTrackDataList) {
		this.feedActivityTrackDataList = feedActivityTrackDataList;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
