package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.Data;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.User;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Sponsor
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class EventSponsor extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Media picture;

	private String type;

	private Boolean isDeleted = false;

	private Boolean isActive = false;

	private List<MultiLingual> multiLingual;

	private String organizerId;

	private String firstName;

	private String lastName;

	private User user;

	private String email;

	private String phone;

	private Boolean invite = false;

	private String businessCategory;

	private Data sponsorCategory;

	private String sponsorCategoryId;

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
		return new Picture(picture);
	}

	public void setPicture(Media picture) {
		this.picture = picture;
	}

	@JsonIgnore
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	public String getBusinessCategory() {
		return businessCategory;
	}

	public void setBusinessCategory(String businessCategory) {
		this.businessCategory = businessCategory;
	}

	@JsonIgnore
	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	@JsonIgnore
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonIgnore
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonIgnore
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@JsonIgnore
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

	@JsonIgnore
	public Data getSponsorCategory() {
		return sponsorCategory;
	}

	@JsonIgnore
	public void setSponsorCategory(Data sponsorCategory) {
		this.sponsorCategory = sponsorCategory;
	}

	@Override
	@JsonIgnore
	public Integer getVersion() {
		// TODO Auto-generated method stub
		return super.getVersion();
	}

	@JsonProperty
	public String getSponsorCategoryId() {
		return sponsorCategoryId;
	}

	public void setSponsorCategoryId(String sponsorCategoryId) {
		this.sponsorCategoryId = sponsorCategoryId;
	}

	// @Override
	// public boolean equals(Object obj) {
	// // TODO Auto-generated method stub
	// return this.getId().equals(((EventSponsor)obj).getId());
	// }

}