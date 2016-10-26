package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.mobile.bean.Picture;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Sponsor
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Sponsor extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	@DBRef(lazy = true)
	private Media picture;

	private String type;

	private Boolean isDeleted = false;

	private Boolean isActive = true;

	private List<MultiLingual> multiLingual;

	private String organizerId;

	private String firstName;

	private String lastName;

	@DBRef(lazy = true)
	private User user;

	private String email;

	private String phone;

	private Boolean invite = false;

	private String sponsorCategoryId;

	private Integer sortOrder;

	@JsonProperty
	public String getSponsorCategoryId() {
		return sponsorCategoryId;
	}

	@JsonIgnore
	public void setSponsorCategoryId(String sponsorCategoryId) {
		this.sponsorCategoryId = sponsorCategoryId;
	}

	@JsonProperty
	public Integer getSortOrder() {
		return sortOrder;
	}

	@JsonIgnore
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	/*
	 * @DBRef BusinessCategory businessCategory;
	 */

	private String businessCategory;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getBusinessCategory() {
		return businessCategory;
	}

	public void setBusinessCategory(String businessCategory) {
		this.businessCategory = businessCategory;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public Boolean getInvite() {
		return invite;
	}

	public void setInvite(Boolean invite) {
		this.invite = invite;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserFirstName() {
		return user==null?null:user.getFirstName();
	}

	public String getUserLastName() {
		
		return user==null?null:user.getLastName();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.getId().equals(((Sponsor) obj).getId());
	}

}