package org.iqvis.nvolv3.objectchangelog.response.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iqvis.nvolv3.bean.FeedbackConfiguration;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.joda.time.DateTime;

public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private List<DateTime> eventDates;

	private String organizerId;

	private String eventTypeId;

	private Boolean isMultiDate;

	private Boolean isDeleted;

	private Boolean isActive;

	private List<MultiLingual> multiLingual;

	private Picture picture;

	private Picture banner;

	private String event_Website;

	private String eventTimeZone;

	private String timeZoneLabel;

	private List<String> eventHashTag;

	private Boolean isProfileMust;

	public List<String> getEventHashTag() {

		return eventHashTag == null ? new ArrayList<String>() : eventHashTag;
	}

	public void setEventHashTag(List<String> eventHashTag) {
		this.eventHashTag = eventHashTag;
	}

	private FeedbackConfiguration feedback_configuration;

	public FeedbackConfiguration getFeedback_configuration() {
		return feedback_configuration;
	}

	public void setFeedback_configuration(FeedbackConfiguration feedback_configuration) {
		this.feedback_configuration = feedback_configuration;
	}

	public Event(org.iqvis.nvolv3.domain.Event existingEvent) {
		if (existingEvent != null) {

			this.id = existingEvent.getId();

			this.name = existingEvent.getName();

			this.eventDates = existingEvent.getEventDates();

			this.organizerId = existingEvent.getOrganizerId();

			this.eventTypeId = existingEvent.getEventTypeId();

			this.isMultiDate = existingEvent.isMultiDate();

			this.isDeleted = existingEvent.getIsDeleted();

			this.isActive = existingEvent.getIsActive();

			this.multiLingual = existingEvent.getMultiLingual();

			this.picture = new Picture(existingEvent.getPictureO());

			this.banner = new Picture(existingEvent.getBannerO());

			this.event_Website = existingEvent.getEvent_Website();

			this.eventTimeZone = existingEvent.getTimeZone();

			this.timeZoneLabel = existingEvent.getTimeZoneLabel();

			this.feedback_configuration = existingEvent.getFeedback_configuration();

			this.eventHashTag = existingEvent.getEventHashTag();

			this.isProfileMust = existingEvent.getIsProfileMust();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DateTime> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<DateTime> eventDates) {
		this.eventDates = eventDates;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public Boolean getIsMultiDate() {
		return isMultiDate;
	}

	public void setIsMultiDate(Boolean isMultiDate) {
		this.isMultiDate = isMultiDate;
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

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public Picture getBanner() {
		return banner;
	}

	public void setBanner(Picture banner) {
		this.banner = banner;
	}

	public String getEvent_Website() {
		return event_Website;
	}

	public void setEvent_Website(String event_Website) {
		this.event_Website = event_Website;
	}

	public String getTimeZoneLabel() {
		return timeZoneLabel;
	}

	public void setTimeZoneLabel(String timeZoneLabel) {
		this.timeZoneLabel = timeZoneLabel;
	}

	public String getEventTimeZone() {
		return eventTimeZone;
	}

	public void setEventTimeZone(String eventTimeZone) {
		this.eventTimeZone = eventTimeZone;
	}

	public Boolean getIsProfileMust() {
		return isProfileMust;
	}

	public void setIsProfileMust(Boolean isProfileMust) {
		this.isProfileMust = isProfileMust;
	}

}
