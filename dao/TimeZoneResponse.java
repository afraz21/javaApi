package org.iqvis.nvolv3.dao;

import java.io.Serializable;

public class TimeZoneResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double dstOffset;

	private double rawOffset;

	private String status;

	private String timeZoneId;

	private String timeZoneName;

	public double getDstOffset() {
		return dstOffset;
	}

	public void setDstOffset(double dstOffset) {
		this.dstOffset = dstOffset;
	}

	public double getRawOffset() {
		return rawOffset;
	}

	public void setRawOffset(double rawOffset) {
		this.rawOffset = rawOffset;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public String getTimeZoneName() {
		return timeZoneName;
	}

	public void setTimeZoneName(String timeZoneName) {
		this.timeZoneName = timeZoneName;
	}

}
