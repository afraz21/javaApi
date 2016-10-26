package org.iqvis.nvolv3.domain;

import java.io.Serializable;

public class FeedActivityTrackData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2688254210377028448L;

	private String trackame;

	private String trackColorCode;

	public String getTrackame() {
		return trackame;
	}

	public void setTrackame(String trackame) {
		this.trackame = trackame;
	}

	public String getTrackColorCode() {
		return trackColorCode;
	}

	public void setTrackColorCode(String trackColorCode) {
		this.trackColorCode = trackColorCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
