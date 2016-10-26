package org.iqvis.nvolv3.objectchangelog.response.bean;

import java.io.Serializable;
import java.util.List;

import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.domain.Media;
import org.iqvis.nvolv3.domain.ReferenceData;
import org.iqvis.nvolv3.domain.Sponsor;
import org.iqvis.nvolv3.domain.User;

public class EventSponsorLogObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private Media picture;

	private String type;

	private Boolean isDeleted = false;

	private Boolean isActive = true;

	private List<MultiLingual> multiLingual;

	private String organizerId;

	private String firstName;

	private String lastName;

	private User user;

	private String email;

	private String phone;

	private Boolean invite = false;

	private String sponsorCategoryId;

	private Integer sortOrder;

	private ReferenceData data;

	public EventSponsorLogObject(Sponsor sp, ReferenceData data) {
		id = sp.getId();
		name = sp.getName();
		// picture = sp.getPicture();
		picture = sp.getPictureO();
		type = sp.getType();
		isActive = sp.getIsActive();
		isDeleted = sp.getIsDeleted();
		multiLingual = sp.getMultiLingual();
		organizerId = sp.getOrganizerId();
		firstName = sp.getFirstName();
		lastName = sp.getLastModifiedBy();
		user = sp.getUser();
		email = sp.getEmail();
		phone = sp.getPhone();
		invite = sp.getInvite();
		sponsorCategoryId = sp.getSponsorCategoryId();
		sortOrder = sp.getSortOrder();
		this.data = data;
	}

	public ReferenceData getData() {
		return data;
	}

	public void setData(ReferenceData data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Media getPicture() {
		return picture;
	}

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

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getSponsorCategoryId() {
		return sponsorCategoryId;
	}

	public void setSponsorCategoryId(String sponsorCategoryId) {
		this.sponsorCategoryId = sponsorCategoryId;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}
