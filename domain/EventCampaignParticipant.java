package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Event
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class EventCampaignParticipant extends Audit implements Serializable {

	private static final long serialVersionUID = -5527566248002296042L;

	private String organizerId;

	@NotEmpty(message = "campaignId cannot be empty.")
	private String campaignId;

	private Boolean isDeleted;

	private Boolean isActive;

	private Boolean completeCampaign;

	private Boolean schedule;

	private DateTime startDate;

	private DateTime startDateToGMT;

	private DateTime endDate;

	private DateTime endDateToGMT;

	private String startTime;

	private DateTime startTimeToGMT;

	private String endTime;

	private DateTime endTimeToGMT;

	private Integer limit;

	private Boolean is_start = false;

	private Boolean is_end = false;

	private boolean prepared = false;

	private boolean sent;

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public boolean isPrepared() {
		return prepared;
	}

	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}

	private List<String> sponsors;

	private List<MultiLingual> multiLingual;

	private Integer sortOrder;

	@DBRef(lazy = true)
	private Media picture;

	@DBRef(lazy = true)
	private Media overlayLandscape;

	@DBRef(lazy = true)
	private Media overlayMediumLandscape;

	@DBRef(lazy = true)
	private Media overlaySmallLandscape;

	@DBRef(lazy = true)
	private Media overlayLargeLandscape;

	@DBRef(lazy = true)
	private Media overlayThumbnailLandscape;

	@DBRef(lazy = true)
	private Media overlayPotrait;

	@DBRef(lazy = true)
	private Media overlayMediumPotrait;

	@DBRef(lazy = true)
	private Media overlaySmallPotrait;

	@DBRef(lazy = true)
	private Media overlayLargePotrait;

	@DBRef(lazy = true)
	private Media overlayThumbnailPotrait;

	private String name;

	// @JsonIgnore
	// public Media getPictureO() {
	// return picture;
	// }
	//
	// public Object getPicture() {
	//
	// if (this.picture != null) {
	// return new Picture(this.picture);
	// }
	//
	// return null;
	// }

	@JsonIgnore
	public Media getOverlayMediumLandscapeO() {

		return overlayMediumLandscape;
	}

	public Object getOverlayMediumLandscape() {

		if (this.overlayMediumLandscape != null) {

			return new Picture(this.overlayMediumLandscape);
		}

		return null;
	}

	// public Media getOverlayMediumLandscape() {
	// return overlayMediumLandscape;
	// }

	public void setOverlayMediumLandscape(Media overlayMediumLandscape) {
		this.overlayMediumLandscape = overlayMediumLandscape;
	}

	@JsonIgnore
	public Media getOverlaySmallLandscapeO() {

		return overlaySmallLandscape;
	}

	public Object getOverlaySmallLandscape() {

		if (this.overlaySmallLandscape != null) {

			return new Picture(this.overlaySmallLandscape);
		}

		return null;
	}

	// public Media getOverlaySmallLandscape() {
	// return overlaySmallLandscape;
	// }

	public void setOverlaySmallLandscape(Media overlaySmallLandscape) {
		this.overlaySmallLandscape = overlaySmallLandscape;
	}

	@JsonIgnore
	public Media getOverlayLargeLandscapeO() {

		return overlayLargeLandscape;
	}

	public Object getOverlayLargeLandscape() {

		if (this.overlayLargeLandscape != null) {

			return new Picture(this.overlayLargeLandscape);
		}

		return null;
	}

	// public Media getOverlayLargeLandscape() {
	// return overlayLargeLandscape;
	// }

	public void setOverlayLargeLandscape(Media overlayLargeLandscape) {
		this.overlayLargeLandscape = overlayLargeLandscape;
	}

	@JsonIgnore
	public Media getOverlayThumbnailLandscapeO() {

		return overlayThumbnailLandscape;
	}

	public Object getOverlayThumbnailLandscape() {

		if (this.overlayThumbnailLandscape != null) {

			return new Picture(this.overlayThumbnailLandscape);
		}

		return null;
	}

	// public Media getOverlayThumbnailLandscape() {
	// return overlayThumbnailLandscape;
	// }

	public void setOverlayThumbnailLandscape(Media overlayThumbnailLandscape) {
		this.overlayThumbnailLandscape = overlayThumbnailLandscape;
	}

	@JsonIgnore
	public Media getOverlayMediumPotraitO() {

		return overlayMediumPotrait;
	}

	public Object getOverlayMediumPotrait() {

		if (this.overlayMediumPotrait != null) {

			// if(this.overlayMediumPotrait.getId()==null){
			// return null;
			// }

			return new Picture(this.overlayMediumPotrait);
		}

		return null;
	}

	// public Media getOverlayMediumPotrait() {
	// return overlayMediumPotrait;
	// }

	public void setOverlayMediumPotrait(Media overlayMediumPotrait) {
		this.overlayMediumPotrait = overlayMediumPotrait;
	}

	@JsonIgnore
	public Media getOverlaySmallPotraitO() {

		return overlaySmallPotrait;
	}

	public Object getOverlaySmallPotrait() {

		if (this.overlaySmallPotrait != null) {

			return new Picture(this.overlaySmallPotrait);
		}

		return null;
	}

	// public Media getOverlaySmallPotrait() {
	// return overlaySmallPotrait;
	// }

	public void setOverlaySmallPotrait(Media overlaySmallPotrait) {
		this.overlaySmallPotrait = overlaySmallPotrait;
	}

	@JsonIgnore
	public Media getOverlayLargePotraitO() {

		return overlayLargePotrait;
	}

	public Object getOverlayLargePotrait() {

		if (this.overlayLargePotrait != null) {

			return new Picture(this.overlayLargePotrait);
		}

		return null;
	}

	// public Media getOverlayLargePotrait() {
	// return overlayLargePotrait;
	// }

	public void setOverlayLargePotrait(Media overlayLargePotrait) {
		this.overlayLargePotrait = overlayLargePotrait;
	}

	@JsonIgnore
	public Media getOverlayThumbnailPotraitO() {

		return overlayThumbnailPotrait;
	}

	public Object getOverlayThumbnailPotrait() {

		if (this.overlayThumbnailPotrait != null) {

			return new Picture(this.overlayThumbnailPotrait);
		}

		return null;
	}

	// public Media getOverlayThumbnailPotrait() {
	// return overlayThumbnailPotrait;
	// }

	public void setOverlayThumbnailPotrait(Media overlayThumbnailPotrait) {
		this.overlayThumbnailPotrait = overlayThumbnailPotrait;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsActive() {
		return isActive == null ? false : isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getCompleteCampaign() {
		return completeCampaign;
	}

	public void setCompleteCampaign(Boolean completeCampaign) {

		this.sent = false;

		this.setPrepared(false);

		this.setIs_end(false);

		this.completeCampaign = completeCampaign;
	}

	public Boolean getSchedule() {
		return schedule;
	}

	public void setSchedule(Boolean schedule) {

		this.sent = false;

		this.setPrepared(false);

		this.setIs_end(false);

		this.schedule = schedule;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getStartDateToGMT() {
		return startDateToGMT;
	}

	public void setStartDateToGMT(DateTime startDateToGMT) {
		this.startDateToGMT = startDateToGMT;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public DateTime getEndDateToGMT() {
		return endDateToGMT;
	}

	public void setEndDateToGMT(DateTime endDateToGMT) {
		this.endDateToGMT = endDateToGMT;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public DateTime getStartTimeToGMT() {
		return startTimeToGMT;
	}

	public void setStartTimeToGMT(DateTime startTimeToGMT) {
		this.startTimeToGMT = startTimeToGMT;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public DateTime getEndTimeToGMT() {
		return endTimeToGMT;
	}

	public void setEndTimeToGMT(DateTime endTimeToGMT) {
		this.endTimeToGMT = endTimeToGMT;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Boolean getIs_start() {
		return is_start;
	}

	public void setIs_start(Boolean is_start) {
		this.is_start = is_start;
	}

	public Boolean getIs_end() {
		return is_end;
	}

	public void setIs_end(Boolean is_end) {
		this.is_end = is_end;
	}

	public List<String> getSponsors() {
		return sponsors;
	}

	public void setSponsors(List<String> sponsors) {
		this.sponsors = sponsors;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public Media getOverlayLandscapeO() {

		return overlayLandscape;
	}

	public Object getOverlayLandscape() {

		if (this.overlayLandscape != null) {

			return new Picture(this.overlayLandscape);
		}

		return null;
	}

	// public Media getOverlayLandscape() {
	// return overlayLandscape;
	// }

	public void setOverlayLandscape(Media overlayLandscape) {
		this.overlayLandscape = overlayLandscape;
	}

	@JsonIgnore
	public Media getOverlayPotraitO() {

		return overlayPotrait;
	}

	public Object getOverlayPotrait() {

		if (this.overlayPotrait != null) {

			return new Picture(this.overlayPotrait);
		}

		return null;
	}

	// public Media getOverlayPotrait() {
	// return overlayPotrait;
	// }

	public void setOverlayPotrait(Media overlayPotrait) {
		this.overlayPotrait = overlayPotrait;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}
