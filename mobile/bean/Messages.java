package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

public class Messages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8719106091152956358L;

	public String NETWORK_FAILURE;

	public String PROFILE_UPDATE_SUCCESS;

	public String getNETWORK_FAILURE() {
		return NETWORK_FAILURE;
	}

	public void setNETWORK_FAILURE(String nETWORK_FAILURE) {
		NETWORK_FAILURE = nETWORK_FAILURE;
	}

	public String getPROFILE_UPDATE_SUCCESS() {
		return PROFILE_UPDATE_SUCCESS;
	}

	public void setPROFILE_UPDATE_SUCCESS(String pROFILE_UPDATE_SUCCESS) {
		PROFILE_UPDATE_SUCCESS = pROFILE_UPDATE_SUCCESS;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
