package org.iqvis.nvolv3.bean;

import java.io.Serializable;

public class EventSponsor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sponsorId;

	private Integer sortOrder;

	private String sponsorCategoryId;

	private int version = 0;

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getSponsorCategoryId() {
		return sponsorCategoryId;
	}

	public void setSponsorCategoryId(String sponsorCategoryId) {
		this.sponsorCategoryId = sponsorCategoryId;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
