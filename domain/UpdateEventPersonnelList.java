package org.iqvis.nvolv3.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.iqvis.nvolv3.bean.EventPersonnel;

public class UpdateEventPersonnelList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "created by cant be NULL")
	private String createdBy;

	@NotNull(message = "data cant be NULL")
	private List<EventPersonnel> data;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<EventPersonnel> getData() {
		return data;
	}

	public void setData(List<EventPersonnel> data) {
		this.data = data;
	}

}
