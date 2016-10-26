package org.iqvis.nvolv3.bean;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a Session
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class Data extends Audit implements Serializable {

	private static final long serialVersionUID = -3527566248002296042L;

	@NotEmpty(message = "Name cannot be empty.")
	private String name;

	@NotEmpty(message = "Type cannot be empty.")
	private String type;

	private Boolean isDeleted = false;

	private Boolean isActive = true;

	// private Integer sortOrder;

	private String sortOrder;

	private List<MultiLingual> multiLingual;

	private String picture;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MultiLingual> getMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(List<MultiLingual> multiLingual) {
		this.multiLingual = multiLingual;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Data guest = (Data) obj;

		return this.getId().equalsIgnoreCase(guest.getId());
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
