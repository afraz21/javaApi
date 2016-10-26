package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userDeviceInformation")
public class DeviceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String deviceToken;

	private String organizerId;

	private List<String> eventId;

	private String appId;

	private String deviceType;

	private String application_language;

	private List<EventLanguage> event_languages;

	private PushNotificationConfiguration pushNotificationConfiguration = new PushNotificationConfiguration();

	@CreatedDate
	private DateTime createdAt;

	@LastModifiedDate
	private DateTime lastModifiedAt;

	public PushNotificationConfiguration getPushNotificationConfiguration() {
		return pushNotificationConfiguration;
	}

	public void setPushNotificationConfiguration(PushNotificationConfiguration pushNotificationConfiguration) {
		this.pushNotificationConfiguration = pushNotificationConfiguration;
	}

	public List<EventLanguage> getEvent_languages() {
		return event_languages;
	}

	public void setEvent_languages(EventLanguage event_languages) {

		if (event_languages == null) {
			this.event_languages = new ArrayList<EventLanguage>();
			return;
		}

		if (this.event_languages == null) {

			this.event_languages = new ArrayList<EventLanguage>();
		}
		if (!this.event_languages.contains(event_languages)) {

			this.event_languages.add(event_languages);
		}
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public String getApplication_language() {
		return application_language;
	}

	public void setApplication_language(String application_language) {
		this.application_language = application_language;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public List<String> getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {

		if (eventId == null || this.eventId == null) {

			this.eventId = new ArrayList<String>();
		}

		if (eventId != null && !this.eventId.contains(eventId)) {

			this.eventId.add(eventId);
		}
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}

	public DateTime getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(DateTime lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

}
