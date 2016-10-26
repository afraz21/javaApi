package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event")
public class EventSelective implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private List<String> socialMediaHashTags;

	private Object eventConfiguration;

	private DateTime twitterFeedPullStartDate;

	private DateTime twitterFeedPullEndDate;

	public DateTime getTwitterFeedPullStartDate() {
		return twitterFeedPullStartDate;
	}

	public void setTwitterFeedPullStartDate(DateTime twitterFeedPullStartDate) {
		this.twitterFeedPullStartDate = twitterFeedPullStartDate;
	}

	public DateTime getTwitterFeedPullEndDate() {
		return twitterFeedPullEndDate;
	}

	public void setTwitterFeedPullEndDate(DateTime twitterFeedPullEndDate) {
		this.twitterFeedPullEndDate = twitterFeedPullEndDate;
	}

	public Object getEventConfiguration() {
		return eventConfiguration;
	}

	public void setEventConfiguration(Object eventConfiguration) {
		this.eventConfiguration = eventConfiguration;
	}

	private Boolean isActive;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getSocialMediaHashTags() {
		return socialMediaHashTags == null ? new ArrayList<String>() : socialMediaHashTags;
	}

	public void setSocialMediaHashTags(List<String> socialMediaHashTags) {
		this.socialMediaHashTags = socialMediaHashTags;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public static String hashTagListToString(List<EventSelective> list) {

		String hashTags = "";

		for (EventSelective eventSelective : list) {

			List<String> hash = eventSelective.getSocialMediaHashTags();

			if (hash != null) {

				for (String tag : hash) {

					tag = !tag.contains("#") ? "#" + tag : tag;

					if (hashTags.equals("")) {

						hashTags += tag;
					}
					else {
						hashTags += " OR " + tag;
					}

				}
			}

		}

		return hashTags;
	}

}
