package org.iqvis.nvolv3.mobile.bean;

import java.io.Serializable;

public class FacebookPicture implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private FacebookData data;

	public FacebookData getData() {
		return data;
	}

	public void setData(FacebookData data) {
		this.data = data;
	}

}
