package org.iqvis.nvolv3.audit;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.index.Indexed;

public abstract class Audit implements Auditable<String, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Indexed
	private String id;

	@Version
	private Integer version = new Integer(1);

	@CreatedBy
	@NotEmpty(message = "Created by cannot be empty.")
	private String createdBy;

	@CreatedDate
	private DateTime createdDate;

	@LastModifiedBy
	private String lastModifiedBy;

	@LastModifiedDate
	private DateTime lastModifiedDate;

	@JsonIgnore
	public boolean isNew() {

		return id == null;
	}

	public String getId() {
		
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@JsonIgnore
	public String getCreatedBy() {
		return createdBy;
	}

	@JsonProperty
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@JsonIgnore
	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(org.joda.time.DateTime createdDate) {
		this.createdDate = createdDate;
	}

	@JsonIgnore
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@JsonIgnore
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}