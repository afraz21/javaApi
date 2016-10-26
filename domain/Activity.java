package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.ActivityPersonnels;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.mobile.bean.EventTrack;
import org.iqvis.nvolv3.serializer.TimeJsonDeSerializer;
import org.iqvis.nvolv3.serializer.TimeJsonSerializer;
import org.iqvis.nvolv3.utils.Constants;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Session
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Activity extends Audit implements Serializable {

	private static final long serialVersionUID = -3527566248002296042L;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	@NotEmpty(message = "Event id cannot be empty.")
	private String eventId;

	private String organizerId;

	@JsonDeserialize(using = TimeJsonDeSerializer.class)
	@JsonSerialize(using = TimeJsonSerializer.class)
	private DateTime startTime;

	@JsonDeserialize(using = TimeJsonDeSerializer.class)
	@JsonSerialize(using = TimeJsonSerializer.class)
	private DateTime endTime;

	private DateTime eventDate;

	private String location;

	@NotEmpty(message = "Type cannot be empty.")
	private String type;

	private String superType;

	private Boolean isDeleted;

	private Boolean isActive;

	private List<ActivityPersonnels> personnels;

	private List<String> tracks;

	private List<MultiLingual> multiLingual;

	private List<EventTrack> trackDetails;

	private Location locationDetails;

	private String questionnaireType = Constants.INHERIT;

	private Long feedbackCount;

	private long populerCount;

	private String timeZoneOffSet;

	private CheckIn checkIn;
	
	private List<String> resources;
	
	private String customId;


	public String getTimeZoneOffSet() {
		return timeZoneOffSet;
	}

	public void setTimeZoneOffSet(String timeZoneOffSet) {
		this.timeZoneOffSet = timeZoneOffSet;
	}

	@JsonProperty
	public long getPopulerCount() {
		return populerCount;
	}

	@JsonIgnore
	public void setPopulerCount(long populerCount) {
		this.populerCount = populerCount;
	}

	public Long getFeedbackCount() {
		return feedbackCount;
	}

	public void setFeedbackCount(Long feedbackCount) {
		this.feedbackCount = feedbackCount;
	}

	public String getQuestionnaireType() {
		return questionnaireType;
	}

	public void setQuestionnaireType(String questionnaireType) {
		this.questionnaireType = questionnaireType;
	}

	private String questionnaireId;

	public String getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(String questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	@JsonProperty
	public List<EventTrack> getTrackDetails() {
		return trackDetails;
	}

	@JsonIgnore
	public void setTrackDetails(List<EventTrack> trackDetails) {
		this.trackDetails = trackDetails;
	}

	@JsonProperty
	public Location getLocationDetails() {
		return locationDetails;
	}

	@JsonIgnore
	public void setLocationDetails(Location locationDetails) {
		this.locationDetails = locationDetails;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public List<String> getTracks() {
		return tracks;
	}

	public void setTracks(List<String> tracks) {
		this.tracks = tracks;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ActivityPersonnels> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<ActivityPersonnels> personnels) {
		this.personnels = personnels;
	}

	public DateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(DateTime eventDate) {
		this.eventDate = eventDate;
	}

	public String getSuperType() {
		return superType;
	}

	public void setSuperType(String superType) {
		this.superType = superType;
	}

	@Override
	public boolean equals(Object obj) {

		return this.getId().equals(((Activity) obj).getId());
	}

	public int compareTo(Activity o) {

		return this.populerCount > o.getPopulerCount() ? -1 : 1;
	}

	public CheckIn getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(CheckIn checkIn) {
		this.checkIn = checkIn;
	}
	
	@JsonProperty
	public List<String> getResources() {
		return resources;
	}
	
	@JsonIgnore
	public void setResources(List<String> resources) {
		this.resources = resources;
	}
	
	@JsonProperty
	public String getCustomId() {
		return customId;
	}
	
	@JsonIgnore
	public void setCustomId(String customId) {
		this.customId = customId;
	}


}
