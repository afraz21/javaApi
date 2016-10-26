package org.iqvis.nvolv3.domain;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.iqvis.nvolv3.audit.Audit;

public class UpdateVendorList extends Audit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "data cant be null")
	private List<VendorSortOrder> data;

	public List<VendorSortOrder> getData() {
		return data;
	}

	public void setData(List<VendorSortOrder> data) {
		this.data = data;
	}

}
