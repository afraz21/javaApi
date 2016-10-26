package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.iqvis.nvolv3.domain.Feed;
import org.iqvis.nvolv3.serializer.DateTimeJsonSerializer;
import org.iqvis.nvolv3.serializer.JodaDateTimeJsonSerializerFeeds;
import org.joda.time.DateTime;

public class MobileEventFeed implements Serializable {

	/**
	 * 
	 */
	public MobileEventFeed(){}
	
	public MobileEventFeed(Feed feed){
		
		this.Id=feed.getId();
		
		this.text=feed.getDescription();
		
		this.type=feed.getType();
		
		this.typeId=feed.getTypeId();
				
		this.eventId=feed.getEventId();
		
		this.createdDate=feed.getCreatedDate();
	}
	private static final long serialVersionUID = 1L;

	private String Id;

	private String text;

	private String type;

	private String typeId;

	private String eventId;

	private String activityId;
	
	private String source;
	
	
	private String dp;
	
	private String attendeeId;	

	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}

	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(String attendeeId) {
		this.attendeeId = attendeeId;
	}

	@JsonSerialize(using = JodaDateTimeJsonSerializerFeeds.class)
	private DateTime createdDate;

	private List<MobileEventFeedComment> comments;

	private DateTime lastModifiedDate;

	private Picture logo;

	private Integer likes;

	private int commentsCount;

	private int shareCount;

	private String shareURL;

	private String createdByname;

	private String createdByEmail;
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	@SuppressWarnings("static-access")
	@JsonProperty
	@JsonSerialize(using = DateTimeJsonSerializer.class)
	public DateTime getSyncTime() {
		return new DateTime().now();
	}

	@JsonIgnore
	private String feedStatus;

	@JsonIgnore
	public String getFeedStatus() {
		return feedStatus;
	}

	@JsonIgnore
	public void setFeedStatus(String feedStatus) {
		this.feedStatus = feedStatus;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	public List<MobileEventFeedComment> getComments() {
		return comments == null || comments.size() <= 0 ? null : comments;
	}

	public void setComments(List<MobileEventFeedComment> comments) {
		this.comments = comments;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@JsonSerialize(using = JodaDateTimeJsonSerializerFeeds.class)
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Picture getLogo() {
		return logo;
	}

	public void setLogo(Picture logo) {
		this.logo = logo;
	}

	public Integer getLikes() {
		return likes == null ? 0 : likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public String getShareURL() {
		return shareURL;
	}

	public void setShareURL(String shareURL) {
		this.shareURL = shareURL;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((MobileEventFeed) obj).getId());
	}

}
