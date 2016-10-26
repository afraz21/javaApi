package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.MultiLingualAddresses;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Venu
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Venue extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	private String organizerId;

	@DBRef(lazy = true)
	private Media picture;

	private String googleMapLink;

	private Boolean isDeleted;

	private Boolean isActive;

	private Boolean isEventVenue;

	private List<Location> locations;

	private List<MultiLingualAddresses> multiLingual;

	private Double latitude;

	private Double longitude;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	@JsonIgnore
	public Media getPictureO() {
		return this.picture;
	}

	@JsonProperty
	public Picture getPicture() {
		// Gson gson = new Gson();
		// System.out.println("-----------------------------------" +
		// this.picture != null ? this.picture.getUrl() : null);
		//
		// String json = gson.toJson(new Picture(this.picture));
		//
		// return gson.fromJson(json, Object.class);
		//
		return new Picture(this.picture);

	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public String getGoogleMapLink() {
		return googleMapLink;
	}

	public void setGoogleMapLink(String googleMapLink) {
		this.googleMapLink = googleMapLink;
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

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public List<MultiLingualAddresses> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingualAddresses> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public Boolean getIsEventVenue() {
		return isEventVenue;
	}

	public void setIsEventVenue(Boolean isEventVenue) {
		this.isEventVenue = isEventVenue;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "longi : " + longitude + " lati : " + latitude;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((Venue) obj).getId());
	}

}