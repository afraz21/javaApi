package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Vendor
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class TimeZone extends Audit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	private String description;

	private Boolean isDeleted = false;

	private Boolean isActive = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}