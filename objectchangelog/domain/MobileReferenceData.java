package org.iqvis.nvolv3.objectchangelog.domain;

import org.iqvis.nvolv3.domain.Location;
import org.iqvis.nvolv3.mobile.bean.MobileLocation;

public class MobileReferenceData extends MobileLocation {

	public MobileReferenceData() {

	}

	public MobileReferenceData(Location l, String sortOrder) {
		super(l);
		this.sortOrder = sortOrder;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sortOrder;

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

}
