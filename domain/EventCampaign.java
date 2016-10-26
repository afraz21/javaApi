package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Event
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class EventCampaign extends Audit implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;

	@NotEmpty(message = "campaignCode cannot be empty.")
	private String campaignCode;

	private String organizerId;

	private String name;

	@NotEmpty(message = "eventId cannot be empty.")
	private String eventId;

	private String campaignType;

	private Boolean isDeleted;

	private Boolean isActive;

	private List<MultiLingual> multiLingual;

	private Boolean completeEvent;

	private Boolean schedule;

	private DateTime startDate;

	private DateTime eventCampaignStartDateToGMT;

	private DateTime endDate;

	private DateTime eventCampaignEndDateToGMT;

	private String startTime;

	private String endTime;

	private Integer limit;
	
	private boolean ended=false;
	
	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	private boolean prepared=false;

	public boolean isPrepared() {
		return prepared;
	}

	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}

	private List<EventCampaignParticipant> participants;

	public String getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
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

	public Boolean getCompleteEvent() {
		return completeEvent;
	}

	public void setCompleteEvent(Boolean completeEvent) {
		
		this.resetParticipants();
		
		this.completeEvent = completeEvent;
	}

	public Boolean getSchedule() {
		return schedule;
	}

	public void setSchedule(Boolean schedule) {
		
		this.resetParticipants();
		
		this.schedule = schedule;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEventCampaignStartDateToGMT() {
		return eventCampaignStartDateToGMT;
	}

	public void setEventCampaignStartDateToGMT(DateTime eventCampaignStartDateToGMT) {
		this.eventCampaignStartDateToGMT = eventCampaignStartDateToGMT;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public DateTime getEventCampaignEndDateToGMT() {
		return eventCampaignEndDateToGMT;
	}

	public void setEventCampaignEndDateToGMT(DateTime eventCampaignEndDateToGMT) {
		this.eventCampaignEndDateToGMT = eventCampaignEndDateToGMT;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<EventCampaignParticipant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<EventCampaignParticipant> participants) {
		this.participants = participants;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public void resetParticipants(){
		if(this.participants!=null){
			for (EventCampaignParticipant participant : participants) {
				
				participant.setSent(false);
				
				participant.setPrepared(false);
				
				participant.setIs_end(false);
				
			}
		}
	}

}
