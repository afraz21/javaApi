package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Venu
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Location extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	@DBRef(lazy = true)
	private Media picture;

	private Double longitude = 0.0;

	private Double latitude = 0.0;

	@NotEmpty(message = "VenueId cannot be empty.")
	private String venueId;

	private Boolean isDeleted;

	private Boolean isActive;

	private List<MultiLingual> multiLingual;

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

		if (this.picture == null) {
			return null;
		}

		return new Picture(this.picture);
	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "longi : " + longitude + " lati : " + latitude;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((Location) obj).getId());
	}

}
