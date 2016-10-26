package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.mobile.bean.EventVendor;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Vendor
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Vendor extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	@DBRef(lazy = true)
	private Media picture;

	private Boolean isDeleted = false;

	private Boolean isActive = true;

	private List<MultiLingual> multiLingual;

	private String organizerId;

	private String vendorCategoryId;

	private String boothNumber;

	private Double latitude = 0.0;

	private Double longitude = 0.0;

	private String eventId;

	private Sponsor sponsor;

	private Data vendorCategory;

	private int sortOrder = 0;
	
	private boolean enableQR;

	@JsonProperty
	public boolean isEnableQR() {
		return enableQR;
	}

	@JsonIgnore
	public void setEnableQR(boolean enableQR) {
		this.enableQR = enableQR;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@JsonProperty
	public Data getVendorCategory() {
		return vendorCategory;
	}

	@JsonIgnore
	public void setVendorCategory(Data vendorCategory) {
		this.vendorCategory = vendorCategory;
	}

	@JsonProperty
	public Sponsor getSponsor() {
		return sponsor;
	}

	@JsonIgnore
	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	@NotEmpty(message = "SponsorId cannot be empty.")
	private String sponsorId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public String getVendorCategoryId() {
		return vendorCategoryId;
	}

	public void setVendorCategoryId(String vendorCategoryId) {
		this.vendorCategoryId = vendorCategoryId;
	}

	public String getBoothNumber() {
		return boothNumber;
	}

	public void setBoothNumber(String boothNumber) {
		this.boothNumber = boothNumber;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
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

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null)
			return false;
		
		if(obj.getClass().toString().equals(EventVendor.class.toString())){
			
			EventVendor o=(EventVendor)obj;
			
			return this.getId().equals(o.getId());
		}
		
		return this.getId().equals(((Vendor) obj).getId());
	}

}