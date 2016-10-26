package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;

public class FeedCommentResponse implements Serializable {

	public FeedCommentResponse(Object eventFeeds) {
		super();
		this.eventFeeds = eventFeeds;
	}

	public Object getEventFeeds() {
		return eventFeeds;
	}

	public void setEventFeeds(Object eventFeeds) {
		this.eventFeeds = eventFeeds;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object eventFeeds;

}
