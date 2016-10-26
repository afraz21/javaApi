package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.iqvis.nvolv3.audit.Audit;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Personnel
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Like extends Audit implements Serializable {

	private static final long serialVersionUID = -2527566248002296042L;

	private Boolean liked;

	private Boolean isDeleted;

	private Boolean isActive;

	private String createdByname;

	private String createdByEmail;

	private String deviceToken;

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
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

	public String getCreatedByname() {
		return createdByname;
	}

	public void setCreatedByname(String createdByname) {
		this.createdByname = createdByname;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}

	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getLiked() {
		return liked;
	}

	public void setLiked(Boolean liked) {
		this.liked = liked;
	}

}
