package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.iqvis.nvolv3.audit.Audit;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Sponsor
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class UserVerification extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean isDeleted = false;

	private Boolean isActive = true;

	private String userId;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}