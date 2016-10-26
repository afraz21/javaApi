package org.iqvis.nvolv3.push;

import java.io.Serializable;

public class Where implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String deviceType;

	private DeviceToken deviceToken;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public DeviceToken getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(DeviceToken deviceToken) {
		this.deviceToken = deviceToken;
	}

}
