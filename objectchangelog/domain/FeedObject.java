package org.iqvis.nvolv3.objectchangelog.domain;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.iqvis.nvolv3.serializer.FeeDateTimeJsonDeSerializer;
import org.joda.time.DateTime;

public class FeedObject implements Serializable, Comparable<FeedObject> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String feedId;

	@JsonDeserialize(using = FeeDateTimeJsonDeSerializer.class)
	private DateTime creationDate;

	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public int compareTo(FeedObject o) {
		// TODO Auto-generated method stub
		return this.creationDate.compareTo(o.getCreationDate()) > 0 ? -1 : 1;
	}

}
