package org.iqvis.nvolv3.controller;

import java.io.Serializable;

public class Contact implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String [] salesEmailList;

	public String[] getSalesEmailList() {
		return salesEmailList;
	}

	public void setSalesEmailList(String[] salesEmailList) {
		this.salesEmailList = salesEmailList;
	}
	
}	
