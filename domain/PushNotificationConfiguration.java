package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class PushNotificationConfiguration implements Serializable {

	public PushNotificationConfiguration() {
		super();
	}

	public PushNotificationConfiguration(boolean allFeeds, boolean onMyFeed, boolean favouriteFeed) {
		super();
		this.allFeeds = allFeeds;
		this.onMyFeed = onMyFeed;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean allFeeds = false;

	private boolean onMyFeed = true;

	private boolean commentNotify = true;

	private boolean likeNotify = false;

	private boolean newFeed = false;

	public boolean isNewFeed() {
		return newFeed;
	}

	public void setNewFeed(boolean newFeed) {
		this.newFeed = newFeed;
	}

	public boolean isAllFeeds() {
		return allFeeds;
	}

	public void setAllFeeds(boolean allFeeds) {
		this.allFeeds = allFeeds;
	}

	public boolean isOnMyFeed() {
		return onMyFeed;
	}

	public void setOnMyFeed(boolean onMyFeed) {
		this.onMyFeed = onMyFeed;
	}

	public boolean isCommentNotify() {
		return commentNotify;
	}

	public void setCommentNotify(boolean commentNotify) {
		this.commentNotify = commentNotify;
	}

	public boolean isLikeNotify() {
		return likeNotify;
	}

	public void setLikeNotify(boolean likeNotify) {
		this.likeNotify = likeNotify;
	}

}
