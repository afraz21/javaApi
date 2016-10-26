package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.iqvis.nvolv3.serializer.DateTimeJsonSerializer;
import org.iqvis.nvolv3.utils.Constants;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import twitter4j.Status;

/**
 * A simple POJO representing a Personnel
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Feed extends Audit implements Serializable {

	private static final long serialVersionUID = -2527566248002296042L;

	@NotEmpty(message = "title cannot be empty.")
	private String title;

	@NotEmpty(message = "description cannot be empty.")
	private String description;

	@DBRef(lazy = true)
	private Media picture;

	private String type;

	private String typeId;

	private FeedActivityData feedActivityData;

	private FeedCampaignData feedCampaignData;

	private Boolean isDeleted;

	private Boolean isActive;

	private String createdByname;

	private String createdByEmail;

	private List<Comment> comments;

	private List<Like> likedBy;

	private Integer likes;

	private Integer dislikes;

	private String eventId;

	private String feedStatus;

	private DateTime approvalDate;

	private String participantId;

	private boolean isPrepared = false;

	private String deviceToken;

	private String source;

	private long socialMediaId;

	private String attendeeId;

	@JsonIgnore
	private int commentCount;

	private String dp;

	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(String attendeeId) {
		this.attendeeId = attendeeId;
	}

	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}

	public long getSocialMediaId() {
		return socialMediaId;
	}

	public void setSocialMediaId(long socialMediaId) {
		this.socialMediaId = socialMediaId;
	}

	public Feed(Status status) {
		super();

		this.title = status.getText();

		this.description = status.getText();

		this.socialMediaId = status.getId();

		this.setCreatedDate(new DateTime(status.getCreatedAt()));

		this.setCreatedBy(status.getUser().getScreenName());

		this.createdByname = status.getUser().getScreenName();

		this.isPrepared = true;

		this.source = Constants.TWITTER;

		if (status.getUser() != null) {

			this.dp = status.getUser().getBiggerProfileImageURL();
		}
		
		if (status.getMediaEntities() != null && status.getMediaEntities().length > 0) {

			Media media = new Media();

			media.setUrl(status.getMediaEntities()[0].getMediaURL());

			media.setUrlCustom(status.getMediaEntities()[0].getMediaURL());

			media.setUrlLarge(status.getMediaEntities()[0].getMediaURL());

			media.setUrlMedium(status.getMediaEntities()[0].getMediaURL());

			media.setUrlSmall(status.getMediaEntities()[0].getMediaURL());

			media.setUrlThumb(status.getMediaEntities()[0].getMediaURL());

			media.setType("TWITTER_FEED_IMAGE");

			this.picture = media;
		}

	}

	public Feed() {
		super();
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	@JsonIgnore
	public boolean isPrepared() {
		return isPrepared;
	}

	public void setPrepared(boolean isPrepared) {
		this.isPrepared = isPrepared;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Override
	@JsonIgnore(false)
	public DateTime getCreatedDate() {
		// TODO Auto-generated method stub
		return super.getCreatedDate();
	}

	@JsonIgnore
	public Media getPictureO() {
		return picture;
	}

	public Object getPicture() {

		if (this.picture != null) {
			return new Picture(this.picture);
		}

		return null;
	}

	// public Media getPicture() {
	// return picture;
	// }

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedByname() {
		return createdByname;
	}

	public void setCreatedByname(String createdByname) {
		this.createdByname = createdByname;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}

	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public List<Like> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(List<Like> likedBy) {
		this.likedBy = likedBy;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public String getFeedStatus() {
		return feedStatus;
	}

	public void setFeedStatus(String feedStatus) {
		this.feedStatus = feedStatus;
	}

	public DateTime getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(DateTime approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public FeedActivityData getFeedActivityData() {
		return feedActivityData;
	}

	public void setFeedActivityData(FeedActivityData feedActivityData) {
		this.feedActivityData = feedActivityData;
	}

	public FeedCampaignData getFeedCampaignData() {
		return feedCampaignData;
	}

	public void setFeedCampaignData(FeedCampaignData feedCampaignData) {
		this.feedCampaignData = feedCampaignData;
	}

	@JsonProperty
	public int getCommentCount() {

		int count = 0;

		if (comments == null) {

			count = this.commentCount;
		}
		else {

			if (comments.size() > this.commentCount) {

				count = comments.size();
			}
			else {

				count = this.commentCount;
			}

		}

		return count;
	}

	@JsonIgnore
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((Feed) obj).getId());
	}

	@JsonProperty
	@JsonSerialize(using = DateTimeJsonSerializer.class)
	public DateTime getSyncTime() {
		return DateTime.now();
	}
}
