package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.domain.CheckIn;
import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.domain.Personnel;
import org.iqvis.nvolv3.serializer.JodaDateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * A simple POJO representing a Session
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public class Activity extends Audit implements Serializable {

	private static final long serialVersionUID = -3527566248002296042L;

	private String name;

	private String eventId;

	private String organizerId;

	private DateTime startTime;

	private DateTime endTime;

	private DateTime eventDate;

	private Location location;

	private String type;

	private Boolean isDeleted = false;

	private Boolean isActive = false;

	private List<Personnel> personnels;

	private List<String> tracks;

	private List<MultiLingual> multiLingual;

	private org.iqvis.nvolv3.domain.Activity inner;

	private boolean showFeed;

	private boolean activity_feed_moderation;

	private String questionnaireType;

	private String questionnaireId;
	
	private String timeZoneOffSet;
	
	private CheckIn checkIn;

	public String getTimeZoneOffSet() {
		return timeZoneOffSet;
	}

	public void setTimeZoneOffSet(String timeZoneOffSet) {
		this.timeZoneOffSet = timeZoneOffSet;
	}

	public String getQuestionnaireType() {
		return questionnaireType;
	}

	public void setQuestionnaireType(String questionnaireType) {
		this.questionnaireType = questionnaireType;
	}

	public String getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(String questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public boolean isActivity_feed_moderation() {
		return activity_feed_moderation;
	}

	public void setActivity_feed_moderation(boolean activity_feed_moderation) {
		this.activity_feed_moderation = activity_feed_moderation;
	}

	public boolean isShowFeed() {
		return showFeed;
	}

	public void setShowFeed(boolean showFeed) {
		this.showFeed = showFeed;
	}

	public void setInner(org.iqvis.nvolv3.domain.Activity inner) {
		this.inner = inner;
	}

	
	@JsonIgnore
	public org.iqvis.nvolv3.domain.Activity getInner() {
		return inner;
	}

	@JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	@JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	@JsonIgnore
	public Location getLocation() {
		return location;
	}

	@JsonProperty
	public String getLocationId() {
		return location != null ? location.getId() : "";
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@JsonIgnore
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@JsonIgnore
	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@JsonIgnore
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

	@JsonIgnore
	public List<Personnel> getPersonnels() {
		return personnels;
	}

	public List<MobileActivityPersonnelList> getPersonnelsList() {
		// List<MobilePersonnal> temp=new ArrayList<MobilePersonnal>();
		// if(personnels!=null){
		//
		// for (Personnel personnalT : personnels) {
		//
		// MobilePersonnal t=new MobilePersonnal();
		//
		// t.setId(personnalT.getId());
		//
		// temp.add(t);
		// }
		// }
		//
		// return temp;

		return MobileActivityPersonnelList.getPersonnels(inner);

	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

	@JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	public DateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(DateTime eventDate) {
		this.eventDate = eventDate;
	}

	@Override
	@JsonIgnore
	public Integer getVersion() {
		// TODO Auto-generated method stub
		return super.getVersion();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null)
			return false;

		if (obj.getClass().equals(Activity.class)) {
			Activity act = (Activity) obj;
			return act.getId().equals(this.getId());
		}
		else {
			org.iqvis.nvolv3.domain.Activity act = (org.iqvis.nvolv3.domain.Activity) obj;
			return act.getId().equals(this.getId());
		}

	}

	public CheckIn getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(CheckIn checkIn) {
		this.checkIn = checkIn;
	}
	
	

}
