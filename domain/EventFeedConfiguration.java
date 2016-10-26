package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class EventFeedConfiguration implements Serializable {

	public EventFeedConfiguration(boolean event_feed_moderation, boolean activity_feed_enabled, boolean activity_feed_moderation) {
		super();
		this.event_feed_moderation = event_feed_moderation;
		this.activity_feed_enabled = activity_feed_enabled;
		this.activity_feed_moderation = activity_feed_moderation;
	}

	public EventFeedConfiguration() {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean event_feed_moderation;

	private boolean activity_feed_enabled;

	private boolean activity_feed_moderation;
	
	private boolean twitter_feed_moderation_enabled;
	
	private boolean twitter_feed_pull_enabled;

	public boolean isTwitter_feed_moderation_enabled() {
		return twitter_feed_moderation_enabled;
	}

	public void setTwitter_feed_moderation_enabled(boolean twitter_feed_moderation_enabled) {
		this.twitter_feed_moderation_enabled = twitter_feed_moderation_enabled;
	}

	public boolean isTwitter_feed_pull_enabled() {
		return twitter_feed_pull_enabled;
	}

	public void setTwitter_feed_pull_enabled(boolean twitter_feed_pull_enabled) {
		this.twitter_feed_pull_enabled = twitter_feed_pull_enabled;
	}

	public boolean isEvent_feed_moderation() {
		return event_feed_moderation;
	}

	public void setEvent_feed_moderation(boolean event_feed_moderation) {
		this.event_feed_moderation = event_feed_moderation;
	}

	public boolean isActivity_feed_enabled() {
		return activity_feed_enabled;
	}

	public void setActivity_feed_enabled(boolean activity_feed_enabled) {
		this.activity_feed_enabled = activity_feed_enabled;
	}

	public boolean isActivity_feed_moderation() {
		return activity_feed_moderation;
	}

	public void setActivity_feed_moderation(boolean activity_feed_moderation) {
		this.activity_feed_moderation = activity_feed_moderation;
	}

}
