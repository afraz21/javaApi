package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.iqvis.nvolv3.serializer.JodaDateTimeJsonSerializer;
import org.joda.time.DateTime;

public class MobileEventFeedListObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DateTime currentTime;

	private List<MobileEventFeed> feeds;

	@JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	public DateTime getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(DateTime currentTime) {
		this.currentTime = currentTime;
	}

	public List<MobileEventFeed> getFeeds() {
		return feeds;
	}

	public void setFeeds(List<MobileEventFeed> feeds) {
		this.feeds = feeds;
	}

}
