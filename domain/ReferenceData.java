package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;
import org.iqvis.nvolv3.bean.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A simple POJO representing a ReferenceData
 * 
 * @author IQVIS at {@link http://www.iqvis.com}
 */

@Document
public class ReferenceData extends Audit implements Serializable {

	private static final long serialVersionUID = -3527566248002296042L;

	@NotEmpty(message = "Type cannot be empty.")
	private String type;

	private String organizerId;

	private List<Data> data = null;

	private Boolean isDeleted = false;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
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

}
