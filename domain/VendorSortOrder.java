package org.iqvis.nvolv3.domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class VendorSortOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "id cant be null")
	private String id;

	private int sortOrder;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
