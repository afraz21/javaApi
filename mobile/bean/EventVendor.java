package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.Vendor;

/**
 * A simple POJO representing a Vendor
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

public class EventVendor extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Media picture;

	private Boolean isDeleted = false;

	private Boolean isActive = false;

	private List<MultiLingual> multiLingual;

	private String organizerId;

	private Data vendorCategory;

	private String boothNumber;

	private Double latitude = 0.0;

	private Double longitude = 0.0;

	private String eventId;

	private String sponsorId;

	private String vendorCategoryId;

	private Integer sortOrder;

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public EventVendor() {
	}

	public EventVendor(Vendor ven) {
		this.setId(ven.getId());

		this.boothNumber = ven.getBoothNumber();

		this.eventId = ven.getEventId();

		this.isActive = ven.getIsActive();

		this.isDeleted = ven.getIsDeleted();

		this.latitude = ven.getLatitude();

		this.longitude = ven.getLongitude();

		this.multiLingual = ven.getMultiLingual();

		this.name = ven.getName();

		this.organizerId = ven.getOrganizerId();

		// this.picture = ven.getPicture();

		this.picture = ven.getPictureO();

		this.sponsorId = ven.getSponsorId();

		this.vendorCategoryId = ven.getVendorCategoryId();

		this.vendorCategory = ven.getVendorCategory();

		this.sortOrder = ven.getSortOrder();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public Media getPicture() {
		return picture;
	}

	@JsonProperty
	public Picture getLogo() {

		if (picture != null) {

			return new Picture(picture);

		}
		else {

			return null;
		}
	}

	public void setPicture(Media picture) {
		this.picture = picture;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	@JsonIgnore
	public Data getVendorCategory() {
		return vendorCategory;
	}

	public void setVendorCategory(Data vendorCategory) {
		this.vendorCategory = vendorCategory;
	}

	public String getBoothNumber() {
		return boothNumber;
	}

	public void setBoothNumber(String boothNumber) {
		this.boothNumber = boothNumber;
	}

	@JsonIgnore
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@JsonIgnore
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@JsonIgnore
	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	@JsonIgnore
	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	@JsonIgnore
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	@Override
	@JsonIgnore
	public Integer getVersion() {
		// TODO Auto-generated method stub
		return super.getVersion();
	}

	@JsonProperty
	String getVendorCategoryId() {
		if (vendorCategory != null) {

			return vendorCategory.getId();

		}

		return vendorCategoryId;
	}

	public void setVendorCategoryId(String vendorCategoryId) {
		this.vendorCategoryId = vendorCategoryId;
	}

//	@Override
//	public boolean equals(Object obj) {
//		// TODO Auto-generated method stub
//		
//		
//
//		if (Vendor.class.toString().equals(obj.getClass().toString())) {
//
//			Vendor vend = (Vendor) obj;
//			
//			return vend.getId().equals(this.getId());
//		}
//
//		return this.getId().equals(((EventVendor) obj).getId());
//	}

}