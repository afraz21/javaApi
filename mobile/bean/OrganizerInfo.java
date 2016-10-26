package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

public class OrganizerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3899612605726971669L;

	private String email;

	private String phone;

	private String facebook;

	private String website;

	private Picture picture;

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

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
