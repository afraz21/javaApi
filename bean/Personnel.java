package org.iqvis.nvolv3.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Personnel
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Personnel extends Audit implements Serializable {

	private static final long serialVersionUID = -2527566248002296042L;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	@DBRef(lazy = true)
	private Media picture;

	@Email
	private String email;

	private String phone;

	private String organizerId;

	@NotEmpty(message = "Type cannot be empty.")
	private String type;

	private List<MultiLingualPersonnelInformation> multiLingual;

	private Boolean isDeleted = false;

	private Boolean isActive = true;

	private List<String> activities;

	private Integer sortOrder;

	private Boolean isFeatured;

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

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<MultiLingualPersonnelInformation> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingualPersonnelInformation> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public List<String> getActivities() {
		return activities;
	}

	public void setActivities(List<String> activities) {
		this.activities = activities;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Boolean isFeatured() {
		return isFeatured;
	}

	public void setFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

}
