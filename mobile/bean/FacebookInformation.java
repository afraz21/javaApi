package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

public class FacebookInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private FacebookPicture picture;

	private String link;

	private String email;

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public FacebookPicture getPicture() {
		return picture;
	}

	public void setPicture(FacebookPicture picture) {
		this.picture = picture;
	}

}
