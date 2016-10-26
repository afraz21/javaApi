package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Session
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class EventAlert extends Audit implements Serializable {

	private static final long serialVersionUID = -3527566248002296042L;

	@NotEmpty(message = "OrganizerId cannot be empty.")
	private String organizerId;

	@NotEmpty(message = "EventId cannot be empty.")
	private String eventId;

	private Boolean isDeleted;

	private Boolean isActive;

	private String startTime;

	private DateTime startDate;

	private Boolean sendAsPush;

	private Boolean isSent;

	private String title;

	private String description;

	private String summary;

	private String personnelId;

	private String activityId;

	private String sponsorId;

	private String scheduledTimeGMT;

	private DateTime expiryTime;

	private String expiryTimeGMT;

	private DateTime sentTime;

	private Boolean sentNow;

	private DateTime scheduledTimeDefault;

	private DateTime scheduledDateTimeGMT;

	public DateTime getScheduledDateTimeGMT() {
		return scheduledDateTimeGMT;
	}

	public void setScheduledDateTimeGMT(DateTime scheduledDateTimeGMT) {
		this.scheduledDateTimeGMT = scheduledDateTimeGMT;
	}

	public Boolean getSentNow() {
		return sentNow;
	}

	public void setSentNow(Boolean sentNow) {
		this.sentNow = sentNow;
	}

	@JsonIgnore
	public DateTime getScheduledTimeDefault() {
		return scheduledTimeDefault;
	}

	public void setScheduledTimeDefault(DateTime scheduledTimeDefault) {

		this.scheduledTimeDefault = scheduledTimeDefault;

	}

	public DateTime getSentTime() {
		return sentTime;
	}

	public void setSentTime(DateTime sentTime) {
		this.sentTime = sentTime;
	}

	@JsonIgnore
	public DateTime getExpiryTimeGMT() {

		if (expiryTimeGMT != null && expiryTimeGMT != "") {

			DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

			return format.parseDateTime(expiryTimeGMT);

		}

		return DateTime.now(DateTimeZone.UTC).plusDays(3);
	}

	@JsonProperty
	public void setExpiryTimeGMT(String expiryTimeGMT) {
		this.expiryTimeGMT = expiryTimeGMT;
	}

	public DateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(DateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	@JsonIgnore
	public String getAlertScheduledTime() {
		return scheduledTimeGMT;
	}

	public void setScheduledTimeGMT(String alertScheduledTime) {
		this.scheduledTimeGMT = alertScheduledTime;
	}

	@DBRef(lazy = true)
	private Media picture;

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {

		this.startDate = startDate;
	}

	public Boolean getSendAsPush() {
		return sendAsPush;
	}

	public void setSendAsPush(Boolean sendAsPush) {
		this.sendAsPush = sendAsPush;
	}

	public Boolean getIsSent() {
		return isSent;
	}

	public void setIsSent(Boolean isSent) {
		this.isSent = isSent;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getPersonnelId() {
		return personnelId;
	}

	public void setPersonnelId(String personnelId) {
		this.personnelId = personnelId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((EventAlert) obj).getId());
	}

}
