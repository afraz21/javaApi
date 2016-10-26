package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class FeedStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Feed Status should ot be null")
	private String feedStatus;

	private Boolean isActive = false;

	public String getFeedStatus() {
		return feedStatus;
	}

	public void setFeedStatus(String feedStatus) {
		this.feedStatus = feedStatus;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
